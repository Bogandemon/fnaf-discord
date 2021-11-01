/*
 * Classname: MasterRenderer
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 29/10/2021
 * Description: Handles the main renderering logic to the screen for the graphics.
 */

package render;

import java.nio.FloatBuffer;
import org.lwjgl.system.MemoryUtil;
import utility.Resources;
import shaders.ShaderProgram;

import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL30.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

public class MasterRenderer {
	
	private int vaoId; //Int variable for the vaoId (Vertex Array Object).
	private int vboId; //Int variable for the vboId (Vertex Buffer Object).
	private ShaderProgram shaderProgram; //ShaderProgram variable that is used to create and combine the shaders.
	
	public MasterRenderer() {
		
	}
	
	//Initialises the master renderer (creates shader program and binds vao and vbo to be used).
	public void init() throws Exception {
		
		//Creates shader program and creates/links the both the vertex and fragment shaders.
		shaderProgram = new ShaderProgram();
		shaderProgram.createVertexShader(Resources.loadResource("/vertex.vs"));
		shaderProgram.createFragmentShader(Resources.loadResource("/fragment.fs"));
		shaderProgram.link();
		
		//Triangle vertices.
		float[] vertices = new float[] {
			0.0f, 0.5f, 0.0f,
		    -0.5f, -0.5f, 0.0f,
		    0.5f, -0.5f, 0.0f
		};
		
		FloatBuffer verticesBuffer = null; //Variable that will be allocated onto the graphics card memory.
		
		try {
			
			//Allocates space in memory and places the triangle vertices in the buffer.
			verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
			verticesBuffer.put(vertices).flip();
		
			vaoId = glGenVertexArrays();
			glBindVertexArray(vaoId);
		
			vboId = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
			glEnableVertexAttribArray(0);
		
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);
			
		//Finally statements that frees up the space in the off-heap memory after the verticesBuffer is finished being used.	
		} finally {
			if (verticesBuffer != null) {
			MemoryUtil.memFree(verticesBuffer);
			}
		}
	}
	
	//Currently clears the colour buffer.
	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	//Renders the displayWindow with the shaderProgram and vaos/vbos.
	public void render(DisplayManager displayWindow) {
		clear();
		
		if (displayWindow.isResized()) {
			glViewport(0, 0, displayWindow.getWidth(), displayWindow.getHeight());
			displayWindow.setResized(false);	
		}
		
		shaderProgram.bind();
		
		glBindVertexArray(vaoId);
		glDrawArrays(GL_TRIANGLES, 0, 3);
		glBindVertexArray(0);
		
		shaderProgram.unbind();
	}
	
	//Cleanup method for when the gameloop finishes.
	public void cleanup() {
		
		//Cleans up the main shader program.
		if (shaderProgram != null) {
			shaderProgram.cleanup();
		}
		
		//Disables, unbinds, and deletes the VBO and VAOs.
		glDisableVertexAttribArray(0);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(vboId);
		
		glBindVertexArray(0);
		glDeleteVertexArrays(vaoId);
	}
}
