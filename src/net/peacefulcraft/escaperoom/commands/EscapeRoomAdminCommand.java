package net.peacefulcraft.escaperoom.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.escaperoom.EscapeRoom;
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
		if (args.length == 0) {
			this.sendInvalidSuboptionMessage(sender, "create, delete, unload, load, deploy, fetch");
			return true;
		}

		if (args[0].equalsIgnoreCase("create")) {
			if (args.length < 2) {
				sender.sendMessage(EscapeRoom.messagingPrefix + "Please include a name for this escape room.");
				return true;
			}

			String worldName = this.argsToWorldName(1, args);
			sender.sendMessage(EscapeRoom.messagingPrefix + "Generating world " + worldName);
			EscapeRoomWorld newWorld = EscapeRoom._this().loadEscapeRoom(worldName.trim());
			sender.sendMessage("World " + worldName + " created.");
		} else if (args[0].equalsIgnoreCase("delete")) {

		} else if (args[0].equalsIgnoreCase("unload")) {

		} else if (args[0].equalsIgnoreCase("load")) {

		} else if (args[0].equalsIgnoreCase("deploy")) {

		} else if (args[0].equalsIgnoreCase("fetch")) {

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
		} else {
			this.sendInvalidSuboptionMessage(sender, "create, delete, unload, load, deploy, fetch");
		}
	}

	return true;
  }
 
  private void sendInvalidSuboptionMessage(CommandSender sender, String options) {
	sender.sendMessage(
		EscapeRoom.messagingPrefix + "Invalid option. Please select one of the following: " +
		ChatColor.GREEN + "[ " + ChatColor.RESET +
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
}