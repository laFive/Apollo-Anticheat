package me.lafive.apollo.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.play.in.abilities.WrappedPacketInAbilities;
import io.github.retrooper.packetevents.packetwrappers.play.in.blockdig.WrappedPacketInBlockDig;
import io.github.retrooper.packetevents.packetwrappers.play.in.blockdig.WrappedPacketInBlockDig.PlayerDigType;
import io.github.retrooper.packetevents.packetwrappers.play.in.blockplace.WrappedPacketInBlockPlace;
import io.github.retrooper.packetevents.packetwrappers.play.in.clientcommand.WrappedPacketInClientCommand;
import io.github.retrooper.packetevents.packetwrappers.play.in.clientcommand.WrappedPacketInClientCommand.ClientCommand;
import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction;
import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction.PlayerAction;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.play.in.keepalive.WrappedPacketInKeepAlive;
import io.github.retrooper.packetevents.packetwrappers.play.in.transaction.WrappedPacketInTransaction;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import io.github.retrooper.packetevents.packetwrappers.play.in.windowclick.WrappedPacketInWindowClick;
import io.github.retrooper.packetevents.packetwrappers.play.out.abilities.WrappedPacketOutAbilities;
import io.github.retrooper.packetevents.packetwrappers.play.out.entity.WrappedPacketOutEntity.WrappedPacketOutRelEntityMove;
import io.github.retrooper.packetevents.packetwrappers.play.out.entity.WrappedPacketOutEntity.WrappedPacketOutRelEntityMoveLook;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityteleport.WrappedPacketOutEntityTeleport;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityvelocity.WrappedPacketOutEntityVelocity;
import io.github.retrooper.packetevents.packetwrappers.play.out.explosion.WrappedPacketOutExplosion;
import io.github.retrooper.packetevents.packetwrappers.play.out.keepalive.WrappedPacketOutKeepAlive;
import io.github.retrooper.packetevents.packetwrappers.play.out.mount.WrappedPacketOutMount;
import io.github.retrooper.packetevents.packetwrappers.play.out.namedentityspawn.WrappedPacketOutNamedEntitySpawn;
import io.github.retrooper.packetevents.packetwrappers.play.out.position.WrappedPacketOutPosition;
import io.github.retrooper.packetevents.packetwrappers.play.out.spawnentity.WrappedPacketOutSpawnEntity;
import io.github.retrooper.packetevents.packetwrappers.play.out.spawnentityliving.WrappedPacketOutSpawnEntityLiving;
import io.github.retrooper.packetevents.packetwrappers.play.out.transaction.WrappedPacketOutTransaction;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import me.lafive.apollo.Apollo;
import me.lafive.apollo.check.Check;
import me.lafive.apollo.event.impl.BlockDigEvent;
import me.lafive.apollo.event.impl.EntityInteractEvent;
import me.lafive.apollo.event.impl.FlyingEvent;
import me.lafive.apollo.event.impl.InteractEvent;
import me.lafive.apollo.event.impl.InventoryInteractEvent;
import me.lafive.apollo.event.impl.PlayerActionEvent;
import me.lafive.apollo.event.impl.SwingEvent;
import me.lafive.apollo.event.impl.TeleportEvent;
import me.lafive.apollo.event.impl.TransactionEvent;
import me.lafive.apollo.event.impl.VelocityEvent;
import me.lafive.apollo.transaction.Transaction;
import me.lafive.apollo.transaction.impl.AbilitiesTransaction;
import me.lafive.apollo.transaction.impl.InventoryTransaction;
import me.lafive.apollo.transaction.impl.RidingTransaction;
import me.lafive.apollo.transaction.impl.TeleportTransaction;
import me.lafive.apollo.transaction.impl.VelocityTransaction;
import me.lafive.apollo.util.AxisAlignedBB;
import me.lafive.apollo.util.BoundingBoxUtils;
import me.lafive.apollo.util.PacketLocation;

public class PlayerData {
	
	private Player player;
	private EntityTracker entityTracker;
	private short clientVersion, serverTickCount;
	private ArrayList<Check> playerChecks = new ArrayList<Check>();
	
	private long keepAlivePing;
	private AxisAlignedBB boundingBox, lastBoundingBox;
	private boolean flying, allowFlying, clientRiding, digging, clientInventoryOpen, serverInventoryOpen, serverGround, sentKeepAlive, sprinting;
	private int teleportTicks, maxVelocityTicks, digTicks, inventoryTicks, stairTicks, jumpBoostTicks, climbableTicks, iceTicks, serverPositionTicks, underBlockTicks, serverGroundStateTicks, webTicks, flyTicks, ridingTicks, liquidTicks;
	
	private PacketLocation location = new PacketLocation(0D, 0D, 0D, 0F, 0F, false);
	private PacketLocation lastLocation = new PacketLocation(0D, 0D, 0D, 0F, 0F, false);
	
	private int groundStateTicks;
	
	private ArrayList<Transaction> pendingTransactions = new ArrayList<Transaction>();
	private ArrayList<PacketLocation> serverPositions = new ArrayList<PacketLocation>();
	private HashMap<Long, Long> pendingKeepAlives = new HashMap<Long, Long>();
	
	public PlayerData(Player player) {
		this.player = player;
		serverTickCount = 0;
		clientVersion = PacketEvents.get().getServerUtils().getVersion().getProtocolVersion();
		entityTracker = new EntityTracker(this);
		Apollo.getInstance().getCheckManager().loadChecks(this);
	}
	
	public void setClientVersion(ClientVersion cl) {
		
		clientVersion = cl.getProtocolVersion();
		
	}
	
	public void handlePacket(NMSPacket packet, byte packetId) {
		
		if (PacketType.Play.Client.Util.isInstanceOfFlying(packetId)) {
			
			digTicks = Math.max(0, --digTicks);
			inventoryTicks = Math.max(0, --inventoryTicks);
			stairTicks = Math.max(0, --stairTicks);
			iceTicks = Math.max(0, --iceTicks);
			climbableTicks = Math.max(0, --climbableTicks);
			jumpBoostTicks = Math.max(0, --jumpBoostTicks);
			liquidTicks = Math.max(0, --liquidTicks);
			ridingTicks = Math.max(0, --ridingTicks);
			serverPositionTicks = Math.max(0, --serverPositionTicks);
			webTicks = Math.max(0, --webTicks);
			teleportTicks = Math.max(0, --teleportTicks);
			underBlockTicks = Math.max(0, --underBlockTicks);
			maxVelocityTicks = Math.max(0, --maxVelocityTicks);
			flyTicks = Math.max(0, --flyTicks);
			WrappedPacketInFlying flying = new WrappedPacketInFlying(packet);
			
			lastLocation = location;
			
			location = new PacketLocation(lastLocation.getPosX(), lastLocation.getPosY(), lastLocation.getPosZ(), lastLocation.getYaw(), lastLocation.getPitch(), lastLocation.isOnGround());
			
			if (flying.isMoving()) {
				location.setPosX(flying.getPosition().getX());
				location.setPosY(flying.getPosition().getY());
				location.setPosZ(flying.getPosition().getZ());
			}
			
			if (flying.isRotating()) {
				location.setYaw(flying.getYaw());
				location.setPitch(flying.getPitch());
			}
			
			if (location.isOnGround() != flying.isOnGround()) {
				location.setOnGround(flying.isOnGround());
				groundStateTicks = 0;
			}
			groundStateTicks++;
			
			if (boundingBox != null)
				lastBoundingBox = boundingBox;
			boundingBox = new AxisAlignedBB(location.getPosX() - (double) 0.3F, location.getPosY(), location.getPosZ() - (double) 0.3F, location.getPosX() + (double) 0.3F, location.getPosY() + (double) 1.8F, location.getPosZ() + (double) 0.3F);
			if (lastBoundingBox == null) lastBoundingBox = boundingBox;
			
			if (BoundingBoxUtils.isUnderBlockBB(boundingBox, player.getWorld()))
				underBlockTicks = 5;
			
			if (BoundingBoxUtils.isInWeb(boundingBox, player.getWorld()))
				webTicks = 5;
			
			if (BoundingBoxUtils.isInLiquid(boundingBox, player.getWorld()))
				liquidTicks = 10;
			
			if (BoundingBoxUtils.isOnClimbable(boundingBox, player.getWorld()))
				climbableTicks = 10;
			
			if (BoundingBoxUtils.isOnIce(boundingBox, player.getWorld()))
				iceTicks = 10;
			
			if (BoundingBoxUtils.isOnStairs(boundingBox, player.getWorld()))
				stairTicks = 10;
			
			boolean serverGround = BoundingBoxUtils.isOnGround(boundingBox, player.getWorld()) || BoundingBoxUtils.isOnGround(lastBoundingBox, player.getWorld());
			if (this.serverGround != serverGround)
				serverGroundStateTicks = 0;
			this.serverGround = serverGround;
			serverGroundStateTicks++;
			
			for (PotionEffect pe : player.getActivePotionEffects()) {
				if (pe.getType().equals(PotionEffectType.JUMP))
					jumpBoostTicks = 50;
			}
			
			Iterator<PacketLocation> positions = serverPositions.iterator();
			
			while (positions.hasNext()) {
				PacketLocation serverPos = positions.next();
				
				if (serverPos.getPosX() == location.getPosX() && serverPos.getPosY() == location.getPosY() && serverPos.getPosZ() == location.getPosZ()) {
					positions.remove();
					serverPositionTicks = 12;
				}
				
			}
			
			/*
			 * We do this to ensure a transaction gets sent as soon as the velocity does to make sure
			 * the transaction lands on the tick of the velocity  
			*/
			entityTracker.handleClientTick();
			
			FlyingEvent fe = new FlyingEvent(location, lastLocation, groundStateTicks);
			fe.setLook(flying.isRotating());
			fe.setPos(flying.isMoving());
			fe.callEvent(this);
			
		} else if (PacketType.Play.Client.TRANSACTION == packetId) {
			
			WrappedPacketInTransaction transaction = new WrappedPacketInTransaction(packet);
			handleTransaction(transaction.getActionNumber());
			TransactionEvent te = new TransactionEvent(transaction.getActionNumber());
			te.callEvent(this);
			
			entityTracker.handleTransaction(transaction.getActionNumber());
			
		} else if (PacketType.Play.Client.ENTITY_ACTION == packetId) {
			
			WrappedPacketInEntityAction entityAction = new WrappedPacketInEntityAction(packet);
			PlayerActionEvent pae = new PlayerActionEvent(entityAction.getAction());
			pae.callEvent(this);
			
			if (entityAction.getAction().equals(PlayerAction.START_SPRINTING)) {
				
				sprinting = true;
				
			} else if (entityAction.getAction().equals(PlayerAction.STOP_SPRINTING)) {
				
				sprinting = false;
				
			}
			
		} else if (PacketType.Play.Client.ARM_ANIMATION == packetId) {
			
			SwingEvent se = new SwingEvent();
			se.callEvent(this);
			
		} else if (PacketType.Play.Client.USE_ENTITY == packetId) {
			
			WrappedPacketInUseEntity useEntity = new WrappedPacketInUseEntity(packet);
			EntityInteractEvent eie = new EntityInteractEvent(useEntity.getEntity(), useEntity.getAction());
			eie.callEvent(this);
			
		} else if (PacketType.Play.Server.ENTITY_VELOCITY == packetId) {
			
			WrappedPacketOutEntityVelocity velo = new WrappedPacketOutEntityVelocity(packet);
			
			if (velo.getEntity() != player) return;
			
			int maxTicks = (int) (((Math.abs(velo.getVelocity().getX()) + Math.abs(velo.getVelocity().getY()) + Math.abs(velo.getVelocity().getZ())) / 2 + 2) * 15);
			VelocityTransaction vt = new VelocityTransaction(serverTickCount, Math.max(5, Math.abs(maxTicks)));
			pendingTransactions.add(vt);
			
			VelocityEvent ve = new VelocityEvent(velo.getVelocity().getX(), velo.getVelocity().getY(), velo.getVelocity().getZ());
			ve.callEvent(this);
			
			handleServerTick();
			
		} else if (PacketType.Play.Server.EXPLOSION == packetId) { 
			
			WrappedPacketOutExplosion explosion = new WrappedPacketOutExplosion(packet);
			
			int maxTicks = (int) (((Math.abs(explosion.getPlayerVelocity().getX()) + Math.abs(explosion.getPlayerVelocity().getY()) + Math.abs(explosion.getPlayerVelocity().getZ())) / 2 + 2) * 15);
			VelocityTransaction vt = new VelocityTransaction(serverTickCount, Math.max(5, Math.abs(maxTicks)));
			pendingTransactions.add(vt);
			
			handleServerTick();
			
		} else if (PacketType.Play.Server.ABILITIES == packetId) {
			
			WrappedPacketOutAbilities abilities = new WrappedPacketOutAbilities(packet);
			if (abilities.isFlightAllowed()) {
				allowFlying = true;
			} else {
				allowFlying = false;
			}
			if (abilities.isFlying()) {
				flying = true;
			} else if (flying) {
				AbilitiesTransaction at = new AbilitiesTransaction(50, serverTickCount);
				pendingTransactions.add(at);
			}
			
		} else if (PacketType.Play.Client.ABILITIES == packetId) {
			
			WrappedPacketInAbilities abilities = new WrappedPacketInAbilities(packet);
			if (abilities.isFlying() && allowFlying) {
				
				flying = true;
				
			} else if (allowFlying) {
				
				flyTicks = 50;
				flying = false;
				
			}
			
		} else if (PacketType.Play.Server.POSITION == packetId) {
			
			WrappedPacketOutPosition posOut = new WrappedPacketOutPosition(packet);
			serverPositions.add(new PacketLocation(posOut.getPosition().getX(), posOut.getPosition().getY(), posOut.getPosition().getZ(), posOut.getYaw(), posOut.getPitch(), false));
			
			TeleportTransaction tt = new TeleportTransaction(8, serverTickCount);
			pendingTransactions.add(tt);
			
			TeleportEvent te = new TeleportEvent();
			te.callEvent(this);
			
		} else if (PacketType.Play.Server.KEEP_ALIVE == packetId) {
			
			WrappedPacketOutKeepAlive oka = new WrappedPacketOutKeepAlive(packet);
			pendingKeepAlives.put(oka.getId(), System.currentTimeMillis());
			
		} else if (PacketType.Play.Client.KEEP_ALIVE == packetId) {
			
			WrappedPacketInKeepAlive ika = new WrappedPacketInKeepAlive(packet);
			if (!pendingKeepAlives.containsKey(ika.getId())) return;
			long time = System.currentTimeMillis() - pendingKeepAlives.remove(ika.getId());
			keepAlivePing = ((keepAlivePing * 4) + time) / 5;
			sentKeepAlive = true;
			
		} else if (PacketType.Play.Client.BLOCK_PLACE == packetId) {
			
			WrappedPacketInBlockPlace bp = new WrappedPacketInBlockPlace(packet);
			InteractEvent ie = new InteractEvent(bp.getBlockPosition().getX(), bp.getBlockPosition().getY(), bp.getBlockPosition().getZ(), bp.getDirection());
			ie.callEvent(this);
			
		} else if (PacketType.Play.Client.BLOCK_DIG == packetId) {
			
			WrappedPacketInBlockDig bd = new WrappedPacketInBlockDig(packet);
			BlockDigEvent bde = new BlockDigEvent(bd.getDigType(), bd.getDirection());
			bde.callEvent(this);
			
			if (bd.getDigType().equals(PlayerDigType.START_DESTROY_BLOCK)) {
				
				digging = true;
				
			} else if (bd.getDigType().equals(PlayerDigType.ABORT_DESTROY_BLOCK) || bd.getDigType().equals(PlayerDigType.STOP_DESTROY_BLOCK)) {
				
				digging = false;
				digTicks = 15;
				
			}
			
		} else if (PacketType.Play.Server.NAMED_ENTITY_SPAWN == packetId) {
			
			WrappedPacketOutNamedEntitySpawn namedEntity = new WrappedPacketOutNamedEntitySpawn(packet);
			entityTracker.createEntity(namedEntity.getEntity(), new PacketLocation(namedEntity.getPosition().getX(), namedEntity.getPosition().getY(), namedEntity.getPosition().getZ(), namedEntity.getYaw(), namedEntity.getPitch()));
			
		} else if (PacketType.Play.Server.SPAWN_ENTITY_LIVING == packetId) {
			
			WrappedPacketOutSpawnEntityLiving spawnLiving = new WrappedPacketOutSpawnEntityLiving(packet);
			entityTracker.createEntity(spawnLiving.getEntity(), new PacketLocation(spawnLiving.getPosition().getX(), spawnLiving.getPosition().getY(), spawnLiving.getPosition().getZ(), spawnLiving.getYaw(), spawnLiving.getPitch()));
			
		} else if (PacketType.Play.Server.SPAWN_ENTITY == packetId) {
			
			WrappedPacketOutSpawnEntity spawnEntity = new WrappedPacketOutSpawnEntity(packet);
			entityTracker.createEntity(spawnEntity.getEntity(), new PacketLocation(spawnEntity.getPosition().getX(), spawnEntity.getPosition().getY(), spawnEntity.getPosition().getZ(), spawnEntity.getYaw(), spawnEntity.getPitch()));
			
		} else if (PacketType.Play.Server.ENTITY_TELEPORT == packetId) {
			
			WrappedPacketOutEntityTeleport entityTeleport = new WrappedPacketOutEntityTeleport(packet);
			if (entityTracker.getEntity(entityTeleport.getEntity()) != null) {
				entityTracker.getEntity(entityTeleport.getEntity()).teleportEntity(new PacketLocation(entityTeleport.getPosition().getX(), entityTeleport.getPosition().getY(), entityTeleport.getPosition().getZ(), entityTeleport.getYaw(), entityTeleport.getPitch()));
			}
			
		} else if (PacketType.Play.Server.REL_ENTITY_MOVE == packetId) {
			
			WrappedPacketOutRelEntityMove relMove = new WrappedPacketOutRelEntityMove(packet);
			if (entityTracker.getEntity(relMove.getEntity()) != null) {
				entityTracker.getEntity(relMove.getEntity()).moveEntityRelative(relMove.getDeltaX(), relMove.getDeltaY(), relMove.getDeltaZ());
			}
			
		} else if (PacketType.Play.Server.REL_ENTITY_MOVE_LOOK == packetId) {
			
			WrappedPacketOutRelEntityMoveLook relMoveLook = new WrappedPacketOutRelEntityMoveLook(packet);
			if (entityTracker.getEntity(relMoveLook.getEntity()) != null) {
				entityTracker.getEntity(relMoveLook.getEntity()).moveEntityRelative(relMoveLook.getDeltaX(), relMoveLook.getDeltaY(), relMoveLook.getDeltaZ(), relMoveLook.getYaw(), relMoveLook.getPitch());
			}
			
		} else if (PacketType.Play.Client.CLIENT_COMMAND == packetId) {
			
			WrappedPacketInClientCommand clientCommand = new WrappedPacketInClientCommand(packet);
			if (clientCommand.getClientCommand().equals(ClientCommand.OPEN_INVENTORY_ACHIEVEMENT)) {
				
				clientInventoryOpen = true;
				serverInventoryOpen = true;
				
			}
			
		} else if (PacketType.Play.Client.CLOSE_WINDOW == packetId) {
			
			clientInventoryOpen = false;
			serverInventoryOpen = false;
			inventoryTicks = 5;
			
		} else if (PacketType.Play.Server.OPEN_WINDOW == packetId) {
			
			InventoryTransaction inventoryTransaction = new InventoryTransaction(true, serverTickCount);
			pendingTransactions.add(inventoryTransaction);
			
		} else if (PacketType.Play.Server.CLOSE_WINDOW == packetId) {
			
			InventoryTransaction inventoryTransaction = new InventoryTransaction(false, serverTickCount);
			pendingTransactions.add(inventoryTransaction);
			
		} else if (PacketType.Play.Client.WINDOW_CLICK == packetId) {
			
			serverInventoryOpen = true;
			
			WrappedPacketInWindowClick windowClick = new WrappedPacketInWindowClick(packet);
			
			InventoryInteractEvent iie;
			if (windowClick.getActionNumber().isPresent()) {
				iie = new InventoryInteractEvent(windowClick.getActionNumber().get(), windowClick.getWindowId(), windowClick.getWindowSlot());
			} else {
				iie = new InventoryInteractEvent((short) -1, windowClick.getWindowId(), windowClick.getWindowSlot());
			}
			iie.callEvent(this);
			
		} else if (PacketType.Play.Server.ATTACH_ENTITY == packetId) {
			
			WrappedPacket pack = new WrappedPacket(packet);
			Entity passenger = null;
			Entity vehicle = null;
			for (Entity ent : player.getWorld().getEntities()) {
				
				if (pack.readInt(0) == ent.getEntityId()) {
					passenger = ent;
				}
				if (pack.readInt(1) > 0 && pack.readInt(1) == ent.getEntityId()) {
					vehicle = ent;
				}
				
			}
			
			if (passenger != player) return;
			
			if (vehicle == null) {
				
				RidingTransaction rt = new RidingTransaction(10, serverTickCount);
				pendingTransactions.add(rt);
				
			} else {
				
				clientRiding = true;
				
			}
			
		}
		
	}
	
	public void handleTransaction(short uid) {
		
		Iterator<Transaction> it = pendingTransactions.iterator();
		while (it.hasNext()) {
			
			Transaction t = it.next();
			if (t.getUid() == uid) {
				it.remove();
				
				if (t instanceof VelocityTransaction) {
					maxVelocityTicks = ((VelocityTransaction) t).getVelocityTicks();
				} else if (t instanceof AbilitiesTransaction) {
					flyTicks = ((AbilitiesTransaction) t).getFlyTicks();
					flying = false;
				} else if (t instanceof TeleportTransaction) {
					teleportTicks = ((TeleportTransaction) t).getTicks();
				} else if (t instanceof RidingTransaction) {
					ridingTicks = ((RidingTransaction) t).getTicks();
					clientRiding = false;
				} else if (t instanceof InventoryTransaction) {
					serverInventoryOpen = ((InventoryTransaction) t).isInventoryOpen();
					clientInventoryOpen = ((InventoryTransaction) t).isInventoryOpen();
					if (!clientInventoryOpen)
						inventoryTicks = 5;
				}
				
			}
		}
		
	}
	
	public void handleServerTick() {
		
		if (!player.isOnline())
			return;
		
		WrappedPacketOutTransaction transactionOut = new WrappedPacketOutTransaction(0, serverTickCount, false);
		Apollo.getInstance().getPacketEvents().getPlayerUtils().sendPacket(player, transactionOut);
		
		if (serverTickCount == Short.MIN_VALUE)
			serverTickCount = 0;
		serverTickCount--;
		
	}
	
	public boolean isFlying() {
		if (flyTicks > 0)
			return true;
		return flying;
	}
	
	public boolean isDigging() {
		if (digging) return true;
		return digTicks > 0;
	}
	
	public boolean isPositionDesynced() {
		if (!serverPositions.isEmpty())
			return true;
		return serverPositionTicks > 0;
	}
	
	public int getInventoryTicks() {
		return inventoryTicks;
	}
	
	public boolean isInventoryOpen() {
		return clientInventoryOpen;
	}
	
	public boolean isServerInventoryOpen() {
		return serverInventoryOpen;
	}
	
	public boolean isSprinting() {
		return sprinting;
	}
	
	public short getServerTick() {
		return serverTickCount;
	}
	
	public EntityTracker getEntityTracker() {
		return entityTracker;
	}
	
	public int getIceTicks() {
		return iceTicks;
	}
	
	public int getStairTicks() {
		return stairTicks;
	}
	
	public int getTeleportTicks() {
		return teleportTicks;
	}
	
	public boolean isOnClimbable() {
		return climbableTicks > 0;
	}
	
	public boolean hasRepliedToKeepAlive() {
		return sentKeepAlive;
	}
	
	public long getKeepAlivePing() {
		return keepAlivePing;
	}
	
	public int getJumpBoostTicks() {
		return jumpBoostTicks;
	}
	
	public boolean isServerGround() {
		return serverGround;
	}
	
	public int getServerGroundStateTicks() {
		return serverGroundStateTicks;
	}
	
	public int getLiquidTicks() {
		return liquidTicks;
	}
	
	public boolean isRiding() {
		if (clientRiding) return true;
		return ridingTicks > 0;
	}
	
	public int getWebTicks() {
		return webTicks;
	}
	
	public int getUnderBlockTicks() {
		return underBlockTicks;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public short getClientVersion() {
		return clientVersion;
	}
	
	public int getVelocityTicks() {
		return maxVelocityTicks;
	}
	
	public ArrayList<Check> getPlayerChecks(){
		return playerChecks;
	}
	
	public void setChecks(ArrayList<Check> newChecks) {
		playerChecks = newChecks;
	}

}
