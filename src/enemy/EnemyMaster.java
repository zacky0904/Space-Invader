package enemy;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.util.vector.Vector3f;

import asteroid.Asteroid;
import audio.Source;
import bullet.Bullet;
import entities.Entity;
import entities.Player;
import load.LoadMaster;
import particleSystem.ParticleSystem_Explode;


public class EnemyMaster {
	private static ArrayList<EnemyInterface> enemies = new ArrayList<EnemyInterface>();

	
	private static ParticleSystem_Explode psExplode = new ParticleSystem_Explode(50,0.8f);
	private static Source source = new Source();
	
	private static Player player;
	
	private static int killScore;
	
	public static void setPlayer(Player pl) {
		player = pl;
	}
	
	public static int update() {
		killScore = 0;
		Iterator<EnemyInterface> iterator = enemies.iterator();
		while(iterator.hasNext()) {
			EnemyInterface e = iterator.next();
			boolean stillAlive = e.update();
			collisionTest(e.getEnemyClass());
			if(!stillAlive) {
				psExplode.generateParticles(e.getEnemyClass().getPosition());
				source.play(LoadMaster.explode_SE);
				iterator.remove();
				killScore++;
			}
		}
		return killScore;
	}
	
	/**
	 * 
	 * @return the kill score of last update
	 */
	public static int getkillScore() {
		return killScore;
	}
	
	public static void renderEnemies() {
		Iterator<EnemyInterface> iterator = enemies.iterator();
		while(iterator.hasNext()) {
			Enemy e = iterator.next().getEnemyClass();
			LoadMaster.renderer.processEntity(e);
		}
	}
	
	public static void cleanUp() {
		enemies.clear();
	}
	
	public static void addEnemy(EnemyInterface enemy) {
		enemies.add(enemy);
	}
	
	public static boolean isEmpty() {
		return enemies.isEmpty();
	}
	
	public static ArrayList<EnemyInterface> getEnemiesList(){
		return enemies;
	}
	
	private static boolean collisionTest(Enemy enemy) {
		Vector3f delta = new Vector3f();
		Vector3f.sub(player.getPosition(), enemy.getPosition(), delta);
		float boxX = (player.getBoundbox().x + enemy.getBoundbox().x)/4;
		float boxY = (player.getBoundbox().y + enemy.getBoundbox().y)/4;
		float boxZ = (player.getBoundbox().z + enemy.getBoundbox().z)/4;
		
		boolean result = Math.abs(delta.x) < boxX && Math.abs(delta.y) < boxY && Math.abs(delta.z) < boxZ;
		if (result) {
			enemy.setHit(true);
			player.setHit(true);
			enemy.setDurability(enemy.getDurability()-10);
			player.setDurability(player.getDurability()-10);
		}
		
		return result;
	}
}
