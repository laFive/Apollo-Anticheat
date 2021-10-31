package me.lafive.apollo.event.impl;

import io.github.retrooper.packetevents.packetwrappers.play.in.blockdig.WrappedPacketInBlockDig.PlayerDigType;
import io.github.retrooper.packetevents.utils.player.Direction;
import me.lafive.apollo.event.Event;

public class BlockDigEvent extends Event {
	
	private PlayerDigType type;
	private Direction direction;
	
	public BlockDigEvent(PlayerDigType type, Direction direction) {
		
		this.type = type;
		this.direction = direction;
		
	}

	public PlayerDigType getType() {
		return type;
	}

	public Direction getDirection() {
		return direction;
	}

}
