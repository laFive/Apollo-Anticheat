package me.lafive.apollo.event.impl;

import me.lafive.apollo.event.Event;

public class InventoryInteractEvent extends Event {
	
	private short actionNumber;
	private int windowId;
	private int windowSlotId;
	
	public InventoryInteractEvent(short action, int windowId, int windowSlotId) {
		
		this.actionNumber = action;
		this.windowId = windowId;
		this.windowSlotId = windowSlotId;
		
	}

	public short getActionNumber() {
		return actionNumber;
	}

	public int getWindowId() {
		return windowId;
	}

	public int getWindowSlotId() {
		return windowSlotId;
	}
	
}
