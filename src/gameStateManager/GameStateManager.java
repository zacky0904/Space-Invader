package gameStateManager;


public class GameStateManager {

	private GameState[] gameStates;
	private int currentState;
	
	public static final int MENU_STATE = 0;
	public static final int TEST_GAME_STATE = 1;
	public static final int ENDING_STATE = 11;
	public static final int HOW2PLAY_STATE = 12;
	
	public GameStateManager() {
		gameStates = new GameState[4];
		currentState = MENU_STATE;
		loadState(currentState);
	}
	
	private void loadState(int state) {
		if(state == MENU_STATE) {
			gameStates[state] = new MenuState(this);
			gameStates[state].init();
		}
		if(state == TEST_GAME_STATE) {
			gameStates[state] = new TestGameState(this);
			gameStates[state].init();
		}
	}
	
	private void unloadState(int state) {
		gameStates[state].cleanUp();
		gameStates[state] = null;
	}
	
	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
	}
	
	public void update() {
		try {
			gameStates[currentState].update();
		}
		catch(Exception e) {
			e.printStackTrace();
			}
	}
	
	public void render() {
		try {
			gameStates[currentState].render();
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	

}
