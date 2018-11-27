package Entity;

import java.util.Arrays;

public class Item {

	private int itemId;
	private int itemLevel;
	private int amount;
	private String itemName;
	private String[] itemList = {
			"pot", "sword", "shield", "Fishing Rod"
	};
	
	public Item(int id) {
		this.setItemId(id);
		this.setItemName(itemName(id));
		itemLevel = 1;
		amount = 1;
	}
	
	public Item(String name) {
		this.setItemName(name);
		this.setItemId(itemId(name));
		itemLevel = 1;
		amount = 1;
	}
	
	
	public void use() {
		if(itemId == 0) {
			switch(itemLevel) {
			case 1: 
				break;
			case 2:
				break;
			case 3:
				break;
			}
		}
	}
	
	
	public String itemName(int id) {
		if(id >= 0 && id < itemList.length) {
			return itemList[id];
		} else return "";
	}

	public int itemId(String name) {
		if(Arrays.asList(itemList).contains(name)) {
			return Arrays.asList(itemList).indexOf(name);
		} else return -1;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getItemLevel() {
		return itemLevel;
	}

	public Item setItemLevel(int itemLevel) {
		this.itemLevel = itemLevel;
		return this;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public void addAmount() {
		this.amount++;
	}
}
