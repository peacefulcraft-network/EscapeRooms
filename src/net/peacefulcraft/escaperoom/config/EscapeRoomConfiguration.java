package net.peacefulcraft.escaperoom.config;

import org.bukkit.Location;

public class EscapeRoomConfiguration extends Configuration {
	public EscapeRoomConfiguration(String name) {
		super("escaperoom.yml", "escaperooms/" + name + ".yml");
		this.name = name;
		this.config.set("name", this.name);
		this.saveConfiguration();
		this.loadValues();
	}

	private void loadValues() {
		this.enabled = this.config.getBoolean("enabled");
		this.devLoadWorld = this.config.getBoolean("dev_load_world");
		this.rulesRoomLocation = this.config.getLocation("rules_room_location");
		this.startLocation = this.config.getLocation("start_location");
	}

	private String name;
		public String getName() { return name; }
		public String getWorldName() { return name.replaceAll(" ", "_"); }

	private Boolean devLoadWorld;
		public Boolean devLoadWorld() { return devLoadWorld; }
		public void setDevLoadWorld(Boolean autoLoad) { this.devLoadWorld = autoLoad; }

	private Boolean enabled;
		public Boolean isEnabled() { return this.enabled; }
		public void setEnabled(Boolean enabled) {
			this.enabled = false;
			this.saveConfiguration();
		}

	private Location rulesRoomLocation;
		public Location getRulesRoomLocation() { return this.rulesRoomLocation; }
		public void setRulesRoomLocation(Location location) {
			this.rulesRoomLocation = location;
			this.config.set("rules_room_location", location);
			this.saveConfiguration();
		}
	
	private Location startLocation;
		public Location getStartLocation() { return this.startLocation; }
		public void setStartLocation(Location location) {
			this.startLocation = location;
			this.config.set("start_location", location);
			this.saveConfiguration();
		}

	private Location looseLocation;
		public Location getLooseLocation() { return this.looseLocation; }
		public void setLooseLocation(Location location) {
			this.looseLocation = location;
			this.config.set("loose_location", location);
			this.saveConfiguration();
		}

	private Location winLocation;
		public Location getWinLocation() { return this.winLocation; }
		public void setWinLocation(Location location) {
			this.winLocation = location;
			this.config.set("win_location", location);
			this.saveConfiguration();
		}
}
