/*
 * Classname: ShaderProgram
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 23/11/2021
 * Description: Creates and combines the shaders used during the programmable graphics pipeline (vertex shader, fragment shader, and uniforms).
 */

package shaders;

import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

public class ShaderProgram {
	
	private final int programId; //Int variable for the main program shader.
	private int vertexShaderId; //Int variable for the vertex shader id.
	private int fragmentShaderId; //Int variable for the fragment shader id.
	private final Map<String, Integer> uniforms; //Hashmap variable used to hold the projection and world matrices.
	
	//Constructor for creating the shader program and returning an error if needed.
	public ShaderProgram() throws Exception {
		programId = glCreateProgram();
		uniforms = new HashMap<>();
		
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
	
	//Method for creating the uniform mapping. Sets the name and gives it a location according to the program.
	public void createUniform(String uniformName) throws Exception {
		int uniformLocation = glGetUniformLocation(programId, uniformName);
		
		if (uniformLocation < 0) {
			throw new Exception("Could not find uniform " + uniformName);
		}
		
		uniforms.put(uniformName, uniformLocation);
	}
	
	//Sets the uniform by putting the uniform in memory, specifically on the stack (quicker and briefer access).
	public void setUniform(String uniformName, Matrix4f value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			glUniformMatrix4fv(uniforms.get(uniformName), false, value.get(stack.mallocFloat(16)));
		}
	}
	
	//Overloaded uniform setter method. Used primarily for the world view matrix.
	public void setUniform(String uniformName, int value) {
		glUniform1i(uniforms.get(uniformName), value);
	}
	
	//Overloaded uniform setter method used for 3D vectors (seen in master renderer).
	public void setUniform(String uniformName, Vector3f value) {
		glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
	}
}
