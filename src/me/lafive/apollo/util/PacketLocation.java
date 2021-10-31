package me.lafive.apollo.util;

public class PacketLocation {
	
	private double posX, posY, posZ;
	private float pitch, yaw;
	private boolean onGround;
	
	public PacketLocation(double posX, double posY, double posZ, float pitch, float yaw, boolean onGround) {
		
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.yaw = yaw;
		this.pitch = pitch;
		this.onGround = onGround;
		
	}
	
	public PacketLocation(double posX, double posY, double posZ, float pitch, float yaw) {
		
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.yaw = yaw;
		this.pitch = pitch;
		
	}
	
	public PacketLocation(double posX, double posY, double posZ) {
		
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		
	}

	public double getPosX() {
		return posX;
	}

	public void setPosX(double posX) {
		this.posX = posX;
	}

	public double getPosY() {
		return posY;
	}

	public void setPosY(double posY) {
		this.posY = posY;
	}

	public double getPosZ() {
		return posZ;
	}

	public void setPosZ(double posZ) {
		this.posZ = posZ;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public boolean isOnGround() {
		return onGround;
	}

	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}

}
