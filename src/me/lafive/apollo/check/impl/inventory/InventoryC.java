package me.lafive.apollo.check.impl.inventory;

import me.lafive.apollo.check.Check;
import me.lafive.apollo.data.PlayerData;
import me.lafive.apollo.event.Event;
import me.lafive.apollo.event.impl.FlyingEvent;
import me.lafive.apollo.util.MathHelper;

public class InventoryC extends Check {
	
	private int inventoryTicks;
	
	public InventoryC(PlayerData data) {
		super(data, "Inventory", "C", 200, 800);
	}
	
	@Override
	public void handleEvent(Event event) {
		
		if (event instanceof FlyingEvent) {
			
			FlyingEvent e = (FlyingEvent) event;
			
			if (e.isPos()) {
				
				if (getData().isServerInventoryOpen()) {
					
					inventoryTicks++;
					
					double distXZ = MathHelper.hypot(e.getLocation().getPosX() - e.getFromLocation().getPosX(), e.getLocation().getPosZ() - e.getFromLocation().getPosZ());
					
					if (inventoryTicks > 15 && distXZ > 5E-2 && getData().getVelocityTicks() == 0 && !getData().isFlying() && !getData().isPositionDesynced()) {
						
						handleViolation(100, "InventoryTicks:" + inventoryTicks + " DistXZ:" + distXZ);
						
					} else if (inventoryTicks > 15) {
						
						decreaseVl(5);
						
					}
					
				} else {
					
					inventoryTicks = 0;
					
				}
				
			}
			
		}
		
	}

}
