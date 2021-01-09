package net.peacefulcraft.escaperoom.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.peacefulcraft.escaperoom.EscapeRoom;

public class ConfigurationManager {
	private MainConfiguration pluginConfig;
		public MainConfiguration getPluginConfig() { return this.pluginConfig; }
	
	private DeploymentProviderConfiguration mainDeploymentProviderConfig;
		public DeploymentProviderConfiguration getMainDeploymentProviderConfig() { return this.mainDeploymentProviderConfig;  }

	private HashMap<String, EscapeRoomConfiguration> escapeRoomConfigs;
		public EscapeRoomConfiguration getEscapeRoomConfig(String name) { return this.escapeRoomConfigs.get(name); }
		public List<EscapeRoomConfiguration> getConfiguredEscapeRooms() {
			List<EscapeRoomConfiguration> list = new ArrayList<EscapeRoomConfiguration>(this.escapeRoomConfigs.size());
			list.addAll(this.escapeRoomConfigs.values());
			return Collections.unmodifiableList(list);
		}

	public ConfigurationManager() {
		this.pluginConfig = new MainConfiguration();
		this.mainDeploymentProviderConfig = new DeploymentProviderConfiguration();
		this.makeEscapeRoomConfigFolder();
		this.escapeRoomConfigs = new HashMap<String, EscapeRoomConfiguration>();
	}

		private void makeEscapeRoomConfigFolder() {
			File path = new File(EscapeRoom._this().getDataFolder(), "escaperooms");
			path.mkdirs();
		}

	public EscapeRoomConfiguration createNewEscapeRoomConfiguration(String name) throws RuntimeException {
		if (this.escapeRoomConfigs.containsKey(name)) {
			throw new RuntimeException("Attempted to configure escape room with name " + name + ", but one already exists");
		}

		EscapeRoomConfiguration newEscapeRoomConfig = new EscapeRoomConfiguration(name);
		this.escapeRoomConfigs.put(name, newEscapeRoomConfig);
		return newEscapeRoomConfig;
	}

	public Collection<EscapeRoomConfiguration> loadAllEscapeRoomConfigurations() {
		File esDataDir = new File(EscapeRoom._this().getDataFolder() + "/escaperooms");
		String[] esConfigFileNames = esDataDir.list(new YAMLFileFilter());
		if (esConfigFileNames == null || esConfigFileNames.length == 0) {
			return new ArrayList<EscapeRoomConfiguration>();
		}

		for(String fileName : esConfigFileNames) {
			try {
				EscapeRoomConfiguration newConfig = new EscapeRoomConfiguration(fileName.replaceAll(".yml", ""));
				this.escapeRoomConfigs.put(newConfig.getName(), newConfig);
			} catch (Exception ex) {
				ex.printStackTrace();
				EscapeRoom._this().logSevere("An error occured while loading the EscapeRoom config for " + fileName + ". See console for details.");
			}
		}

		return Collections.unmodifiableCollection(this.escapeRoomConfigs.values());
	}

	public EscapeRoomConfiguration loadEscapeRoomConfiguration(String name) {
		if (Configuration.configFileExists("escaperooms/" + name + ".yml")) {
			EscapeRoomConfiguration loadedConfig = new EscapeRoomConfiguration(name);
			this.escapeRoomConfigs.put(name, loadedConfig);
			return loadedConfig;
		} else {
			throw new RuntimeException("Attempted to load EscapeRoomConfiguration for " + name + ", but no such configuration exists.");
		}
	}

	public void unloadEscapeRoomConfiguration(String name) {
		this.escapeRoomConfigs.remove(name);
	}

	public void deleteEscapeRoomConfig(String name) {
		EscapeRoomConfiguration targetConfig = this.escapeRoomConfigs.get(name);
		if (targetConfig == null) {
			return;
		}

		targetConfig.configFile.delete();
	}
}
