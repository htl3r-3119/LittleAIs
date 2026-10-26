package net.little.ais;

import net.fabricmc.api.ModInitializer;

import net.little.ais.block.ModBlocks;
import net.little.ais.block.entity.ModBlockEntities;
import net.little.ais.itemgroups.ModItemGroups;
import net.little.ais.items.ModItems;
import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LittleAIs implements ModInitializer {
	public static final String MOD_ID = "little_ais";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Initializing LittleAIs");

		ModItems.initialize();
		ModItemGroups.initialize();
		ModBlocks.initialize();
		ModBlockEntities.initialize();
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
