package menu;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import audio.AudioMaster;
import audio.Source;
import fontMeshCreator.GUIText;
import fontMeshCreator.GUITextTwink;
import gameStateManager.MenuState.scenesState;
import load.LoadMaster;
import renderEngine.DisplayManager;

public class MenuSystem {
	
	private boolean KEY_DOWN_FLAG = false;
	private boolean KEY_UP_FLAG = false;
	private boolean KEY_SPACE_FLAG = false;
	private boolean KEY_ESCAPE_FLAG = true;
	
	private boolean visible;
	
	private Source sourceSE;
	
	private int totalChoice;
	private int currentChoice;
	private String[] subTitle;
	private GUIText title;
	private ArrayList<GUIText> textList = new ArrayList<GUIText>();
	private GUITextTwink cursor;
	
	private float lineSpace = 0.1f;
	private Vector2f subTitlePos;
	
	
	public MenuSystem(String strTitle, String[] subTitle, String strCursor, Vector2f titlePos, Vector2f subTitlePos) {
		this.totalChoice = subTitle.length;
		this.subTitlePos = new Vector2f(subTitlePos);
		currentChoice = 0;
		title = new GUIText(strTitle, 3f, LoadMaster.FONT_Airstrike , titlePos, 1, true);
		title.setColour(1, 1, 1);
		cursor = new GUITextTwink(strCursor, 1.5f, LoadMaster.FONT_Airstrike , new Vector2f(subTitlePos.x - 0.03f, subTitlePos.y), 1, false,0.3f, 0.1f );
		cursor.setColour(1, 1, 1);
		for(int i=0; i<totalChoice; i++) {
			textList.add( new GUIText(subTitle[i], 1.5f, LoadMaster.FONT_Airstrike , new Vector2f(subTitlePos.x, subTitlePos.y+lineSpace*i), 1, false));
			textList.get(i).setColour(1, 1, 1);
		}
		sourceSE = new Source(0.5f);
		setVisible(false);
	}
	
	public void setVisible(boolean flag) {
		title.setVisible(flag);
		cursor.setVisible(flag);
		for(int i=0; i<totalChoice; i++)
			textList.get(i).setVisible(flag);
		if(!flag) {
			sourceSE.delete();
		}
		else {
			sourceSE = new Source(0.5f);
		}
	}
	
	public void changeText(int index, String str) {
		textList.get(index).setTextString(str);
	}

	public int update() {
		int returnValue = -1;
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) && !KEY_DOWN_FLAG) {
			sourceSE.play(LoadMaster.click_SE);
			currentChoice ++;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_UP) && !KEY_UP_FLAG) {
			sourceSE.play(LoadMaster.click_SE);
			currentChoice --;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !KEY_SPACE_FLAG) {
			returnValue = currentChoice;
		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !KEY_ESCAPE_FLAG) {
//			returnValue = -2;
//		}
		
		if(currentChoice > totalChoice-1)
			currentChoice = totalChoice-1;
		if(currentChoice < 0)
			currentChoice = 0;
		
		cursor.setPosition(new Vector2f(subTitlePos.x - 0.03f, currentChoice*lineSpace + subTitlePos.y));
		cursor.update();
		
		KEY_DOWN_FLAG = Keyboard.isKeyDown(Keyboard.KEY_DOWN);
		KEY_UP_FLAG = Keyboard.isKeyDown(Keyboard.KEY_UP);
		KEY_SPACE_FLAG = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
//		KEY_ESCAPE_FLAG = Keyboard.isKeyDown(Keyboard.KEY_ESCAPE);
		
		
		return returnValue;
	}
}
