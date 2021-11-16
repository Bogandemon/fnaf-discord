/*
 * Classname: MainLogic
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 16/11/2021
 * Description: Logic class that combines the renderer and the game objects to create the logic for the game.
 */

//CURRENTLY NOT COMMENTED BECAUSE THIS WILL BE DRASTICALLY CHANGED.
//CURRENTLY NOT COMMENTED BECAUSE THIS WILL BE DRASTICALLY CHANGED.
//CURRENTLY NOT COMMENTED BECAUSE THIS WILL BE DRASTICALLY CHANGED.

package game;

import org.joml.Vector2f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;

import engine.Camera;
import engine.DisplayManager;
import engine.MouseInput;
import interfaces.GameLogic;
import objects.GameItem;
import objects.Mesh;
import objects.Texture;
import render.MasterRenderer;

public class MainLogic implements GameLogic {

	private final MasterRenderer renderer;
	private final Vector3f cameraInc;
	private Mesh mesh;
	private GameItem[] gameItems;
	private Camera camera;
	
	public MainLogic() {
		renderer = new MasterRenderer();
		camera = new Camera();
		cameraInc = new Vector3f();
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
			 
			-0.5f, 0.5f,  -0.5f,
			 0.5f, 0.5f,  -0.5f,
			-0.5f, 0.5f,   0.5f,
			 0.5f, 0.5f,   0.5f,
			  
			 0.5f,  0.5f,  0.5f,
			 0.5f, -0.5f,  0.5f,
			  
			-0.5f,  0.5f,  0.5f,
			-0.5f, -0.5f,  0.5f,
			  
			-0.5f, -0.5f, -0.5f,
			 0.5f, -0.5f, -0.5f,
			-0.5f, -0.5f,  0.5f,
			 0.5f, -0.5f, 0.5f,
			 
		};
		
		int[] indices = new int[] {
				0, 1, 3, 3, 1, 2,
				8, 10, 11, 9, 8, 11,
				12, 13, 7, 5, 12, 7,
				14, 15, 6, 4, 14, 6,
				16, 18, 19, 17, 16, 19,
				4, 6, 7, 5, 4, 7,
		};
		
		float[] textureCoordinates = new float[] {
				0.0f, 0.0f,
	            0.0f, 0.5f,
	            0.5f, 0.5f,
	            0.5f, 0.0f,
	            
	            0.0f, 0.0f,
	            0.5f, 0.0f,
	            0.0f, 0.5f,
	            0.5f, 0.5f,
	            
	            // For text coords in top face
	            0.0f, 0.5f,
	            0.5f, 0.5f,
	            0.0f, 1.0f,
	            0.5f, 1.0f,

	            // For text coords in right face
	            0.0f, 0.0f,
	            0.0f, 0.5f,

	            // For text coords in left face
	            0.5f, 0.0f,
	            0.5f, 0.5f,

	            // For text coords in bottom face
	            0.5f, 0.0f,
	            1.0f, 0.0f,
	            0.5f, 0.5f,
	            1.0f, 0.5f,
		};
		
		
		Texture texture = new Texture("textures/grassblock.png");
		mesh = new Mesh(positions, indices, textureCoordinates, texture);
		GameItem gameItem1 = new GameItem(mesh);
		gameItem1.setPosition(-2, 0, -5);
		
		mesh = new Mesh(positions, indices, textureCoordinates, texture);
		GameItem gameItem2 = new GameItem(mesh);
		gameItem2.setPosition(2, 0, -5);
		
		mesh = new Mesh(positions, indices, textureCoordinates, texture);
		GameItem gameItem3 = new GameItem(mesh);
		gameItem3.setPosition(0, 2, -5);
		
		mesh = new Mesh(positions, indices, textureCoordinates, texture);
		GameItem gameItem4 = new GameItem(mesh);
		gameItem4.setPosition(0, -2, -5);
		
		gameItems = new GameItem[] {gameItem1, gameItem2, gameItem3, gameItem4};
	}
	
	@Override
	public void input(DisplayManager displayWindow, MouseInput mouseInput) {
		cameraInc.set(0, 0, 0);
		
		if (displayWindow.isKeyPressed(GLFW_KEY_Z)) {
			cameraInc.z = 1;
		}
		
		else if (displayWindow.isKeyPressed(GLFW_KEY_X)) {
			cameraInc.z = -1;
		}
		
		if (displayWindow.isKeyPressed(GLFW_KEY_A)) {
			cameraInc.x = -1;
		}
		
		else if (displayWindow.isKeyPressed(GLFW_KEY_D)) {
			cameraInc.x = 1;
		}
		
		if (displayWindow.isKeyPressed(GLFW_KEY_W)) {
			cameraInc.y = 1;
		}
		
		else if (displayWindow.isKeyPressed(GLFW_KEY_S)) {
			cameraInc.y = -1;
		}
	}
	
	@Override
	public void update(float interval, MouseInput mouseInput) {
		camera.movePosition(cameraInc.x * 0.05f,
			   cameraInc.y * 0.05f,
			   cameraInc.z * 0.05f);
		
		if (mouseInput.isRightPressed()) {
			Vector2f rotVec = mouseInput.getDisplayVector();
			camera.moveRotation(rotVec.x * 0.2f, rotVec.y * 0.2f, 0);
		}
		
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
		renderer.render(displayWindow, gameItems, camera);
	}
	
	@Override
	public void cleanup() {
		renderer.cleanup();
		
		for (GameItem gameItem : gameItems) {
			gameItem.getMesh().cleanup();
		}
		
	}
}
