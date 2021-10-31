package me.lafive.apollo.check.impl.badpackets;

import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction.PlayerAction;
import me.lafive.apollo.check.Check;
import me.lafive.apollo.data.PlayerData;
import me.lafive.apollo.event.Event;
import me.lafive.apollo.event.impl.FlyingEvent;
import me.lafive.apollo.event.impl.PlayerActionEvent;

public class BadPacketsA extends Check {
	
	private boolean sprintStreak;
	private boolean sneakStreak;

	public BadPacketsA(PlayerData data) {
		super(data, "BadPackets", "A", 0, 300);
	}
	
	@Override
	public void handleEvent(Event event) {
		
		if (getData().getClientVersion() > 47)
			return;
		
		if (event instanceof FlyingEvent) {
			
			sprintStreak = false;
			sneakStreak = false;
			
		} else if (event instanceof PlayerActionEvent) {
			
			PlayerActionEvent e = (PlayerActionEvent) event;
			
			if (e.getAction().equals(PlayerAction.START_SPRINTING) || e.getAction().equals(PlayerAction.STOP_SPRINTING)) {
				
				if (sprintStreak) {
					handleViolation(100, "Type:Sprint");
				} else {
					decreaseVl(1);
				}
				
			} else if (e.getAction().equals(PlayerAction.START_SNEAKING) || e.getAction().equals(PlayerAction.STOP_SNEAKING)) {
				
				if (sneakStreak) {
					handleViolation(100, "Type:Sneak");
				} else {
					decreaseVl(1);
				}
				
			}
			
		}
		
	}
	
}
