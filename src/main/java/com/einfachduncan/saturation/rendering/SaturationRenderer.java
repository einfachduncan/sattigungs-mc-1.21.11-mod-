package com.einfachduncan.saturation.rendering;

import com.mojang.blaze3d.systems.RenderSystem;

/**
 * Verwaltet den aktuellen Sättigungsfaktor und wendet ihn beim Rendern an.
 */
public final class SaturationRenderer {
    private static float saturation = 1.0f;

    private SaturationRenderer() {
    }

    public static void setSaturation(float value) {
        saturation = Math.max(0.0f, Math.min(2.0f, value));
    }

    public static float getSaturation() {
        return saturation;
    }

    /**
     * Setzt vor dem Frame-Rendern den Shader-Farbfaktor.
     */
    public static void applyPreRender() {
        float grayscaleBlend = 1.0f - Math.max(0.0f, Math.min(1.0f, saturation));
        float colorScale = 1.0f - (grayscaleBlend * 0.35f);
        RenderSystem.setShaderColor(colorScale, colorScale, colorScale, 1.0f);
    }

    /**
     * Stellt den Standardzustand nach dem Frame wieder her.
     */
    public static void resetRenderState() {
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
