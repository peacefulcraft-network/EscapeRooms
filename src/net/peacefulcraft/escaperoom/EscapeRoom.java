package net.peacefulcraft.escaperoom;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.escaperoom.commands.EscapeRoomAdmin;
import net.peacefulcraft.escaperoom.commands.EscapeRoomAdminTC;
import net.peacefulcraft.escaperoom.commands.EscapeRoomTC;
import net.peacefulcraft.escaperoom.config.ConfigurationManager;
import net.peacefulcraft.escaperoom.config.MainConfiguration;
import net.peacefulcraft.escaperoom.deploy.DeploymentManager;
import net.peacefulcraft.escaperoom.deploy.DeploymentProvider;
import net.peacefulcraft.escaperoom.listeners.PlayerJoinListener;

public class EscapeRoom extends JavaPlugin {

	public static final String messagingPrefix = ChatColor.GREEN + "[" + ChatColor.BLUE + "PCN" + ChatColor.GREEN + "]" + ChatColor.RESET;

	private static EscapeRoom _this;
		public static EscapeRoom _this() { return _this; }

	private ConfigurationManager configManager;
		public MainConfiguration getConfiguration() { return configManager.getPluginConfig(); }

	private DeploymentManager deploymentManager;
		public DeploymentManager getDeploymentManager() { return this.deploymentManager; }

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
  }

	private void setupCommands() {
		this.getCommand("escaperoom").setExecutor(new EscapeRoom());
		this.getCommand("escaperoom").setTabCompleter(new EscapeRoomTC());

		this.getCommand("escaperoomadmin").setExecutor(new EscapeRoomAdmin());
		this.getCommand("escaperoomadmin").setTabCompleter(new EscapeRoomAdminTC());
	}

	private void setupEventListeners() {
		this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
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