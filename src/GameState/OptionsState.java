package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import TileMap.Background;

public class OptionsState extends GameState{

	private Background bg;

	private int currentChoice = 0;
	private String[] options = {"Graphics", "Sound", "Controls", "Help", "Credits", "Enter Key", "Back" };

	private Color titleColor;
	private Font titleFont;
	private Font font;
	
	public OptionsState(GameStateManager gsm) {
		
		this.gsm = gsm;

		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void init() {
		bg = new Background("/Backgrounds/grassbg1.gif", 1);
		bg.setVector(-0.1, 0);
		
		titleColor = new Color(128, 0, 0);
		titleFont = new Font("Century Gothic", Font.PLAIN, 28);
		font = new Font("Arial", Font.PLAIN, 12);
	}

	public void update() {
		bg.update();
	}

	public void draw(Graphics2D g) {
		bg.draw(g);
		
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Options", 100, 70);
		
		g.setFont(font);
		for (int i = 0; i < options.length; i++) {
			if (i == currentChoice) {
				g.setColor(Color.BLACK);
			} else {
				g.setColor(Color.RED);
			}
			g.drawString(options[i], 155 - (options[i].length() * 3), 120 + i * 15);
		}
	}

	private void select() {
		if (currentChoice == 0) {
			//gsm.setState(GameStateManager.GRAPHICSSTATE);
		}
		if (currentChoice == 1) {
			//gsm.setState(GameStateManager.SOUNDSSTATE);
		}
		if (currentChoice == 2) {
			//gsm.setState(GameStateManager.CONTROLSSTATE);
		}
		if (currentChoice == 3) {
			//gsm.setState(GameStateManager.HELPSTATE);
		}
		if (currentChoice == 4) {
			//gsm.setState(GameStateManager.KEYENTERSTATE);
		}
		if (currentChoice == 5) {
			//gsm.setState(GameStateManager.CREDITSSTATE);
		}
		if (currentChoice == 6) {
			gsm.setState(GameStateManager.MENUSTATE);
		}
	}

	public void keyPressed(int k) {
		if (k == KeyEvent.VK_ENTER) {
			select();
		}
		if (k == KeyEvent.VK_UP) {
			currentChoice--;
			if (currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		if (k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if (currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}

	public void keyReleased(int k) {}

}
