package GameState;

import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

import Entity.Item;
import Main.GamePanel;

public class BuildingState extends GameState {

	private Background bg;

	private int currentChoice = 0;
	private String[] menuOptions;
	private int menuOptionNumber;
	private String[] Bank = { "Deposit Cash", "Draw Cash", "Exit" };
	private String[] Inn = { "Eat(-2 c)", "Sleep (-10 c)", "Exit" };
	private String[] Market = { "Buy", "Exit" };
	private String[] BlackSmith = { "Enchant Sword", "Enchant Shield", "Exit" };
	private String[] Library = { "Read", "Study", "Exit" };
	private String[] Academy = { "Learn", "Exit" };
	private String[] TrainingAcademy = { "Train", "Body Build", "Exit" };
	private String[] JunkDealer = { "Repair", "Sell", "Exit" };
	private String[] Casino = { "Gamble", "Exit" };
	private String[] Workshops = { "Work", "Craft", "Exit" };
	private String[][] Store = {{ "Weapon", "Armor", "Exit" },
			{"Sword", "Gun", "Back"}};
	private String[][] Hospital = { {"Buy medicine", "Treatment (-5 c)", "Exit" },
			{"Small health potion(-3 c)", "Large health potion(-6 c)", "Back"}};

	private Color titleColor;
	private Font titleFont, titleFont2;
	private Font font;
	String input;
	private int alpha;
	private boolean takeInput;
	private boolean sleeping;
	private boolean waking;
	
	private int buildingType; 
	private int buildingTypeCount = 5; 
	private int amount;
	
	private final static int TOWN = 0;
	private final static int BANK = 1;
	private final static int SHOP = 2;
	private final static int HOSPITAL = 3;
	private final static int INN = 4;
	private final static int BLACKSMITH= 5;
	
	
	public BuildingState(GameStateManager gsm) {
		this.gsm = gsm;
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public BuildingState(GameStateManager gsm, int type) {
		this.gsm = gsm;
		this.buildingType = type;
		init();
	}

	public void init() {
		switch(buildingType) {
		case BANK: 
			bg = new Background("/Backgrounds/interior.png", 1); break;
		case INN:
			bg = new Background("/Backgrounds/interior_2.jpg", 1); break;
		default: 
			bg = new Background("/Backgrounds/interior.png", 1); break;
		}
		titleColor = new Color(140,140,140);
		titleFont = new Font("Century Gothic", Font.PLAIN, 28);
		font = new Font("Arial", Font.BOLD, 14);
		input=""; amount = 0;
		alpha = 0;
		takeInput = true;
		sleeping = false;
		waking = false;
		
		menuOptionNumber = 0;
	}

	public void update() {
		bg.update();
	}
	
	public void lastFrame() {}

	public void draw(Graphics2D g) {
		// draw bg
		bg.draw(g);
		
		// draw title
		g.setFont(titleFont);
		g.setColor(titleColor);
		
		// titleHeader
		Rectangle title = new Rectangle(0,1,GamePanel.WIDTH,30);
		g.setColor(new Color(255,255,255,127));
		g.fillRect(title.x, title.y, title.width, title.height);
		
		// draw menu options
		switch(buildingType) {
		case BANK: 
			Rectangle r = new Rectangle(115,105,100,20);
			g.setColor(new Color(0,0,0,127));
			g.fillRect(r.x, r.y, r.width, r.height);
			g.setColor(Color.black);
			
			g.setFont(font.deriveFont(Font.PLAIN, 14));
			if(input.length()>0)
				g.drawString(input + "          Coin".substring(2*(input.length()-1), 14), r.x+8, r.y+r.height/2 + 5);
			else 
				g.drawString("Enter   Coin", r.x+8, r.y+r.height/2 + 5);
			
			menuOptions = Bank;
			g.drawString("Bank", 30, 25);
			menuOptions = Bank;
			g.setFont(font);
			g.drawString(gsm.GameValues().getMoneyInBank()+" coin in bank", 200, 13);
			g.drawString(gsm.GameValues().getMoney()+" coin in pocket", 200, 28);
		break;
		case HOSPITAL:
			menuOptions = Hospital[menuOptionNumber];
			g.drawString("Hospital", 30, 25);
		break;
		case INN:
			menuOptions = Inn;
			g.drawString("Inn", 30, 25);
			sleeping();
			waking();
			printDay(g, 170, 15);
			printMoney(g, 250, 28);
			break;
		default:break;
		}
		
		g.setFont(font);
		for (int j = 0; j < menuOptions.length; j++) {
		if (j == currentChoice) {
				g.setColor(Color.WHITE);
				g.drawString(menuOptions[j], 155 - (menuOptions[j].length() * 3) - j, 140 + j * 15);
			} else {
				g.setColor(Color.BLACK.brighter());
				g.drawString(menuOptions[j], 155 - (menuOptions[j].length() * 3) - j, 140 + j * 15);
			}
		}
		
		g.setColor(new Color(0,0,0,alpha));
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
	}
	
	public void waking() {
		if(waking) {
			if(alpha > 0) {
				alpha--;
				alpha--;
			} else {
				waking = false;
				takeInput = true;
			}
		}
	}
	
	public void sleeping() {
		if(sleeping) {
			if(alpha < 254) {
				alpha++;
				alpha++;
			} else {
				sleeping = false;
				nextDay();
				moneyAfterInterest(1.1);
				addMoney(-10);
				waking = true;
			}
		} 
	}
	
	public void printDay(Graphics g, int x, int y) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 14));
		g.drawString("DAY " + (int)(gsm.GameValues().getDay()+1), x+100, y);
	}
	
	public void printMoney(Graphics g, int x, int y) {
		g.drawString(gsm.GameValues().getMoney()+" coin", x, y);
	}
	
	private void select() {
		switch(buildingType) {
		case BANK:
			switch(currentChoice) {
			case 0: // deposit
				if(addMoney(-amount))
					addMoneyToBank(amount);
				break;
			case 1: // draw
				if(addMoneyToBank(-amount))
					addMoney(amount); 
				break;
			case 2:
				if (currentChoice == 2)
					goBack();
				break;
			}
			break;
		case HOSPITAL:
			switch(currentChoice) {
			case 0:
				if(menuOptionNumber == 0) // open other tab
					menuOptionNumber = 1; 
				else {
					if(addMoney(-3))
						addItem(0, 1);
				} break;
			case 1:
				if(menuOptionNumber == 0) 
					if(addMoney(-5)) {
						heal(3);
					}
				else {
					if(addMoney(-6))
						addItem(0, 2);
				} break;
			case 2:
				if (currentChoice == 2) {
					if(menuOptionNumber == 1) {
						menuOptionNumber = 0;
						currentChoice = 0;
					} else 
						goBack();
				} break;
			}
			break;
		case INN:
			switch(currentChoice) {
			case 0:
				if(addMoney(-2)) {
					resting(10);
				} break;
			case 1:
				if(isMoneyEnough(10)) {
					sleeping = true;
					takeInput = false;
					// functions moved into end of sleeping();
				} break;
			case 2:
				if (currentChoice == 2) {
					goBack();
				} break;
			}
			break;
		}
	}
	
	public void resting(int energy) {
		if(gsm.GameValues().getEnergy()+energy < gsm.GameValues().getMaxEnergy())
			gsm.GameValues().setEnergy(gsm.GameValues().getEnergy()+energy);
		else gsm.GameValues().setEnergy(gsm.GameValues().getMaxEnergy());
	}
	
	public void goBack() {
		gsm.setState(gsm.getPreviousState());
	}
	
	public void addItem(int itemId,int itemLvl) {
		gsm.GameValues().getInventory().addItem(new Item(itemId).setItemLevel(itemLvl));
	}
	public boolean addMoneyToBank(double amount) {
		return gsm.GameValues().addMoneytoBank(amount);
	}
	public void moneyAfterInterest(double interestRate) {
		gsm.GameValues().setMoneyInBank((
				gsm.GameValues().getMoneyInBank())*interestRate);
	}
	public void nextDay() {
		gsm.GameValues().setDay(
				gsm.GameValues().getDay()+1);
		// when one day pass
		
	}
	public boolean addMoney(double amount) {
		return gsm.GameValues().addMoney(amount);
	}
	public boolean isMoneyEnough(double amount) {
		return gsm.GameValues().isMoneyEnough(amount);
	}
	public void heal(int health) {
		gsm.GameValues().treatment(health);
	}
	
	public void keyPressed(int k) {
		if(takeInput) {
			// amount input
			if((char)k>='0' && (char)k<='9' && input.length()<5)
			input+=Character.toString((char)k);
			
			if(input.length()>0 && k == KeyEvent.VK_BACK_SPACE)
				input = input.substring(0, input.length()-1);
			
			if(input.length()>0) {
				amount = Integer.parseInt(input);
			} else amount = 0;
			
			
			
			if (k == KeyEvent.VK_ENTER	|| k == KeyEvent.VK_SPACE
			 || k == KeyEvent.VK_RIGHT	|| k == KeyEvent.VK_LEFT
			 || k == KeyEvent.VK_A		|| k == KeyEvent.VK_D)
				select();
			if (k == KeyEvent.VK_UP || k == KeyEvent.VK_W) {
				currentChoice--;
				if (currentChoice == -1) {
					currentChoice = menuOptions.length - 1;
				}
			}
			if (k == KeyEvent.VK_DOWN || k == KeyEvent.VK_S) {
				currentChoice++;
				if (currentChoice == menuOptions.length) {
					currentChoice = 0;
				}
			}
		}
	}
	
	public void keyReleased(int k) {}
	
	
}
