package audio;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import static org.lwjgl.openal.AL10.*;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
//import org.lwjgl.util.WaveData;
import org.newdawn.slick.openal.WaveData;

import objConverter.OBJFileLoader;


public class AudioMaster {
	
	private static ArrayList<Integer> buffers = new ArrayList<Integer>();
	@SuppressWarnings("fallthrough")
	public static void init() {
		try {
//			System.err.close();
			if(!AL.isCreated())
				AL.create();
			
		}
		catch(LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public static void setListernerData() {
		AL10.alListener3f(AL10.AL_POSITION, 0, 0, 0);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
	}
	
	public static int loadSound(String file) {
		int buffer = AL10.alGenBuffers();
		buffers.add(buffer);
		InputStream stream = null;
		AudioInputStream audioStream = null;
		try {
			stream = AudioMaster.class.getResourceAsStream(file);
			//add buffer for mark/reset support
			InputStream bufferedIn = new BufferedInputStream(stream);
			audioStream = AudioSystem.getAudioInputStream(bufferedIn);
		}catch(Exception e){
			e.printStackTrace();
		}
		WaveData waveFile = WaveData.create(audioStream);
		if(waveFile != null) {
			AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();
		}
		return buffer;
	}
	
	public static void cleanUp() {
		for(int buffer : buffers) {
			AL10.alDeleteBuffers(buffer);
		}
		AL.destroy();
	}
	

}
