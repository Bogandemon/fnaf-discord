/*
 * Classname: GameEngine
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 29/10/2021
 * Description: Major class that houses the primary while loop and contacts all other classes for a compiled project, such as the renderer, the game logic, and the display manager.
 */

package engine;

import render.DisplayManager;

public class GameEngine implements Runnable {
	
	public static final int TARGET_FPS = 60;
	public static final int TARGET_UPS = 30;
	
	private final DisplayManager displayWindow;
	private final GameLogic gameLogic;
	private final Timer timer;
	
	public GameEngine(String windowTitle, int width, int height, boolean vSync, GameLogic gameLogic) throws Exception {
		displayWindow = new DisplayManager(windowTitle, width, height, vSync);
		this.gameLogic = gameLogic;
		timer = new Timer();
	}
	
	@Override
	public void run() {
		try {
			init();
			gameLoop();
		} catch (Exception error) {
			System.err.println(error);
		}
	}
	
	private void init() throws Exception {
		DisplayManager.init();
		timer.init();
		gameLogic.init();
	}
	
	private void gameLoop() {
		float elapsedTime;
		float accumulator = 0f;
		float interval = 1f / TARGET_UPS;
		
		boolean running = true;
		
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
	
	private void sync() {
		float loopSlot = 1f / TARGET_FPS;
		double endTime = timer.getLastLoopTime() + loopSlot;
		
		while (timer.getTime() < endTime) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException error) {
				System.err.println(error);
			}
		}
	}
	
	private void input() {
		gameLogic.input(displayWindow);
	}
	
	private void update(float interval) {
		gameLogic.update(interval);
	}
	
	private void render() {
		gameLogic.render(displayWindow);
		displayWindow.update();
	}
}
