package net.little.ais.ai.model;

import net.little.ais.LittleAIs;

public class StaticModel {

    public static RedstoneSecurityModel REDSTONE_SECURITY_MODEL = new RedstoneSecurityModel();

    public static void initialize() {
        LittleAIs.LOGGER.info("Initializing Static Models");
    }

}
