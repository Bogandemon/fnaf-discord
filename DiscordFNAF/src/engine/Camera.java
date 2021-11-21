/*
 * Classname: Camera
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 22/11/2021
 * Description: Camera class that moves the camera within and around the 3D world space.
 */

package engine;

import org.joml.Vector3f;

public class Camera {
	private final Vector3f position; //Vector variable for the position of the camera.
	private final Vector3f rotation; //Rotation variable for the rotation of the camera.
	
	//Two constructors for creating a camera with/without a position and rotation vector.
	public Camera() {
		position = new Vector3f();
		rotation = new Vector3f();
	}
	
	public Camera(Vector3f position, Vector3f rotation) {
		this.position = position;
		this.rotation = rotation;
	}
	
	//Setter for position.
	public void setPosition(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
	}
	
	//Method that updates the position according to the offset.
	public void movePosition(float offsetX, float offsetY, float offsetZ) {
		if (offsetZ != 0) {
			position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
			position.z += (float) Math.cos(Math.toRadians(rotation.y)) * offsetZ; 
		}
		
		if (offsetX != 0) {
			position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0 * offsetX;
			position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
		}
		
		position.y += offsetY;
	}
	
	//Setter for the rotation.
	public void setRotation(float x, float y, float z) {
		rotation.x = x;
		rotation.y = y;
		rotation.z = z;
	}
	
	//Method that updates the rotation according to the offset.
	public void moveRotation(float offsetX, float offsetY, float offsetZ) {
		rotation.x += offsetX;
		rotation.y += offsetY;
		rotation.z += offsetZ;
	}
	
	//Getters for the variables.
	public Vector3f getPosition() {
		return position;
	}
	
	public Vector3f getRotation() {
		return rotation;
	}
}
