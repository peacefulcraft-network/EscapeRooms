package net.peacefulcraft.escaperoom.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import net.peacefulcraft.escaperoom.EscapeRoom;
import net.peacefulcraft.escaperoom.config.EscapeRoomConfiguration;

public class EscapeRoomAdminTC implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (command.getName().equalsIgnoreCase("escaperoomadmin")) {
			if (!sender.isOp() && !sender.hasPermission("escaperoom.admin")) { return null; }
			ArrayList<String> suggestions = new ArrayList<String>();

			if (args.length == 1) {
				suggestions.add("create");
				suggestions.add("delete");
				suggestions.add("unload");
				suggestions.add("load");
				suggestions.add("push");
				suggestions.add("pull");
				suggestions.add("goto");
				suggestions.add("setup");
				this.argMatch(suggestions, args[0].trim());
			}

			if (args.length == 2) {
				if (args[0].trim().equalsIgnoreCase("push")) {
					suggestions.addAll(this.getEscapeRoomNames());

				} else if (args[0].trim().equalsIgnoreCase("setup")) {
					suggestions.add("rulesRoom");
					suggestions.add("startLocation");
					suggestions.add("winLocation");
					suggestions.add("looseLocation");
					this.argMatch(suggestions, args[1].trim());

				} else if (args[0].trim().equalsIgnoreCase("goto")) {
					suggestions.addAll(EscapeRoom._this().getWorldManager().getWorlds().keySet());
					this.argMatch(suggestions, args[1].trim());
				}
			}

			return suggestions;
		}

		return null;
	}
	
	/**
	 * Crude way to filter the current list of suggestions by what the user has typed so far for the given arguemnt
	 * @param suggestions Reference to the suggestions list
	 * @param arg The current arguemnt that has been typed so far
	 * No return value. Uses references.
	 */
	private void argMatch(ArrayList<String> suggestions, String arg) {
		if (arg.trim().length() == 0) { return; }
		Iterator<String> i = suggestions.iterator();
		while (i.hasNext()) {
			String opt = i.next();
			if (!opt.contains(arg)) {
				i.remove();
			}
		}
	}

	private List<String> getEscapeRoomNames() {
		List<String> names = new ArrayList<String>();
		List<EscapeRoomConfiguration> escs = EscapeRoom._this().getEscapeRoomConfigurtions();
		for (int i=0; i<escs.size(); i++) {
			names.add(escs.get(i).getName());
		}
		
		return names;
	  }
}
