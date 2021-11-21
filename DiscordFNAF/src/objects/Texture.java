/*
 * Classname: Texture
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 22/11/2021
 * Description: Texture class used to wrap a 2D image onto a 3D model/mesh in the game environment.
 */

package objects;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
	
	private int id; //Int variable that keeps track of the ID the texture has (to map it appropriately).
	
	public Texture (String fileName) throws Exception {
		loadTexture(fileName);
	}
	
	//Method for loading the texture from a file to be used and mapped onto a model.
	public static int loadTexture(String fileName) throws Exception {
		int width = 0; //Variable for the width of the image.
		int height = 0; //Variable for the height of the image.
		ByteBuffer buffer; //Variable for loading the actual image into a buffer.
		
		//Pushes the texture image onto the stack, as it will need to be quickly accessed.
		try (MemoryStack stack = MemoryStack.stackPush()) {
			
			//Int buffers for obtaining the width and height of the actual image.
			IntBuffer imWidth = stack.mallocInt(1);
			IntBuffer imHeight = stack.mallocInt(1);
			
			IntBuffer channels = stack.mallocInt(1); //Int buffer that obtains how many channels the image has (such as 4 for RGBA).
			
			//Loads the image into the actual buffer.
			buffer = stbi_load(fileName, imWidth, imHeight, channels, 4);
			
			if (buffer == null) {
				throw new Exception("Image file " + fileName + " did not load. Reason given: " + stbi_failure_reason());
			}
			
			width = imWidth.get();
			height = imHeight.get();
		}
		
		//Creates and binds the texture appropriately.
		int textureId = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureId);
		
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1); //Tells openGL how to unpack the actual texture.
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer); //Uploads the actual texture image.
		
		glGenerateMipmap(GL_TEXTURE_2D); //Creates a mipmap for the texture (group of decreasing resolution images for the texture used when scaling).
		
		stbi_image_free(buffer); //Frees the memory for the image.
		
		return textureId;
	}
	
	//Getter for the ID.
	public int getId() {
		return id;
	}
	
	//Method for the texture, which just deletes the actual texture.
	public void cleanup() {
		glDeleteTextures(id);
	}
}
