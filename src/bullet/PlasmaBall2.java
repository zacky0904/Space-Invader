package bullet;

import org.lwjgl.util.vector.Vector3f;

import bullet.BulletsMaster.BULLET_LABLE;
import load.LoadMaster;
import renderEngine.DisplayManager;

public class PlasmaBall2 extends Bullet {

	public PlasmaBall2( BULLET_LABLE label, Vector3f position, Vector3f velocity, float speed, float lifeTime) {
		super(LoadMaster.plasmaBallGreen_TexturedModel, position, (float)Math.random()*180f, 0, 0, 0.4f, label, velocity, speed, lifeTime, 2);
		super.setBoundBox(1.4f,1.4f,1.4f);
	}
	
	public boolean update() {
		Vector3f vel = new Vector3f(super.velocity);
		vel.scale(speed*DisplayManager.getFrameTime());
		Vector3f.add(position, vel, position);
		elapsedTime += DisplayManager.getFrameTime();
		return (elapsedTime < lifeTime) && !disposed;
	}

	

}
