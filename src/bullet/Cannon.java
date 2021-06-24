package bullet;

import org.lwjgl.util.vector.Vector3f;

import bullet.BulletsMaster.BULLET_LABLE;
import load.LoadMaster;
import renderEngine.DisplayManager;

public class Cannon extends Bullet {

	
	public Cannon( BULLET_LABLE label, Vector3f position, Vector3f velocity, float speed, float lifeTime) {
		super(LoadMaster.bullet_TexturedModel, position, -90, 0, 0, 1.2f, label, velocity, speed, lifeTime, 2);
		super.setBoundBox(1f,1f,2f);
	}

	@Override
	public boolean update() {
		Vector3f vel = new Vector3f(super.velocity);
		vel.scale(speed*DisplayManager.getFrameTime());
		Vector3f.add(position, vel, position);
		elapsedTime += DisplayManager.getFrameTime();
		return (elapsedTime < lifeTime) && !disposed;
	}

}
