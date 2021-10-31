package me.lafive.apollo.check.impl.velocity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import me.lafive.apollo.check.Check;
import me.lafive.apollo.data.PlayerData;
import me.lafive.apollo.event.Event;
import me.lafive.apollo.event.impl.FlyingEvent;
import me.lafive.apollo.event.impl.TransactionEvent;
import me.lafive.apollo.event.impl.VelocityEvent;

public class VelocityA extends Check {
	
	private HashMap<Short, Double> pendingVelocityTransactions = new HashMap<Short, Double>();
	private HashMap<Double, Integer> pendingMotion = new HashMap<Double, Integer>();
	
	public VelocityA(PlayerData data) {
		super(data, "Velocity", "A", 0, 600);
	}
	
	@Override
	public void handleEvent(Event event) {
		
		if (event instanceof FlyingEvent) {
			
			FlyingEvent e = (FlyingEvent) event;
			double motionY = e.getLocation().getPosY() - e.getFromLocation().getPosY();
			
			Iterator<Entry<Double, Integer>> motionIterator = pendingMotion.entrySet().iterator();
			while (motionIterator.hasNext()) {
				
				Entry<Double, Integer> motionValue = motionIterator.next();
				
				double expectedMotion = motionValue.getKey() * 0.9;
				if (motionY >= expectedMotion) {
					motionIterator.remove();
					decreaseVl(1);
					continue;
				}
				
				pendingMotion.put(motionValue.getKey(), motionValue.getValue() - 1);
				if (pendingMotion.get(motionValue.getKey()) == 0) {
					
					motionIterator.remove();
					if (getData().getWebTicks() == 0 && getData().getTeleportTicks() == 0 &&
							getData().getUnderBlockTicks() == 0 && getData().getLiquidTicks() == 0 && !getData().isRiding()) {
						
						handleViolation(100, "ExpectedMotion:" + motionValue.getKey() + " LastTickMotion:" + motionY);
						
					}
					
				}
				
			}
			
		} else if (event instanceof VelocityEvent) {
			
			VelocityEvent e = (VelocityEvent) event;
			if (e.getVelocityY() > 0.2 && getData().getWebTicks() == 0 && getData().getTeleportTicks() == 0 &&
					getData().getUnderBlockTicks() == 0 && getData().getLiquidTicks() == 0 && !getData().isRiding()) {
				
				pendingVelocityTransactions.put(getData().getServerTick(), e.getVelocityY());
				
				/*
				 * We do this to ensure a transaction gets sent as soon as the velocity does to make sure
				 * the transaction lands on the tick of the velocity  
				*/
				getData().handleServerTick();
				
			}
			
		} else if (event instanceof TransactionEvent) {
			
			TransactionEvent e = (TransactionEvent) event;
			if (pendingVelocityTransactions.containsKey(e.getUid())) {
				
				pendingMotion.put(pendingVelocityTransactions.remove(e.getUid()), 3);
				
			}
			
		}
		
	}

}
