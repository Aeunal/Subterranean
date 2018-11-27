package GameState;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import Entity.Enemy;
import Entity.HUD;
import Entity.Player;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;
import java.util.Random;

public class RoamState extends GameState {

	private int[] roadLengths = { // initial road lengths;
			400, 600, 700
	};
	
	private TileMap tileMap;
	private Background bg;
	private Player player;
	private Enemy enemy;
	private HUD hud;
	private Random rand;
	
	private int x;
	private int bgX;

	private boolean enemySighted;
	private boolean inputLock;
	private boolean forward;
	private boolean escape;
	private boolean heal;
	
	private int roamLevel;
	private int roadGone;
	
	public RoamState(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}

	public void init() {
		
		bgX = 0;
		rand = new Random();
		
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/cavetileset.gif");
		tileMap.loadMap("/Maps/Roam.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Backgrounds/cavebg2.png", 0.1);

		player = new Player(tileMap,gsm);
		
		enemy = new Enemy(tileMap,gsm);
		enemy.setPosition(230+player.getx(), 200);
		
		roamLevel = gsm.GameValues().RoamLevelUp(); //same time increase lvl

		hud = new HUD(player);
		
		inputLock = true;
		enemySighted = false;
		roadGone = 400 - gsm.GameValues().getRemainingRoad();
		
		player.setRemainingRoad(gsm.GameValues().getRemainingRoad());

		// bgMusic = new AudioPlayer("/Music/level1-1.mp3");
		// bgMusic.play();

	}

	
	public void update() {
		
		player.update();
		enemy.update();
		
		tileMap.setPosition(-1.5*x+1, GamePanel.HEIGHT / 2 - player.gety());
		bg.setPosition(-bgX, tileMap.gety());
		
		player.checkActionPotion();
		
		if(enemySighted) { //enemy encounter
			
			inputLock = false;
			enemy.setLeft(true);
			if(enemy.getx()-player.getx() < 50) {
				gsm.GameValues().setRemainingRoad(400-roadGone);
				gsm.setState(gsm.FIGHTSTATE);
			}
			
		} else { // go forward
			
			// set background + move effect
			
			if(player.wayToGo()) {
				if(forward) {
					enemy.setPosition(230+player.getx(), 200);
					x++;
					player.setRight(true);
					bgX++;
					if(x>100) {
						player.walkedOnRoad();
						roadGone += 50;
						
						int n = rand.nextInt(100);
						if(n < 35) {
							enemySighted = true;
							enemy.setPosition(80+player.getx(), 200);
						}
						
						x=0;
						inputLock=true;
						forward=!forward;
						player.setRight(false);
						player.setPosition(100, 200);
					}
				}
			} else { // go front town
				if(forward) {
					x--;
					player.setRight(true);
					if(x<-230) { //fixed to 230
						x=0;
						inputLock=true;
						forward=!forward;
						player.setRight(false);
						gsm.GameValues().levelUp();
						gsm.setState(gsm.GameValues().town());
					}
				}
			}
			
			if(escape) { // go back town
				x--;
				player.setLeft(true);
				if(x<-115) { // fixed to 115
					x=0;
					inputLock=true;
					escape=!escape;
					player.setLeft(false);
					gsm.setState(gsm.GameValues().town());
				}
			}
		}
		
	}

	public void draw(Graphics2D g) {
		// draw bg
		bg.draw(g);

		// draw tilemap
		tileMap.draw(g);

		// draw player
		player.draw(g);
		
		// draw enemy
		enemy.draw(g);

		// draw hud
		hud.draw(g);
	}
	
	public void walk() {
		
	}
	
	public void escape() {
		
	}

	public void drinkPotion() {
		
	}
	
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_A  || k == KeyEvent.VK_LEFT && inputLock) {
			escape = true;
			inputLock = false;
		}
		if (k == KeyEvent.VK_S || k == KeyEvent.VK_RIGHT && inputLock) {
			forward = true;
			inputLock = false;
		}
		if (k == KeyEvent.VK_D || k == KeyEvent.VK_DOWN && inputLock) {
			inputLock = false;
			player.setAction();
			inputLock = true;
			
		}
		//if (k == KeyEvent.VK_A && inputLock) player.setLeft(true);
		//if (k == KeyEvent.VK_S && inputLock) player.setAction();
		//if (k == KeyEvent.VK_D && inputLock) player.setRight(true);
	}

	public void keyReleased(int k) {}

	public int[] getRoadLengths() {
		return roadLengths;
	}

	public void setRoadLengths(int[] roadLengths) {
		this.roadLengths = roadLengths;
	}

}
