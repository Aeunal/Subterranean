package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import GameState.GameStateManager;

public class HUD {

	private Player player;
	private Enemy enemy;

	private BufferedImage image;
	private Font font;
	private long startTime;
	private int seconds;

	public HUD(Player p) {
		player = p;
		startTime = System.nanoTime();
		try {
			switch (player.getLocation()) {
			case GameStateManager.TOWN1STATE:
			case GameStateManager.TOWN2STATE:
			case GameStateManager.TOWN3STATE:
				image = ImageIO.read(getClass().getResourceAsStream("/HUD/hud_0.gif"));
				break;
			case GameStateManager.ROAMSTATE:
				image = ImageIO.read(getClass().getResourceAsStream("/HUD/hud_4.gif"));
				break;
			case GameStateManager.FIGHTSTATE:
				image = ImageIO.read(getClass().getResourceAsStream("/HUD/hud_6.gif"));
			default:
				break;
			}
			font = new Font("Arial", Font.PLAIN, 14);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void draw(Graphics2D g) {

		g.drawImage(image, 0, 10, null);
		g.setFont(font);
		g.setColor(Color.WHITE);
		
		switch (player.getLocation()) {
		case GameStateManager.TOWN1STATE:
		case GameStateManager.TOWN2STATE:
		case GameStateManager.TOWN3STATE:	
			healthBar(g, 17, 14);
			EnergyBar(g, 17, 35);
			printMoney(g, 16, 66);
			printDay(g, 170, 15);
			break;
		case GameStateManager.ROAMSTATE:
			healthBar(g, 17, 14);
			EnergyBar(g, 17, 35);
			printRoad(g, 28, 66);
			break;
		case GameStateManager.FIGHTSTATE:
			healthBar(g, 17, 14);
			enemyHealthBar(g, 251, 14);
			EnergyBar(g, 17, 35);
			EnemyEnergyBar(g, 251, 35);
			break;
		default:
			break;
		}

	}

	public void healthBar(Graphics g, int x, int y) {
		// g.drawString(player.getHealth() + "/" + player.getMaxHealth(), 30, 25);
		double ratio = player.getHealth() / (double) player.getMaxHealth();
		int width = 50;
		int height = 10;
		Color memory = g.getColor();
		g.setColor(Color.BLACK);
		g.fill3DRect(x, y, width, height, true);
		g.setColor(Color.GREEN);
		g.fill3DRect(x + 1, y + 1, (int) ((width - 2) * ratio), height - 2, true);
		g.setColor(memory);
	}

	public void enemyHealthBar(Graphics g, int x, int y) {
		// g.drawString(player.getHealth() + "/" + player.getMaxHealth(), 30, 25);
		double ratio = player.values.getEnemyHealth() / (double)player.values.getEnemyMaxHealth();
		//System.out.println(ratio);
		int width = 50;
		int height = 10;
		Color memory = g.getColor();
		g.setColor(Color.BLACK);
		g.fill3DRect(x, y, width, height, true);
		g.setColor(Color.GREEN);
		g.fill3DRect(x + 1, y + 1, (int) ((width - 2) * ratio), height - 2, true);
		g.setColor(memory);
	}
	
	public void EnergyBar(Graphics g, int x, int y) {
		double ratio = player.getEnergy() / (double) player.getMaxEnergy();
		int width = 50;
		int height = 10;
		Color memory = g.getColor();
		g.setColor(Color.BLACK);
		g.fill3DRect(x, y, width, height, true);
		g.setColor(Color.CYAN);
		g.fill3DRect(x + 1, y + 1, (int) ((width - 2) * ratio), height - 2, true);
		g.setColor(memory);
	}
	
	public void EnemyEnergyBar(Graphics g, int x, int y) {
		double ratio = player.values.getEnemyEnergy() / (double) player.values.getEnemyMaxEnergy();
		int width = 50;
		int height = 10;
		Color memory = g.getColor();
		g.setColor(Color.BLACK);
		g.fill3DRect(x, y, width, height, true);
		g.setColor(Color.CYAN);
		g.fill3DRect(x + 1, y + 1, (int) ((width - 2) * ratio), height - 2, true);
		g.setColor(memory);
	}
	
	public void printMoney(Graphics g, int x, int y) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.ITALIC, 12));
		g.drawString(player.getMoney() + " coin",  x, y);
	}
	
	public void printRoad(Graphics g, int x, int y) {
		g.drawString(player.getRemainingRoad() + " m", x, y);
	}
	
	public void printDay(Graphics g, int x, int y) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 14));
		g.drawString("DAY " + (int)(player.getDay()+1), x+100, y);
		//seconds = (int) (System.nanoTime()/1000000000) - (int) (startTime/1000000000);
		/*int startingSec = 19;
		int startingMin = 27;
		int startingHour = 7;
		// 1 year = 4 underworld Year
		// 1 year = 13 underworld Month  
		// 1 underworld Month = 28 day //13th month 29 day , 30 every 4 year once
		// 1 week = 1 underworld Month
		// 1 day = 4 underworld day // when tides increase one day passes
		// 1 underworld year = 6 hour
		// 1 underworld sec = 4/3 sec // heart pulse
		int underworldMin = 55; // underowrld Second
		int underworldHour = 30; //underworld Minute
		int underworldDay = 10; // underworld Hour
		seconds+=startingSec+underworldMin*startingMin+underworldHour*startingHour*underworldMin;
		String date = (int)player.getDay()+10 + " / 5 / 784"+
				"   "+((seconds%(underworldHour*underworldMin*underworldDay))/(underworldHour*underworldMin)) +
				" : "+(((seconds%(underworldHour*underworldMin))/underworldMin)) + // +1 ?
				" : "+((seconds)%underworldMin);
		g.drawString(date, x, y);*/
	}
	
}
