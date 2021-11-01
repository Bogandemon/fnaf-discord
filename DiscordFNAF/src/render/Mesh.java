/*
 * Classname: Mesh
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 29/10/2021
 * Description: Class that creates the vao and vbo objects (for further separation of concerns).
 */

package render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {
	private final int vaoId; //VAO that combines all of the VBOs together as a wrapper.
	private final int posVboId; //VBO that provides the position of all of the vertices to be mapped onto the screen.
	private final int idxVboId; //VBO that provides the index of each vertex in a list (so that no vertices are duplicated).
	private final int colourVboId; //VBO that provides colour attributes to each vertex.
	private final int vertexCount; //Int variable that holds the total number of vertices in a given mesh.
	
	public Mesh(float[] positions, int[] indices, float[] colours) {
		FloatBuffer positionBuffer = null;
		IntBuffer indicesBuffer = null;
		FloatBuffer colourBuffer = null;
		
		try {
			vertexCount = indices.length;
			
			//Creates and binds the VAO to hold the two VBOs.
			vaoId = glGenVertexArrays();
			glBindVertexArray(vaoId);
			
			//Assigning buffers graphics card memory and placing appropriate values.
			positionBuffer = MemoryUtil.memAllocFloat(positions.length);
			positionBuffer.put(positions).flip();
			indicesBuffer = MemoryUtil.memAllocInt(indices.length);
			indicesBuffer.put(indices).flip();
			colourBuffer = MemoryUtil.memAllocFloat(colours.length);
			colourBuffer.put(colours).flip();
			
			//Position VBO information.
			posVboId = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, posVboId);
			glBufferData(GL_ARRAY_BUFFER, positionBuffer, GL_STATIC_DRAW);
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
			
			//Index VBO information.
			idxVboId = glGenBuffers();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
			
			//Colour VBO information.
			colourVboId = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, colourVboId);
			glBufferData(GL_ARRAY_BUFFER, colourBuffer, GL_STATIC_DRAW);
			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
			
			
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);
		//Finally statements that frees up the space in the off-heap memory after the verticesBuffer is finished being used.	
		} finally {
			if (positionBuffer != null) {
				MemoryUtil.memFree(positionBuffer);
			}
		}
	}
	
	//Returns the VAO ID.
	public int getVaoId() {
		return vaoId;
	}
	
	//Returns the vertex count.
	public int getVertexCount() {
		return vertexCount;
	}
	
	public void cleanup() {
		//Disables, unbinds, and deletes the VBO and VAOs.
		glDisableVertexAttribArray(0);
				
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(posVboId);
		glDeleteBuffers(idxVboId);
				
		glBindVertexArray(0);
		glDeleteVertexArrays(vaoId);
	}
}
