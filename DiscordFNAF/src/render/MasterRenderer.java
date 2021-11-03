/*
 * Classname: MasterRenderer
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 29/10/2021
 * Description: Handles the main renderering logic to the screen for the graphics.
 */

package render;

import utility.Resources;
import shaders.ShaderProgram;

import org.joml.Matrix4f;

import engine.DisplayManager;
import engine.Transformation;
import objects.GameItem;

import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class MasterRenderer {
	
	private ShaderProgram shaderProgram; //ShaderProgram variable that is used to create and combine the shaders.
	private Transformation transformation;
	private static final float FOV = (float) Math.toRadians(60.0f);
	private static final float Z_NEAR = 0.01f;
	private static final float Z_FAR = 1000.f;
	
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
		
		shaderProgram.createUniform("projectionMatrix");
		shaderProgram.createUniform("worldMatrix");
	}
		
	
	//Currently clears the colour buffer.
	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	//Renders the displayWindow with the shaderProgram and vaos/vbos.
	public void render(DisplayManager displayWindow, GameItem[] gameItems) {
		clear();
		
		if (displayWindow.isResized()) {
			glViewport(0, 0, displayWindow.getWidth(), displayWindow.getHeight());
			displayWindow.setResized(false);	
		}
		
		shaderProgram.bind();
		Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, displayWindow.getWidth(), displayWindow.getHeight(), Z_NEAR, Z_FAR);
		shaderProgram.setUniform("projectionMatrix", projectionMatrix);
		
		for (GameItem gameItem : gameItems) {
			Matrix4f worldMatrix = transformation.getWorldMatrix(gameItem.getPosition(), gameItem.getRotation(), gameItem.getScale());
			shaderProgram.setUniform("worldMatrix", worldMatrix);
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
