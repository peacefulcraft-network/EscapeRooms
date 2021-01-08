package net.peacefulcraft.escaperoom.gamehandle;

import net.peacefulcraft.escaperoom.config.EscapeRoomConfiguration;

public class EscapeRoomWorld {

	private EscapeRoomConfiguration config;
		public EscapeRoomConfiguration getConfig() { return this.config; }

	private Boolean hostsActiveGame;
		public Boolean hostsActiveGame() { return this.hostsActiveGame; }
	
	private EscapeRoomGame gameInstance;
		public EscapeRoomGame getGameInstance() { return this.gameInstance; }

	public EscapeRoomWorld(EscapeRoomConfiguration config) {
		this.config = config;
		this.hostsActiveGame = false;
	}

	public EscapeRoomWorld(EscapeRoomConfiguration config, EscapeRoomGame gameInstance) {
		this.config = config;
		this.gameInstance = gameInstance;
		this.hostsActiveGame = true;
	}
}
