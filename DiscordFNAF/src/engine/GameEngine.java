/*
 * Classname: GameEngine
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 10/11/2021
 * Description: Major class that houses the primary while loop and contacts all other classes for a compiled project, such as the renderer, the game logic, and the display manager.
 */

package engine;

import interfaces.GameLogic;

public class GameEngine implements Runnable {
	
	public static final int TARGET_FPS = 60; //Variable that keeps track of target FPS.
	public static final int TARGET_UPS = 30;
	
	private final DisplayManager displayWindow; //Variable that creates the window with various settings (AP, title, vSync).
	private final GameLogic gameLogic; //Variable that handles the gamelogic.
	private final Timer timer; //Variable that handles the timer (for vSync and proper framerate/no screen tearing).
	
	public GameEngine(String windowTitle, int width, int height, boolean vSync, GameLogic gameLogic) throws Exception {
		displayWindow = new DisplayManager(windowTitle, width, height, vSync);
		this.gameLogic = gameLogic;
		timer = new Timer();
	}
	
	//Run class used to initialise the game engine and gameloop.
	@Override
	public void run() {
		try {
			init();
			gameLoop();
		} catch (Exception error) {
			System.err.println(error);
		} finally {
			cleanup();
		}
	}
	
	//Initialisation method that will call the initialisation functions for the window, the timer, the gamelogic, and any other integral classes.
	private void init() throws Exception {
		DisplayManager.init();
		timer.init();
		gameLogic.init(displayWindow);
	}
	
	//Gameloop function that will take care of all of the information for the game to render to the screen.
	private void gameLoop() {
		float elapsedTime; //Keeps track of amount of time elapsed in each time slice.
		float accumulator = 0f; //Keeps track of elapsed time per time slice compared to how long it should last.
		float interval = 1f / TARGET_UPS;
		
		boolean running = true;
		
		//Main while loop fop the game.
		while (running && !displayWindow.windowShouldClose()) {
			elapsedTime = timer.getElapsedTime();
			accumulator += elapsedTime;
			
			input();
			
			while (accumulator >= interval) {
				update(interval);
				accumulator -= interval;
			}
			
			render();
			
			if (!displayWindow.isVsync()) {
				sync();
			}
		}
	}
	
	//Syncs the frame rate to the in accordance to vsync, meaning its synced with the refresh rate of the graphics card.
	private void sync() {
		float loopSlot = 1f / TARGET_FPS;
		double endTime = timer.getLastLoopTime() + loopSlot; //Variable for how long each time slice should last.
		
		while (timer.getTime() < endTime) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException error) {
				System.err.println(error);
			}
		}
	}
	
	//Private method used to check for inputs from the gamelogic.
	private void input() {
		gameLogic.input(displayWindow);
	}
	
	//Private method used to check for updates from the gamelogic.
	private void update(float interval) {
		gameLogic.update(interval);
	}
	
	//Private method used to check for anything to be rendered from the gamelogic.
	private void render() {
		gameLogic.render(displayWindow);
		displayWindow.update();
	}
	
	//Cleans up all segments involved.
	private void cleanup() {
		gameLogic.cleanup();
	}
}
