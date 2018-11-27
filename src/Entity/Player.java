package Entity;

import TileMap.*;
import Audio.AudioPlayer;
import GameState.GameStateManager;
import GameState.GameValues;

import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Player extends MapObject {
	
	// player stuff
	
	// resources
	private boolean eating;
	private int hunger;
	//private int income;
	
	// action
	private boolean action;
	private int actionRange;
	
	// gliding
	private boolean gliding;
	
	// parameters
	private Point coordinates;
	
	// setting
	private boolean isEnemy;
	
	//private Inventory inventory;
	
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
	//private static final int DEAD = 7;//7
	//private static final int RUNNING = 8;//8
	
	//private HashMap<String, AudioPlayer> sfx;
	GameStateManager gsm;
	GameValues values;
	
	public Player(TileMap tm, GameStateManager gsm) {
		super(tm);
		this.gsm = gsm;
		this.values = gsm.GameValues();
		
		initParameters();
		initStats();
		
		// load sprites
		try {
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Player/playersprites2.gif"
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
	
	
	
	public void initParameters() {
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
		
		facingRight = true;
		actionRange = 25;
	}
	
	public void initStats() {
		hunger = 200;
		//income = 5;
		
		setPosition(
				values.getLastPosition()[gsm.getState()-1].getX(),
				values.getLastPosition()[gsm.getState()-1].getY());
	
	}
	
	public GameValues changedValues() { return values; }
	
	public void checkActionDoor(ArrayList<Door> doors) {
		
		// loop through doors
		for(int i = 0; i < doors.size(); i++) {
			
			Door e = doors.get(i);
			// action check
			if(action) {
				if(facingRight) {
					if(
						e.getx() > x - (actionRange - 10) &&
						e.getx() < x + actionRange && 
						e.gety() > y - height / 2 &&
						e.gety() < y + height / 2
					) {
						e.open();
					}
				}
				else {
					if(
						e.getx() < x + (actionRange - 10) &&
						e.getx() > x - actionRange &&
						e.gety() > y - height / 2 &&
						e.gety() < y + height / 2
					) {
						e.open();
					}
				}
			}
			
			// check door collision
			if(intersects(e)) {
				//bright door or open door (then enter?)
			}
			
		}
		
	}
	
	private void getNextPosition() {
		
		switch (getLocation()) {
		case GameStateManager.ROAMSTATE:
			if(left) {
				x--;
			}
			else if(right) {
				if(wayToGo()) x=x+1.5;
				else x++;
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
				
				if(dy > 0 && gliding) dy += fallSpeed * 0.1;
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
		
		// economical movements
		//money ++;
		//if(money > maxMoney) money = maxMoney;
		if(eating && currentAction != EATING) {
			if(hunger > 10) {//10 will be change
				hunger -= 10;
			}
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
		else if(eating) {
			if(currentAction != EATING) {
				currentAction = EATING;
				animation.setFrames(sprites.get(EATING));
				animation.setDelay(100);
				width = 30;
			}
		}
		else if(dy > 0) {
			if(gliding) {
				if(currentAction != GLIDING) {
					currentAction = GLIDING;
					animation.setFrames(sprites.get(GLIDING));
					animation.setDelay(100);
					width = 30;
				}
			}
			else if(currentAction != FALLING) {
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
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		super.draw(g);
		values.setLastPosition(
				new Point(this.getx(), this.gety()), 
				gsm.getState());
		
	}

	public boolean walkedOnRoad() {
		if(getRemainingRoad()>=50) {
			setRemainingRoad(getRemainingRoad()-50);
			tired(10);
			return true;
		} else return false;
	}
	
	public boolean wayToGo() {
		if(getRemainingRoad()>0) {
			return true;
		} else return false;
	}
	
	//private void escape() {}
	/*private Point startingCoordinate() { // previousLocation to location
		switch(getLocation()) { //to
		case GameStateManager.TOWN1STATE:
			switch (getPreviousLocation()) { // from
			case GameStateManager.STORYSTATE:
				return new Point(100,200);
			case GameStateManager.ROAMSTATE:
				return new Point(650, 170);
			case GameStateManager.TOWN2STATE:
				return new Point(650, 170);
			default:
				return new Point(100, 200);
			}
		case GameStateManager.TOWN2STATE:
			switch (getPreviousLocation()) { // from
			case GameStateManager.ROAMSTATE:
				if(values.getLastRoamLevel() == 1) {
					return new Point(100, 200);
				} else {
					return new Point(650, 170);
				}
			case GameStateManager.TOWN1STATE:
				return new Point(100,200);
			default:
				return new Point(100, 200);
			}
		case GameStateManager.ROAMSTATE:
			return new Point(100, 200);
		case GameStateManager.FIGHTSTATE:
			return new Point(130, 200);
		default:
			return new Point(100, 200);
		}
	}*/
	
	public int getLocation() { return gsm.getState();}
	public int getPreviousLocation() { return gsm.getPreviousState();}
	public int getLevel() { return values.getLevel();}
	public int getRoamLevel() { return values.getLastRoamLevel();}
	public int getRoad() {return  values.getRemainingRoad();}
	public int getRemainingRoad() {return values.getRemainingRoad();}
	public int getMaxMoney() { return values.getMaxMoney(); }
	public int getMaxHealth() { return values.getMaxHealth(); }
	public int getMaxDebt() { return values.getMaxDebt(); }
	public double getMaxEnergy() {return values.getMaxEnergy();}
	public double getEnergy() {return values.getEnergy();}
	public double getStartingMoney() {return values.getStartingMoney();}
	public double getStartingTime() {return values.getStartingTime();}
	public double getBaseAttack() {return values.getBaseAttack();}
	public double getBaseDefence() {return values.getBaseDefence();}
	public double getStartingTown() {return values.getStartingTown();}
	public double getStartingRoam() {return values.getStartingRoam();}
	public double getHealth() { return values.getHealth(); }
	public double getMoney() { return values.getMoney(); }
	public double getDay() { return values.getDay(); }
	public boolean isEnemy() {return isEnemy;}
	
	
	public void setMoney(double amount) {this.values.setMoney(amount);}
	public void setRoad(int road) {this.values.setRemainingRoad(road);}
	public void setRemainingRoad(int remainingRoad) {this.values.setRemainingRoad(remainingRoad);}
	public void setGliding(boolean b) { gliding = b;}
	public void setPaying() { eating = true;}
	public void setAction() { action = true;}
	public void setEnemy(boolean isEnemy) {this.isEnemy = isEnemy;}
	public boolean addMoney(double amount) { // auto change money
		if(getMoney() + amount >= 0) {
			setMoney(getMoney()+amount);
			return true;
		} else return false;
	}

	public void checkActionPotion() { // dont forget to fill
		
	}
	
	public void tired(int energy) {
		values.setEnergy(values.getEnergy()-energy);
	}

	
}
