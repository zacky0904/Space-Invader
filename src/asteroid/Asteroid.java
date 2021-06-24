package asteroid;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import models.TexturedModel;
import renderEngine.DisplayManager;

public class Asteroid extends Entity{
	
	private float speed;
	private float liveTime;
	private float elapsed = 0;

	public Asteroid(TexturedModel model, Vector3f position, float speed,float rotX, float rotY, float rotZ, float scale, float liveTime) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.speed = speed;
		this.liveTime = liveTime;
		super.setBoundBox(scale*3, scale*3, scale*3);
		AsteroidMaster.addAsteroid(this);
	}
	
	public boolean update() {
		super.increasePosition(0, 0, speed*DisplayManager.getFrameTime());
		elapsed += DisplayManager.getFrameTime();
		return elapsed <= liveTime;
	}
}
