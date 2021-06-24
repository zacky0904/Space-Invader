package camera;

import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private Vector3f position = new Vector3f(0,0,0);
	private float pitch = 0;
	private float yaw  = 0;
	private float roll = 0;

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	public void setRotation(float[] rotate) {
		this.pitch = rotate[0];
		this.yaw = rotate[1];
		this.roll = rotate[2];
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public void increasePosition(Vector3f position) {
		Vector3f.add(position, this.position, this.position);
	}
	
	public void increaseRotation(float pitch, float yaw, float roll) {
		this.pitch += pitch;
		this.yaw += yaw;
		this.roll += roll;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}
	
	
}
