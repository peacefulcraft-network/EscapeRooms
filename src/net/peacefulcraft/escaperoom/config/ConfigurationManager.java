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

	/**
	 * Creates a new EscapeRoomConfiguration file with the given name from the EscapeRoomConfiguration template.
	 * New configuration is automatically registered with the ConfigurationManager for use accross the plugin.
	 * @param name Name of the new escape room
	 * @return Reference to the new EscapeRoomConfiguration object.
	 */
	public EscapeRoomConfiguration createNewEscapeRoomConfiguration(String name) throws RuntimeException {
		if (this.escapeRoomConfigs.containsKey(name)) {
			throw new RuntimeException("Attempted to configure escape room with name " + name + ", but one already exists");
		}

		EscapeRoomConfiguration newEscapeRoomConfig = new EscapeRoomConfiguration(name);
		this.escapeRoomConfigs.put(name, newEscapeRoomConfig);
		return newEscapeRoomConfig;
	}

	/**
	 * Attempts to load all .yml files in the /escaperooms/ directory as escape room cofnigurations.
	 * All sucesfully loaded files are registered with the ConfigurationManager for use accross the plugin.
	 * This will forecefully evict any previous configurations from memory without saving them if there are name colisions.
	 * @return A collection containing all the EscapeRoomConfigurations that were loaded into memory.
	 */
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

	/**
	 * Loads the escape room cofniguration file which represents the escape room by name.
	 * The configuration file is also registered with ConfigurationManager to access accross the plugin.
	 * @param name Name of the escape room which is described by this configuration
	 * @return Reference to the EscapeRoomConfiguration object that was created.
	 */
	public EscapeRoomConfiguration loadEscapeRoomConfiguration(String name) {
		if (Configuration.configFileExists("escaperooms/" + name + ".yml")) {
			EscapeRoomConfiguration loadedConfig = new EscapeRoomConfiguration(name);
			this.escapeRoomConfigs.put(name, loadedConfig);
			return loadedConfig;
		} else {
			throw new RuntimeException("Attempted to load EscapeRoomConfiguration for " + name + ", but no such configuration exists.");
		}
	}

	/**
	 * Remove an escape room config from memory. This does not delete the config file from disk.
	 * This also does not trigger the .save() method on the config file. It is assumed to have already been saved if needed.
	 * @param name Name of the escape room releated to this configuration file.
	 */
	public void unloadEscapeRoomConfiguration(String name) {
		this.escapeRoomConfigs.remove(name);
	}

	/**
	 * Delete the requested EscapeRoomConfiguration file by the name of the EscapeRoom which it represents.
	 * @param name The name of the related escape room.
	 */
	public void deleteEscapeRoomConfig(String name) {
		EscapeRoomConfiguration targetConfig = this.escapeRoomConfigs.get(name);
		if (targetConfig == null) {
			return;
		}

		targetConfig.configFile.delete();
	}
}
