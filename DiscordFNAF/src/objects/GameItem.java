package objects;

import org.joml.Vector3f;
import render.Mesh;

public class GameItem {
	private final Mesh mesh;
	private final Vector3f position;
	private final Vector3f rotation;
	private float scale;
	
	public GameItem(Mesh mesh) {
		this.mesh = mesh;
		position = new Vector3f();
		rotation = new Vector3f();
		scale = 1;
	}
	
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
