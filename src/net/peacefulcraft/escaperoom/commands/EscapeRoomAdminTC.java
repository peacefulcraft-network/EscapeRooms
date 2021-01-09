package net.peacefulcraft.escaperoom.commands;

import java.util.ArrayList;
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
			ArrayList<String> suggestions = new ArrayList<String>();

			if (args.length == 1) {
				suggestions.add("create");
				suggestions.add("delete");
				suggestions.add("unload");
				suggestions.add("load");
				suggestions.add("push");
				suggestions.add("pull");
				suggestions.add("goto");
			}

			if (args.length == 2) {
				if (args[0].trim().equalsIgnoreCase("push")) {
					suggestions.addAll(this.getEscapeRoomNames());
				} else if (args[0].trim().equalsIgnoreCase("goto")) {
					suggestions.addAll(EscapeRoom._this().getWorldManager().getWorlds().keySet());
				}
			}

			return suggestions;
		}

		return null;
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
