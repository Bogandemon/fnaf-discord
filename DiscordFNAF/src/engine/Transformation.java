/*
 * Classname: Transformation
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 10/11/2021
 * Description: Transformation class that is used to combine both the projection and world matrices. The world matrix is the combination 
 * of the translation, rotation, and scale matrices which are combined to convert from object space to world space. This will then be used
 * later for camera space and clip space. 
 */

package engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {
	private final Matrix4f projectionMatrix; //Matrix for the projection matrix.
	private final Matrix4f worldMatrix; //Matrix for the world matrix.
	
	//Creates the transformation object and instantiates both matrices.
	public Transformation() {
		projectionMatrix = new Matrix4f();
		worldMatrix = new Matrix4f();
	}
	
	//Retrieves the projection matrix, which is used to take care of the aspect ratio and for scaling objects appropriately.
	public final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
		return projectionMatrix.setPerspective(fov, width/height, zNear, zFar);
	}
	
	//Retrieves and properly creates the world matrix, which takes care of translation, rotation, and scaling.
	public Matrix4f getWorldMatrix(Vector3f offset, Vector3f rotation, float scale) {
		return worldMatrix.translation(offset).
			rotateX((float)Math.toRadians(rotation.x)).
			rotateY((float)Math.toRadians(rotation.y)).
			rotateZ((float)Math.toRadians(rotation.z)).
			scale(scale);
	}
}
