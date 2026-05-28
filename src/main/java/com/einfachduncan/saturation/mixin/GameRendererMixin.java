package com.einfachduncan.saturation.mixin;

import com.einfachduncan.saturation.rendering.SaturationRenderer;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hookt in den GameRenderer, um den Sättigungszustand pro Frame anzuwenden.
 */
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Inject(method = "render", at = @At("HEAD"))
    private void saturation$beforeRender(CallbackInfo ci) {
        SaturationRenderer.applyPreRender();
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void saturation$afterRender(CallbackInfo ci) {
        SaturationRenderer.resetRenderState();
    }
}
