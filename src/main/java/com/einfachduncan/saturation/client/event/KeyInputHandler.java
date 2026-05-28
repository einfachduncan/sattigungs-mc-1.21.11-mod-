package com.einfachduncan.saturation.client.event;

import com.einfachduncan.saturation.client.SaturationClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

/**
 * Verarbeitet Tastatureingaben und öffnet/schließt bei Bedarf den GUI-Screen.
 */
public final class KeyInputHandler {
    private KeyInputHandler() {
    }

    public static void register(Runnable onHotkeyPressed) {
        ClientTickEvents.END_CLIENT_TICK.register(KeyInputHandler::handleTick);
        HOTKEY_CALLBACK = onHotkeyPressed;
    }

    private static Runnable HOTKEY_CALLBACK;

    private static void handleTick(MinecraftClient client) {
        if (HOTKEY_CALLBACK == null || client.player == null) {
            return;
        }

        while (SaturationClient.getOpenScreenKeyBinding().wasPressed()) {
            HOTKEY_CALLBACK.run();
        }
    }
}
