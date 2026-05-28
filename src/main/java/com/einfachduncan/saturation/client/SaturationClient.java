package com.einfachduncan.saturation.client;

import com.einfachduncan.saturation.client.event.KeyInputHandler;
import com.einfachduncan.saturation.client.gui.SaturationScreen;
import com.einfachduncan.saturation.config.ConfigManager;
import com.einfachduncan.saturation.rendering.SaturationRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

/**
 * Client-seitige Initialisierung der Mod inklusive GUI, Hotkey und Rendering.
 */
public class SaturationClient implements ClientModInitializer {
    private static ConfigManager configManager;
    private static KeyBinding openScreenKeyBinding;

    @Override
    public void onInitializeClient() {
        configManager = new ConfigManager();
        configManager.load();

        openScreenKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.saturation.toggle",
                InputUtil.Type.KEYSYM,
                configManager.getHotkey(),
                "category.saturation.main"
        ));

        KeyInputHandler.register(() -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.currentScreen instanceof SaturationScreen) {
                client.setScreen(null);
            } else {
                client.setScreen(new SaturationScreen(client.currentScreen, configManager));
            }
        });

        SaturationRenderer.setSaturation(configManager.getSaturation());
    }

    public static KeyBinding getOpenScreenKeyBinding() {
        return openScreenKeyBinding;
    }

    public static void updateHotkey(int keyCode) {
        configManager.setHotkey(keyCode);
        openScreenKeyBinding.setBoundKey(InputUtil.Type.KEYSYM.createFromCode(keyCode));
        KeyBinding.updateKeysByCode();
        configManager.save();
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }
}
