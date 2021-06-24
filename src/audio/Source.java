package audio;

import org.lwjgl.openal.AL10;

public class Source {
	private int sourceId;
	private float gain = 1;
	
	public Source() {
		sourceId = AL10.alGenSources();
		AL10.alSourcef(sourceId, AL10.AL_GAIN, gain);
		AL10.alSourcef(sourceId, AL10.AL_PITCH, 1);
		AL10.alSource3f(sourceId, AL10.AL_POSITION, 0, 0, 0);
	}
	
	public Source(float gain) {
		sourceId = AL10.alGenSources();
		this.gain = gain;
		AL10.alSourcef(sourceId, AL10.AL_GAIN, gain);
		AL10.alSourcef(sourceId, AL10.AL_PITCH, 1);
		AL10.alSource3f(sourceId, AL10.AL_POSITION, 0, 0, 0);
	}
	
	public void setVolume(float gain) {
		this.gain = gain;
		AL10.alSourcef(sourceId, AL10.AL_GAIN, gain);
	}
	
	public float getGain() {
		return gain;
	}
	
	
	public void play(int buffer) {
		AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
		AL10.alSourcePlay(sourceId);
	}
	
	public void pause() {
		AL10.alSourcePause(sourceId);
	}
	
	public void resume() {
		AL10.alSourcePlay(sourceId);
	}
	
	public void delete() {
		AL10.alDeleteSources(sourceId);
	}
}
