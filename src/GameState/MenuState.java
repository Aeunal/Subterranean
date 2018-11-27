package GameState;

import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState extends GameState {

	private Background bg;

	private int currentChoice = 0;
	private String[] options = { "Start", "Help", "Quit" };

	private Color titleColor;
	private Font titleFont, titleFont2;
	private Font font;

	private int gradient;
	private int colorFactor;
	
	private double t;
	
	public MenuState(GameStateManager gsm) {

		this.gsm = gsm;
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void init() {
		gsm.GameValues().refreshPlayer();
		
		bg = new Background("/Backgrounds/darkbg.png", 1);
		bg.setVector(-0.2, 0);

		//titleColor = new Color(128, 0, 0);
		colorFactor=0;
		titleColor = new Color(colorFactor,colorFactor,colorFactor);
		titleFont = new Font("Century Gothic", Font.PLAIN, 28);
		font = new Font("Arial", Font.PLAIN, 12);
		
		gradient = 0;
		t=0;
	}

	public void update() {
		bg.update();
	}

	public void draw(Graphics2D g) {

		// draw bg
		bg.draw(g);
		
		titleColor = new Color(colorFactor,colorFactor,colorFactor);
		drawCube(g, 160, 115, 150, 150, 330, 0.05, titleColor);
		
		// draw title
		g.setColor(Color.BLACK);
		g.setFont(titleFont);
		g.drawString("Subterraneous", 59, 71);
		g.setColor(titleColor);
		g.drawString("Subterraneous", 60, 70);
		
		gradient++;
		colorFactor = Math.abs((200 - (gradient % 400))/2); 
		// draw menu options
		g.setFont(font);
		for (int i = 0; i < options.length; i++) {
			if (i == currentChoice) {
				g.setColor(Color.RED.darker());
				g.drawString("> "+options[i], 155 - (options[i].length() * 3) - i, 140 + i * 15);
			} else {
				g.setColor(Color.BLACK.brighter());
				g.drawString("  "+options[i], 155 - (options[i].length() * 3) - i, 140 + i * 15);
			}
			
		}
	}
	
	private void drawCube(Graphics2D g, int x, int y, int height, int width, int verticalAngle, double speed, Color color) {
		Point[] points = new Point[4];
		int[][] coefficient = new int[2][4];
		double midAngle = 720;
		double vertiacalAngleModded = verticalAngle%midAngle;
		double ratio = 1-(vertiacalAngleModded/(midAngle/2));
		if(vertiacalAngleModded>(360*(height/100))) vertiacalAngleModded = (360*(height/100)) - vertiacalAngleModded%(360*(height/100));
		//int heightAfterAngle = (int) (height * ratio);
		int heightAfterAngle = (int) (height - vertiacalAngleModded/5);
		int bottomSideA = heightAfterAngle; // connected to bottom
		int bottomSideB = heightAfterAngle; // connected to bottom
		int shiftBottom = (int)(vertiacalAngleModded/10);
		int horizontalAngle = 2;
		t=t+speed;
		for (int i = 0; i < coefficient[0].length; i++) {
			coefficient[0][i] = (int) (Math.sin(((i+1)*Math.PI/2)+t/Math.PI)*width);
			coefficient[1][i] = (int) (Math.sin(((i+1)*Math.PI/2)+(Math.PI/horizontalAngle)+t/Math.PI)*vertiacalAngleModded);
			points[i] = new Point(x+(coefficient[0][i]/2), y+(coefficient[1][i]/10));
		}
		g.setColor(color);
		for (int i = 0; i < points.length; i++) {
			g.drawLine(points[i].x, points[i].y+heightAfterAngle, points[i].x, points[i].y);
			g.drawLine(points[i].x, points[i].y+bottomSideA, points[(i+1)%4].x, points[(i+1)%4].y+bottomSideB);
			g.drawLine(points[i].x, points[i].y, points[(i+1)%4].x, points[(i+1)%4].y);
		}
	}

	private void select() {
		if (currentChoice == 0) {
			gsm.setState(GameStateManager.STORYSTATE);
		}
		if (currentChoice == 1) {
			gsm.setState(GameStateManager.HELPSTATE);
		}
		if (currentChoice == 2) {
			System.exit(0);
		}
	}

	public void keyPressed(int k) {
		if (k == KeyEvent.VK_ENTER	|| k == KeyEvent.VK_SPACE
		 || k == KeyEvent.VK_RIGHT	|| k == KeyEvent.VK_LEFT
		 || k == KeyEvent.VK_A		|| k == KeyEvent.VK_D)
			select();
		if (k == KeyEvent.VK_UP || k == KeyEvent.VK_W) {
			currentChoice--;
			if (currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		if (k == KeyEvent.VK_DOWN || k == KeyEvent.VK_S) {
			currentChoice++;
			if (currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}

	public void keyReleased(int k) {}

}
