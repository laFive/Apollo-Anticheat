package me.lafive.apollo.event.impl;

import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction.PlayerAction;
import me.lafive.apollo.event.Event;

public class PlayerActionEvent extends Event {
	
	private PlayerAction action;
	
	public PlayerActionEvent(PlayerAction action) {
		this.action = action;
	}
	
	public PlayerAction getAction() {
		return action;
	}

}
