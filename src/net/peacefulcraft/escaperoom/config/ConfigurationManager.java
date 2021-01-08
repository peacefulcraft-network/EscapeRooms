package net.peacefulcraft.escaperoom.config;

import java.io.File;

import net.peacefulcraft.escaperoom.EscapeRoom;

public class ConfigurationManager {
	private MainConfiguration pluginConfig;
		public MainConfiguration getPluginConfig() { return this.pluginConfig; }
	
	private DeploymentProviderConfiguration mainDeploymentProviderConfig;
		public DeploymentProviderConfiguration getMainDeploymentProviderConfig() { return this.mainDeploymentProviderConfig;  }

	public ConfigurationManager() {
		this.pluginConfig = new MainConfiguration();
		this.mainDeploymentProviderConfig = new DeploymentProviderConfiguration();
		this.makeEscapeRoomConfigFolder();	
	}

	private void makeEscapeRoomConfigFolder() {
		File path = new File(EscapeRoom._this().getDataFolder(), "escaperooms");
		path.mkdirs();
	}
}
