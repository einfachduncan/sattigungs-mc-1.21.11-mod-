package com.einfachduncan.saturation.client.gui;

import com.einfachduncan.saturation.client.SaturationClient;
import com.einfachduncan.saturation.config.ConfigManager;
import com.einfachduncan.saturation.rendering.SaturationRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

import java.util.Locale;

/**
 * GUI zur Laufzeit-Anpassung der Sättigung und des Hotkeys.
 */
public class SaturationScreen extends Screen {
    private final Screen parent;
    private final ConfigManager configManager;
    private boolean waitingForKey;

    public SaturationScreen(Screen parent, ConfigManager configManager) {
        super(Text.translatable("screen.saturation.title"));
        this.parent = parent;
        this.configManager = configManager;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        SliderWidget saturationSlider = new SliderWidget(centerX - 100, centerY - 20, 200, 20, Text.empty(), configManager.getSaturation() / 2.0f) {
            @Override
            protected void updateMessage() {
                float currentSaturation = (float) (this.value * 2.0);
                String formatted = String.format(Locale.ROOT, "%.2f", currentSaturation);
                this.setMessage(Text.translatable("screen.saturation.slider", formatted));
            }

            @Override
            protected void applyValue() {
                float currentSaturation = (float) (this.value * 2.0);
                configManager.setSaturation(currentSaturation);
                configManager.save();
                SaturationRenderer.setSaturation(currentSaturation);
            }
        };
        saturationSlider.updateMessage();
        addDrawableChild(saturationSlider);

        addDrawableChild(ButtonWidget.builder(getHotkeyButtonText(), button -> {
            waitingForKey = true;
            button.setMessage(Text.translatable("screen.saturation.hotkey.waiting"));
        }).dimensions(centerX - 100, centerY + 10, 200, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Done"), button -> close())
                .dimensions(centerX - 100, centerY + 40, 200, 20)
                .build());
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (waitingForKey) {
            waitingForKey = false;
            SaturationClient.updateHotkey(keyCode);
            clearAndInit();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, height / 2 - 50, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    private Text getHotkeyButtonText() {
        Text keyName = InputUtil.fromKeyCode(configManager.getHotkey(), 0).getLocalizedText();
        return Text.translatable("screen.saturation.hotkey", keyName);
    }
}
