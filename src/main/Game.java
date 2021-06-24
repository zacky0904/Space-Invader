package main;

import org.lwjgl.opengl.Display;

import audio.AudioMaster;
import renderEngine.DisplayManager;
import gameStateManager.GameStateManager;
import input.KeyboardMaster;
import load.LoadMaster;
public class Game {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		AudioMaster.init();
		AudioMaster.setListernerData();
		LoadMaster.load();	
		
		GameStateManager gsm = new GameStateManager();
	
		//==================Main Loop==================
		while(!Display.isCloseRequested()){
			gsm.update();
			gsm.render();
			DisplayManager.updateDisplay();
		}
		//==================Main Loop End==================
		AudioMaster.cleanUp();
		DisplayManager.closeDisplay();
	}

}
