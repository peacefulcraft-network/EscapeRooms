package net.peacefulcraft.escaperoom.gamehandle;

import org.bukkit.World;

import net.peacefulcraft.escaperoom.EscapeRoom;
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

	public void unloadWorld() throws RuntimeException {
		EscapeRoom._this().logNotice("Unloading world " + this.config.getWorldName());
		if (this.hostsActiveGame) {
			throw new RuntimeException("Unable to unload world. There is an active game running in this world.");
		}

		EscapeRoom._this().logNotice("...removing players from world " + this.config.getWorldName());
		this.world.getPlayers().forEach((player) -> {
			player.teleport(EscapeRoom._this().getConfiguration().getLobbySpawn());
			player.sendMessage(EscapeRoom.messagingPrefix + "The world you were in was unloaded for deploymnet. You've been teleported to spawn.");
		});

		EscapeRoom._this().logNotice("...requesting server unload the world " + this.config.getWorldName());
		EscapeRoom._this().getServer().unloadWorld(this.world, true);
	}
}
