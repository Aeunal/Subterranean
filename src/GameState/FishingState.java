package GameState;

import java.awt.Graphics2D;
import java.util.Random;

import Entity.Enemy;
import Entity.HUD;
import Entity.Item;
import Entity.Player;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

public class FishingState extends GameState {

	private Background bg;
	private Random rand = new Random();
	private TileMap tileMap;
	private HUD hud;
	private Player player;
	private Enemy enemy;

	// Timer timer = new Timer();
	private int seconds;
	private int frames;
	private int countdown;

	private int fontSize;
	private int fontColor;

	private boolean listenKeys;
	private boolean rod;

	private boolean tick;

	public FishingState(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}

	public boolean hasFishingRod() {
		if (gsm.GameValues().getInventory().hasItem(new Item("Fishing Rod")))
			return true;
		else
			return false;
	}

	@Override
	public void init() {
		// at start
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/cavetileset.gif");
		tileMap.loadMap("/Maps/Fight.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		bg = new Background("/Backgrounds/cavebg2.png", 0.1);

		player = new Player(tileMap, gsm);

		enemy = new Enemy(tileMap, gsm);
		enemy.setPosition(70 + player.getx(), 200);

		hud = new HUD(player);

		listenKeys = false;

		seconds = 0;
		tick = false;
		countdown = 3;
		fontSize = 60;
		fontColor = 240;

		if (hasFishingRod()) {
			
		} else {

		}
	}

	@Override
	public void update() {
		player.update();
		enemy.update();
		tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
		bg.setPosition(tileMap.getx(), tileMap.gety());
		

	}

	@Override
	public void draw(Graphics2D g) {

	}

	@Override
	public void keyPressed(int k) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub

	}

}
