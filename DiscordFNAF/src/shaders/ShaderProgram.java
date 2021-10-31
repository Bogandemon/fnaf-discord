/*
 * Classname: ShaderProgram
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 29/10/2021
 * Description: Creates and combines the shaders used during the programmable graphics pipeline (vertex and fragment shaders).
 */

package shaders;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
	
	private final int programId; //Int variable for the main program shader.
	private int vertexShaderId; //Int variable for the vertex shader id.
	private int fragmentShaderId; //Int variable for the fragment shader id.
	
	//Constructor for creating the shader program and returning an error if needed.
	public ShaderProgram() throws Exception {
		programId = glCreateProgram();
		
		if (programId == 0) {
			throw new Exception("Could not create shader.");
		}
	}
	
	//Method for creating the vertex shader with the appropriate destination to the vs file.
	public void createVertexShader(String vertexCode) throws Exception {
		vertexShaderId = createShader(vertexCode, GL_VERTEX_SHADER);
	}
	
	//Method for creating the fragment shader with the appropriate destination to the fs file.
	public void createFragmentShader(String fragmentCode) throws Exception {
		fragmentShaderId = createShader(fragmentCode, GL_FRAGMENT_SHADER);
	}
	
	//Method for creating, compiling, and attaching the various shaders used during the programmable graphic pipeline.
	private int createShader(String shaderCode, int shaderType) throws Exception {
		int shaderId = glCreateShader(shaderType);
		
		if (shaderId == 0) {
			throw new Exception("Error creating shader. Shader type is " + shaderType);
		}
		
		glShaderSource(shaderId, shaderCode);
		glCompileShader(shaderId);
		
		if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
			throw new Exception("Shader does not compile. Info: " + glGetShaderInfoLog(shaderId, 1024));
		}
		
		glAttachShader(programId, shaderId); //Ataches the vertex/geometry/fragment shaders to the main shader program.
		
		return shaderId;
	}
	
	//Links all of the code and the shader program to the actual game.
	public void link() throws Exception {
		
		glLinkProgram(programId);
		
		if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
			throw new Exception("Error linking shader. Info: " + glGetProgramInfoLog(programId, 1024));
		}
		
		//Detaches both of the shaders after the linking process is completed.
		if (vertexShaderId != 0) {
			glDetachShader(programId, vertexShaderId);
		}
		
		if (fragmentShaderId != 0) {
			glDetachShader(programId, fragmentShaderId);
		}
		
		glValidateProgram(programId); //Validates the correctness of the shader program.
		
		if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
			System.err.println("The shader code is not validated. Info: " + glGetProgramInfoLog(programId, 1024));
		}
	}
	
	//Activates the shader program to be used for rendering.
	public void bind() {
		glUseProgram(programId);
	}
	
	//Unbinds the shader program.
	public void unbind() {
		glUseProgram(0);
	}
	
	//Cleanup method that deletes the shaders and the program.
	public void cleanup() {
		unbind();
		glDeleteShader(vertexShaderId);
		glDeleteShader(fragmentShaderId);
		
		if (programId != 0) {
			glDeleteProgram(programId);
		}
	}
}