package me.lafive.apollo.check.impl.hitbox;

import org.bukkit.entity.Entity;

import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity.EntityUseAction;
import me.lafive.apollo.check.Check;
import me.lafive.apollo.data.PlayerData;
import me.lafive.apollo.event.Event;
import me.lafive.apollo.event.impl.EntityInteractEvent;
import me.lafive.apollo.event.impl.FlyingEvent;
import me.lafive.apollo.util.AxisAlignedBB;
import me.lafive.apollo.util.BoundingBoxUtils;
import me.lafive.apollo.util.MathHelper;
import me.lafive.apollo.util.MovingObjectPosition;
import me.lafive.apollo.util.PacketLocation;
import me.lafive.apollo.util.Vec3;

public class HitboxA extends Check {
	
	private int threshold;
	private PacketLocation location, fromLocation;
	
	public HitboxA(PlayerData data) {
		super(data, "Hitbox", "A", 200, 800);
	}
	
	@Override
	public void handleEvent(Event event) {
		
		if (event instanceof FlyingEvent) {
			
			FlyingEvent e = (FlyingEvent) event;
			location = e.getLocation();
			fromLocation = e.getFromLocation();
			
		} else if (event instanceof EntityInteractEvent) {
			
			EntityInteractEvent e = (EntityInteractEvent) event;
			if (e.getAction().equals(EntityUseAction.ATTACK)) {
				
				double distance = Double.MAX_VALUE;
				
				Entity target = e.getEntity();
				
				for (PacketLocation pl : getData().getEntityTracker().getEntity(target).getRecentLocations()) {
					
					if (pl == null) continue;
					
					PacketLocation targetClientLocation = pl;
					AxisAlignedBB targetBB = BoundingBoxUtils.getBoundingBox(targetClientLocation, 0.3F, 1.8F);
					AxisAlignedBB expandedBB = targetBB.expand(0.1f, 0.5f, 0.1f);
					
					Vec3 eyePosition = getEyePosition();
					Vec3 look = getLook(location.getYaw(), location.getPitch());
					Vec3 lastLook = getLook(fromLocation.getYaw(), location.getPitch());
					
					Vec3 rayTrace = eyePosition.addVector(look.xCoord * 9, look.yCoord * 9, look.zCoord * 9);
					Vec3 lastRayTrace = eyePosition.addVector(lastLook.xCoord * 9, lastLook.yCoord * 9, lastLook.zCoord * 9);
					
					MovingObjectPosition mop = expandedBB.calculateIntercept(eyePosition, rayTrace);
					if (mop != null) {
						double preDistance = mop.hitVec.distanceTo(eyePosition);
						if (preDistance < distance)
							distance = preDistance;
					}
					
					MovingObjectPosition lastMop = expandedBB.calculateIntercept(eyePosition, lastRayTrace);
					if (lastMop != null) {
						double preDistance = lastMop.hitVec.distanceTo(eyePosition);
						if (preDistance < distance)
							distance = preDistance;
					}
					
				}
				
				if (distance == Double.MAX_VALUE) {
					
					if (++threshold > 5) {
						
						handleViolation(100, "Threshold:" + threshold);
						
					}
					
				} else {
					
					threshold = 0;
					
				}
				
				
				
			}
			
			
			
		}
		
	}
	
	protected final Vec3 getLook(float yaw, float pitch)
    {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3((double)(f1 * f2), (double)f3, (double)(f * f2));
    }
	
	private Vec3 getEyePosition() {
		
		return new Vec3(location.getPosX(), location.getPosY() + (0.85 * 1.8F), location.getPosZ());
		
	}

}
