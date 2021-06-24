package asteroid;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.util.vector.Vector3f;

import bullet.Bullet;
import enemy.Enemy;
import entities.Player;
import load.LoadMaster;


public class AsteroidMaster {
	
	private static ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
	private static Player player;
	
	
	
	public static void setTarget(Player player) {
		AsteroidMaster.player = player;
	}

	public static void update() {
		Iterator<Asteroid> iterator = asteroids.iterator();
		while(iterator.hasNext()) {
			Asteroid asteroid = iterator.next();
			boolean stillAlive = asteroid.update();
			boolean collid = collisionTest(asteroid);
			if(!stillAlive || collid) {
				iterator.remove();
			}
		}
	}
	
	public static void renderAsteroids() {
		Iterator<Asteroid> iterator = asteroids.iterator();
		while(iterator.hasNext()) {
			Asteroid astroid = iterator.next();
			LoadMaster.renderer.processEntity(astroid);
		}
	}
	
	private static boolean collisionTest(Asteroid asteroid) {
			Vector3f delta = new Vector3f();
			Vector3f.sub(player.getPosition(), asteroid.getPosition(), delta);
			float boxX = (player.getBoundbox().x + asteroid.getBoundbox().x)/4;
			float boxY = (player.getBoundbox().y + asteroid.getBoundbox().y)/4;
			float boxZ = (player.getBoundbox().z + asteroid.getBoundbox().z)/4;
			
			boolean result = Math.abs(delta.x) < boxX && Math.abs(delta.y) < boxY && Math.abs(delta.z) < boxZ;
			if (result) {
				player.setHit(true);
				player.setDurability(player.getDurability()-10);
			}
			
			return result;
	}
	
	public static void cleanUp() {
		asteroids.clear();
	}
	
	public static void addAsteroid(Asteroid enemy) {
		asteroids.add(enemy);
	}
	
	public static boolean isEmpty() {
		return asteroids.isEmpty();
	}
	
	public static ArrayList<Asteroid> getAstroidsList(){
		return asteroids;
	}
}
