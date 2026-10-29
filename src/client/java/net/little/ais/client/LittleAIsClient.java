package net.little.ais.client;

import net.fabricmc.api.ClientModInitializer;
import net.little.ais.client.event.ModEvents;

public class LittleAIsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ModEvents.initialize();
	}
}