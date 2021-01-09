package net.peacefulcraft.escaperoom.gamehandle.world;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.World;

import net.peacefulcraft.escaperoom.config.EscapeRoomConfiguration;
import net.peacefulcraft.escaperoom.gamehandle.EscapeRoomWorld;

public class WorldManager {
	
	private HashMap<String, EscapeRoomWorld> configuredWorlds;
		public EscapeRoomWorld getWorld(String name) { return this.configuredWorlds.get(name); }
		public Map<String, EscapeRoomWorld> getWorlds() { return Collections.unmodifiableMap(this.configuredWorlds); }

	public WorldManager() {
		this.configuredWorlds = new HashMap<String, EscapeRoomWorld>();
	}

	/**
	 * Loads the desired world, or creates one by that name if one does not already exist
	 * @param worldConfig The EscapeRoomConfiguration for that world - namley, 'name', must be set.
	 * @return The loaded EscapeRoomWorld
	 */
	public EscapeRoomWorld loadWorld(EscapeRoomConfiguration worldConfig) throws RuntimeException {
		if (this.configuredWorlds.containsKey(worldConfig.getName())) {
			throw new RuntimeException("Attempted to load world with name " + worldConfig.getWorldName() + ", but one already exists by that name.");
		}

		World newWorld = (new VoidWorldGenerator()).loadWorld(worldConfig.getWorldName());

		EscapeRoomWorld newESWorld = new EscapeRoomWorld(newWorld, worldConfig);
		this.configuredWorlds.put(worldConfig.getName(), newESWorld);
		return newESWorld;
	}

	public EscapeRoomWorld copyWorld(EscapeRoomWorld srcWorld, String worldDest) {
		return null;
	}
	
	public void deleteWorld(EscapeRoomWorld world) {

	}

	public void unloadWorld(EscapeRoomWorld world) {

	}
}
