/*
 * Classname: Mesh
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 27/11/2021
 * Description: Class that creates the vao and vbo objects (for further separation of concerns).
 */

package objects;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {
	private static final Vector3f DEFAULT_COLOUR = new Vector3f(1.0f, 0.0f, 1.0f);
	private final int vaoId; //VAO that combines all of the VBOs together as a wrapper.
	private final int vertexCount; //Int variable that holds the total number of vertices in a given mesh.
	private Texture texture;
	private Vector3f colour;
	private final List<Integer> vboIdList; //List that holds all of the vertex buffer objects.
	
	public Mesh(int[] indices) {
		
		colour = Mesh.DEFAULT_COLOUR;
		vboIdList = new ArrayList<Integer>();
		
		//Creates and binds the VAO to hold the two VBOs.
		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);
		
		IntBuffer indicesBuffer = null; //Buffer variable used to keep track of each indice of the vertices, to remove duplicates.
		vertexCount = indices.length;
		
		try {
			indicesBuffer = MemoryUtil.memAllocInt(indices.length);
			indicesBuffer.put(indices).flip();
		
			//Index VBO information.
			int VboId = glGenBuffers();
			vboIdList.add(VboId);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, VboId);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
		} 
		
		finally {
			if (indicesBuffer != null) {
				MemoryUtil.memFree(indicesBuffer);
			}
		}
		
		
	}
	
	public void loadVaos(float[] positions, float[] textureCoordinates, float[] normals) {
		FloatBuffer positionBuffer = null; //Buffer variable used for the positions of each vertex.
		FloatBuffer textureBuffer = null; //Buffer variable used for the positions of each vertex to map the texture.
		FloatBuffer normalsBuffer = null; //Buffer variable used for the normals of each face in the mesh.
		
		try {
			//Assigning buffers graphics card memory and placing appropriate values.
			positionBuffer = MemoryUtil.memAllocFloat(positions.length);
			positionBuffer.put(positions).flip();
			textureBuffer = MemoryUtil.memAllocFloat(textureCoordinates.length);
			textureBuffer.put(textureCoordinates).flip();
			normalsBuffer = MemoryUtil.memAllocFloat(normals.length);
			normalsBuffer.put(normals).flip();
			
			//Position VBO information.
			int VboId = glGenBuffers();
			vboIdList.add(VboId);
			glBindBuffer(GL_ARRAY_BUFFER, VboId);
			glBufferData(GL_ARRAY_BUFFER, positionBuffer, GL_STATIC_DRAW);
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
			
			//Texture VBO information.
			VboId = glGenBuffers();
			vboIdList.add(VboId);
			glBindBuffer(GL_ARRAY_BUFFER, VboId);
			glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW);
			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
			
			//Vertex normal VBO information.
			VboId = glGenBuffers();
			vboIdList.add(VboId);
			glBindBuffer(GL_ARRAY_BUFFER, VboId);
			glBufferData(GL_ARRAY_BUFFER, normalsBuffer, GL_STATIC_DRAW);
			glEnableVertexAttribArray(2);
			glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
			
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);
			
		//Finally statements that frees up the space in the off-heap memory after the buffers are finished being used.	
		} finally {
			if (positionBuffer != null) {
				MemoryUtil.memFree(positionBuffer);
			}
			
			if (textureBuffer != null) {
				MemoryUtil.memFree(textureBuffer);
			}
			
			if (normalsBuffer != null) {
				MemoryUtil.memFree(normalsBuffer);
			}
		}
	}
	
	//Method for rendering the vertex array object with the vertex buffer objects.
	public void render() {
		if (texture != null) {
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, texture.getId());
		}
		
		glBindVertexArray(getVaoId());
		glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
		glBindVertexArray(0);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	//Returns the VAO ID.
	public int getVaoId() {
		return vaoId;
	}
	
	//Returns the vertex count.
	public int getVertexCount() {
		return vertexCount;
	}
	
	//Method check for the texture.
	public boolean isTextured() {
		return this.texture != null;
	}
	
	//Setter and getter for the texture.
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	//Setter and getter for the colour.
	public void setColour(Vector3f colour) {
		this.colour = colour;
	}
	
	public Vector3f getColour() {
		return colour;
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
