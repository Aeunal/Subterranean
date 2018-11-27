package GameState;

//import java.util.ArrayList;

public class GameStateManager {

	private GameState[] gameStates;
	private int currentState;
	private int buildingType;// = 4;
	private int previousState;
	
	private GameValues gameValues;
	
	// state names
	public static final int MENUSTATE = 0;
	public static final int TOWN1STATE = 1;
	public static final int TOWN2STATE = 2;
	public static final int TOWN3STATE = 3;
	public static final int ROAMSTATE = 4;
	public static final int FIGHTSTATE = 5;
	public static final int BUILDINGSTATE = 6;
	public static final int STORYSTATE = 7;
	public static final int FINALSTORYSTATE = 8;
	public static final int DEATHSTATE= 9;
	public static final int HELPSTATE= 10;
	
	public static final int NUMGAMESTATES = 11; // state number
	
	private static final int STARTINGSTATE = MENUSTATE;
	//private static final int STARTINGSTATE = FIGHTSTATE; ////// CHANGE WHILE DEBUG THEN FIX

	public GameStateManager() {
		
		gameValues = new GameValues();
		gameStates = new GameState[NUMGAMESTATES];
		currentState = STARTINGSTATE;
		previousState = currentState;
		
		loadState(currentState);

	}

	private void loadState(int state) {
		if (state == MENUSTATE)
			gameStates[state] = new MenuState(this);
		if (state == TOWN1STATE)
			gameStates[state] = new TownState(this,1);
		if (state == TOWN2STATE)
			gameStates[state] = new TownState(this,2);
		if (state == TOWN3STATE)
			gameStates[state] = new TownState(this,3);
		if (state == ROAMSTATE)
			gameStates[state] = new RoamState(this);
		if (state == FIGHTSTATE)
			gameStates[state] = new FightState(this);
		if (state == BUILDINGSTATE)
			gameStates[state] = new BuildingState(this, getBuildingType());
		if (state == STORYSTATE)
			gameStates[state] = new StoryState(this);
		if (state == FINALSTORYSTATE)
			gameStates[state] = new StoryState(this); // add parameter to constructor
		if (state == DEATHSTATE)
			gameStates[state] = new DeathState(this); 
		if (state == HELPSTATE)
			gameStates[state] = new HelpState(this); 
	}
	
	private void unloadState(int state) {
		gameStates[state] = null;
	}

	public void setState(int state) {
		if(currentState != state) {
			previousState = currentState;
		}
		unloadState(currentState);
		
		currentState = state;
		loadState(currentState);
		
		// gameStates[currentState].init(); //transferred to constructor
	}
	
	public int getState() {
		return this.currentState;
	}
	
	public int getPreviousState() {
		return this.previousState;
	}

	public void update() {
		try {
			gameStates[currentState].update();
		} catch (Exception e) {}
	}

	public void draw(java.awt.Graphics2D g) {
		try {
			gameStates[currentState].draw(g);
		} catch (Exception e) {}
	}

	public void keyPressed(int k) {
		gameStates[currentState].keyPressed(k);
	}

	public void keyReleased(int k) {
		gameStates[currentState].keyReleased(k);
	}

	public GameValues GameValues() {
		return gameValues;
	}

	public void setGameValues(GameValues gameValues) {
		this.gameValues = gameValues;
	}

	public int getBuildingType() {
		return buildingType;
	}

	public void setBuildingType(int buildingType) {
		this.buildingType = buildingType;
	}

}
