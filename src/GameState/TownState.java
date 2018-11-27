package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Entity.Door;
import Entity.HUD;
import Entity.Player;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

public class TownState extends GameState {

	protected TileMap tileMap;
	protected Background bg;
	protected Background[] bgLayer = new Background[4];
	protected Player player;
	protected ArrayList<Door> doors;
	protected HUD hud;
	// protected AudioPlayer bgMusic;

	protected String tileMapPath;
	protected String mapPath;
	protected String bgPath;
	protected String[] bglayerPath = new String[4];
	protected int [][] doorFeatures;
	private int townLevel;

	public TownState(GameStateManager gsm, int townLevel) {
		this.gsm = gsm;
		this.townLevel = townLevel;
		switch (townLevel) {
		case 1:
			tileMapPath = "/Tilesets/cavetileset.gif";
			mapPath = "/Maps/town1.map";
			bgPath = "/Backgrounds/cavebg.png";
			int[][] town1Doors = {
					{365,200,GameStateManager.BUILDINGSTATE,0,1},
					{670,170,GameStateManager.ROAMSTATE,0},
					{505,170,GameStateManager.BUILDINGSTATE,0,4},
			};
			doorFeatures = town1Doors;
			break;
		case 2:
			tileMapPath = "/Tilesets/cavetileset.gif";
			mapPath = "/Maps/town2.map";
			bgPath = "/Backgrounds/cavebg.png";
			int[][] town2Doors = {
					{50,200,GameStateManager.TOWN1STATE,0},
					{375,140,GameStateManager.BUILDINGSTATE,1,3},
					{670,170,GameStateManager.ROAMSTATE,0}
			};
			doorFeatures = town2Doors;
			break;
		case 3:
			tileMapPath = "/Tilesets/cavetileset.gif";
			mapPath = "/Maps/town1.map";
			bgPath = "/Backgrounds/cavebg.png";
			int[][] town3Doors = {
					{365,200,GameStateManager.BUILDINGSTATE,0,1},
					{670,170,GameStateManager.ROAMSTATE,0},
					{505,170,GameStateManager.BUILDINGSTATE,0,4},
			};
			doorFeatures = town3Doors;
			break;

		default:
			break;
		}
		
		bglayerPath[0] = "/Backgrounds/cavebgLayer0.png";
		bglayerPath[1] = "/Backgrounds/cavebgLayer1.png";
		bglayerPath[2] = "/Backgrounds/cavebgLayer2.png";
		bglayerPath[3] = "/Backgrounds/cavebgLayer3.png";
		
		init();
	}

	public void init() {
		tileMap = new TileMap(30);
		tileMap.loadTiles(tileMapPath);
		tileMap.loadMap(mapPath);
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);

		//bg = new Background(bgPath, 0.1);

		for (int i = 0; i < bgLayer.length; i++)
			bgLayer[i] = new Background(bglayerPath[i],0.1*i);//0.1*(4-i) rotating effect
		
		player = new Player(tileMap,gsm);
		populateDoors();
		
		hud = new HUD(player);

		// bgMusic = new AudioPlayer("/Music/level1-1.mp3");
		// bgMusic.play();
	}

	public void update() {

		// update player
		player.update();

		tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());

		// set background
		//bg.setPosition(tileMap.getx(), tileMap.gety());
		for (int i = 0; i < bgLayer.length; i++)
			bgLayer[i].setPosition(tileMap.getx(), tileMap.gety());
		
		// check action at doors
		player.checkActionDoor(doors);
		// update all doors
		for (int i = 0; i < doors.size(); i++) {
			Door e = doors.get(i);
			e.update();
			if (e.passing && player.actionStopped()) {
				doorOpened(e);
			}
		}

	}

	public void draw(Graphics2D g) {

		// draw bg
		//bg.draw(g);
		for (int i = 0; i < bgLayer.length; i++)
			bgLayer[i].draw(g);

		// draw tilemap
		tileMap.draw(g);

		// draw doors
		for (int i = 0; i < doors.size(); i++) {
			doors.get(i).draw(g);
		}

		// draw player
		player.draw(g);

		// draw hud
		hud.draw(g);

	}

	public void keyPressed(int k) {
		if (k == KeyEvent.VK_LEFT)
			player.setLeft(true);
		if (k == KeyEvent.VK_RIGHT)
			player.setRight(true);
		if (k == KeyEvent.VK_UP)
			player.setJumping(true);
		if (k == KeyEvent.VK_DOWN)
			player.setAction();
		if (k == KeyEvent.VK_W)
			player.setJumping(true);
		if (k == KeyEvent.VK_A)
			player.setLeft(true);
		if (k == KeyEvent.VK_S)
			player.setAction();
		if (k == KeyEvent.VK_D)
			player.setRight(true);
	}

	public void keyReleased(int k) {
		if (k == KeyEvent.VK_LEFT)
			player.setLeft(false);
		if (k == KeyEvent.VK_RIGHT)
			player.setRight(false);
		if (k == KeyEvent.VK_UP)
			player.setJumping(false);
		if (k == KeyEvent.VK_W)
			player.setJumping(false);
		if (k == KeyEvent.VK_A)
			player.setLeft(false);
		if (k == KeyEvent.VK_D)
			player.setRight(false);
	}

	public void doorOpened(Door door) {
		if (door.getDestination() == GameStateManager.ROAMSTATE 
				&& gsm.GameValues().town() != gsm.getState()) {
			lastFrame(door.getDestination());
			gsm.setGameValues(player.changedValues());
			//gsm.setState(gsm.getState() + 1); // -very risky !!!-
			gsm.setState(GameStateManager.ROAMSTATE ); // -very risky !!!-
		}
		else if (door.getDestination() == GameStateManager.BUILDINGSTATE) {
			gsm.setBuildingType(door.getBuildingType());
			lastFrame(door.getDestination());
			gsm.setGameValues(player.changedValues());
			gsm.setState(door.getDestination());
		} else
			lastFrame(door.getDestination());
			gsm.setGameValues(player.changedValues());
			gsm.setState(door.getDestination());
	}
	
	private void populateDoors() {
		doors = new ArrayList<Door>();
		Door s;
		for(int i = 0; i < doorFeatures.length; i++) {
			s = new Door(tileMap,doorFeatures[i]);
			doors.add(s);
		}
	}

	public void lastFrame(int destination) {
		System.out.println("Exited from Town " + townLevel + ", to "+ destination);
	}
	
}
