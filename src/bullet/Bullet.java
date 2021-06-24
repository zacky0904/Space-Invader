package bullet;

import org.lwjgl.util.vector.Vector3f;

import bullet.BulletsMaster.BULLET_LABLE;
import entities.Entity;
import models.TexturedModel;

public abstract class Bullet extends Entity{
	
	
	protected Vector3f velocity;
	protected float speed;
	protected float lifeTime;
	protected BULLET_LABLE label;
	protected float elapsedTime = 0;
	protected float damage = 10;
	protected Vector3f boundbox;
	protected boolean disposed = false;
	
	public Bullet( TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, BULLET_LABLE label, Vector3f velocity, float speed, float lifeTime, float brightness) {
		super(model, position, rotX, rotY, rotZ, scale, brightness);
		this.label = label;
		velocity.normalise();
		this.velocity = new Vector3f(velocity);
		this.speed = speed;
		this.lifeTime = lifeTime;
		boundbox = new Vector3f();
		BulletsMaster.addBullet(this);
	}

	public Bullet( TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, BULLET_LABLE label, Vector3f velocity, float speed, float lifeTime) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.label = label;
		velocity.normalise();
		this.velocity = new Vector3f(velocity);
		this.speed = speed;
		this.lifeTime = lifeTime;
		boundbox = new Vector3f();
		BulletsMaster.addBullet(this);
	}
	
	public abstract boolean update();
	
	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public float getSpeed() {
		return speed;
	}

	public float getLifeTime() {
		return lifeTime;
	}

	public BulletsMaster.BULLET_LABLE getLabel() {
		return label;
	}

	public float getElapsedTime() {
		return elapsedTime;
	}
	
	public void setDamage(float damage) {
		this.damage = damage;
	}

	public float getDamage() {
		return damage;
	}

	public Vector3f getBoundbox() {
		return boundbox;
	}

	public void setBoundbox(Vector3f boundbox) {
		this.boundbox = boundbox;
	}
	
	public void setDisposed(boolean flag) {
		disposed = flag;
	}

}
