package me.lafive.apollo.check.impl.badpackets;

import io.github.retrooper.packetevents.packetwrappers.play.in.blockdig.WrappedPacketInBlockDig.PlayerDigType;
import me.lafive.apollo.check.Check;
import me.lafive.apollo.data.PlayerData;
import me.lafive.apollo.event.Event;
import me.lafive.apollo.event.impl.BlockDigEvent;
import me.lafive.apollo.event.impl.FlyingEvent;

public class BadPacketsD extends Check {
	
	private int streak;
	private int flyingCount;
	
	public BadPacketsD(PlayerData data) {
		super(data, "BadPackets", "D", 0, 200);
	}
	
	@Override
	public void handleEvent(Event event) {
		
		if (getData().getClientVersion() > 47)
			return;
		
		if (event instanceof FlyingEvent) {
			
			if (flyingCount++ > 2) {
				if (streak <= 1)
					decreaseVl(1);
				streak = 0;
				flyingCount = 0;
			}
			
		} else if (event instanceof BlockDigEvent) {
			
			BlockDigEvent e = (BlockDigEvent) event;
			if (e.getType().equals(PlayerDigType.RELEASE_USE_ITEM) || e.getType().equals(PlayerDigType.STOP_DESTROY_BLOCK)) {
				
				if (streak++ > 1) {
					
					handleViolation(100, "Streak:" + streak);
					
				}
				
			}
			
			
		}
		
	}

}
