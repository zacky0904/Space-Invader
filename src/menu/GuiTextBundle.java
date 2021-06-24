package menu;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;

public class GuiTextBundle {
	
	private boolean visible;
	private ArrayList<GUIText> textlists;
	
	public GuiTextBundle() {
		textlists = new ArrayList<GUIText>();
	}
	
	public void addText(String text, float fontSize, FontType font, Vector2f position, float maxLineLength, boolean centered, Vector3f color) {
		GUIText temp = new GUIText(text,fontSize , font, position, maxLineLength, centered);
		temp.setColour(color.x, color.y, color.z);
		textlists.add(temp);
	}
	
	public void addText(GUIText text) {
		textlists.add(text);
	}
	
	public void setVisible(boolean flag) {
		for(GUIText text:textlists) {
			text.setVisible(flag);
		}
	}
	
}
