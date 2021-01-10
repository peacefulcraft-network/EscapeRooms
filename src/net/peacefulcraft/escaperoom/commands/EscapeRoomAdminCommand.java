package net.peacefulcraft.escaperoom.commands;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.escaperoom.EscapeRoom;
import net.peacefulcraft.escaperoom.config.EscapeRoomConfiguration;
import net.peacefulcraft.escaperoom.gamehandle.EscapeRoomWorld;

public class EscapeRoomAdminCommand implements CommandExecutor {

  /**
   * @param sender Entity which used the command.
   * @param Command Object with details about the executed command
   * @param String label String with the command run, without the passed arguements.
   * @param String args The arguements passed by the player. Each space in the command counts as a new argument. 
   */
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (command.getName().equalsIgnoreCase("escaperoomadmin")) {

		if (!sender.isOp() && !sender.hasPermission("escaperoom.admin")) {
			sender.sendMessage(EscapeRoom.messagingPrefix + ChatColor.RED + "Permission denied. You must have OP or escaperoom.admin to use this command.");
		}

		if (args.length == 0) {
			this.sendInvalidSuboptionMessage(sender, "create, delete, unload, load, push, pull, setup");
			return true;
		}

		// Creates a new escape room (world + config)
		if (args[0].equalsIgnoreCase("create")) {
			if (args.length < 2) {
				sender.sendMessage(EscapeRoom.messagingPrefix + "Please include a name for this escape room.");
				return true;
			}

			String worldName = this.argsToWorldName(1, args);
			sender.sendMessage(EscapeRoom.messagingPrefix + "Generating world " + worldName);
			EscapeRoomWorld newWorld = EscapeRoom._this().loadEscapeRoom(worldName.trim());
			sender.sendMessage("World " + worldName + " created.");

		// Delets an existing escape room (world + config)
		} else if (args[0].equalsIgnoreCase("delete")) {
			if (args.length < 2) {
				sender.sendMessage(EscapeRoom.messagingPrefix + "Please include a name for the escape room to delete.");
				return true;
			}

			try {
				EscapeRoom._this().deleteEscapeRoom(args[1].trim());
				sender.sendMessage(EscapeRoom.messagingPrefix + ChatColor.GREEN + "Escape room " + args[1].trim() + " sucesfully deleted.");
			} catch (RuntimeException ex) {
				sender.sendMessage(EscapeRoom.messagingPrefix + ChatColor.RED + "An error occured while attempting to unload world " + args[1].trim());
				sender.sendMessage(EscapeRoom.messagingPrefix + ChatColor.RED + ex.getMessage());
			}
			return true;

		// Unloads an escape room world from memory (configs stay in memory)
		} else if (args[0].equalsIgnoreCase("unload")) {
			if (args.length < 2) {
				sender.sendMessage(EscapeRoom.messagingPrefix + "Please include a name for the escape room to unload.");
				return true;
			}

			String worldName = this.argsToWorldName(1, args);
			EscapeRoomWorld world = EscapeRoom._this().getWorldManager().getWorld(worldName);
			if (world == null) {
				sender.sendMessage(EscapeRoom.messagingPrefix + "No world by that name is currently loaded.");
				return true;
			} else {
				try {
					EscapeRoom._this().getWorldManager().unloadWorld(world);
					sender.sendMessage(EscapeRoom.messagingPrefix + ChatColor.GREEN + "World " + worldName + " succesfully unloaded. All players in that world were sent to the lobby area.");
				} catch(RuntimeException ex) {
					sender.sendMessage(EscapeRoom.messagingPrefix + ChatColor.RED + "An error occured while attempting to unload world " + worldName);
					sender.sendMessage(EscapeRoom.messagingPrefix + ChatColor.RED + ex.getMessage());
				}
			}

		// Loads an escape room world into memory
		} else if (args[0].equalsIgnoreCase("load")) {
			if (args.length < 2) {
				sender.sendMessage(EscapeRoom.messagingPrefix + ChatColor.RED + "Please include a name for the escape room to load.");
				return true;
			}

			String worldName = this.argsToWorldName(1, args);
			EscapeRoomConfiguration worldConfig = EscapeRoom._this().getEscapeRoomConfiguration(worldName);
			if (worldConfig == null) {
				sender.sendMessage(EscapeRoom.messagingPrefix + ChatColor.RED + "No escape room by that name exists.");
				return true;
			}
			
			try {
				EscapeRoom._this().getWorldManager().loadWorld(worldConfig);
				sender.sendMessage(EscapeRoom.messagingPrefix + ChatColor.GREEN + "Sucesfully loaded world " + worldName);
			} catch(RuntimeException ex) {
				sender.sendMessage(EscapeRoom.messagingPrefix + ChatColor.RED + "An error occured while attempting to load world " + worldName);
				sender.sendMessage(EscapeRoom.messagingPrefix + ChatColor.RED + ex.getMessage());
			}

			return true;

		// Packages and ships an escape room deployment package to the delivery network
		} else if (args[0].equalsIgnoreCase("push")) {
			if (args.length < 2 || !this.isValidEscapeRoomName(args[1])) {
				this.sendInvalidSuboptionMessage(sender, this.getEscapeRoomNames());
				return true;
			}

			try {
				EscapeRoom._this().shipEscapeRoomDeployment(args[1]);
			} catch (RuntimeException ex) {
				ex.printStackTrace();
				EscapeRoom._this().logSevere("An error occured while attempting to drop ship an escape room. Target:" + args[1]);
				sender.sendMessage(EscapeRoom.messagingPrefix + ChatColor.RED + "An error occured during the deployment proccess.");
				sender.sendMessage(EscapeRoom.messagingPrefix + ex.getMessage());
				sender.sendMessage(EscapeRoom.messagingPrefix + "The console should have more details.");
			}

			sender.sendMessage(EscapeRoom.messagingPrefix + ChatColor.GREEN + "Deployment reported no errors!");

		// Pulls an updated copy of the requested escape room deployment package from the deployment network
		} else if (args[0].equalsIgnoreCase("pull")) {
			// TODO: Finish /esa pull command

		// Teleports the player to the requested escape room world
		} else if (args[0].equalsIgnoreCase("goto")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("The goto command is only executable by Players.");
				return true;
			}

			if (args.length < 2) {
				sender.sendMessage(EscapeRoom.messagingPrefix + "Please include a world name to teleport to.");
				return true;
			}
			
			EscapeRoomWorld esWorld = EscapeRoom._this().getWorldManager().getWorld(this.argsToWorldName(1, args));
			if (esWorld == null) {
				this.sendInvalidSuboptionMessage(sender, EscapeRoom._this().getWorldManager().getWorlds().keySet().toString());
				return true;
			}

			((Player) sender).teleport(esWorld.getWorld().getSpawnLocation());
			sender.sendMessage(EscapeRoom.messagingPrefix + "Teleported to world " + esWorld.getConfig().getName());

		// Used to set various spawn points for escape rooms
		} else if (args[0].equalsIgnoreCase("setup")) {
			if (args.length < 2) {
				this.sendInvalidSuboptionMessage(sender, "rulesRoom, startLocation, winLocation, looseLocation");
				return true;
			}

			if (!(sender instanceof Player)) {
				sender.sendMessage(EscapeRoom.messagingPrefix + "The /esa setup command requires location data and is only executable by players.");
				return true;
			}
			Location playersLocation = ((Player) sender).getLocation();

			if (args[1].equalsIgnoreCase("globalSpawn")) {
				EscapeRoom._this().getConfiguration().setLobbySpawn(playersLocation);
				sender.sendMessage(EscapeRoom.messagingPrefix + "Global spawn point saved.");
				return true;
			}

			EscapeRoomWorld esWorld = EscapeRoom._this().getWorldManager().getWorld(((Player) sender).getWorld().getName().replaceAll("_", " "));
			if (esWorld == null) {
				sender.sendMessage(EscapeRoom.messagingPrefix + ChatColor.RED + "You don't seen to be in an Escape Room World. Create one with /esa create [name]");
				return true;
			}
			
			EscapeRoomConfiguration esConfig = esWorld.getConfig();

			if (args[1].equalsIgnoreCase("rulesRoom")) {
				esConfig.setRulesRoomLocation(playersLocation);
				sender.sendMessage(EscapeRoom.messagingPrefix + "Escaperoom rule room location saved.");
				return true;

			} else if (args[1].equalsIgnoreCase("startLocation")) {
				esConfig.setStartLocation(playersLocation);
				sender.sendMessage(EscapeRoom.messagingPrefix + "Escaperoom start location saved.");
				return true;

			} else if (args[1].equalsIgnoreCase("winLocation")) {
				esConfig.setWinLocation(playersLocation);
				sender.sendMessage(EscapeRoom.messagingPrefix + "Escaperoom win room location saved.");
				return true;

			} else if (args[1].equalsIgnoreCase("looseLocation")) {
				esConfig.setLooseLocation(playersLocation);
				sender.sendMessage(EscapeRoom.messagingPrefix + "Escaperoom loose room location saved.");
				return true;

			} else {
				this.sendInvalidSuboptionMessage(sender, "rulesRoom, startLocation, winLocation, looseLocation");
				return true;
			}
 		} else {
			this.sendInvalidSuboptionMessage(sender, "create, delete, unload, load, push, pull, setup");
		}
	}

	return true;
  }
 
  private void sendInvalidSuboptionMessage(CommandSender sender, String options) {
	options = options.replaceAll(",", ChatColor.RESET + ", " + ChatColor.GOLD);
	sender.sendMessage(
		EscapeRoom.messagingPrefix + ChatColor.RED + "Invalid option. Please select one of the following:\n" +
		ChatColor.GREEN + "[ " + ChatColor.GOLD +
		options +
		ChatColor.GREEN + " ]" 	
	);
  }

  private String argsToWorldName(int startIndex, String[] args) {
	String worldName = "";
	for(int i=startIndex; i<args.length; i++) {
		worldName += args[i] + " ";
	}

	return worldName.trim();
  }

  private Boolean isValidEscapeRoomName(String name) {
	List<EscapeRoomConfiguration> escs = EscapeRoom._this().getEscapeRoomConfigurtions();
	for (int i=0; i<escs.size(); i++) {
		if (name.equalsIgnoreCase(escs.get(i).getName())) {
			return true;
		}
	}

	return false;
  }

  private String getEscapeRoomNames() {
	String escapeRoomNames = "";
	List<EscapeRoomConfiguration> escs = EscapeRoom._this().getEscapeRoomConfigurtions();
	for (int i=0; i<escs.size(); i++) {
		escapeRoomNames += escs.get(i).getName() + ", ";
	}
	return escapeRoomNames.substring(0, escapeRoomNames.length() -2);
  }
}