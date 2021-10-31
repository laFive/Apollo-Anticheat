package me.lafive.apollo.event.impl;

import me.lafive.apollo.event.Event;
import me.lafive.apollo.util.PacketLocation;

public class FlyingEvent extends Event {
	
	private PacketLocation location, fromLocation;
	private int groundStateTicks;
	private boolean look;
	private boolean pos;

	public FlyingEvent(PacketLocation location, PacketLocation fromLocation, int groundStateTicks) {
		
		this.location = location;
		this.fromLocation = fromLocation;
		this.groundStateTicks = groundStateTicks;
	
	}
	
	public boolean isNewLocation() {
		
		return location.getPosX() != fromLocation.getPosX() || 
				location.getPosY() != fromLocation.getPosY() || 
				location.getPosZ() != fromLocation.getPosZ();
		
	}
	
	public PacketLocation getLocation() {
		return location;
	}
	
	public PacketLocation getFromLocation() {
		return fromLocation;
	}
	
	public int getGroundStateTicks() {
		return groundStateTicks;
	}

	public boolean isLook() {
		return look;
	}

	public void setLook(boolean look) {
		this.look = look;
	}

	public boolean isPos() {
		return pos;
	}

	public void setPos(boolean pos) {
		this.pos = pos;
	}
	
}
