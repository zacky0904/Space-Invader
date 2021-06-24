package enemy;

import org.lwjgl.util.vector.Vector3f;

import bullet.Bullet;
import bullet.BulletsMaster.BULLET_LABLE;
import bullet.BulletsMaster.BULLET_TYPE;
import models.TexturedModel;

public class Alien1 extends Enemy implements EnemyInterface{
	
	private long shootStartTime = 0;
	
	public Alien1(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		EnemyMaster.addEnemy(this);
		// TODO Auto-generated constructor stub
	}

	public boolean update() {
		if(! (super.getPosition().z > -20))
			super.increasePosition(0, 0, 0.8f);
		
		long elapsed = (System.nanoTime() - shootStartTime) / 1000000;
		if(elapsed > 1000) {
			//new Bullet(BULLET_LABLE.ENEMY, BULLET_TYPE.FIRE_BALL, new Vector3f(super.getPosition()), new Vector3f(0,0,1), 8, 8);
			shootStartTime = System.nanoTime();
		}
		return super.getDurability() > 0;
}

	public Enemy getEnemyClass() {
		return this;
	}

}
