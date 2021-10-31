package me.lafive.apollo.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Entity;

import me.lafive.apollo.util.MathHelper;
import me.lafive.apollo.util.PacketLocation;

public class TrackedEntity {
	
	private PlayerData data;
	private int rotationIncrements;
	private PacketLocation serverLocation;
	private PacketLocation clientLocation;
	private PacketLocation rawClientLocation;
	
	private ArrayList<PacketLocation> clientLocations = new ArrayList<PacketLocation>();
	private HashMap<Short, PacketLocation> pendingClientLocations = new HashMap<Short, PacketLocation>();
	
	public TrackedEntity(PacketLocation initialLocation, Entity entity, PlayerData data) {
		
		this.data = data;
		this.serverLocation = initialLocation;
		pendingClientLocations.put(data.getServerTick(), clientLocation);
		
	}
	
	public void teleportEntity(PacketLocation location) {
		
		rotationIncrements = 3;
		
		PacketLocation clientLocation;
		
		if (Math.abs(serverLocation.getPosX() - location.getPosX()) < 0.03125D && Math.abs(serverLocation.getPosY() - location.getPosY()) < 0.015625D && Math.abs(serverLocation.getPosZ() - location.getPosZ()) < 0.03125D) {
			
			clientLocation = new PacketLocation(serverLocation.getPosX(), serverLocation.getPosY(), serverLocation.getPosZ(), location.getYaw(), location.getPitch());
			
		} else {
			
			clientLocation = new PacketLocation(location.getPosX(), location.getPosY(), location.getPosZ(), location.getYaw(), location.getPitch());
			
		}
		this.serverLocation = location;
		pendingClientLocations.put(data.getServerTick(), clientLocation);
		
	}
	
	public void handleClientTick() {
		
		if (clientLocation == null) return;
		
		double clampedX = MathHelper.clamp_double(clientLocation.getPosX(), -2.9999999E7D, 2.9999999E7D);
        double clampedZ = MathHelper.clamp_double(clientLocation.getPosZ(), -2.9999999E7D, 2.9999999E7D);
        
        if (clampedX != clientLocation.getPosX() || clampedZ != clientLocation.getPosZ()) {
        	
        	clientLocation.setPosX(clampedX);
        	clientLocation.setPosZ(clampedZ);
        	
        }
		
		if (rotationIncrements > 0) {
			
			double interpolatedX = clientLocation.getPosX() + (rawClientLocation.getPosX() - clientLocation.getPosX()) / (double)rotationIncrements;
			double interpolatedY = clientLocation.getPosY() + (rawClientLocation.getPosY() - clientLocation.getPosY()) / (double)rotationIncrements;
			double interpolatedZ = clientLocation.getPosZ() + (rawClientLocation.getPosZ() - clientLocation.getPosZ()) / (double)rotationIncrements;
			
			clientLocation.setPosX(interpolatedX);
			clientLocation.setPosY(interpolatedY);
			clientLocation.setPosZ(interpolatedZ);
			
			--rotationIncrements;
			
		}
		
	}
	
	public void moveEntityRelative(double x, double y, double z, float yaw, float pitch) {
		
		rotationIncrements = 3;
		
		PacketLocation newLocation = new PacketLocation(serverLocation.getPosX(), serverLocation.getPosY(), serverLocation.getPosZ(), yaw, pitch);
		newLocation.setPosX(newLocation.getPosX() + x);
		newLocation.setPosY(newLocation.getPosY() + y);
		newLocation.setPosZ(newLocation.getPosZ() + z);
		
		serverLocation = newLocation;
		pendingClientLocations.put(data.getServerTick(), newLocation);
		
	}
	
	public void moveEntityRelative(double x, double y, double z) {
		
		rotationIncrements = 3;
		
		PacketLocation newLocation = new PacketLocation(serverLocation.getPosX(), serverLocation.getPosY(), serverLocation.getPosZ(), serverLocation.getYaw(), serverLocation.getPitch());
		newLocation.setPosX(newLocation.getPosX() + x);
		newLocation.setPosY(newLocation.getPosY() + y);
		newLocation.setPosZ(newLocation.getPosZ() + z);
		
		serverLocation = newLocation;
		pendingClientLocations.put(data.getServerTick(), newLocation);
		
	}
	
	public void handleTransaction(short uid) {
		
		if (pendingClientLocations.containsKey(uid)) {
			
			if (clientLocations.size() >= 20) {
				clientLocations.remove(0);
			}
			while (clientLocations.size() < 20) {
				clientLocations.add(clientLocation);
			}
			
			PacketLocation loc = pendingClientLocations.remove(uid);
			clientLocation = loc;
			rawClientLocation = loc;
			
		}
		
	}
	
	public List<PacketLocation> getRecentLocations(){
		
		ArrayList<PacketLocation> recentLocations = new ArrayList<PacketLocation>();
		recentLocations.add(clientLocations.get(clientLocations.size() - 1));
		recentLocations.add(clientLocation);
		return recentLocations;
		
	}
	
	public PacketLocation getClientLocation() {
		return clientLocation;
	}

}
