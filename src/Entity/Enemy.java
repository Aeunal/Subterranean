package Entity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import GameState.GameStateManager;
import TileMap.TileMap;

public class Enemy extends MapObject {
	
	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage;
	
	protected boolean flinching;
	protected long flinchTimer;
	
	// animations
		private ArrayList<BufferedImage[]> sprites;
		private final int[] numFrames = {2, 8, 1, 2, 4, 2, 5};
		
		// animation actions
		private static final int IDLE = 0;//0
		private static final int WALKING = 1;//1
		private static final int JUMPING = 2;//2
		private static final int FALLING = 3;//3
		private static final int GLIDING = 4;//4
		private static final int EATING = 5;//5
		private static final int ACTION = 6;//6
		
		GameStateManager gsm;
		
	public Enemy(TileMap tm, GameStateManager gsm) {
		super(tm);
		this.gsm = gsm;
		// load sprites
		width = 30;//30
		height = 30;//30
		cwidth = 20;//20
		cheight = 20;//20
		
		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 10.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		
		facingRight = false;
		try {
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Player/enemysprites2.gif"
				)
			);
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < 7; i++) {
				BufferedImage[] bi =
					new BufferedImage[numFrames[i]];
				for(int j = 0; j < numFrames[i]; j++) {
					if(i != ACTION) {
						bi[j] = spritesheet.getSubimage(
								j * width,	i * height,
									width,		height
						);
					}
					else {
						bi[j] = spritesheet.getSubimage(
								j * width * 2,	i * height,
									width * 2,		height
						);
					}
				}
				sprites.add(bi);
			}
		} catch(Exception e) { e.printStackTrace();}
		
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
		
		//sfx = new HashMap<String, AudioPlayer>();
		//sfx.put("jump", new AudioPlayer("/SFX/jump.mp3"));
		//sfx.put("scratch", new AudioPlayer("/SFX/scratch.mp3"));
		
	}
	
	public boolean isDead() { return dead; }
	
	public int getDamage() { return damage; }
	
	public void hit(int damage) {
		if(dead || flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
private void getNextPosition() {
		
		switch (gsm.getState()) {
		case GameStateManager.ROAMSTATE:
			if(left) {
				x--;
			}
			break;
		case GameStateManager.FIGHTSTATE:
			
			// !!!   ATTENTION   !!! \\
			
			break;
		case GameStateManager.BUILDINGSTATE:
			
			break;
		case GameStateManager.TOWN1STATE:
		case GameStateManager.TOWN2STATE:
		case GameStateManager.TOWN3STATE:
			// movement
			if(left) {
				dx -= moveSpeed;
				if(dx < -maxSpeed) dx = -maxSpeed;
			} else if(right) {
				dx += moveSpeed;
				if(dx > maxSpeed) dx = maxSpeed;
			} else {
				if(dx > 0) {
					dx -= stopSpeed;
					if(dx < 0) dx = 0;
				} else if(dx < 0) {
					dx += stopSpeed;
					if(dx > 0) dx = 0;
				}
			}
			// cannot move while in action, except in air
			if((currentAction == ACTION) && !(jumping || falling)) 
				dx = 0;
			
			// jumping
			if(jumping && !falling) {
				//sfx.get("jump").play();
				dy = jumpStart;
				falling = true;
			}
			
			// falling
			if(falling) {
				
				if(dy > 0 && false) dy += fallSpeed * 0.1;
				else dy += fallSpeed;
				
				if(dy > 0) jumping = false;
				if(dy < 0 && !jumping) dy += stopJumpSpeed;
				
				if(dy > maxFallSpeed) dy = maxFallSpeed;
				
			} 
			
			break;
		default:
			break;
		}
		
		
	}
	
	public boolean actionStopped() {
		if(currentAction == ACTION) {
			if(animation.hasPlayedOnce()) return true;
			else return false;
		} else return false;
	}
	
	public void update() {
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// check attack has stopped
		if(currentAction == ACTION) {
			if(animation.hasPlayedOnce()) action = false;
		}
		
		// set animation
		if(action) {
			if(currentAction != ACTION) {
				//sfx.get("scratch").play();
				currentAction = ACTION;
				animation.setFrames(sprites.get(ACTION));
				animation.setDelay(50);
				width = 60;
			}
		}
		else if(dy > 0) {
			if(currentAction != FALLING) {
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
				width = 30;
			}
		}
		else if(dy < 0) {
			if(currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1);
				width = 30;
			}
		}
		else if(left || right) {
			if(currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
				width = 30;
			}
		}
		else {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width = 30;
			}
		}
		
		animation.update();
		
		// set direction
		if(currentAction != ACTION 
		&& currentAction != EATING) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
		
	}
	
	public void setRock() {}
	public void setPaper() {}
	public void setScissors() {}
	
	public void draw(Graphics2D g) {
		setMapPosition();
		super.draw(g);
	}
}














