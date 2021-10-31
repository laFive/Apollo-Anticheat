package me.lafive.apollo.event.impl;

import io.github.retrooper.packetevents.utils.player.Direction;
import me.lafive.apollo.event.Event;

public class InteractEvent extends Event {
	
	private double x;
	private double y;
	private double z;
	private Direction direction;
	
	public InteractEvent(double x, double y, double z, Direction direction) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.direction = direction;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}
	
	public Direction getDirection() {
		return direction;
	}

}
