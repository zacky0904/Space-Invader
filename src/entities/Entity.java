package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;

public class Entity {
	private TexturedModel model;
	protected Vector3f position;
	private float rotX;
	private float rotY;
	private float rotZ;
	private float scale;
	
	private float brightness;

	private Vector3f boundbox;
	
	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			float scale) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.brightness = 0;
		boundbox = new Vector3f(1,1,1);

	}
	
	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			float scale, float brightness) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.brightness = brightness;
		boundbox = new Vector3f(1,1,1);

	}

	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public Vector3f getBoundbox() {
		return boundbox;
	}

	public void setBoundBox(float x, float y, float z) {
		boundbox.setX(x);
		boundbox.setY(y);
		boundbox.setZ(z);
	}
	
	public float getBrightness() {
		return brightness;
	}

	public void setBrightness(float brightness) {
		this.brightness = brightness;
	}
	
	public boolean collisionTest(Entity entity) {
		Vector3f delta = new Vector3f();
		Vector3f.sub(position, entity.position, delta);
		
		float boxX = (boundbox.x + entity.boundbox.x)/4;
		float boxY = (boundbox.y + entity.boundbox.y)/4;
		float boxZ = (boundbox.z + entity.boundbox.z)/4;
	
		return (Math.abs(delta.x) < boxX && Math.abs(delta.y) < boxY && Math.abs(delta.z) < boxZ);		
		
	}

}
