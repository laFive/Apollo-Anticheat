package me.lafive.apollo.transaction.impl;

import me.lafive.apollo.transaction.Transaction;

public class InventoryTransaction extends Transaction {
	
	private boolean open;
	
	public InventoryTransaction(boolean open, short uid) {
		super(uid);
		this.open = open;
	}
	
	public boolean isInventoryOpen() {
		return open;
	}

}
