package bullet;

import org.lwjgl.util.vector.Vector3f;

import bullet.BulletsMaster.BULLET_LABLE;
import bullet.BulletsMaster.BULLET_TYPE;
import renderEngine.DisplayManager;

public class Bullet{
	
	
	private Vector3f position;
	private Vector3f velocity;
	private float speed;
	private float lifeTime;
	private BULLET_LABLE label;
	private BULLET_TYPE type;
	private float elapsedTime = 0;
	private float damage = 10;
	private Vector3f boundbox;
	private boolean DISPOSED = false;

	public Bullet( BULLET_LABLE label,BULLET_TYPE type , Vector3f position,  Vector3f velocity, float speed, float lifeTime) {
		this.label = label;
		this.type = type;
		this.position = position;
		this.velocity = velocity;
		this.speed = speed;
		this.lifeTime = lifeTime;
		BulletsMaster.addBullet(this);
		if(type == BULLET_TYPE.DEFAULT)
			boundbox = new Vector3f(1,1,2);
		if(type == BULLET_TYPE.FIRE_BALL)
			boundbox = new Vector3f(1.4f,1.4f,1.4f);
		if(type == BULLET_TYPE.GREEN_BALL)
			boundbox = new Vector3f(1.4f,1.4f,1.4f);

	}
	
	public boolean update() {
		Vector3f vel = new Vector3f(velocity);
		vel.normalise();
		vel.scale(speed*DisplayManager.getFrameTime());
		Vector3f.add(position, vel, position);
		elapsedTime += DisplayManager.getFrameTime();
		return (elapsedTime < lifeTime) && !DISPOSED;
	}
	
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
		DISPOSED = flag;
	}

	public BULLET_TYPE getType() {
		return type;
	}
	
	

	

}
