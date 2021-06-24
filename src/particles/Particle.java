package particles;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import renderEngine.DisplayManager;

public class Particle {
	
	private Vector3f position;
	private Vector3f velocity;
	private Vector4f colour;
	private float gravityEffect;
	private float lifeLength;
	private float rotation;
	private float scale;
	private float dScale;
	
	private float elapsedTime = 0;

	public Particle(Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation,
			float scale,float dScale ,Vector4f colour) {
		super();
		this.position = position;
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		this.colour = colour;
		this.dScale = dScale;
		ParticleMaster.addParticle(this);
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}
	
	public void setColour(Vector4f colour) {
		this.colour = colour;
	}
	
	public Vector4f getColour() {
		return colour;
	}

	protected boolean update() {
		scale = scale-dScale*DisplayManager.getFrameTime();
		Vector3f change = new Vector3f(velocity);
		change.scale(DisplayManager.getFrameTime());
		Vector3f.add(change, position, position);
		elapsedTime += DisplayManager.getFrameTime();
		return elapsedTime < lifeLength;
		
	}
	
	


}
