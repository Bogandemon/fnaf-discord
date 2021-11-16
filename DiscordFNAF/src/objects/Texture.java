/*
 * Classname: Texture
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 16/11/2021
 * Description:
 */

package objects;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
	
	private int id;
	
	public Texture (String fileName) throws Exception {
		loadTexture(fileName);
	}
	
	public static int loadTexture(String fileName) throws Exception {
		int width = 0;
		int height = 0;
		ByteBuffer buffer;
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer imWidth = stack.mallocInt(1);
			IntBuffer imHeight = stack.mallocInt(1);
			IntBuffer channels = stack.mallocInt(1);
			
			buffer = stbi_load(fileName, imWidth, imHeight, channels, 4);
			
			if (buffer == null) {
				throw new Exception("Image file " + fileName + " did not load. Reason given: " + stbi_failure_reason());
			}
			
			width = imWidth.get();
			height = imHeight.get();
		}
		
		int textureId = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureId);
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		
		glGenerateMipmap(GL_TEXTURE_2D);
		
		stbi_image_free(buffer);
		
		return textureId;
	}
	
	public int getId() {
		return id;
	}
	
	public void cleanup() {
		glDeleteTextures(id);
	}
}
