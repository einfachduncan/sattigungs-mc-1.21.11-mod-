package com.einfachduncan.saturation.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.util.InputUtil;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Verwaltet die Mod-Konfiguration (Sättigung und Hotkey) als JSON-Datei.
 */
public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("saturation.json");

    private ModConfig config = new ModConfig();

    /**
     * Lädt die Konfiguration aus der Datei oder erstellt Standardwerte.
     */
    public void load() {
        if (!Files.exists(CONFIG_PATH)) {
            save();
            return;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
            ModConfig loaded = GSON.fromJson(reader, ModConfig.class);
            if (loaded != null) {
                config = loaded;
            }
        } catch (IOException ignored) {
            config = new ModConfig();
        }

        config.saturation = clamp(config.saturation);
    }

    /**
     * Speichert die aktuelle Konfiguration als JSON-Datei.
     */
    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(config, writer);
            }
        } catch (IOException ignored) {
            // No-op: Wenn Speichern fehlschlägt, bleiben Werte nur im Speicher erhalten.
        }
    }

    public float getSaturation() {
        return clamp(config.saturation);
    }

    public void setSaturation(float saturation) {
        config.saturation = clamp(saturation);
    }

    public int getHotkey() {
        return config.hotkey;
    }

    public void setHotkey(int hotkey) {
        config.hotkey = hotkey;
    }

    private static float clamp(float value) {
        return Math.max(0.0f, Math.min(2.0f, value));
    }

    /**
     * Datenmodell für die JSON-Struktur.
     */
    private static final class ModConfig {
        private float saturation = 1.0f;
        private int hotkey = InputUtil.GLFW_KEY_S;
    }
}
