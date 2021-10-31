package me.lafive.apollo.data;

import java.util.HashMap;

import org.bukkit.entity.Entity;

import me.lafive.apollo.util.PacketLocation;

public class EntityTracker {
	
	private PlayerData data;
	
	private HashMap<Entity, TrackedEntity> trackedEntities = new HashMap<Entity, TrackedEntity>();
	
	public EntityTracker(PlayerData data) {
		this.data = data;
	}
	
	public TrackedEntity getEntity(Entity e) {
		return trackedEntities.get(e);
	}
	
	public void createEntity(Entity entity, PacketLocation initialLocation) {
		
		trackedEntities.put(entity, new TrackedEntity(initialLocation, entity, data));
		
	}
	
	public void teleportEntity(Entity entity, PacketLocation teleportLocation) {
		
		if (!trackedEntities.containsKey(entity)) {
			
			trackedEntities.put(entity, new TrackedEntity(teleportLocation, entity, data));
			return;
			
		}
		
		trackedEntities.get(entity).teleportEntity(teleportLocation);
		
	}
	
	public void moveEntity(Entity entity, double x, double y, double z, float yaw, float pitch) {
		
		if (!trackedEntities.containsKey(entity)) {
			return;
		}
		
		trackedEntities.get(entity).moveEntityRelative(x, y, z, yaw, pitch);
		
	}
	
	public void handleTransaction(short uid) {
		
		trackedEntities.values().forEach(trackedEntity -> trackedEntity.handleTransaction(uid));
		
	}
	
	public void handleClientTick() {
		
		trackedEntities.values().forEach(trackedEntity -> trackedEntity.handleClientTick());
		
	}

}
