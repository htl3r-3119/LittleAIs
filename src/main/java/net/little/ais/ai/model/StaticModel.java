package net.little.ais.ai.model;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.little.ais.LittleAIs;

public class StaticModel {

    private static RedstoneSecurityModel REDSTONE_SECURITY_MODEL;

    public static RedstoneSecurityModel REDSTONE_SECURITY_MODEL() {
        return REDSTONE_SECURITY_MODEL;
    }

    public static void initialize() {
        LittleAIs.LOGGER.info("Initializing Static Models");
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            REDSTONE_SECURITY_MODEL = new RedstoneSecurityModel();
        });
    }

}
