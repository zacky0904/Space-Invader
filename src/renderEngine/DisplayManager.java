package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final int FPS_CAP = 100;
	
	private static long lastFrameTime = 0;
	private static float Duration = 0;
	private static int FPS_RATE;
	
	public static void createDisplay(){		
		ContextAttribs attribs = new ContextAttribs(3,2)
		.withForwardCompatible(true)
		.withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("Space Invader");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0,0, WIDTH, HEIGHT);
		lastFrameTime = System.nanoTime(); 
	}
	
	public static void updateDisplay(){
		
		Display.sync(FPS_CAP);
		Display.update();
		long currentTime = System.nanoTime();
		Duration = (currentTime - lastFrameTime)/1000000000f;
		FPS_RATE = (int) (1/Duration);
		Display.setTitle("SPACE INVADER" + " FPS:" + FPS_RATE);
		lastFrameTime = System.nanoTime();
		
	}
	
	public static float getFrameTime() {
		return Duration;
	}
	
	public static void closeDisplay(){
		
		Display.destroy();
		
	}

}
