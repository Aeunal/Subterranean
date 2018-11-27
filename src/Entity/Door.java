package Entity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import GameState.GameStateManager;
import TileMap.TileMap;

public class Door extends MapObject {

	private int buildingType;
	private int destination;
	private int doorType;
	private BufferedImage[] sprites;
	
	public boolean passing;
	protected boolean opening;
	
	public Door(TileMap tm) {
		super(tm);

		width = 20;
		height = 40;
		cwidth = 20;
		cheight = 20;

		try {

			BufferedImage spritesheet = 
					ImageIO.read(getClass().getResourceAsStream
							("/Sprites/Enemies/Door2.gif"));

			sprites = new BufferedImage[1];
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(300);
		
		facingRight = true;
		opening = false;
		passing = false;
		
		setPosition(x, y);
		
	}
	
	public Door(TileMap tm , int[] features) {
		super(tm);

		Point point = new Point(features[0],features[1]);
		setDestination(features[2]);
		setDoorType(features[3]);
		if(features[2] == GameStateManager.BUILDINGSTATE)
			setBuildingType(features[4]);
		
		x = point.getX();
		y = point.getY();
		
		width = 20;
		height = 40;
		cwidth = 20;
		cheight = 20;

		try {
			
			BufferedImage spritesheet;
			if(doorType == 0) {
				spritesheet = 
						ImageIO.read(getClass().getResourceAsStream
								("/Sprites/Enemies/Door2.gif"));
			} else {
				spritesheet = 
						ImageIO.read(getClass().getResourceAsStream
								("/Sprites/Enemies/Door.gif"));
			}

			sprites = new BufferedImage[1];
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(300);
		
		facingRight = true;
		opening = false;
		passing = false;
		
		setPosition(x, y);
		
	}


	public Door(TileMap tm, int type) {
		super(tm);
		this.setBuildingType(type);
	}
	
	public int getBuildingType() { return buildingType; }
	
	public void setBuildingType(int buildingType) { this.buildingType = buildingType; }
	
	public void open() {
		if(opening) return;
		opening = true;
	}

	public void update() {
		if(opening) {
			passing = true;
			opening = false;
		}
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		animation.update();
	}

	public void draw(Graphics2D g) {
		//if (notOnScreen()) return;
		setMapPosition();
		super.draw(g);
	}

	public int getDoorType() {
		return doorType;
	}

	public void setDoorType(int doorType) {
		this.doorType = doorType;
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}

}
