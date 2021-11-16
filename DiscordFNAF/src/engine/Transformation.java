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

import objects.GameItem;

public class Transformation {
	private final Matrix4f projectionMatrix; //Matrix for the projection matrix.
	private final Matrix4f viewMatrix;
	private final Matrix4f modelViewMatrix;
	
	//Creates the transformation object and instantiates both matrices.
	public Transformation() {
		projectionMatrix = new Matrix4f();
		viewMatrix = new Matrix4f();
		modelViewMatrix = new Matrix4f();
	}
	
	//Retrieves the projection matrix, which is used to take care of the aspect ratio and for scaling objects appropriately.
	public final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
		return projectionMatrix.setPerspective(fov, width/height, zNear, zFar);
	}
	
	public Matrix4f getModelViewMatrix(GameItem gameItem, Matrix4f viewMatrix) {
		Vector3f gIRotation = gameItem.getRotation();
		
		modelViewMatrix.set(viewMatrix).translate(gameItem.getPosition()).
					    rotateX((float) Math.toRadians(-gIRotation.x)).
					    rotateY((float) Math.toRadians(-gIRotation.y)).
					    rotateZ((float) Math.toRadians(-gIRotation.z)).
					    scale(gameItem.getScale());
		
		return modelViewMatrix;
	}
	
	public Matrix4f getViewMatrix(Camera camera) {
		Vector3f camPosition= camera.getPosition();
		Vector3f camRotation = camera.getRotation();
		
		viewMatrix.identity();
		viewMatrix.rotate((float) Math.toRadians(camRotation.x), new Vector3f(1, 0, 0))
				  .rotate((float) Math.toRadians(camRotation.y), new Vector3f(0, 1, 0));
		viewMatrix.translate(-camPosition.x, -camPosition.y, -camPosition.z);
		return viewMatrix;
	}
}
