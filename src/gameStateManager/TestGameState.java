package gameStateManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import asteroid.Asteroid;
import asteroid.AsteroidMaster;
import audio.Source;
import camera.Camera;
import enemy.EnemyMaster;
import enemy.EnemySystem;
import entities.Light;
import entities.Player;
import fontMeshCreator.GUIText;
import fontMeshCreator.GUITextTwink;
import fontRendering.TextMaster;
import gui.GuiTexture;
import load.LoadMaster;
import menu.GuiTextBundle;
import menu.MenuSystem;
import particleSystem.ParticleSystem_Speed;
import particleSystem.ParticleSystem_Explode;
import particles.ParticleMaster;
import renderEngine.DisplayManager;
import bullet.BulletsMaster;

public class TestGameState extends GameState {
	
	private boolean KEY_ESCAPE_FLAG = true;
	
	private GameStateManager gsm;
	private long EnemyResponesTime = 0;
	
	private Player player;
	private Vector3f playerInitPos = new Vector3f(0,0,10);
	
	
	//Camera Attribute
	private Vector3f camBottomViewPosition = new Vector3f(0,0,15);
//	private float[] BottomView_Angle = {0, 0, 0};
	
	private Vector3f camTopViewPosition = new Vector3f(0,35,0);
	private float[] TopView_Angle = {90, 0, 0};
	
	private Random random;
	private Light light;
	private Camera camera;	
	
	//GUI
	private List<GuiTexture> guis;
	
	//particles
	private ParticleSystem_Speed pe_speed;
	private ParticleSystem_Explode pe_explode;
	
	private GuiTextBundle textBundle_inGame;
	private GuiTextBundle textBundle_endGame;
	
	private GUIText tx_endScore;
	private GUITextTwink tx_end;
	
	private GUIText tx_score;
	private GUIText tx_durability;
	
	private GuiTexture intro;
	private GUIText tx_intro;
	
	private Source sourceSE;
	private Source sourceBG;
	
	private float score;
	private float subScore;
	private String str_score;
	
	private float counter = 0;

	private MenuSystem paused_MS;
	
	private enum scenesState{
		INTRO,
		INITIAL,
		TOP_VIEW_MODE,
		TOP_TO_BOTTOM_CAMERA_SHIFT,
		BOTTOM_VIEW_MODE,
		BOTTOM_TOP_CAMERA_SHIFT,
		BOTTOM_PLAYER_POS_INIT,
		TOP_PLAYER_POS_INIT,
		TOP_CAMERA_ZOOM_OUT,
		BOTTOM_CAMERA_ZOOM_OUT,
		PAUSED,
		FAILED
		
	}
	
	private scenesState currentScenes = scenesState.INTRO;
	private scenesState lastScenes;
	private GuiTexture gui_pausedBackground;

	private GUIText tx_end2;

	private GUIText tx_missileCount;
	
	
	public TestGameState(GameStateManager gsm) {
		this.gsm = gsm;
	}

	public void init() {
		player = new Player(LoadMaster.player_TexturedModel, new Vector3f(0,0,10), 0, 0, 0, 0.6f);
		TextMaster.init(LoadMaster.loader);
		ParticleMaster.init(LoadMaster.loader, LoadMaster.renderer.getProjectionMatrix());
		BulletsMaster.setPlayer(player);
		AsteroidMaster.setTarget(player);
		EnemyMaster.setPlayer(player);
		random = new Random();
		light = new Light(new Vector3f(0,50,10),new Vector3f(1,1,1));
		camera = new Camera();	
	
		gui_pausedBackground = new GuiTexture(LoadMaster.loader.loadTexture("backgroung1"), new Vector2f(0f ,0f), new Vector2f(1.2f, 1.2f));
		
		textBundle_inGame = new GuiTextBundle();
		textBundle_endGame = new GuiTextBundle();
		
		tx_score = new GUIText("Score:", 1.5f, LoadMaster.FONT_Airstrike, new Vector2f(0,0), 1, false);
		tx_score.setColour(1, 1, 1);
		tx_durability = new GUIText("100 %", 2f, LoadMaster.FONT_Airstrike, new Vector2f(0.85f,0.9f), 1, false);
		tx_durability.setColour(1, 1, 1);
		tx_missileCount = new GUIText("M:", 2f, LoadMaster.FONT_Airstrike, new Vector2f(0.85f,0.85f), 1, false);
		tx_missileCount.setColour(1, 1, 1);
		textBundle_inGame.addText(tx_score);
		textBundle_inGame.addText(tx_durability);
		textBundle_inGame.addText(tx_missileCount);
		textBundle_inGame.setVisible(false);
	
		tx_intro = new GUIText("ARCADE MODE", 2f, LoadMaster.FONT_Airstrike, new Vector2f(0f,0.5f), 1, true);
		tx_intro.setColour(1, 1, 1);
		tx_intro.setVisible(false);

		tx_endScore = new GUIText("", 3f, LoadMaster.FONT_Airstrike, new Vector2f(0f,0.3f), 1, true);
		tx_endScore.setColour(1, 1, 1);
		
		tx_end2 = new GUIText("MISSION FAILED", 4f, LoadMaster.FONT_Airstrike, new Vector2f(0f,0.2f), 1, true);
		tx_end2.setColour(1, 1, 1);
		
		tx_end = new GUITextTwink("PRESSED SPACE TO CONTINUED", 1.2f, LoadMaster.FONT_Airstrike, new Vector2f(0f,0.8f), 1, true, 0.8f, 0.3f);
		tx_end.setColour(1, 1, 1);
		
		textBundle_endGame.addText(tx_endScore);
		textBundle_endGame.addText(tx_end);
		textBundle_endGame.addText(tx_end2);

		
		paused_MS = new MenuSystem("Paused", new String[] {"RETURN","GOD MODE: OFF","BACK To MENU"}, ">", new Vector2f(0f,0.2f), new Vector2f(0.38f,0.45f));

		pe_speed = new ParticleSystem_Speed(100,0.3f,6f);
		pe_explode = new ParticleSystem_Explode(50,0.8f);
		
		sourceSE = new Source(0.05f);
		sourceBG = new Source(0.08f);
		
		guis = new ArrayList<GuiTexture>();
		intro = new GuiTexture(LoadMaster.loader.loadGuiTexture("black"), new Vector2f(0f ,0f), new Vector2f(1f, 1f));
		guis.add(intro);
	}
 
	public void update() {
//		System.out.println(currentScenes);
		//main game state proc
		switch(currentScenes) {
		case TOP_VIEW_MODE:
			TopViewMode_PROC();
			break;
		case TOP_TO_BOTTOM_CAMERA_SHIFT:
			TopToBottomCameraSwitch_PROC();
			break;	
		case BOTTOM_TOP_CAMERA_SHIFT:
			BottomToTopCameraSwitch_PROC();
			break;
		case BOTTOM_VIEW_MODE:
			BottomViewMode_PROC();
			break;
		case BOTTOM_PLAYER_POS_INIT:
			BottomPlayerPositionInitial_PROC();
			break;
		case TOP_CAMERA_ZOOM_OUT:
			TopPlayerZoomOut_PROC();
			break;
		case BOTTOM_CAMERA_ZOOM_OUT:
			bottomPlayerZoomOut_PROC();
			break;
		case TOP_PLAYER_POS_INIT:
			TopPlayerPositionInitial_PROC();
			break;
		case INTRO:
			intro_PROC();
			break;
		case INITIAL:
			init_PROC();
			break;
		case FAILED:
			failed_PROC();
			break;
		case PAUSED:
			paused_PROC();
			break;
		default:
			break;

		
		}
		

		//check if player is still alive
		if(player.getDurability() <= 0 && (currentScenes == scenesState.TOP_VIEW_MODE || currentScenes == scenesState.BOTTOM_VIEW_MODE)) {
			currentScenes = scenesState.FAILED;
			counter = 0;
		}
		
		//pause proc
		if(currentScenes != scenesState.PAUSED) {
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !KEY_ESCAPE_FLAG) {
				lastScenes = currentScenes;
				currentScenes = scenesState.PAUSED;
				paused_MS.setVisible(true);
				guis.add(gui_pausedBackground);
			}
			pe_speed.generateParticles(new Vector3f(0,0,-200));
			player.update();
			AsteroidMaster.update();
			ParticleMaster.update();
			BulletsMaster.update();
			EnemyMaster.update();
		}

		//gui text info
		str_score = "SCORE: " + (int)score;
		tx_score.setTextString(str_score);
		tx_missileCount.setTextString("M: " + (int)player.getMissileCount());
		tx_durability.setTextString(player.getDurability()+ " %");
		
		//keyboard update
		KEY_ESCAPE_FLAG = Keyboard.isKeyDown(Keyboard.KEY_ESCAPE);

	}

	public void render() {
		LoadMaster.renderer.processEntity(player);
		EnemyMaster.renderEnemies();
		LoadMaster.renderer.render(light, camera);
		AsteroidMaster.renderAsteroids();
		ParticleMaster.renderParticles(camera);
		BulletsMaster.renderBullets();
		LoadMaster.guiRenderer.render(guis);
		TextMaster.render();
	}

	public void cleanUp() {
		ParticleMaster.cleanUp();
		BulletsMaster.cleanUp();
		AsteroidMaster.cleanUp();
		EnemyMaster.cleanUp();
		TextMaster.cleanUp();
		guis.clear();
		sourceSE.delete();
		sourceBG.delete();
	}
	
	
	//==============================================================ScenesState Procedure==============================================================

	
	//-----------------------------------BottomViewMode_PROC-----------------------------------
	private void BottomViewMode_PROC() {
		//pe_speed.generateParticles(new Vector3f(0,0,-200));
//		if(Keyboard.isKeyDown(Keyboard.KEY_M)) {
//			currentScenes = scenesState.BOTTOM_PLAYER_POS_INIT;
//		}
		
		score += 40*DisplayManager.getFrameTime();
		subScore += 40*DisplayManager.getFrameTime();
		if(subScore >= 1000) {
			counter += DisplayManager.getFrameTime();
			if(counter > 4)
				currentScenes = scenesState.BOTTOM_PLAYER_POS_INIT;
		}else {
			long elapsed = (System.nanoTime() - EnemyResponesTime) / 1000000;
			if(elapsed > 200) {
//				new Alien1(LoadMaster.enemy_TexturedModel, new Vector3f(random.nextFloat()*10-5.5f, random.nextFloat()*5 -2.5f , -(200)),0,180,0,0.5f);
				for(int i=0;i<30;i++)
					new Asteroid(LoadMaster.asteroid1_TexturedModel,new Vector3f(random.nextFloat()*400-200f, random.nextFloat()*200 -100f , -400), 100, random.nextFloat()*180, 0, random.nextFloat()*180, random.nextFloat()*5+2, 5);
				EnemyResponesTime = System.nanoTime();
			}
			
		}
		
	}
	
	//-----------------------------------TopViewMode_PROC-----------------------------------
	private void TopViewMode_PROC() {
		int totalScore = 0;
		if(subScore >= 3000 && EnemyMaster.isEmpty())
			currentScenes = scenesState.TOP_PLAYER_POS_INIT;
		if(EnemyMaster.isEmpty()) {
			EnemySystem.gernerateEnemy1(10, -10);
			EnemySystem.gernerateEnemy1Reverse(10, -5);
			EnemySystem.gernerateEnemy1(10, 0);
		}

//		if(Keyboard.isKeyDown(Keyboard.KEY_M)) {
//			currentScenes = scenesState.TOP_PLAYER_POS_INIT;
//		}

		totalScore += BulletsMaster.getHitScore() * 10;
		totalScore += EnemyMaster.getkillScore() * 50;
		subScore += totalScore;
		score += totalScore;

		
	}
	
	//-----------------------------------BottomToTopCameraSwitch_PROC-----------------------------------
	private void BottomToTopCameraSwitch_PROC() {
		float distance = 10;
		float pitch = camera.getPitch();
		if(pitch < 90) {
//			camera.setPitch(pitch + dSpeed*DisplayManager.getFrameTime());
			camera.setPitch(pitch + 1f);
			camera.setPosition(new Vector3f(0, (float)(distance*Math.sin(Math.toRadians(pitch))), (float)(distance*Math.cos(Math.toRadians(pitch))) ));
		}
		else {
			camera.setPitch(90);
			currentScenes = scenesState.TOP_CAMERA_ZOOM_OUT;
		}

	}
	
	//-----------------------------------BottomPlayerPositionInitial_PROC-----------------------------------
	private void BottomPlayerPositionInitial_PROC() {
		subScore = 0;
		boolean POSITION_FLAG = false;
		boolean CAMERA_FLAG = false;
//		boolean WAIT_TIME_FLAG = false;
		player.setControlFlag(false);
		BulletsMaster.cleanUp();
		AsteroidMaster.cleanUp();
		if(!EnemyMaster.getEnemiesList().isEmpty()) {
			for(int i=0;i<EnemyMaster.getEnemiesList().size();i++) {
				pe_explode.generateParticles(EnemyMaster.getEnemiesList().get(i).getPosition());
				sourceSE.play(LoadMaster.explode_SE);
				EnemyMaster.getEnemiesList().remove(i);
			}
		}
		
		float dSpeed = 7*DisplayManager.getFrameTime();
		Vector3f position = new Vector3f(player.getPosition());
		if(position.length() >= dSpeed) {	
			Vector3f velocity = new Vector3f(player.getPosition());
			velocity.negate();
			velocity.normalise();
			velocity.scale(dSpeed);
			Vector3f.add(player.position, velocity, player.position);
		}
		else {
			player.position = new Vector3f(0,0,0);
			POSITION_FLAG = true;
		}
		
		Vector3f cameraPos = new Vector3f(camera.getPosition());
		if(cameraPos.z > 10) {
			cameraPos.z -= 5*DisplayManager.getFrameTime();
		}
		else {
			cameraPos.z = 10;
			CAMERA_FLAG = true;
		}
		
		camera.setPosition(cameraPos);
		
		counter += DisplayManager.getFrameTime();

		
		if(CAMERA_FLAG && POSITION_FLAG)
			currentScenes = scenesState.BOTTOM_TOP_CAMERA_SHIFT;
		
	}
	
	//-----------------------------------TopPlayerZoomOut_PROC-----------------------------------
	public void TopPlayerZoomOut_PROC() {
		boolean POSITION_FLAG = false;
		boolean CAM_FLAG = false;
		Vector3f position = new Vector3f(camera.getPosition());
		float dSpeed = 40;
		if(position.y < camTopViewPosition.y) {
			position.y += dSpeed*DisplayManager.getFrameTime();
		}
		else {	
			position.y = camTopViewPosition.y;
			CAM_FLAG = true;
		}
		camera.setPosition(position);
		
		Vector3f target = new Vector3f(0,0,10);
		Vector3f delta = new Vector3f();
		Vector3f.sub(target, player.getPosition(), delta);
		dSpeed = 16;
		if(delta.length() >= dSpeed*DisplayManager.getFrameTime()) {	
			delta.normalise();
			delta.scale(dSpeed*DisplayManager.getFrameTime());
			Vector3f.add(player.position, delta, player.position);
		}
		else {
			player.position = new Vector3f(target);
			POSITION_FLAG = true;
		}
		
		
		if(POSITION_FLAG && CAM_FLAG) {
			player.setMode(1);
			player.setControlFlag(true);
			currentScenes = scenesState.TOP_VIEW_MODE;
		}
		
	}
	
	//-----------------------------------TopPlayerPositionInitial_PROC-----------------------------------
	public void TopPlayerPositionInitial_PROC() {
		subScore = 0;
		boolean POSITION_FLAG = false;
		boolean CAMERA_FLAG = false;
		player.setControlFlag(false);
		BulletsMaster.cleanUp();
		EnemyMaster.cleanUp();
		
//		if(!EnemyMaster.getEnemiesList().isEmpty()) {
//			for(int i=0;i<EnemyMaster.getEnemiesList().size();i++) {
//				pe_explode.generateParticles(EnemyMaster.getEnemiesList().get(i).getEnemyClass().getPosition());
//				sourceSE.play(LoadMaster.explode_SE);
//				EnemyMaster.getEnemiesList().remove(i);
//			}
//		}
		
		float dSpeed = 14*DisplayManager.getFrameTime();
		Vector3f position = new Vector3f(player.getPosition());
		if(position.length() >= dSpeed) {	
			Vector3f velocity = new Vector3f(player.getPosition());
			velocity.negate();
			velocity.normalise();
			velocity.scale(dSpeed);
			Vector3f.add(player.position, velocity, player.position);
		}
		else {
			player.position = new Vector3f(0,0,0);
			POSITION_FLAG = true;
		}
		
		Vector3f cameraPos = new Vector3f(camera.getPosition());
		if(cameraPos.y > 10) {
			cameraPos.y -= 15*DisplayManager.getFrameTime();
		}
		else {
			cameraPos.y = 10;
			CAMERA_FLAG = true;
		}
		
		camera.setPosition(cameraPos);
		
		if(CAMERA_FLAG && POSITION_FLAG)
			currentScenes = scenesState.TOP_TO_BOTTOM_CAMERA_SHIFT;
	}
	
	//-----------------------------------TopToBottomCameraSwitch_PROC-----------------------------------
	public void TopToBottomCameraSwitch_PROC() {
		float distance = 10;
		float pitch = camera.getPitch();
		
		long elapsed = (System.nanoTime() - EnemyResponesTime) / 1000000;
		if(elapsed > 300) {
//			new Alien1(LoadMaster.enemy_TexturedModel, new Vector3f(random.nextFloat()*10-5.5f, random.nextFloat()*5 -2.5f , -(200)),0,180,0,0.5f);
			for(int i=0;i<30;i++)
				new Asteroid(LoadMaster.asteroid1_TexturedModel,new Vector3f(random.nextFloat()*400-200f, random.nextFloat()*200 -100f , -400), 100, random.nextFloat()*180, 0, random.nextFloat()*180, random.nextFloat()*5+2, 10);
			EnemyResponesTime = System.nanoTime();
		}
		
		if(pitch > 0) {
//			camera.setPitch(pitch - dSpeed*DisplayManager.getFrameTime());
			camera.setPitch(pitch - 1f);
			camera.setPosition(new Vector3f(0, (float)(distance*Math.sin(Math.toRadians(pitch))), (float)(distance*Math.cos(Math.toRadians(pitch))) ));
		}
		else {
			camera.setPitch(0);
			currentScenes = scenesState.BOTTOM_CAMERA_ZOOM_OUT;
		}
	}
	
	//-----------------------------------BottomPlayerZoomOut_PROC-----------------------------------
	public void bottomPlayerZoomOut_PROC() {
		Vector3f position = new Vector3f(camera.getPosition());
		float dSpeed = 20;
		if(position.z < camBottomViewPosition.z) {
			position.z += dSpeed*DisplayManager.getFrameTime();
		}
		else {
			player.setMode(0);
			player.setControlFlag(true);
			position.z = camBottomViewPosition.z;
			currentScenes = scenesState.BOTTOM_VIEW_MODE;
			counter = 0;
		}
		
		camera.setPosition(position);
	}
	
	//-----------------------------------intro_PROC-----------------------------------
	public void intro_PROC() {
		tx_intro.setVisible(true);
		counter += DisplayManager.getFrameTime();
		if(counter >= 2) {
			currentScenes = scenesState.INITIAL;
			guis.clear();
			player.setControlFlag(true);
			tx_intro.setVisible(false);
			tx_score.setVisible(true);
			tx_durability.setVisible(true);
			sourceBG.play(LoadMaster.bgMusic1);
		}
		
	}
	
	//-----------------------------------Failed_PROC-----------------------------------
	public void failed_PROC() {
		if(counter == 0) {
			pe_explode.generateParticles(player.getPosition());
			player.setControlFlag(false);
			player.setPosition(new Vector3f(100,0,0));
			tx_endScore.setTextString("SCORE:" + (int)score);
			textBundle_inGame.setVisible(false);
			sourceSE.play(LoadMaster.explode_SE);
		}
		
		if(counter > 2) {
			textBundle_inGame.setVisible(false);
			tx_endScore.setVisible(true);
			tx_end2.setVisible(true);
			tx_end.update();
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				currentScenes = scenesState.INITIAL;
				textBundle_endGame.setVisible(false);
			}
			
		}
		else {
			counter += DisplayManager.getFrameTime();
		}
		
	}
	
	//-----------------------------------Paused_PROC-----------------------------------
	public void paused_PROC() {
		textBundle_inGame.setVisible(false);
		textBundle_endGame.setVisible(false);
		boolean exitFlag = Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !KEY_ESCAPE_FLAG;
		int returnValue = paused_MS.update();
		KEY_ESCAPE_FLAG = true;
		sourceBG.pause();
		
		if(returnValue == 0 || exitFlag ) {
			currentScenes =  lastScenes;
			guis.remove(gui_pausedBackground);
			paused_MS.setVisible(false);
			textBundle_inGame.setVisible(true);
			sourceBG.resume();
		}
		if(returnValue == 2) {
			gsm.setState(GameStateManager.MENU_STATE);
		}
		
		if(returnValue == 1) {
			player.setGodMode(!player.isGodMode());
			if( player.isGodMode())
				paused_MS.changeText(1, "GOD MODE: ON");
			else
				paused_MS.changeText(1, "GOD MODE: OFF");
		}
		
	}
	
	//
	public void init_PROC() {
		player.setDurability(100);
		player.setControlFlag(true);
		player.setMode(1);
		player.setPosition(new Vector3f(playerInitPos));
		camera.setPosition(camTopViewPosition);
		camera.setRotation(TopView_Angle);
		currentScenes = scenesState.TOP_VIEW_MODE;
		EnemyMaster.cleanUp();
		AsteroidMaster.cleanUp();
		BulletsMaster.cleanUp();
		textBundle_inGame.setVisible(true);
		score = 0;
		subScore = 0;
	}
	
	
	
	
	

}
