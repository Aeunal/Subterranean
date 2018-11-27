package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import TileMap.Background;

public class HelpState extends GameState {

	private Background bg;

	private Color titleColor;
	private Font font;
	
	private int x;
	private int speed;
	private int acceleration;

	private String[] Instructions = {
			"STORY",
			"  In the year 2062, there has been a great apocalypse,",
			" but some people succeed at hiding in underground and",
			" continued to live there. In 2258, our protagonist ",
			" tries to go to the surface to understand what happened.",
			"",
			"","Controls\n","",
			"","Help/Story","",
			"W - Slow down the text",
			"A,D - Skip the text",
			"S - Accelerate the text",
			"","Fight\r\n","",
			"A – Defend\r\n", 
			"S – Surprise Opponent (Punch)\r\n",
			"D – Attack \r\n",
			"","Town\r\n","",
			"A, D – Walk\r\n", 
			"W – Jump\r\n",
			"S – Enter Building\r\n",
			"","Roam\r\n","",
			"D – Go forward\r\n",
			"S – Drink Potion\r\n", 
			"A – Escape\r\n",
			"",
			"You can use arrow keys instead of WASD keys..."
	};
	
	public HelpState(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}

	@Override
	public void init() {
		bg = new Background("/Backgrounds/apocalypse.gif", 1);
		bg.setVector(-0.1, 0);
		
		x = 0;
		speed = 0;
		acceleration = 5;

		titleColor = new Color(250, 250, 250);
		font = new Font("Arial", Font.PLAIN, 12);
	}

	@Override
	public void update() {
		bg.update();
		System.out.println(x);
		if(x<=-750) {
			skip();
		}
	}

	@Override
	public void draw(Graphics2D g) {
		// draw bg
		bg.draw(g);
		
		// draw title
		g.setColor(titleColor);
		g.setFont(font);
		
		for (int i = 0; i < Instructions.length; i++) {
			g.drawString(Instructions[i], 5, 140 + i * 15 + x +100);
		}
		
		speed+=acceleration;
		if(speed>=15) {
			x--;
			speed=0;
		}
	}

	@Override
	public void keyPressed(int k) {
		//skip();
		if(k == KeyEvent.VK_W || k == KeyEvent.VK_UP) acceleration = 2;
		else if( k == KeyEvent.VK_S || k == KeyEvent.VK_DOWN) acceleration = 15;
		if (k == KeyEvent.VK_SPACE	|| k == KeyEvent.VK_ENTER 
		 || k == KeyEvent.VK_LEFT	|| k == KeyEvent.VK_RIGHT
		 || k == KeyEvent.VK_A		|| k == KeyEvent.VK_D)
			skip();
	}

	private void skip() {
		gsm.setState(GameStateManager.MENUSTATE);
	}

	@Override
	public void keyReleased(int k) {
		acceleration = 5;
	}

}
