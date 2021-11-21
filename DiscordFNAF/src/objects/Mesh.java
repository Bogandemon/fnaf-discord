/*
 * Classname: Mesh
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 22/11/2021
 * Description: Class that creates the vao and vbo objects (for further separation of concerns).
 */

package objects;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {
	private final int vaoId; //VAO that combines all of the VBOs together as a wrapper.
	private final int vertexCount; //Int variable that holds the total number of vertices in a given mesh.
	private final Texture texture; //Texture variable used to apply a 2D image to a mesh.
	private final List<Integer> vboIdList; //List that holds all of the vertex buffer objects.
	
	public Mesh(float[] positions, int[] indices, float[] textureCoordinates, Texture texture) {
		FloatBuffer positionBuffer = null; //Buffer variable used for the positions of each vertex.
		IntBuffer indicesBuffer = null; //Buffer variable used to keep track of each indice of the vertices, to remove duplicates.
		FloatBuffer textureBuffer = null; //Buffer variable used for the positions of each vertex to map the texture.
		
		try {
			this.texture = texture;
			vertexCount = indices.length;
			vboIdList = new ArrayList<Integer>();
			
			//Creates and binds the VAO to hold the two VBOs.
			vaoId = glGenVertexArrays();
			glBindVertexArray(vaoId);
			
			//Assigning buffers graphics card memory and placing appropriate values.
			positionBuffer = MemoryUtil.memAllocFloat(positions.length);
			positionBuffer.put(positions).flip();
			indicesBuffer = MemoryUtil.memAllocInt(indices.length);
			indicesBuffer.put(indices).flip();
			textureBuffer = MemoryUtil.memAllocFloat(textureCoordinates.length);
			textureBuffer.put(textureCoordinates).flip();
			
			//Position VBO information.
			int VboId = glGenBuffers();
			vboIdList.add(VboId);
			glBindBuffer(GL_ARRAY_BUFFER, VboId);
			glBufferData(GL_ARRAY_BUFFER, positionBuffer, GL_STATIC_DRAW);
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
			
			//Index VBO information.
			VboId = glGenBuffers();
			vboIdList.add(VboId);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, VboId);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
			
			//Texture VBO information.
			VboId = glGenBuffers();
			vboIdList.add(VboId);
			glBindBuffer(GL_ARRAY_BUFFER, VboId);
			glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW);
			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
			
			
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);
			
		//Finally statements that frees up the space in the off-heap memory after the buffers are finished being used.	
		} finally {
			if (positionBuffer != null) {
				MemoryUtil.memFree(positionBuffer);
			}
			
			if (indicesBuffer != null) {
				MemoryUtil.memFree(indicesBuffer);
			}
			
			if (textureBuffer != null) {
				MemoryUtil.memFree(textureBuffer);
			}
		}
	}
	
	//Method for rendering the vertex array object with the vertex buffer objects.
	public void render() {
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, texture.getId());
		
		glBindVertexArray(getVaoId());
		glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
		glBindVertexArray(0);
	}
	
	//Returns the VAO ID.
	public int getVaoId() {
		return vaoId;
	}
	
	//Returns the vertex count.
	public int getVertexCount() {
		return vertexCount;
	}
	
	//Cleanup method for the mesh.
	public void cleanup() {
		//Disables, unbinds, and deletes the VBO and VAOs.
		glDisableVertexAttribArray(0);
				
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		for (int i=0; i<vboIdList.size(); i++) {
			glDeleteBuffers(i);
		}
		
		texture.cleanup(); //Cleans up the texture class.
				
		glBindVertexArray(0);
		glDeleteVertexArrays(vaoId);
	}
}
