package me.lafive.apollo.event.impl;

import me.lafive.apollo.event.Event;

public class VelocityEvent extends Event {
	
	private double velocityX;
	private double velocityY;
	private double velocityZ;
	
	public VelocityEvent(double x, double y, double z) {
		this.velocityX = x;
		this.velocityY = y;
		this.velocityZ = z;
	}

	public double getVelocityX() {
		return velocityX;
	}

	public double getVelocityY() {
		return velocityY;
	}

	public double getVelocityZ() {
		return velocityZ;
	}
	
	

}
