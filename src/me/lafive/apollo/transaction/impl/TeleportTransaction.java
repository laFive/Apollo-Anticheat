package me.lafive.apollo.transaction.impl;

import me.lafive.apollo.transaction.Transaction;

public class TeleportTransaction extends Transaction {
	
	private int ticks;
	
	public TeleportTransaction(int ticks, short uid) {
		super(uid);
		this.ticks = ticks;
	}
	
	public int getTicks() {
		return ticks;
	}

}
