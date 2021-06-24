package enemy;

import java.util.Iterator;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import bullet.Bullet;
import bullet.BulletsMaster.BULLET_LABLE;
import bullet.BulletsMaster.BULLET_TYPE;
import load.LoadMaster;
import models.TexturedModel;
import renderEngine.DisplayManager;

public class Enemy2 extends Enemy implements EnemyInterface{
	
	private long shootStartTime = 0;
	private float elapsed = 0;
	
	private float WarZoneLeft = -20;
	private float WarZoneRight = 20;
	private float WarZoneTop = -10;
	private float WarZoneDown = 2;
	
	private float speed = 10;
	private float zSpeed = 100;
	private float dRoll = 100;
	private float depth = 0;
	
	private boolean hasArrived = false;
	private Vector2f heading;
	private Vector3f targetPos;
	
	public Enemy2(Vector3f position,Vector3f targetPos, float speed, Vector2f heading) {
		super(LoadMaster.enemy2_TexturedModel, position, 0, 180, 0, 1.2f);
		super.setID(EnemyMaster.getEnemiesList().size());
		this.targetPos = targetPos;
		this.speed = speed;
		this.heading = new Vector2f(heading);
		this.heading.scale(speed);
		//elapsed = (float) (Math.random()*3);
		EnemyMaster.addEnemy(this);
	}

	public boolean update() {
		
		if(hasArrived) {
			super.setPosition(new Vector3f(super.getX(),depth,super.getZ()));
			if(super.getPosition().x >= WarZoneRight || super.getPosition().x <= WarZoneLeft)
				heading.setX(-heading.getX());
			if(super.getPosition().z >= WarZoneDown || super.getPosition().z <= WarZoneTop)
				heading.setY(-heading.getY());
//			if(super.getPosition().x >= WarZoneRight)
//				heading.setX(-Math.abs(heading.getX()));
//			if(super.getPosition().x <= WarZoneLeft)
//				heading.setX(Math.abs(heading.getX()));
			super.increasePosition(heading.getX()*0.008f, 0, heading.getY()*0.008f);
			
			elapsed += DisplayManager.getFrameTime();
			if(elapsed >= 3) {
				for(int i=0; i<10;i++) {
					float rad = (float) Math.toRadians(i*36);
					new Bullet(BULLET_LABLE.ENEMY, BULLET_TYPE.GREEN_BALL, new Vector3f(super.getPosition()), new Vector3f((float)Math.sin(rad),0,(float)Math.cos(rad)),6, 4);
				}
				elapsed = 0;
			}
		}else {
			if(super.getPosition().z >= targetPos.getZ() && !hasArrived) {
				hasArrived = true;
				super.setPosition(new Vector3f(super.getX(),-1,targetPos.z));
			}else {
				super.increasePosition(0, 0, zSpeed*DisplayManager.getFrameTime());
			}
		}
		
		smoothRoll();
		

		
		super.hitShrink();
		return super.getDurability() > 0;
	}
	
	private void smoothRoll() {
		if(heading.x > 0) {
			depth = -0.3f;
			if(super.getRotZ() <= -20)
				super.setRotZ(-20);
			else
				super.setRotZ(super.getRotZ()-dRoll*DisplayManager.getFrameTime());
				
		}else {
			depth = -0.3f;
			if(super.getRotZ() >= 20)
				super.setRotZ(20);
			else
				super.setRotZ(super.getRotZ()+dRoll*DisplayManager.getFrameTime());
				
		}
	}

	@Override
	public Enemy getEnemyClass() {
		return this;
	}

}
