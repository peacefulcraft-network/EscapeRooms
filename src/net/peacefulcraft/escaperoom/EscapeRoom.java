package net.peacefulcraft.escaperoom;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.escaperoom.commands.EscapeRoomAdminCommand;
import net.peacefulcraft.escaperoom.commands.EscapeRoomAdminTC;
import net.peacefulcraft.escaperoom.commands.EscapeRoomCommand;
import net.peacefulcraft.escaperoom.commands.EscapeRoomTC;
import net.peacefulcraft.escaperoom.config.ConfigurationManager;
import net.peacefulcraft.escaperoom.config.EscapeRoomConfiguration;
import net.peacefulcraft.escaperoom.config.MainConfiguration;
import net.peacefulcraft.escaperoom.deploy.DeploymentManager;
import net.peacefulcraft.escaperoom.deploy.DeploymentPackage;
import net.peacefulcraft.escaperoom.deploy.DeploymentProvider;
import net.peacefulcraft.escaperoom.deploy.ServerMode;
import net.peacefulcraft.escaperoom.gamehandle.EscapeRoomWorld;
import net.peacefulcraft.escaperoom.gamehandle.GameManager;
import net.peacefulcraft.escaperoom.gamehandle.world.WorldManager;
import net.peacefulcraft.escaperoom.listeners.PlayerJoinListener;

public class EscapeRoom extends JavaPlugin {

	public static final String messagingPrefix = ChatColor.GREEN + "[" + ChatColor.BLUE + "PCN" + ChatColor.GREEN + "]" + ChatColor.RESET;

	private static EscapeRoom _this;
		public static EscapeRoom _this() { return _this; }

	private ConfigurationManager configManager;
		public MainConfiguration getConfiguration() { return configManager.getPluginConfig(); }
		public EscapeRoomConfiguration getEscapeRoomConfiguration(String name) { return this.configManager.getEscapeRoomConfig(name); }
		public List<EscapeRoomConfiguration> getEscapeRoomConfigurtions() { return this.configManager.getConfiguredEscapeRooms(); }

	private DeploymentManager deploymentManager;
		public DeploymentManager getDeploymentManager() { return this.deploymentManager; }

	private GameManager gameManager;
		public GameManager getGameManager() { return this.gameManager; }

	private WorldManager worldManager;
		public WorldManager getWorldManager() { return this.worldManager; }

  /**
   * Called when Bukkit server enables the plguin
   * For improved reload behavior, use this as if it was the class constructor
   */
  public void onEnable() {
	this._this = this;
	// Save default config if one does not exist, load the configuration into memory
	this.configManager = new ConfigurationManager();

	this.setupCommands();
	this.setupEventListeners();

	DeploymentProvider httpProvider = new DeploymentProvider(this.configManager.getMainDeploymentProviderConfig());
	this.deploymentManager = new DeploymentManager(httpProvider);

	this.worldManager = new WorldManager();

	// In development, load all the worlds [set for dev loading]
	if (this.getConfiguration().getServerMode() == ServerMode.DEVELOPMENT) {
		Collection<EscapeRoomConfiguration> configs = this.configManager.loadAllEscapeRoomConfigurations();
		for(EscapeRoomConfiguration config : configs) {
			this.logNotice("Loaded config for EscapeRoom " + config.getName() + "...");
			if (config.devLoadWorld()) {
				this.worldManager.loadWorld(config);
				this.logNotice("...loaded world.");
			} else {
				this.logNotice("...skipping world load becuase it is not set to auto load.");
			}
		}
	} else {
		// Download updated versions of the worlds and place them, but don't load them. They're created ad-hoc.
		this.deploymentManager.getDeploymentPackages().forEach((name, pack) -> {
			this.deploymentManager.pullDeploymentPackage(name);
			pack.unpack();
			this.logNotice("Succesfully unpacked package " + name);
		});
		
		Collection<EscapeRoomConfiguration> configs = this.configManager.loadAllEscapeRoomConfigurations();
	}
  }

	private void setupCommands() {
		this.getCommand("escaperoom").setExecutor(new EscapeRoomCommand());
		this.getCommand("escaperoom").setTabCompleter(new EscapeRoomTC());

		this.getCommand("escaperoomadmin").setExecutor(new EscapeRoomAdminCommand());
		this.getCommand("escaperoomadmin").setTabCompleter(new EscapeRoomAdminTC());
	}

	private void setupEventListeners() {
		this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
	}

	/**
	 * Loads the EscapeRoomWorld / world with that name, or creates a new one if it does not exist.
	 * @param name Name of the EscapeRoom to create
	 * @return The loaded EscapeRoomWorld
	 */
	public EscapeRoomWorld loadEscapeRoom(String name) {
		EscapeRoomConfiguration newEscapeRoomConfig = this.configManager.createNewEscapeRoomConfiguration(name);
		return this.worldManager.loadWorld(newEscapeRoomConfig);
	}

	/**
	 * Packages and ships the requested EscapeRoom to the DeploymentNetwork for production usage
	 * @param name The name of the EsapeRoom to package
	 */
	public void shipEscapeRoomDeployment(String name) throws RuntimeException {
		EscapeRoomConfiguration targetConfig = this.configManager.getEscapeRoomConfig(name);
		if (targetConfig == null) {
			throw new RuntimeException("Unable to find coresponding configration file for EscapeRoom " + name + ". Is this the correct name?");
		}

		EscapeRoomWorld targetWorld = this.worldManager.getWorld(targetConfig.getName());
		if (targetWorld != null) {
			this.worldManager.unloadWorld(targetWorld);
		}

		this.deploymentManager.packageForDeployment(name);
		this.deploymentManager.shipDeploymentPackage(name);
	}

	public void logDebug(String message) {
		if (this.getConfiguration().isDebugEnabled()) {
			this.getServer().getLogger().log(Level.INFO, message);
		}
	}

	public void logNotice(String message) {
		this.getServer().getLogger().log(Level.INFO, message);
	}

	public void logWarning(String message) {
		this.getServer().getLogger().log(Level.WARNING, message);
	}

	public void logSevere(String message) { 
		this.getServer().getLogger().log(Level.SEVERE, message);
	}

	/**
	 * Called whenever Bukkit server disableds the plugin
	 * For improved reload behavior, try to reset the plugin to it's initaial state here.
	 */
	public void onDisable () {
		this.getServer().getScheduler().cancelTasks(this);
	}
}