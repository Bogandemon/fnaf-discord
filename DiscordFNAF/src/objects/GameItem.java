/*
 * Classname: GameItem
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 10/11/2021
 * Description: Class that creates a specific mesh with information about its size, rotation, and scaling (z-coordinate). The mesh handles 
 * the game objects memory allocation and colours, whilst GameItem handles how it interacts with the world matrix. Each object will have a
 * different GameItem instance and will be kept in an array.
 */

package objects;

import org.joml.Vector3f;

public class GameItem {
	private final Mesh mesh; //Mesh variable that keeps track of colour and vertices.
	private final Vector3f position; //Vector variable for the objects positioning in the world matrix.
	private final Vector3f rotation; //Vector variable for the objects rotation in the world matrix.
	private float scale; //Float variable that keeps track of its z-coordinate.
	
	public GameItem(Mesh mesh) {
		this.mesh = mesh;
		position = new Vector3f();
		rotation = new Vector3f();
		scale = 1;
	}
	
	//Setters for the position, rotation, and scaling.
	public void setPosition(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}
	
	public void setRotation(float x, float y, float z) {
		this.rotation.x = x;
		this.rotation.y = y;
		this.rotation.z = z;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	//Getters for all of the variables.
	public Mesh getMesh() {
		return mesh;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public Vector3f getRotation() {
		return rotation;
	}
	
	public float getScale() {
		return scale;
	}
}
