package net.peacefulcraft.escaperoom.config;

import net.peacefulcraft.escaperoom.deploy.ServerMode;

public class MainConfiguration extends Configuration {

	public MainConfiguration() {
		super("config.yml", "config.yml");

		this.loadValues();
	}

	private void loadValues() {
		this.isDebugEnabled = this.config.getBoolean("debug");
		this.serverMode = ServerMode.valueOf(this.config.getString("server_mode").toUpperCase());
	}

	private Boolean isDebugEnabled;
		public Boolean isDebugEnabled() {
			return this.isDebugEnabled;
		}
		public void setDebugEnabled(Boolean enabled) {
			this.isDebugEnabled = enabled;
		}

	private ServerMode serverMode;
		public ServerMode getServerMode() { return this.serverMode; }
	
}
