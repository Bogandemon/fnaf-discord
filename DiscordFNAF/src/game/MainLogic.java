/*
 * Classname: MainLogic
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 29/10/2021
 * Description: Logic class that combines the renderer and the game objects to create the logic for the game.
 */

//CURRENTLY NOT COMMENTED BECAUSE THIS WILL BE DRASTICALLY CHANGED.
//CURRENTLY NOT COMMENTED BECAUSE THIS WILL BE DRASTICALLY CHANGED.
//CURRENTLY NOT COMMENTED BECAUSE THIS WILL BE DRASTICALLY CHANGED.

package game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import engine.GameLogic;
import render.DisplayManager;
import render.MasterRenderer;
import render.Mesh;

public class MainLogic implements GameLogic {
	
	private float color = 0.0f;
	private final MasterRenderer renderer;
	private Mesh mesh;
	private int direction = 0;
	
	public MainLogic() {
		renderer = new MasterRenderer();
	}
	
	@Override
	public void init(DisplayManager displayWindow) throws Exception {
		renderer.init(displayWindow);
		
		float[] positions = new float[] {
			-0.5f,  0.5f, -1.05f,
		    -0.5f, -0.5f, -1.05f,
		     0.5f, -0.5f, -1.05f,
		     0.5f,  0.5f, -1.05f,
		};
		
		int[] indices = new int[] {
				0, 1, 3, 3, 1, 2,
		};
		
		float[] colours = new float[] {
			0.5f, 0.0f, 0.0f,
			0.0f, 0.5f, 0.0f,
			0.0f, 0.0f, 0.5f,
			0.0f, 0.5f, 0.5f,
		};
		
		mesh = new Mesh(positions, indices, colours);
	}
	
	@Override
	public void input(DisplayManager displayWindow) {
		if (displayWindow.isKeyPressed(GLFW_KEY_UP)) {
			direction = 1;
		}
		
		else if (displayWindow.isKeyPressed(GLFW_KEY_DOWN)) {
			direction = -1;
		}
		
		else {
			direction = 0;
		}
	}
	
	@Override
	public void update(float interval) {
		color += direction * 0.01f;
		if (color > 1) {
			color = 1.0f;
		}
		
		else if (color < 0) {
			color = 0.0f;
		}
	}
	
	@Override
	public void render(DisplayManager displayWindow) {
		displayWindow.setClearColor(color, color, color, 0.0f);
		renderer.render(displayWindow, mesh);
	}
	
	@Override
	public void cleanup() {
		renderer.cleanup();
		mesh.cleanup();
	}
}
