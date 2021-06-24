package bullet;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.util.vector.Vector3f;

import enemy.Enemy;
import enemy.EnemyInterface;
import enemy.EnemyMaster;
import entities.Entity;
import entities.Player;
import load.LoadMaster;

public class BulletsMaster {
	private static ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private static Player player;
	
	private static int hitScore;
	
	public static enum BULLET_TYPE{
		DEFAULT,
		FIRE_BALL,
		GREEN_BALL,
	}
	
	public static enum BULLET_LABLE{
		PLAYER,
		ENEMY
	}
	
	public static void setPlayer(Player pl) {
		player = pl;
	}
	
	public static int update() {
		hitScore = 0;
		Iterator<Bullet> iterator = bullets.iterator();
		while(iterator.hasNext()) {
			Bullet b = iterator.next();
			collisionTest(b);
			boolean stillAlive = b.update();
			if(!stillAlive) {
				iterator.remove();
			}
		}
		return hitScore;
	}
	
	public static void renderBullets() {
		Iterator<Bullet> iterator = bullets.iterator();
		while(iterator.hasNext()) {
			Bullet b = iterator.next();
			if(b.getType() == BULLET_TYPE.DEFAULT)
				LoadMaster.renderer.processEntity(new Entity(LoadMaster.bullet_TexturedModel, b.getPosition(), -90, 0, 0, 1.5f, 1));
			else if(b.getType() == BULLET_TYPE.FIRE_BALL)
				LoadMaster.renderer.processEntity(new Entity(LoadMaster.plasmaBall_TexturedModel, b.getPosition(), 0, 0, 0, 0.4f, 1));
			else if(b.getType() == BULLET_TYPE.GREEN_BALL)
				LoadMaster.renderer.processEntity(new Entity(LoadMaster.plasmaBallGreen_TexturedModel, b.getPosition(), 0, 0, 0, 0.4f, 1));
		}
	}
	
	public static void cleanUp() {
		bullets.clear();
	}
	
	public static void addBullet(Bullet bullet) {
		bullets.add(bullet);
	}
	
	public static ArrayList<Bullet> getBulletList(){
		return bullets;
	}
	
	
	/**
	 * 
	 * @return the hit score of last update
	 */
	public static int getHitScore() {
		return hitScore;
	}
	
	
	private static void collisionTest(Bullet bullet) {
		if(bullet.getLabel() == BULLET_LABLE.PLAYER) {
			Iterator<EnemyInterface> iterator = EnemyMaster.getEnemiesList().iterator();
			while(iterator.hasNext()) {
				Enemy enemy = iterator.next().getEnemyClass();
				Vector3f delta = new Vector3f();
				Vector3f.sub(bullet.getPosition(), enemy.getPosition(), delta);
				
				float boxX = (bullet.getBoundbox().x + enemy.getBoundbox().x)/4;
				float boxY = (bullet.getBoundbox().y + enemy.getBoundbox().y)/4;
				float boxZ = (bullet.getBoundbox().z + enemy.getBoundbox().z)/4;
				
				boolean result = Math.abs(delta.x) < boxX && Math.abs(delta.y) < boxY && Math.abs(delta.z) < boxZ;
				if (result) {
					enemy.setHit(true);
					enemy.setDurability(enemy.getDurability()-bullet.getDamage());
					bullet.setDisposed(true);
					hitScore++;
					break;
				}
			}
		}else {
			Vector3f delta = new Vector3f();
			Vector3f.sub(bullet.getPosition(), player.getPosition(), delta);
			
			float boxX = (bullet.getBoundbox().x + player.getBoundbox().x)/4;
			float boxY = (bullet.getBoundbox().y + player.getBoundbox().y)/4;
			float boxZ = (bullet.getBoundbox().z + player.getBoundbox().z)/4;
			if (Math.abs(delta.x) < boxX && Math.abs(delta.y) < boxY && Math.abs(delta.z) < boxZ) {
				player.setHit(true);
				player.setDurability(player.getDurability()-bullet.getDamage());
				bullet.setDisposed(true);
			}
		}
	}
}
