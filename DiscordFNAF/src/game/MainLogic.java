/*
 * Classname: MainLogic
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 1/12/2021
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
import engine.MasterRenderer;
import engine.MouseInput;
import interfaces.GameLogic;
import objects.GameItem;
import objects.Mesh;
import objects.ModelLoader;
import objects.Texture;

public class MainLogic implements GameLogic {

	private final MasterRenderer renderer;
	private final Vector3f cameraInc;
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
		
		Mesh mesh = ModelLoader.loadMesh("models/cube.obj");
		Mesh mesh2 = ModelLoader.loadMesh("models/Basketball.obj");
		Mesh mesh3 = ModelLoader.loadMesh("models/BasketballHoop.obj");
		
		Texture texture = new Texture("textures/grassblock.png");
		Texture texture2 = new Texture("textures/Basketball.jpg");
		Texture texture3 = new Texture("textures/HoopColour.png");
		
		mesh.setTexture(texture);
		mesh2.setTexture(texture2);
		mesh3.setTexture(texture3);
		
		GameItem gameItem = new GameItem(mesh);
		gameItem.setScale(0.5f);
		gameItem.setPosition(0, 0, -2);
		
		GameItem gameItem2 = new GameItem(mesh2);
		gameItem2.setScale(0.4f);
		gameItem2.setPosition(0, 0, -4);
		
		GameItem gameItem3 = new GameItem(mesh3);
		gameItem3.setScale(0.4f);
		gameItem3.setPosition(0, 0, -4);
		
		gameItems = new GameItem[] {gameItem, gameItem2, gameItem3};
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
		camera.movePosition(cameraInc.x * 0.10f,
			   cameraInc.y * 0.10f,
			   cameraInc.z * 0.10f);
		
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
