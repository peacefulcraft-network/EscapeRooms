package net.peacefulcraft.escaperoom.gamehandle;

import org.bukkit.entity.Player;

public class EscapeRoomPlayer {

	private Long registryId;
		public Long getRegistryId() { return this.registryId; }

	private Player player;
		public Player getPlayer() { return this.player; }

	public EscapeRoomPlayer(Long registryId) {
		this.registryId = registryId;
	}

	public void linkPlayer(Player player) {
		this.player = player;
	}
	
}
