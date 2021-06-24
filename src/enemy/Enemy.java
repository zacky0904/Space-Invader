package enemy;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import models.TexturedModel;
import renderEngine.DisplayManager;

public abstract class Enemy extends Entity {
	
	private float durability = 15;
	private int ID;
	
	private boolean isHIT = false;
	private float isHITelapsed = 0;

	public Enemy(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		super.setBoundBox(2, 2, 2);
	}
	
	public abstract boolean update();
	
	public void hitShrink() {
		if(isHIT) {
			super.setBrightness(10);
			isHITelapsed += DisplayManager.getFrameTime();
			if(isHITelapsed > 0.1) {
				isHITelapsed = 0;
				isHIT = false;
				super.setBrightness(0);
			}
		}
	}
	
	public float getDurability() {
		return durability;
	}

	public void setDurability(float durability) {
		this.durability = durability;
	}


	public int getID() {
		return ID;
	}


	public void setID(int iD) {
		ID = iD;
	}
	
	public float getX() {
		return super.getPosition().x;
	}
	
	public float getY() {
		return super.getPosition().y;
	}
	
	public float getZ() {
		return super.getPosition().z;
	}
	
	public void setHit(boolean hit) {
		isHIT = hit;
	}
	
	

	
	
	
	

}
