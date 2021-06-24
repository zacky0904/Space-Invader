package fontMeshCreator;

import org.lwjgl.util.vector.Vector2f;

import fontRendering.TextMaster;
import renderEngine.DisplayManager;

public class GUITextTwink extends GUIText{

	private float highTime;
	private float lowTime;
	private float totalTime;
	private float eslapsed = 0;
	
	public GUITextTwink(String text, float fontSize, FontType font, Vector2f position, float maxLineLength, boolean centered, float twinkTime) {
		super(text, fontSize, font, position, maxLineLength, centered);
		this.highTime = twinkTime;
		this.lowTime = twinkTime;
		totalTime = highTime + lowTime;
		TextMaster.loadText(this);;
	}
	
	public GUITextTwink(String text, float fontSize, FontType font, Vector2f position, float maxLineLength, boolean centered, float hightTime, float lowTime) {
		super(text, fontSize, font, position, maxLineLength, centered);
		this.highTime = hightTime;
		this.lowTime = lowTime;
		totalTime = highTime + lowTime;
		TextMaster.loadText(this);;
	}

	public void update() {
		eslapsed += DisplayManager.getFrameTime();
		if(eslapsed > lowTime) {
			super.setVisible(true);
		}
		if(eslapsed > totalTime) {
			eslapsed = 0;
			super.setVisible(false);
		}
	}
	
	
	
	

}
