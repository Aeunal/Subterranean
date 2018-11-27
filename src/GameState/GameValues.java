package GameState;

import java.awt.Point;
import java.util.Random;

import Entity.Inventory;
import Entity.Player;

public class GameValues {

	private static final int HEALTH = 0;
	private static final int ENERGY = 1;
	private static final int MONEY = 2;
	private static final int DEPOSIT = 3;
	private static final int TIME = 4;
	private static final int ATTACK = 5;
	private static final int DEFENCE = 6;
	private static final int ROAD = 7;
	private static final int TOWNLEVEL = 8;
	private static final int ROAMLEVEL = 9;
	private static final int VALUECOUNT = 10;
	
	private final int maxHealth = 100;
	private final int maxEnergy = 100;
	private final int startingMoney = 75;
	private final int startingTime = 0;
	private final int baseAttack = 10;
	private final int baseDefence = 10;
	private final int startingTown = 1;
	private final int startingRoam = 0;
	private final int maxMoney = 10000;
	private int maxDebt = -100;
	
	public static final int TOWN1STATE = 0;//1
	public static final int TOWN2STATE = 1;//2
	public static final int TOWN3STATE = 2;//3
	public static final int ROAMSTATE = 3;//4
	public static final int FIGHTSTATE = 4;//5
	//public static final int BUILDINGSTATE = 5;//6
	
	private Point[] lastPosition = new Point[5];
	
	private double[] value = new double[VALUECOUNT];
	private double[] enemyValue = new double[4];
	double[] initValues = new double[VALUECOUNT];
	Point[] startingPositions = new Point[6];
	private Inventory inventory;
	
	private Random rand = new Random();
	
	public GameValues() {
		refreshPlayer();
		
		refreshEnemy();
	}
	
	public void refreshPlayer() {
		double[] initValues = {
				getMaxHealth(),
				getMaxEnergy(),
				getStartingMoney(),
				0,
				getStartingTime(),
				getBaseAttack(),
				getBaseDefence(),
				400, //????????????????,
				getStartingTown(),
				getStartingRoam()
		};
		this.initValues = initValues;
		Point[] startingPositions = {
				new Point(100,200),// !
				new Point(100,200),// !
				new Point(100,200),// !
				new Point(100,200),
				new Point(130,200),
				//new Point(100,200),
		};
		this.startingPositions = startingPositions;
		
		for (int i = 0; i < getLastPosition().length; i++) {
			getLastPosition()[i] = startingPositions [i];
		}
		for (int i = 0; i < value.length; i++)
			value[i] = initValues[i];
	}
	
	public void refreshEnemy() {
		enemyValue[0] = getEnemyMaxHealth() ;//+ 10 * rand.nextInt(4);; // health
		enemyValue[1] = getEnemyMaxEnergy() ;//+ 10 * rand.nextInt(4);; // energy
		enemyValue[2] = getEnemyBaseAttack() + rand.nextInt(1+(int) value[TOWNLEVEL]*5);; // attack
		enemyValue[3] = getEnemyBaseDefence() + rand.nextInt(1+(int) value[TOWNLEVEL]*5);; // defence
	}
	
	public int getEnemyMaxHealth() { return 100; }
	public int getEnemyMaxEnergy() { return 100; }
	public int getEnemyBaseAttack() { return 10; }
	public int getEnemyBaseDefence() { return 10; }
	
	public boolean enemyHurt(int amount) {
		if(getEnemyHealth() - amount >= 0) {
			enemyValue[0] -= amount;
			return true;
		} else return false;
	}
	
	public boolean enemyTired(int amount) {
		if(getEnemyEnergy() - amount >= 0) {
			enemyValue[1] -= amount;
			return true;
		} else return false;
	}
	
	public void enemyAttackedPlayer(boolean succeed) {
		if(succeed) {
			hurt(getEnemyAttack());
		} 
		enemyTired(5);
	}
	
	public void playerAttackedEnemy(boolean succeed) {
		if(succeed) {
			enemyHurt(getAttack());
		}
		tired(2);
	}
	
	public int getEnemyHealth() { return (int) enemyValue[0]; }
	public int getEnemyEnergy() { return (int) enemyValue[1]; }
	public int getEnemyAttack() { return (int) enemyValue[2]; }
	public void setEnemyAttack(int attack) { enemyValue[2] = attack; }
	public int getEnemyDefence() { return (int) enemyValue[3]; }
	public void setetEnemyDefence(int defence) { enemyValue[3] = defence; }
	
	// Money Functions
	
	public boolean addMoney(double money) {
		if(this.value[MONEY] + money < 0) return false;
		else this.value[MONEY] += money;
		return true;
	}
	public boolean isMoneyEnough(double money) {
		if(this.value[MONEY] - money < 0) return false;
		else return true;
	}
	
	public boolean addMoneytoBank(double moneyInBank) {
		if(this.value[DEPOSIT] + moneyInBank < getMaxDebt()) return false;
		else {
			this.value[DEPOSIT] += moneyInBank;
			return true;
		}
	}
	
	public double getMoney() { return value[MONEY]; } 
	public void setMoney(double money) { this.value[MONEY] = money; }
	public double getMoneyInBank() {return value[DEPOSIT];}
	public void setMoneyInBank(double moneyInBank) {this.value[DEPOSIT] = moneyInBank;}
	
	// Level Functions

	public int levelUp() {
		this.value[TOWNLEVEL]++;
		return (int) value[TOWNLEVEL];
	}
	
	public int RoamLevelUp() {
		this.value[ROAMLEVEL] = value[TOWNLEVEL];
		return (int) value[ROAMLEVEL];
	}
	
	public int levelToTown() {
		switch ((int) value[TOWNLEVEL]) {
		case 1: return GameStateManager.TOWN1STATE;
		case 2: return GameStateManager.TOWN2STATE;
		case 3: return GameStateManager.TOWN3STATE;
		default: return 0;
		}
	}
	
	public int levelToTown(int level) {
		switch (level) {
		case 1: return GameStateManager.TOWN1STATE;
		case 2: return GameStateManager.TOWN2STATE;
		case 3: return GameStateManager.TOWN3STATE;
		default: return 0;
		}
	}
	
	public int getLevel() { return (int) value[TOWNLEVEL]; } 
	public void setLevel(int level) { this.value[TOWNLEVEL] = level; }
	public int upperTown() { return levelToTown((int) value[TOWNLEVEL]+1); }
	public int town() { return levelToTown((int) value[TOWNLEVEL]); }
	public int getLastRoamLevel() { return (int) value[ROAMLEVEL]; } 
	public void setLastRoamLevel(int lastRoamLevel) { this.value[ROAMLEVEL] = lastRoamLevel; }
	
	// Values

	public void treatment(int health) {
		setHealth(getHealth()+health);
		if(getHealth() > getMaxHealth()) setHealth(getMaxHealth());
	}
	
	public void hurt(int health) {
		setHealth(getHealth()-health);
		if(getHealth() < 0) setHealth(0);
	}
	
	public void tired(int energy) {
		setEnergy(getEnergy()-energy);
		if(getEnergy() < 0) setEnergy(0);
	}

	public void setInventory(Inventory inventory) { this.inventory = inventory; }
	public void setDay(int day) {this.value[TIME] = day;}
	public void setHealth(double health) { this.value[HEALTH] = health; }
	public void setRemainingRoad(int remainingRoad) { this.value[ROAD] = remainingRoad; }
	public void setMaxDeposit(int maxDebt) {this.maxDebt = maxDebt;}

	public Inventory getInventory() { return inventory; }
	public double getHealth() { return value[HEALTH];}
	public int getDay() {return (int) value[TIME];}
	public int getAttack() {return (int) value[ATTACK];}
	public int getRemainingRoad() { return (int) value[ROAD]; }
	public int getMaxHealth() {return maxHealth;}
	public int getMaxEnergy() {return maxEnergy;}
	public int getStartingMoney() {return startingMoney;}
	public int getStartingTime() {return startingTime;}
	public int getBaseAttack() {return baseAttack;}
	public int getBaseDefence() {return baseDefence;}
	public int getStartingTown() {return startingTown;}
	public int getStartingRoam() {return startingRoam;}
	public int getMaxMoney() {return maxMoney;}
	public int getMaxDebt() {return maxDebt;}

	public Point[] getLastPosition() {
		return lastPosition;
	}

	public void setLastPosition(Point[] lastPosition) {
		this.lastPosition = lastPosition;
	}
	public void setLastPosition(Point lastPosition,int location) {
		this.lastPosition[location-1] = lastPosition;
	}

	public double getEnergy() {
		return value[ENERGY];
	}
	public double setEnergy(double amount) {
		return value[ENERGY] = amount;
	}
	
	
	
}
