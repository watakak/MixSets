package net.watakak.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import net.watakak.MixSets;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow
    @Final
    private static Identifier ICONS;

    @Redirect(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 0))
    private void drawTextureRedirect(DrawContext instance, Identifier texture, int x, int y, int u, int v, int width, int height) {
        float scaleFactor = (float) MinecraftClient.getInstance().getWindow().getScaleFactor();
        float scaledCenterX = (MinecraftClient.getInstance().getWindow().getFramebufferWidth() / scaleFactor) / 2f;
        float scaledCenterY = (MinecraftClient.getInstance().getWindow().getFramebufferHeight() / scaleFactor) / 2f;

        float moveFactor = 8.0f;
        if (MixSets.getConfig().isCrosshairPositionFix()) {
            moveFactor = 7.35f;
        }

        instance.centered_crosshair$drawTexture(ICONS, scaledCenterX - moveFactor, scaledCenterY - moveFactor, 15, 15);
    }
}