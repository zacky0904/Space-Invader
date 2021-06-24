package gameStateManager;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import audio.AudioMaster;
import audio.Source;
import camera.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import entities.Ship;
import fontMeshCreator.GUIText;
import fontMeshCreator.GUITextTwink;
import fontRendering.TextMaster;
import gui.GuiTexture;
import load.LoadMaster;
import particleSystem.ParticleSystem_Speed;
import particles.Particle;
import particles.ParticleMaster;
import renderEngine.DisplayManager;

public class MenuState extends GameState {
	
	private GameStateManager gsm;
	
	private Light light;
	private Camera camera;	
	
	private boolean KEY_DOWN_FLAG = false;
	private boolean KEY_UP_FLAG = false;
	private boolean KEY_SPACE_FLAG;
	
	//GUI
	private List<GuiTexture> guis;
	
	//particles
	private ParticleSystem_Speed speedEffect;
	
	
	private Source sourceBG;
	private Source sourceSE;
	private GUIText title;
	private GUIText info;
	private GUITextTwink helloWorld;
	
	private GUIText tx_story;
	private GUIText tx_arcade;
	private GUIText tx_help;
	private GUIText tx_quit;
	
	private GUITextTwink cursor;
	
	private Ship ship;
	
	private int choice = 0;
	
	private float brightness = 0;
	
	public static GuiTexture howToPlay;
	
	
	public enum scenesState{
		PressedAnyKeyToStart,
		Menu,
		Help,
		flyAway,
		
	}
	
	private scenesState currentScenes = scenesState.PressedAnyKeyToStart;
	
	
	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;
	}

	public void init() {
		TextMaster.init(LoadMaster.loader);
		ParticleMaster.init(LoadMaster.loader, LoadMaster.renderer.getProjectionMatrix());
		
		howToPlay = new GuiTexture(LoadMaster.loader.loadGuiTexture("backgroung2"), new Vector2f(0.06f ,-0.02f), new Vector2f(1f, 1.7f));
		
		ship = new Ship(new Vector3f(0,0,0), 0, 0, 0, 0.6f);
		light = new Light(new Vector3f(0,50,10),new Vector3f(1,1,1));
		camera = new Camera();	
		camera.setPosition(new Vector3f(5.6707115f,3.660906f, 3.672783f));
		camera.setRotation(new float[]{0,-45.512856f,-25.613176f});
		

		info = new GUIText("ZACK LAI 2021", 0.5f, LoadMaster.FONT_Airstrike, new Vector2f(0, 0.98f), 1, false);
		info.setColour(1, 1, 1);
		info.setVisible(true);
		
		title = new GUIText("SPACE INVADER DX", 4.9f, LoadMaster.FONT_Airstrike, new Vector2f(0, 0.15f), 1, true);
		title.setColour(1, 1, 1);
		title.setVisible(true);
		helloWorld = new GUITextTwink("Pressed SPACE To Start", 1.5f, LoadMaster.FONT_Airstrike, new Vector2f(0.2f, 0.85f), 1, true, 0.8f, 0.5f);
		helloWorld.setColour(1, 1, 1);
		
		tx_story = new GUIText("STORY MODE", 1.5f, LoadMaster.FONT_Airstrike, new Vector2f(0.7f, 0.5f), 1, false);
		tx_story.setColour(0.5f, 0.5f, 0.5f);
		tx_story.setVisible(false);
		tx_help = new GUIText("help", 1.5f, LoadMaster.FONT_Airstrike, new Vector2f(0.7f, 0.7f), 1, false);
		tx_help.setColour(1, 1, 1);
		tx_help.setVisible(false);
		tx_arcade = new GUIText("ARCADE MODE", 1.5f, LoadMaster.FONT_Airstrike, new Vector2f(0.7f, 0.6f), 1, false);
		tx_arcade.setColour(1, 1, 1);
		tx_arcade.setVisible(false);
		tx_quit = new GUIText("quit", 1.5f, LoadMaster.FONT_Airstrike, new Vector2f(0.7f, 0.8f), 1, false);
		tx_quit.setColour(1, 1, 1);
		tx_quit.setVisible(false);
		
		cursor = new GUITextTwink(">", 1.5f, LoadMaster.FONT_Airstrike, new Vector2f(0.67f, 0.5f), 1, false, 0.3f, 0.1f);
		cursor.setColour(1, 1, 1);
		cursor.setVisible(false);
		
		speedEffect = new ParticleSystem_Speed(100,0.3f,6f);
		sourceBG = new Source(0.1f);
		sourceSE = new Source(0.5f);
		
		guis = new ArrayList<GuiTexture>();
		sourceBG.play(LoadMaster.bgMusic1);

	}
 
	public void update() {
//		camerMove();
		switch(currentScenes) {
		case PressedAnyKeyToStart:
			helloWorld.update();
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				//sourceSE.play(LoadMaster.click_SE);
				currentScenes = scenesState.Menu;
				helloWorld.setVisible(false);
				helloWorld.remove();
				
				tx_story.setVisible(true);
				tx_arcade.setVisible(true);
				tx_help.setVisible(true);
				tx_quit.setVisible(true);
				cursor.setVisible(true);
			}
			break;
		case Menu:
			if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) && !KEY_DOWN_FLAG) {
				sourceSE.play(LoadMaster.click_SE);
				choice ++;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_UP) && !KEY_UP_FLAG) {
				sourceSE.play(LoadMaster.click_SE);
				choice --;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !KEY_SPACE_FLAG) {
				sourceSE.play(LoadMaster.click_SE);
				if(choice == 0) {
					//currentScenes = scenesState.flyAway;
				}
				else if(choice == 2) {
					currentScenes = scenesState.Help;
					guis.add(howToPlay);
				}
				else if(choice == 1) {
					currentScenes =  scenesState.flyAway;
				}
				else if(choice == 3) {
					AudioMaster.cleanUp();
					DisplayManager.closeDisplay();
					System.exit(0);
				}
				
			}
				
			
			if(choice > 3)
				choice = 3;
			if(choice < 0)
				choice = 0;
			cursor.setPosition(new Vector2f(0.67f, choice*0.1f+0.5f));
			cursor.update();

			break;
		case Help:
			
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !KEY_SPACE_FLAG) {
				currentScenes = scenesState.Menu;
				guis.clear();
			}
			break;
		case flyAway:
			TextMaster.cleanUp();
			float dSpeed = 30;
			float dRotate = 200;
			sourceBG.setVolume(sourceBG.getGain()-0.0007f);
			if(ship.getRoll() < 60)
				ship.setRoll(ship.getRoll()+dRotate*DisplayManager.getFrameTime());
			else {
				ship.increasePosition(0, 0, -dSpeed*DisplayManager.getFrameTime());
				if(ship.getPosition().z < -30)
					gsm.setState(gsm.TEST_GAME_STATE);
			}
			break;
		default:
			break;
		}
		


		ship.update();
		speedEffect.generateParticles(new Vector3f(0,0,-200));
		KEY_DOWN_FLAG = Keyboard.isKeyDown(Keyboard.KEY_DOWN);
		KEY_UP_FLAG = Keyboard.isKeyDown(Keyboard.KEY_UP);
		KEY_SPACE_FLAG = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
		ParticleMaster.update();
	}

	public void render() {
		LoadMaster.renderer.render(light, camera);
		LoadMaster.renderer.processEntity(ship);
		ParticleMaster.renderParticles(camera);
		
		TextMaster.render();
		LoadMaster.guiRenderer.render(guis);
		
		
	
	}

	public void cleanUp() {
		TextMaster.cleanUp();
		ParticleMaster.cleanUp();
		guis.clear();
		sourceBG.delete();
		sourceSE.delete();
	}
	
	private void camerMove() {
		float speed = 10;
		float rotateSpeed = 10;
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			camera.increasePosition(new Vector3f(0,0,-speed*DisplayManager.getFrameTime()));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			camera.increasePosition(new Vector3f(0,0,speed*DisplayManager.getFrameTime()));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			camera.increasePosition(new Vector3f(speed*DisplayManager.getFrameTime(),0,0));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			camera.increasePosition(new Vector3f(-speed*DisplayManager.getFrameTime(),0,0));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_R)) {
			camera.increasePosition(new Vector3f(0,speed*DisplayManager.getFrameTime(),0));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_F)) {
			camera.increasePosition(new Vector3f(0,-speed*DisplayManager.getFrameTime(),0));
		}
		
		
		if(Keyboard.isKeyDown(Keyboard.KEY_E)) {
			camera.increaseRotation(0, rotateSpeed*DisplayManager.getFrameTime(), 0);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			camera.increaseRotation(0, -rotateSpeed*DisplayManager.getFrameTime(), 0);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			camera.increaseRotation(rotateSpeed*DisplayManager.getFrameTime(), 0, 0);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			camera.increaseRotation(-rotateSpeed*DisplayManager.getFrameTime(), 0, 0);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			camera.increaseRotation(0, 0, rotateSpeed*DisplayManager.getFrameTime());
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			camera.increaseRotation(0, 0, -rotateSpeed*DisplayManager.getFrameTime());
		}
	
		

		
		System.out.println("Position:"+ camera.getPosition().toString());
		System.out.println("Pitch:" + camera.getPitch());
		System.out.println("Yaw:" + camera.getYaw());
		System.out.println("Roll:" + camera.getRoll());
	}
	

	//==============================================================ScenesState Procedure==============================================================

	
	
}
