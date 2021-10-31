package me.lafive.apollo.check.impl.killaura;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity.EntityUseAction;
import me.lafive.apollo.check.Check;
import me.lafive.apollo.data.PlayerData;
import me.lafive.apollo.event.Event;
import me.lafive.apollo.event.impl.EntityInteractEvent;
import me.lafive.apollo.event.impl.FlyingEvent;
import me.lafive.apollo.util.MathHelper;

public class KillAuraE extends Check {
	
	private int hitTicks;
	private int threshold;
	private double lastDist;
	
	public KillAuraE(PlayerData data) {
		super(data, "KillAura", "E", 0, 800);
	}
	
	@Override
	public void handleEvent(Event event) {
		
		if (event instanceof FlyingEvent) {
			
			FlyingEvent e = (FlyingEvent) event;
			hitTicks++;
			
			if (!e.isPos()) return;
			
			double dist = MathHelper.hypot(e.getLocation().getPosX() - e.getFromLocation().getPosX(), e.getLocation().getPosZ() - e.getFromLocation().getPosZ());
			
			if (hitTicks <= 2) {
				
				double minDistance = 0.2325;
				
				for (final PotionEffect effect : getData().getPlayer().getActivePotionEffects()) {
					if (effect.getType().equals(PotionEffectType.SPEED)) {
						minDistance = minDistance + (0.02 * (effect.getAmplifier() + 1));
					}
				}
				
				if (e.getLocation().isOnGround() && e.getGroundStateTicks() > 2 && getData().isSprinting() && dist >= (lastDist * 0.99) && dist > minDistance && getData().getPlayer().getGameMode() != GameMode.CREATIVE) {
					
					if (++threshold > 3) {
						handleViolation(100, "GroundTicks:" + e.getGroundStateTicks() + " Dist:" + dist + " LastDist:" + lastDist + " HitTicks:" + hitTicks);
						threshold = 0;
					}
					
				} else {
					
					threshold = 0;
					decreaseVl(1);
					
				}
				
			}
			
			lastDist = dist;
			
		} else if (event instanceof EntityInteractEvent) {
			
			EntityInteractEvent e = (EntityInteractEvent) event;
			if (e.getAction().equals(EntityUseAction.ATTACK)) {
				
				if (e.getEntity() instanceof Player) {
					hitTicks = 0;
				}
		
			}
			
			
		}
		
	}

}
