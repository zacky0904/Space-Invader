package test;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import audio.AudioMaster;
import audio.Source;

public class AudioTest {

	public static void main(String[] args) throws IOException, LWJGLException {
		AudioMaster.init();
		AudioMaster.setListernerData();
		int buffer = AudioMaster.loadSound("/audios/laser.wav");
		Source source = new Source();
		
		char c = ' ';
		while(c != 'q') {
			c = (char)System.in.read();
			
			if(c == 'p') {
				source.play(buffer);
			}
		}
		
		source.delete();
		AudioMaster.cleanUp();
	}

}
