/*
 * Classname: Transformation
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 22/11/2021
 * Description: Transformation class that is used to work with the projection, view, and model view matrices. The model view matrix is the 
 * combination of the projection and view matrices. This combination is used to put the current environment in camera space. This will then 
 * be used later for camera space and clip space. 
 */

package engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import objects.GameItem;

public class Transformation {
	private final Matrix4f projectionMatrix; //Matrix for the projection matrix.
	private final Matrix4f viewMatrix; //Matrix for the view matrix. Is used in conjunction with the camera to scale objects conversely to the camera, such that movement can work.
	private final Matrix4f modelViewMatrix; //Matrix for the model view matrix. Converts the world space to camera space and as such combines the projection and view matrix.
	
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
	
	//Method for retrieving the model view matrix, which will be done for each game item (translation, rotation, and scaling).
	public Matrix4f getModelViewMatrix(GameItem gameItem, Matrix4f viewMatrix) {
		Vector3f gIRotation = gameItem.getRotation(); //Vector that stores the rotation of the current game item being translated.
		
		modelViewMatrix.set(viewMatrix).translate(gameItem.getPosition()).
					    rotateX((float) Math.toRadians(-gIRotation.x)).
					    rotateY((float) Math.toRadians(-gIRotation.y)).
					    rotateZ((float) Math.toRadians(-gIRotation.z)).
					    scale(gameItem.getScale());
		
		return modelViewMatrix;
	}
	
	//Method for retrieving the view matrix. Is used in conjunction with the camera to make all objects move in accordance to inputs.
	public Matrix4f getViewMatrix(Camera camera) {
		
		//Vectors for retrieving the camera position and rotaiton.
		Vector3f camPosition= camera.getPosition();
		Vector3f camRotation = camera.getRotation();
		
		viewMatrix.identity();
		viewMatrix.rotate((float) Math.toRadians(camRotation.x), new Vector3f(1, 0, 0))
				  .rotate((float) Math.toRadians(camRotation.y), new Vector3f(0, 1, 0));
		viewMatrix.translate(-camPosition.x, -camPosition.y, -camPosition.z);
		return viewMatrix;
	}
}
