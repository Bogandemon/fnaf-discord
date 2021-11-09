/*
 * Classname: MainLogic
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 10/11/2021
 * Description: Logic class that combines the renderer and the game objects to create the logic for the game.
 */

//CURRENTLY NOT COMMENTED BECAUSE THIS WILL BE DRASTICALLY CHANGED.
//CURRENTLY NOT COMMENTED BECAUSE THIS WILL BE DRASTICALLY CHANGED.
//CURRENTLY NOT COMMENTED BECAUSE THIS WILL BE DRASTICALLY CHANGED.

package game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import engine.DisplayManager;
import interfaces.GameLogic;
import objects.GameItem;
import objects.Mesh;
import render.MasterRenderer;

public class MainLogic implements GameLogic {
	
	private float color = 0.0f;
	private final MasterRenderer renderer;
	private Mesh mesh;
	private int direction = 0;
	private GameItem[] gameItems;
	
	public MainLogic() {
		renderer = new MasterRenderer();
	}
	
	@Override
	public void init(DisplayManager displayWindow) throws Exception {
		renderer.init(displayWindow);
		
		float[] positions = new float[] {
			-0.5f,  0.5f,  0.5f,
		    -0.5f, -0.5f,  0.5f,
		     0.5f, -0.5f,  0.5f,
		     0.5f,  0.5f,  0.5f,
		    -0.5f,  0.5f, -0.5f,
			 0.5f,  0.5f, -0.5f,
			-0.5f, -0.5f, -0.5f,
			 0.5f, -0.5f, -0.5f,
		};
		
		int[] indices = new int[] {
				0, 1, 3, 3, 1, 2,
				4, 0, 3, 5, 4, 3,
				3, 2, 7, 5, 3, 7,
				6, 1, 0, 6, 0, 4,
				2, 1, 6, 2, 6, 7,
				7, 6, 4, 7, 4, 5,
		};
		
		float[] colours = new float[] {
			0.5f, 0.0f, 0.0f,
			0.0f, 0.5f, 0.0f,
			0.0f, 0.0f, 0.5f,
			0.0f, 0.5f, 0.5f,
			0.5f, 0.0f, 0.0f,
			0.0f, 0.5f, 0.0f,
			0.0f, 0.0f, 0.5f,
			0.0f, 0.5f, 0.5f,
		};
		
		mesh = new Mesh(positions, indices, colours);
		GameItem gameItem = new GameItem(mesh);
		gameItem.setPosition(0, 0, -2);
		gameItems = new GameItem[] {gameItem};
	}
	
	@Override
	public void input(DisplayManager displayWindow) {
	}
	
	@Override
	public void update(float interval) {
		for (GameItem gameItem : gameItems) {
			float rotation = gameItem.getRotation().x + 1.5f;
			
			if (rotation > 360) {
				rotation = 0;
			}
			
			gameItem.setRotation(rotation, rotation, rotation);
		}
	}
	
	@Override
	public void render(DisplayManager displayWindow) {
		displayWindow.setClearColor(color, color, color, 0.0f);
		renderer.render(displayWindow, gameItems);
	}
	
	@Override
	public void cleanup() {
		renderer.cleanup();
		mesh.cleanup();
	}
}
