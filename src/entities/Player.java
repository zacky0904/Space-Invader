package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import audio.Source;
import bullet.Cannon;
import bullet.Missile;
import load.LoadMaster;
import models.TexturedModel;
import particles.Particle;
import renderEngine.DisplayManager;
import bullet.BulletsMaster;

public class Player extends Entity{
	
	private boolean KEY_Z_FLAG = false;
	
	public Vector3f position;
	
	private final float topViewWidth = 46;
	private final float topViewHeight = 25f;
	
	private final float bottomViewWidth = 16;
	private final float bottomViewHeight = 9;
	
	private final float TOP_MOVE_SPEED = 20;
	private final float BOTTOM_MOVE_SPEED = 10;
	private final float PITCH_SPEED = 120;
	private final float ROLL_SPEED = 120;
	
	private boolean DOWN_FLAG;
	private boolean UP_FLAG;
	private boolean RIGHT_FLAG;
	private boolean LEFT_FLAG;
	private boolean FORWARD_FLAG;
	private boolean BACKWARD_FLAG;
	private boolean CONTROL_DISABLE = false;
	
	private boolean isHIT = false;
	private float isHITelapsed = 0;
	
	private float pitch;
	private float roll;
	
	private float missileMax = 8;
	private float missileCount = missileMax;
	private float missileTimer = 0;
	private boolean godMode = false;

	private Source source;
	private int PLAY_MODE = 0;
	
	private float flyingNoiseTargetRoll = 0;
	private float flyingNoiseCurrentRoll = 0;
	private float flyingNoiseTargetX = 0;
	private float flyingNoiseCurrentX = 0;
	
	private float shootCoolDown = 0.2f;
	
	
	
	public enum MODE{
		TOP_VIEW,
		BOYYOM_VIEW
	}
	
	private float speed;
	private float[] shootSpeed = {
			200,
			180,
			150,
			100,
			50
	};
	
	private float elapsed = 0;
	
	private float experience;
	private int level;
	
	private float shield = 10;
	private float durability = 100;
	
	private Vector3f leftExhaust = new Vector3f(-0.33f,0.05f,0.65f);
	private Vector3f rightExhaust = new Vector3f(0.33f,0.05f,0.65f);
	private Vector3f newLeftExhaust;
	private Vector3f newRightExhaust;

	

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.position = position;
		this.pitch = rotX;
		this.roll = rotZ;
		this.setBoundBox(1.5f, 1.5f, 4);
		source = new Source();
	}
	
	public void update() {
		if(!CONTROL_DISABLE) {
			move();
			
			//shoot cannon
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				elapsed += DisplayManager.getFrameTime();
				if(godMode) {
					shootCoolDown = 0.01f;
				}
				else
					shootCoolDown = 0.1f;
				if(elapsed >= shootCoolDown) {
					source.play(LoadMaster.laser_SE);
					new Cannon(BulletsMaster.BULLET_LABLE.PLAYER, new Vector3f(position), new Vector3f(0,0,-1), 100, 1);
					elapsed = 0;
				}
			}else {
				elapsed = 0.1f;
			}
			
			//launch missile
			if(Keyboard.isKeyDown(Keyboard.KEY_Z) && !KEY_Z_FLAG) {
				if(godMode)
					missileCount = 8;
				if(missileCount > 0) {
					missileCount -= 2;
					new Missile(BulletsMaster.BULLET_LABLE.PLAYER, new Vector3f(position.x, position.y, position.z-0.1f), new Vector3f(1f,-1,0), 20, 3);
					new Missile(BulletsMaster.BULLET_LABLE.PLAYER, new Vector3f(position.x, position.y, position.z-0.1f), new Vector3f(-1f,-1,0), 20, 3);
				}
			}
			
			if(missileCount != missileMax) {
				missileTimer += DisplayManager.getFrameTime();
				if(missileTimer > 3) {
					missileTimer = 0;
					missileCount += 2;
				}
			}
				
			

		}else {
			DOWN_FLAG = false;
			UP_FLAG = false;
			FORWARD_FLAG = false;
			BACKWARD_FLAG = false;
			RIGHT_FLAG = false;
			LEFT_FLAG = false;
			
		}
		
		if(isHIT) {
			super.setBrightness(10);
			isHITelapsed += DisplayManager.getFrameTime();
			if(isHITelapsed > 0.1) {
				isHITelapsed = 0;
				isHIT = false;
				super.setBrightness(0);
			}
		}
		flyingRollNoise();
		flyingMoveNoise();
		afterBurnerEffect();
		smoothRotate();
		super.setPosition(position);
		super.setRotX(pitch);
		super.setRotZ(roll + flyingNoiseCurrentRoll);
		KEY_Z_FLAG = Keyboard.isKeyDown(Keyboard.KEY_Z);
	}
	
	private void afterBurnerEffect() {
		
		Matrix4f left = new Matrix4f();
		Matrix4f right = new Matrix4f();
		Matrix4f rotate = new Matrix4f();
		Matrix4f.translate(leftExhaust,left,left);
		Matrix4f.translate(rightExhaust,right,right);
		Matrix4f.rotate((float) Math.toRadians(pitch),new Vector3f(1, 0, 0) , rotate,rotate); 
		Matrix4f.rotate((float) Math.toRadians(roll+flyingNoiseCurrentRoll),new Vector3f(0, 0, 1) , rotate,rotate);
		Matrix4f.mul(rotate, left, left);
		Matrix4f.mul(rotate, right, right);
		newLeftExhaust  = new Vector3f(left.m30,left.m31,left.m32);
		newRightExhaust  = new Vector3f(right.m30,right.m31,right.m32);
		Vector3f.add(position, newLeftExhaust, newLeftExhaust);
		Vector3f.add(position, newRightExhaust, newRightExhaust);
		new Particle(newLeftExhaust, new Vector3f(0,0,10), 0, 0.1f, 45, 2.5f, 12,new Vector4f(0.8f,0.95f,1,0.8f));
		new Particle(newRightExhaust, new Vector3f(0,0,10), 0, 0.1f, 45, 2.5f, 12,new Vector4f(0.8f,0.95f,1,0.8f));	
		
	}
	
	public void move() {
			if(PLAY_MODE == 0) {
				if(Keyboard.isKeyDown(Keyboard.KEY_UP))
					UP_FLAG = true;
				else 
					UP_FLAG = false;
				if(Keyboard.isKeyDown(Keyboard.KEY_DOWN))
					DOWN_FLAG = true;
				else 
					DOWN_FLAG = false;

				if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
					LEFT_FLAG = true;
				else 
					LEFT_FLAG = false;
				if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
					RIGHT_FLAG = true;
				else			
					RIGHT_FLAG = false;
				smoothMove_BottomView();
			}
			
			if(PLAY_MODE == 1) {
				if(Keyboard.isKeyDown(Keyboard.KEY_UP)) 
					FORWARD_FLAG = true;
				else 
					FORWARD_FLAG = false;
				if(Keyboard.isKeyDown(Keyboard.KEY_DOWN))
					BACKWARD_FLAG = true;
				else
					BACKWARD_FLAG = false;
				if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
					LEFT_FLAG = true;
				else 
					LEFT_FLAG = false;
				if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
					RIGHT_FLAG = true;
				else	
					RIGHT_FLAG = false;
				smoothMove_TopView();
			} 
	}
	
	private void smoothMove_BottomView() {
		//=========UP DOWN============================
		if(DOWN_FLAG) {
			if(position.getY() > -bottomViewHeight/2)
				position.setY(position.getY() - BOTTOM_MOVE_SPEED*DisplayManager.getFrameTime());
		}
		if(UP_FLAG) {
			if(position.getY() < bottomViewHeight/2)
				position.setY(position.getY() + BOTTOM_MOVE_SPEED*DisplayManager.getFrameTime());
		}
		
		//=========RIGHT LEFT============================
		if(LEFT_FLAG) {
			if(position.getX() > -bottomViewWidth/2)
				position.setX(position.getX() - BOTTOM_MOVE_SPEED*DisplayManager.getFrameTime());
		}
		if(RIGHT_FLAG) {
			if(position.getX() < bottomViewWidth/2)
				position.setX(position.getX() + BOTTOM_MOVE_SPEED*DisplayManager.getFrameTime());
		}
	}
	
	private void smoothMove_TopView() {
		//=========UP DOWN============================
		if(BACKWARD_FLAG) {
			if(position.getZ() < topViewHeight/2)
				position.setZ(position.getZ() + TOP_MOVE_SPEED*DisplayManager.getFrameTime());
		}
		if(FORWARD_FLAG) {
			if(position.getZ() > -topViewHeight/2)
				position.setZ(position.getZ() - TOP_MOVE_SPEED*DisplayManager.getFrameTime());
		}

		//=========RIGHT LEFT============================
		if(LEFT_FLAG) {
			if(position.getX() > -topViewWidth/2)
				position.setX(position.getX() - TOP_MOVE_SPEED*DisplayManager.getFrameTime());
		}
		if(RIGHT_FLAG) {
			if(position.getX() < topViewWidth/2)
				position.setX(position.getX() + TOP_MOVE_SPEED*DisplayManager.getFrameTime());
		}
	}
	
	private void smoothRotate() {
		//============================UP DOWN============================
		if(BACKWARD_FLAG || UP_FLAG) {
			pitch += PITCH_SPEED*DisplayManager.getFrameTime();
			if(pitch>25)
				pitch = 25;

		}
		else if(FORWARD_FLAG || DOWN_FLAG) {
			pitch -= PITCH_SPEED*DisplayManager.getFrameTime();
			if(pitch<-25)
				pitch = -25;
		}
		else {
			if(pitch<0)
				pitch += PITCH_SPEED*DisplayManager.getFrameTime();
			if(pitch>0)
				pitch -= PITCH_SPEED*DisplayManager.getFrameTime();
		}
		
		//============================RIGHT LEFT============================
		if(LEFT_FLAG) {
			roll += ROLL_SPEED*DisplayManager.getFrameTime();
			if(roll>25)
				roll = 25;
		}
		else if(RIGHT_FLAG) {
			roll -= ROLL_SPEED*DisplayManager.getFrameTime();
			if(roll<-25)
				roll = -25;
		}
		else {
			if(roll<0)
				roll += ROLL_SPEED*DisplayManager.getFrameTime();
			if(roll>0)
				roll -= ROLL_SPEED*DisplayManager.getFrameTime();
		}
		
	}
	
	private void flyingRollNoise() {
		float dRotateSpeed = 5;
		float delta = flyingNoiseCurrentRoll - flyingNoiseTargetRoll;
		if(delta<0) 
			flyingNoiseCurrentRoll += dRotateSpeed*DisplayManager.getFrameTime();
		if(delta>0) 
			flyingNoiseCurrentRoll -= dRotateSpeed*DisplayManager.getFrameTime();
		if(Math.abs(delta) <= dRotateSpeed*DisplayManager.getFrameTime())
			flyingNoiseTargetRoll = (float)Math.random()*20-10;			
	}
	
	private void flyingMoveNoise() {
		float dRotateSpeed = 0.2f;
		float delta = position.x - flyingNoiseTargetX;
		if(delta<0) 
			position.x += dRotateSpeed*DisplayManager.getFrameTime();
		if(delta>0) 
			position.x -= dRotateSpeed*DisplayManager.getFrameTime();
		if(Math.abs(delta) <= dRotateSpeed*DisplayManager.getFrameTime())
			flyingNoiseTargetX = (float)Math.random()*1-0.5f;	
	}
	
	
	public void setMode(int mode) {
		this.PLAY_MODE = mode;
	}
	
	public void setPosition(Vector3f pos) {
		position = pos;
	}
	
	public void setControlFlag(boolean flag) {
		CONTROL_DISABLE = !flag;
	}

	public float getDurability() {
		return durability;
	}

	public void setDurability(float durability) {
		if(!godMode)
			this.durability = durability;
	}
	
	public void setHit(boolean hit) {
		isHIT = hit;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

	public boolean isGodMode() {
		return godMode;
	}

	public void setGodMode(boolean godMode) {
		this.godMode = godMode;
	}

	public float getMissileCount() {
		return missileCount;
	}

	
	
	
	
	
	

	
}
