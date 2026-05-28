package com.einfachduncan.saturation;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hauptklasse der Mod und zentraler Einstiegspunkt für gemeinsame Initialisierung.
 */
public class SaturationMod implements ModInitializer {
    public static final String MOD_ID = "saturation";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing {}", MOD_ID);
    }
}
