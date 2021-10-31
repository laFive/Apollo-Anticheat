package me.lafive.apollo.event.impl;

import org.bukkit.entity.Entity;

import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity.EntityUseAction;
import me.lafive.apollo.event.Event;

public class EntityInteractEvent extends Event {
	
	private Entity entity;
	private EntityUseAction action;
	
	public EntityInteractEvent(Entity entity, EntityUseAction action) {
		this.entity = entity;
		this.action = action;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public EntityUseAction getAction() {
		return action;
	}

}
