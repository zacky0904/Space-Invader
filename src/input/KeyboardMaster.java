package input;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

public class KeyboardMaster{
	
	public boolean KEY_SPACE_PRESSED = false;
	public boolean KEY_SPACE_RELEASED = false;
	
	public boolean KEY_UP_PRESSED = false;
	public boolean KEY_UP_RELEASED = false;
	
	public boolean KEY_DOWN_PRESSED = false;
	public boolean KEY_DOWN_RELEASED = false;
	
	public boolean KEY_LEFT_PRESSED = false;
	public boolean KEY_LEFT_RELEASED = false;
	
	
	
	public static void init() {
		try {
			Keyboard.create();
			 Keyboard.enableRepeatEvents(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void update() {
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {       

		}
		
	}
	
	public static void cleanUp() {
		if(Keyboard.isCreated())
			Keyboard.destroy();
	}
}
