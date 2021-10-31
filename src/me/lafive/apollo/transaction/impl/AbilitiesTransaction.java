package me.lafive.apollo.transaction.impl;

import me.lafive.apollo.transaction.Transaction;

public class AbilitiesTransaction extends Transaction {
	
	private int flyTicks;
	
	public AbilitiesTransaction(int flyTicks, short uid) {
		super(uid);
		this.flyTicks = flyTicks;
	}
	
	public int getFlyTicks() {
		return flyTicks;
	}

}
