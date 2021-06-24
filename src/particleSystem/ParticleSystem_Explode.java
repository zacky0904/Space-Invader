package particleSystem;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import particles.Particle;
import renderEngine.DisplayManager;

public class ParticleSystem_Explode {
	
	private float speed;
	private float lifeLength;
	private Vector4f colour = new Vector4f(1,1,1,1);
	
	public ParticleSystem_Explode(float speed, float lifeLength) {
		this.speed = speed;
		this.lifeLength = lifeLength;
	}
	
	public void generateParticles(Vector3f systemCenter){
//		Vector3f vec = new Vector3f(0,1,0);
//		float rotateOffset = (float) (Math.random()*90);
//		for(int i=0;i<=360;i+=10) {
//			for(int j=0;j<=180;j+=10) {
//				Matrix4f result = new Matrix4f();
//				Matrix4f rotate = new Matrix4f();
//				Matrix4f.translate(vec,result,result);
//				Matrix4f.rotate((float) Math.toRadians(i),new Vector3f(0, 1, 0) , rotate,rotate); 
//				Matrix4f.rotate((float) Math.toRadians(j),new Vector3f(1, 0, 0) , rotate,rotate);
//				Matrix4f.mul(rotate, result, result);
//				Vector3f velocityVec = new Vector3f(result.m30,result.m31,result.m32);
//				velocityVec.scale(speed);
//				new Particle(new Vector3f(systemCenter), velocityVec, 0, lifeLength, 0, 4f, colour);
//			}
//		}
		for(int i=0;i<200;i++) {
			Vector3f velocityVec = new Vector3f((float) (Math.random()-0.5),(float) (Math.random()-0.5),(float) (Math.random()-0.5));
			velocityVec.normalise();
			velocityVec.scale(speed);
			new Particle(new Vector3f(systemCenter), velocityVec, 0, lifeLength, 0, 2f, 0,colour);
		}
	}
	
	

}
