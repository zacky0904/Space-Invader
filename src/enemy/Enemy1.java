package enemy;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import bullet.PlasmaBall;
import bullet.BulletsMaster.BULLET_LABLE;
import load.LoadMaster;
import renderEngine.DisplayManager;

public class Enemy1 extends Enemy{
	
	private float elapsed = 0;
	
	private float WarZoneLeft = -20;
	private float WarZoneRight = 20;
//	private float WarZoneTop = -10;
//	private float WarZoneDown = 10;
	
	private float speed = 10;
	private float zSpeed = 100;
	private float dRoll = 100;
	private float depth = 0;
	private Vector2f velocity;
	private Vector3f targetPos;
	
	public Enemy1(Vector3f position,Vector3f targetPos, float  speed) {
		super(LoadMaster.enemy1_TexturedModel, position, 0, 180, 0, 1);
		super.setID(EnemyMaster.getEnemiesList().size());
		this.targetPos = targetPos;
		this.speed = speed;
		super.setDurability(30);
		velocity = new Vector2f((float)Math.random(), (float)Math.random());
		velocity.normalise();
		//elapsed = (float) (Math.random()*3);
		EnemyMaster.addEnemy(this);
	}

	public boolean update() {
		if(super.getPosition().z >= targetPos.getZ()) {
			super.setPosition(new Vector3f(super.getX(),depth,targetPos.getZ()));
			if(super.getPosition().x >= WarZoneRight)
				speed = -Math.abs(speed);
			if(super.getPosition().x <= WarZoneLeft)
				speed = Math.abs(speed);
			super.increasePosition(speed*0.008f, 0, 0);
			
			elapsed += DisplayManager.getFrameTime();
			if(elapsed >= 2) {
				new PlasmaBall(BULLET_LABLE.ENEMY, new Vector3f(super.getPosition()), new Vector3f(0,0,1),6, 6);
				elapsed = 0;
			}
			
		}else {
			super.increasePosition(0, 0, zSpeed*DisplayManager.getFrameTime());
		}
		
		smoothRoll();
		

		
		super.hitShrink();
		return super.getDurability() > 0;
	}
	
	private void smoothRoll() {
		if(speed > 0) {
			depth = -0.3f;
			if(super.getRotZ() <= -20)
				super.setRotZ(-20);
			else
				super.setRotZ(super.getRotZ()-dRoll*DisplayManager.getFrameTime());
				
		}else {
			depth = 0.3f;
			if(super.getRotZ() >= 20)
				super.setRotZ(20);
			else
				super.setRotZ(super.getRotZ()+dRoll*DisplayManager.getFrameTime());
				
		}
	}

}
