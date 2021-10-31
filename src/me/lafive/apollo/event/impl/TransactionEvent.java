package me.lafive.apollo.event.impl;

import me.lafive.apollo.event.Event;

public class TransactionEvent extends Event {
	
	private short uid;
	
	public TransactionEvent(short uid) {
		this.uid = uid;
	}
	
	public short getUid() {
		return uid;
	}

}
