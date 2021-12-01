/*
 * Classname: MasterRenderer
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 22/11/2021
 * Description: Handles the main renderering logic to the screen for the graphics.
 */

package engine;

import utility.Resources;

import org.joml.Matrix4f;

import objects.GameItem;

import static org.lwjgl.opengl.GL11.*;

public class MasterRenderer {
	
	private ShaderProgram shaderProgram; //ShaderProgram variable that is used to create and combine the shaders.
	private Transformation transformation; //Transformation used which combines both the FOV, Z_NEAR, and Z_FAR variables and the shaderProgram to create the two required matrices.
	private static final float FOV = (float) Math.toRadians(60.0f); //Float variable for the field of view (how wide the camera can view).
	private static final float Z_NEAR = 0.01f; //Float variable that defines what is considered the minimum distance of the truncated pyramid camera.
	private static final float Z_FAR = 1000.f; //Float variable that defines what is considered the maximum distance for the pyramid.
	
	//Constructor which instantiates a new transformation.
	public MasterRenderer() {
		transformation = new Transformation();
	}
	
	//Initialises the master renderer (creates shader program and binds vao and vbo to be used).
	public void init(DisplayManager displayWindow) throws Exception {
		
		//Creates shader program and creates/links the both the vertex and fragment shaders.
		shaderProgram = new ShaderProgram();
		shaderProgram.createVertexShader(Resources.loadResource("/vertex.vs"));
		shaderProgram.createFragmentShader(Resources.loadResource("/fragment.fs"));
		shaderProgram.link();
		
		//Creates the two uniform matrices and sets them up in the hashmap for accessing in the shader program.
		shaderProgram.createUniform("projectionMatrix");
		shaderProgram.createUniform("modelViewMatrix");
		shaderProgram.createUniform("texture_sampler");
		shaderProgram.createUniform("colour");
		shaderProgram.createUniform("useColour");
	}
		
	
	//Currently clears the colour buffer.
	public void clear() {
		glClearColor(0, 0, 0, 1);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	//Renders the displayWindow with the shaderProgram and vaos/vbos.
	public void render(DisplayManager displayWindow, GameItem[] gameItems, Camera camera) {
		clear();
		
		if (displayWindow.isResized()) {
			glViewport(0, 0, displayWindow.getWidth(), displayWindow.getHeight());
			displayWindow.setResized(false);
		}
		
		//Renders the projection matrix (sets up the camera to appropriately handle scaling and aspect ratio/FOV concepts).
		shaderProgram.bind();
		Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, displayWindow.getWidth(), displayWindow.getHeight(), Z_NEAR, Z_FAR);
		
		shaderProgram.setUniform("projectionMatrix", projectionMatrix);
		shaderProgram.setUniform("texture_sampler", 0); //CHANGE 0 LATER FOR MULTI-TEXTURING.
		
		//Obtains the view matrix (for the camera movement).
		Matrix4f viewMatrix = transformation.getViewMatrix(camera);
		
		//For loop that renders each specific object into the game according to their own specific world matrix.
		for (GameItem gameItem : gameItems) {
			Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
			
			shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
			shaderProgram.setUniform("colour", gameItem.getMesh().getColour());
			shaderProgram.setUniform("useColour", gameItem.getMesh().isTextured() ? 0 : 1);
			
			gameItem.getMesh().render();
		}
		
		shaderProgram.unbind();
	}
	
	//Cleanup method for when the gameloop finishes.
	public void cleanup() {
		
		//Cleans up the main shader program.
		if (shaderProgram != null) {
			shaderProgram.cleanup();
		}
	}
}
