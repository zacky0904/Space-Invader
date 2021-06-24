package entities;


import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;


import load.LoadMaster;

import particles.Particle;
import renderEngine.DisplayManager;


public class Ship extends Entity{
	
	public Vector3f position;
	
	private float pitch;
	private float roll;
	
	private float flyingNoiseTargetRoll = 0;
	private float flyingNoiseCurrentRoll = 0;
	private float flyingNoiseTargetX = 0;
	private float flyingNoiseCurrentX = 0;

	private Vector3f leftExhaust = new Vector3f(-0.33f,0.05f,0.65f);
	private Vector3f rightExhaust = new Vector3f(0.33f,0.05f,0.65f);
	private Vector3f newLeftExhaust;
	private Vector3f newRightExhaust;

	

	public Ship(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(LoadMaster.player_TexturedModel, position, rotX, rotY, rotZ, scale);
		this.position = position;
		this.pitch = rotX;
		this.roll = rotZ;
		this.setBoundBox(1.5f, 1.5f, 4);
	}
	
	public void update() {
		
		flyinRollgNoise();
		flyinMovegNoise();
		afterBurnerEffect();
		super.setRotX(pitch);
		super.setRotZ(roll + flyingNoiseCurrentRoll);
	}
	
	private void afterBurnerEffect() {
		
		Matrix4f left = new Matrix4f();
		Matrix4f right = new Matrix4f();
		Matrix4f rotate = new Matrix4f();
		Matrix4f.translate(leftExhaust,left,left);
		Matrix4f.translate(rightExhaust,right,right);
		Matrix4f.rotate((float) Math.toRadians(pitch),new Vector3f(1, 0, 0) , rotate,rotate); 
		Matrix4f.rotate((float) Math.toRadians(roll + flyingNoiseCurrentRoll),new Vector3f(0, 0, 1) , rotate,rotate);
		Matrix4f.mul(rotate, left, left);
		Matrix4f.mul(rotate, right, right);
		newLeftExhaust  = new Vector3f(left.m30,left.m31,left.m32);
		newRightExhaust  = new Vector3f(right.m30,right.m31,right.m32);
		Vector3f.add(position, newLeftExhaust, newLeftExhaust);
		Vector3f.add(position, newRightExhaust, newRightExhaust);
		new Particle(newLeftExhaust, new Vector3f(0,0,10), 0, 0.15f, 45, 2.5f, 12,new Vector4f(0.8f,0.95f,1,0.8f));
		new Particle(newRightExhaust, new Vector3f(0,0,10), 0, 0.15f, 45, 2.5f, 12,new Vector4f(0.8f,0.95f,1,0.8f));	
		
	}
	

	public void setPosition(Vector3f pos) {
		position = pos;
	}
	
	private void flyinRollgNoise() {
		float dRotateSpeed = 5;
		float delta = flyingNoiseCurrentRoll - flyingNoiseTargetRoll;
		if(delta<0) 
			flyingNoiseCurrentRoll += dRotateSpeed*DisplayManager.getFrameTime();
		if(delta>0) 
			flyingNoiseCurrentRoll -= dRotateSpeed*DisplayManager.getFrameTime();
		if(Math.abs(delta) <= dRotateSpeed*DisplayManager.getFrameTime())
			flyingNoiseTargetRoll = (float)Math.random()*20-10;			
	}
	
	private void flyinMovegNoise() {
		float dRotateSpeed = 0.2f;
		float delta = position.x - flyingNoiseTargetX;
		if(delta<0) 
			position.x += dRotateSpeed*DisplayManager.getFrameTime();
		if(delta>0) 
			position.x -= dRotateSpeed*DisplayManager.getFrameTime();
		if(Math.abs(delta) <= dRotateSpeed*DisplayManager.getFrameTime())
			flyingNoiseTargetX = (float)Math.random()*1-0.5f;	
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}
	
	

	

	
}
