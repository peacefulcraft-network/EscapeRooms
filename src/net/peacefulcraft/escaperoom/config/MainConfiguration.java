package net.peacefulcraft.escaperoom.config;

import org.bukkit.Location;

import net.peacefulcraft.escaperoom.deploy.ServerMode;

public class MainConfiguration extends Configuration {

	public MainConfiguration() {
		super("config.yml", "config.yml");

		this.loadValues();
	}

	private void loadValues() {
		this.isDebugEnabled = this.config.getBoolean("debug");
		this.serverMode = ServerMode.valueOf(this.config.getString("server_mode").toUpperCase());
		this.lobbySpawn = this.config.getLocation("lobby_spawn_location");
	}

	private Boolean isDebugEnabled;
		public Boolean isDebugEnabled() {
			return this.isDebugEnabled;
		}
		public void setDebugEnabled(Boolean enabled) {
			this.isDebugEnabled = enabled;
			this.config.set("debug", enabled);
			this.saveConfiguration();
		}

	private ServerMode serverMode;
		public ServerMode getServerMode() { return this.serverMode; }

	
	private Location lobbySpawn;
		public Location getLobbySpawn() { return this.lobbySpawn; }
		public void setLobbySpawn(Location loc) {
			this.lobbySpawn = loc;
			this.config.set("lobby_spawn_location", loc);
			this.saveConfiguration(); 
		}
}
