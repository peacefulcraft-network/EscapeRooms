package net.peacefulcraft.escaperoom.gamehandle.world;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.World;

import net.peacefulcraft.escaperoom.EscapeRoom;
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

	/**
	 * Copes and existing world to the requested path and loads it into memory.
	 * @param srcWorld The source world folder to copy
	 * @param worldDest The name for the new world
	 * @return The loaded EscapeRoomWorld
	 */
	public EscapeRoomWorld copyWorld(String srcWorld, String worldDest) {
		return null;
	}
	
	/**
	 * Unloads and deletes the requested world.
	 * WARNING: It is possible to delete non-world folders in the server data folder (ie /plugins)
	 * by passing erroronious or malicious input to this function. This function should only be granted to privledged users.
	 * @param name Name of the world to delete
	 */
	public void deleteWorld(String name) throws RuntimeException {
		EscapeRoomWorld targetWorld = this.configuredWorlds.get(name);
		if (targetWorld != null) {
			this.unloadWorld(targetWorld);
		} else {
			// Make sure user can't delete main server worlds
			if (EscapeRoom._this().getServer().getWorld(name.replaceAll(" ", "_")) != null) {
				throw new RuntimeException("World " + name + " is not a valid escape room world. Delete request aborted.");
			}
		}

		File worldFolder = new File(EscapeRoom._this().getDataFolder().getParentFile().getParentFile(), name.replaceAll(" ", "_"));
		if (!worldFolder.isDirectory()) {
			throw new RuntimeException("Error. No world with name " + name + " exists on this server.");
		}
		
		if (!worldFolder.delete()) {
			if (worldFolder.exists()) {
				throw new RuntimeException("Error deleting world " + name + ". This is most likley a file permissions issue. Please contact an admin.");
			}
		}
	}

	/**
	 * Unloads a world from active memory, saving it to disk
	 * @param world The EscapeRoomWorld to unload
	 * @throws RuntimeException If something goes wrong in the unload process
	 */
	public void unloadWorld(EscapeRoomWorld world) throws RuntimeException {
		world.unloadWorld(); // can throw runtime exception
		this.configuredWorlds.remove(world.getConfig().getName());
	}
}
