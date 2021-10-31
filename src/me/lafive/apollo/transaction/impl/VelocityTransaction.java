package me.lafive.apollo.transaction.impl;

import me.lafive.apollo.transaction.Transaction;

public class VelocityTransaction extends Transaction {
	
	private int velocityTicks;
	
	public VelocityTransaction(short uid, int ticks) {
		super(uid);
		velocityTicks = ticks;
	}
	
	public int getVelocityTicks() {
		return velocityTicks;
	}

}
