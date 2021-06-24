package particleSystem;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import particles.Particle;
import renderEngine.DisplayManager;

public class ParticleSystem_Speed {
	
	private float speed;
	private float gravityComplient;
	private float lifeLength;
	private Vector4f colour = new Vector4f(1,1,1,0.8f);
	
	public ParticleSystem_Speed(float speed, float gravityComplient, float lifeLength) {
		this.speed = speed;
		this.gravityComplient = gravityComplient;
		this.lifeLength = lifeLength;
	}
	
	public void generateParticles(Vector3f systemCenter){
 //		float delta = DisplayManager.getFrameTime();
		for(int i=0;i<2;i++) {
			float angle = (float) (Math.random()* 6.28f );
			float length = (float) (Math.random()* 100 + 20);
			float x = (float) (length*Math.cos(angle));
			float y = (float) (length*Math.sin(angle));
			emitParticle(new Vector3f(x, y, systemCenter.z));
		}
	}
	
//	public void generateParticles(Vector3f systemCenter, float speed){
//		this.speed = speed;
//		for(int i=0;i<1;i++) {
//			float angle = (float) (Math.random()* 6.28f );
//			float length = (float) (Math.random()* 50f + 10);
//			float x = (float) (length*Math.cos(angle));
//			float y = (float) (length*Math.sin(angle));
//			emitParticle(new Vector3f(x, y, systemCenter.z));
//		}
//	}
	
	private void emitParticle(Vector3f center){
		Vector3f velocity = new Vector3f(0, 0, 1);
		velocity.scale(speed);
		new Particle(new Vector3f(center), velocity, gravityComplient, lifeLength, 0, 2f, 0, colour);
	}
	
	

}
