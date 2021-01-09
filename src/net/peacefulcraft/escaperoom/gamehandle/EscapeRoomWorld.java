package net.peacefulcraft.escaperoom.gamehandle;

import org.bukkit.World;

import net.peacefulcraft.escaperoom.config.EscapeRoomConfiguration;

public class EscapeRoomWorld {

	private World world;
		public World getWorld() { return this.world; }

	private EscapeRoomConfiguration config;
		public EscapeRoomConfiguration getConfig() { return this.config; }

	private Boolean isLoaded;
		public Boolean isWorldLoaded() { return this.isLoaded; }
		public void setIsWorldLoaded(Boolean loaded) { this.isLoaded = loaded; }

	private Boolean hostsActiveGame;
		public Boolean hostsActiveGame() { return this.hostsActiveGame; }
	
	private EscapeRoomGame gameInstance;
		public EscapeRoomGame getGameInstance() { return this.gameInstance; }

	public EscapeRoomWorld(World world, EscapeRoomConfiguration config) {
		this.world = world;
		this.config = config;
		this.isLoaded = false;
		this.hostsActiveGame = false;
	}

	public EscapeRoomWorld(World world, EscapeRoomConfiguration config, EscapeRoomGame gameInstance) {
		this.world = world;
		this.config = config;
		this.gameInstance = gameInstance;
		this.isLoaded = false;
		this.hostsActiveGame = true;
	}
}
