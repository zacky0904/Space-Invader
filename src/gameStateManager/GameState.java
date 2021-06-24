package gameStateManager;

public abstract class GameState {
	
	protected GameStateManager gsm;
	
	public abstract void init();
	public abstract void update();
	public abstract void render();
	public abstract void cleanUp();



}
