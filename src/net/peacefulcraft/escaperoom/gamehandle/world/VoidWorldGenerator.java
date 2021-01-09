package net.peacefulcraft.escaperoom.gamehandle.world;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.generator.ChunkGenerator;

public class VoidWorldGenerator extends ChunkGenerator {

	/**
	 * Create or load the requested world. If wrold with this name already exists, it will be loaded. Otherwise, create.
	 * @param name Name of the world to load / create
	 * @return Reference to the new world
	 */
	public World loadWorld(String name) {
		WorldCreator creator = new WorldCreator(name);
		creator.generator(this);
		creator.generateStructures(false);
		creator.environment(Environment.NORMAL);
		return creator.createWorld();
	}

	/**
	 * Returns an empty ChunkData object to generate a void world
	 * Useful reference: https://bukkit.gamepedia.com/Developing_a_World_Generator_Plugin
	 * Other useful reference: https://www.youtube.com/watch?v=M_sd6-fuKwI
	 */
	@Override
	public ChunkData generateChunkData(World world, Random random, int chunkX, int ChunkZ, BiomeGrid biome) {
		return createChunkData(world);
	}
}