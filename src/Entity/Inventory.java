package Entity;

//import java.util.ArrayList;
import java.util.Arrays;

public class Inventory {

	private static final int MAX_INVENTORY_SIZE = 5;

	private Item[] items = new Item[MAX_INVENTORY_SIZE];
	//private ArrayList<Item> items = new ArrayList<Item>();
	
	public Inventory() {
		items[0] = new Item(1); //auto add sword
		items[1] = new Item(2); //auto add shield
	}
	
	public boolean hasItem(Item item) {
		boolean contain = false;
		for (int i = 0; i < items.length; i++)
			if(items[i].getItemName().equals(item.getItemName()))
				contain = true;
		return contain;
	}

	public boolean addItem(Item item) {
		int slot = findFreeSlot();
		if (slot >= 0) {
			items[slot] = item;
			return true;
		} else return false;
	}

	public Item removeItem(Item item) {
		for (int i = 0; i < items.length; i++) {
			if (items[i].equals(item)) {
				Item temp = items[i];
				items[i] = null;
				return temp;
			}
		}

		return null;
	}

	public Item getItemAt(int index) {
		return items[index];
	}

	public int getInventorySize() {
		return MAX_INVENTORY_SIZE;
	}

	private int findFreeSlot() {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				return i;
			}
		}

		return -1;
	}

	private int maxPotionAmount = 3;
	public int buyPotion(int type) {
		
		if(items[Arrays.asList(items).indexOf(new Item(0))].getAmount()>maxPotionAmount) return -1;
		else switch (type) {
		case 1:
			if(addItem(new Item(0).setItemLevel(1))) return 1;
			else return -1;
		case 2:
			if(addItem(new Item(0).setItemLevel(2))) return 2;
			else return -1;
		case 3:
			if(addItem(new Item(0).setItemLevel(3))) return 3;
			else return -1;
		default:
			return 0;
		}
		
	}
	
	private int maxWeaponEnchant = 3;
	public boolean enchantWeapon() {
		if(items[0].getItemLevel()<maxWeaponEnchant) {
			items[0].setItemLevel(items[0].getItemLevel()+1);
			return true;
		} else return false;
	}

}
