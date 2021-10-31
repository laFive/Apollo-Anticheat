package me.lafive.apollo.listener;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.PacketListenerPriority;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.event.impl.PlayerInjectEvent;
import io.github.retrooper.packetevents.event.impl.PostPlayerInjectEvent;
import me.lafive.apollo.Apollo;
import me.lafive.apollo.data.PlayerData;

public class PlayerListener extends PacketListenerAbstract {
	
	public PlayerListener() {
		super(PacketListenerPriority.HIGHEST);
	}
	
	@Override
	public void onPacketPlayReceive(PacketPlayReceiveEvent e) {
		
		PlayerData data = Apollo.getInstance().getDataManager().getPlayerData(e.getPlayer());
		if (data != null) {
			data.handlePacket(e.getNMSPacket(), e.getPacketId());
		}
		
	}
	
	@Override
	public void onPacketPlaySend(PacketPlaySendEvent e) {
		
		PlayerData data = Apollo.getInstance().getDataManager().getPlayerData(e.getPlayer());
		if (data != null) {
			data.handlePacket(e.getNMSPacket(), e.getPacketId());
		}
		
	}
	
	@Override
    public void onPlayerInject(PlayerInjectEvent e) {
		Apollo.getInstance().getDataManager().createPlayerData(e.getPlayer());
	}
	
	@Override
	public void onPostPlayerInject(PostPlayerInjectEvent e) {
		PlayerData data = Apollo.getInstance().getDataManager().getPlayerData(e.getPlayer());
		data.setClientVersion(e.getClientVersion());
	}

}
