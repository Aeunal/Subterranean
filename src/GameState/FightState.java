package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.util.Random;

import Entity.Enemy;
import Entity.HUD;
import Entity.Player;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

//import com.sun.glass.events.KeyEvent;

public class FightState extends GameState {

	private Background bg;
	private Random rand = new Random();
	private TileMap tileMap;
	private HUD hud;
	private Player player;
	private Enemy enemy;
	
	//Timer timer = new Timer();
	private int seconds;
	private int frames;
	private int countdown;
	
	private int fontSize;
	private int fontColor;
	
	private int nextMoveOfPlayer;
	private int nextMoveOfEnemy;
	
	private boolean listenKeys;
	private boolean enemyMoved;
	
	private boolean tick;
	
	private static final int ROCK = 1;
	private static final int PAPER = 2;
	private static final int SCISSORS = 3;
	
	public FightState(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}

	public void init() {
		// at start
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/cavetileset.gif");
		tileMap.loadMap("/Maps/Fight.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		bg = new Background("/Backgrounds/cavebg2.png", 0.1);
		
		player = new Player(tileMap,gsm);
		
		enemy = new Enemy(tileMap,gsm);
		enemy.setPosition(70+player.getx(), 200);
		
		hud = new HUD(player);
		
		listenKeys = false;
		enemyMoved = false;
		
		seconds = 0;
		tick = false;
		countdown = 3;
		fontSize = 60;
		fontColor = 240;
	}

	public void update() {
		// every frame
		player.update();
		enemy.update();
		tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
		bg.setPosition(tileMap.getx(), tileMap.gety());
		
		if(frames%60 == 0) {
			seconds++;
			tick = true;
		}
		frames++;
		fontSize = 90 - (frames % 60);
		fontColor = (frames*3)%180;
		
		if(tick && countdown <= 0) {
			if(countdown == 0 ) {
				if(tick) {
					tick = false;
					countdown--;
				}
			}
			fight();
			int n = rand.nextInt(99);
			tick = false;
			if(n%3==0) {
				//enemySighted = true;
				nextMoveOfEnemy = 1;
			} else if (n%3==1) {
				nextMoveOfEnemy = 2;
			} else {
				nextMoveOfEnemy = 3;
			}
		}
		
		
		switch(encounter(nextMoveOfPlayer, nextMoveOfEnemy)) {
		case 0:
			
			break;
		case 1: 
			break;
		case -1: 
			break;
		}
		
		switch(nextMoveOfEnemy) {
		case ROCK: enemy.setRock();
		break; case PAPER: enemy.setPaper();
		break; case SCISSORS: enemy.setScissors();
		}
		
		
		if(player.getHealth()<=0) {
			gsm.GameValues().refreshPlayer();
			gsm.GameValues().refreshEnemy();
			gsm.setState(gsm.DEATHSTATE);
		}
		
		if(gsm.GameValues().getEnemyHealth()<=0) {
			gsm.GameValues().refreshEnemy();
			gsm.GameValues().addMoney(20+rand.nextInt(20));
			gsm.setState(gsm.ROAMSTATE);
		}
		
		//System.out.println(nextMoveOfPlayer+" p-e "+nextMoveOfEnemy);
		
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
		
		if(countdown >= 0) {
			if(tick) {
				tick = false;
				countdown--;
			}	
			g.setColor(new Color(fontColor, fontColor, fontColor));
			g.setFont(new Font(Font.DIALOG, Font.PLAIN, fontSize));
			g.drawString(""+(countdown+1), 
					GamePanel.WIDTH / 2 + (frames % 60)/5 - 20 , 
					GamePanel.HEIGHT / 2 - (frames % 60)/5 + 20);
			if (countdown == 0) {
				listenKeys = true;
			}
		} else {}
		
		switch(nextMoveOfPlayer) {
		case 1:
			drawShield(g, true);
		break; case 2:
			drawPunch(g, true);
		break; case 3:
			drawSword(g, true);
		break;}
		
		switch (nextMoveOfEnemy) {
		case 1:
			drawShield(g, false);
		break; case 2:
			drawPunch(g, false);
		break; case 3:
			drawSword(g, false);
		break;}
		/*
		// draw enemies
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}
		
		// draw explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition(
				(int)tileMap.getx(), (int)tileMap.gety());
			explosions.get(i).draw(g);
		}
		 */
		
		
	}
	
	public void drawShield(Graphics2D g, boolean forPlayer) {
		if(forPlayer) {
			Color c = new Color(127,0,0,g.getColor().getAlpha());
			g.setColor(c.darker());
			g.drawRoundRect(player.getx()+player.getCWidth()-10, player.gety()-player.getCHeight()/2, 5, 24, 5, 5);
			g.setColor(c.brighter());
			g.fillRoundRect(player.getx()+player.getCWidth()-10, player.gety()-player.getCHeight()/2, 5, 24, 5, 5);
		} else {
			Color c = new Color(127,0,0,g.getColor().getAlpha());
			g.setColor(c.darker());
			g.drawRoundRect(enemy.getx()-enemy.getCWidth()+10, enemy.gety()-enemy.getCHeight()/2, 5, 24, 5, 5);
			g.setColor(c.brighter());
			g.fillRoundRect(enemy.getx()-enemy.getCWidth()+10, enemy.gety()-enemy.getCHeight()/2, 5, 24, 5, 5);
		}
	}
	
	public void drawPunch(Graphics2D g, boolean forPlayer) {
		if(forPlayer) {
			player.setAction();
		} else {
			enemy.setLeft(true);
			enemy.setAction(true);
			enemy.setLeft(false);
		}
		//player.actionStopped();
	}
	
	public void drawSword(Graphics2D g, boolean forPlayer) {
		//Color c = new Color(g.getColor().getBlue()/3,g.getColor().getGreen()/3,g.getColor().getBlue()/3,240);
		//Color c = new Color(g.getColor().getBlue()/3,g.getColor().getGreen()/3,200,210);
		if(forPlayer) {
			Color c = new Color(0,0,200,250);
			int[][] points = {
					{	player.getx()-5, 
						player.getx()-5+2, 
						player.getx()+player.getCWidth()+2, 
						player.getx()+player.getCWidth()},
					{	player.gety()+player.getCHeight()/2, 
						player.gety()+player.getCHeight()/2+2, 
						player.gety()-player.getCHeight()/2+2, 
						player.gety()-player.getCHeight()/2}
			};
			g.setColor(c.darker());
			g.fillPolygon(new Polygon(points[0], points[1], 4) );
		} else {
			Color c = new Color(0,0,200,250);
			int[][] points = {
					{	enemy.getx()-enemy.getCWidth(),
						enemy.getx()+5, 
						enemy.getx()+5-2, 
						enemy.getx()-enemy.getCWidth()-2, },
					{	enemy.gety()-enemy.getCHeight()/2, 
						enemy.gety()+enemy.getCHeight()/2,
						enemy.gety()+enemy.getCHeight()/2+2, 
						enemy.gety()-enemy.getCHeight()/2+2, }
			};
			
			g.setColor(c.darker());
			g.fillPolygon(new Polygon(points[0], points[1], 4) );
		}
	}
	
	public void fight() {
		switch (encounter(nextMoveOfPlayer, nextMoveOfEnemy)) {
		case 0:
			gsm.GameValues().enemyAttackedPlayer(false);
			gsm.GameValues().playerAttackedEnemy(false);
			break;
		case 1:
			gsm.GameValues().enemyAttackedPlayer(false);
			gsm.GameValues().playerAttackedEnemy(true);
			break;
		case -1:
			gsm.GameValues().enemyAttackedPlayer(true);
			gsm.GameValues().playerAttackedEnemy(false);
			break;
		}
	}
	
	public void keyPressed(int k) {
		// if(k == KeyEvent.VK_ENTER) ;
		if(listenKeys) {
			if(k == KeyEvent.VK_A) {
				//listenKeys = false;
				nextMoveOfPlayer = 1;
			} else if(k == KeyEvent.VK_S) {
				//listenKeys = false;
				nextMoveOfPlayer = 2;
			} else if(k == KeyEvent.VK_D) {
				//listenKeys = false;
				nextMoveOfPlayer = 3;
			} else {
				nextMoveOfPlayer = -1;
				//gsm.setState(gsm.ROAMSTATE);
			}
		}
	}

	public void keyReleased(int k) {
		//nextMoveOfPlayer = 0;
	}
	
	public int encounter(int userMove, int enemyMove) {
		switch (userMove) {
		case ROCK:
			switch (enemyMove) {
			case ROCK: return 0;
			case PAPER: return -1;
			case SCISSORS: return 1;
			}
		case PAPER:
			switch (enemyMove) {
			case ROCK: return 1;
			case PAPER: return 0;
			case SCISSORS: return -1;
			}
		case SCISSORS:
			switch (enemyMove) {
			case ROCK: return -1;
			case PAPER: return 1;
			case SCISSORS: return 0;
			}
		default: return 0;
		}
	}
	
	//private void select() {
		/*
		if(currentChoice == 0) {
			gsm.setState(GameStateManager.LEVEL1STATE);
		}
		if(currentChoice == 1) {
			// help
		}
		if(currentChoice == 2) {
			System.exit(0);
		}*/
	//}

}
