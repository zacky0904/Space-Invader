package bullet;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import bullet.BulletsMaster.BULLET_LABLE;
import enemy.Enemy;
import enemy.EnemyMaster;
import load.LoadMaster;
import particles.Particle;
import renderEngine.DisplayManager;

public class Missile extends Bullet {
	
	private Enemy enemy;
	private float counter = 0;
	private boolean noTarget = false;
	private boolean firstRun = true;
	private Vector3f heading = new Vector3f(0,0,-1);

	public Missile( BULLET_LABLE label, Vector3f position, Vector3f velocity,float speed, float lifeTime) {
		super(LoadMaster.missile_TexturedModel, position, -90, 0, 0, 0.12f, label, velocity, speed, lifeTime, 1);
		super.setBoundBox(1f,1f,2f);
		super.setDamage(50);
		if(EnemyMaster.getEnemiesList().isEmpty()) {
			noTarget = true;
		}
		else {	
			int rand = (int)(Math.random()*EnemyMaster.getEnemiesList().size());
			enemy = EnemyMaster.getEnemiesList().get(rand);
		}
	}

	
	public boolean update() {
		if(counter > 0.5f) {
			new Particle(new Vector3f(super.getPosition()), new Vector3f(velocity), 0, 0.1f, 45, 1.5f, 12,new Vector4f(0.8f,0.95f,1,0.8f));
			if(firstRun) {
				firstRun = false;
				velocity = heading;
				LoadMaster.source.play(LoadMaster.missileFiring_SE);
			}
			if(enemy == null || enemy.getDurability() <= 0 )
				noTarget = true;
			
			if(!noTarget) {
				Vector3f delta = new Vector3f();
				Vector3f.sub(enemy.getPosition(), super.getPosition(), delta);
				delta.normalise();
				delta.scale(0.01f);
				Vector3f.add(delta, velocity, velocity);
			}
			
			float deg = (float)(Math.atan2(velocity.x, velocity. z) * 180/Math.PI - 180);
			super.setRotZ(deg);
			elapsedTime += DisplayManager.getFrameTime();
			velocity.normalise();
			velocity.scale(speed*DisplayManager.getFrameTime());
		}
		else {
			counter += DisplayManager.getFrameTime();
			velocity.normalise();
			velocity.scale(5*DisplayManager.getFrameTime());
		}
		
		
		

		Vector3f.add(position, velocity, position);
		return (elapsedTime < lifeTime) && !disposed;
		
	}

}
