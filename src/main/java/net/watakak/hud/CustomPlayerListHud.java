package net.watakak.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.watakak.MixSets;
import net.watakak.mixin.TabPlayerListInvokerMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;

public final class CustomPlayerListHud {
    private static final int PING_TEXT_RENDER_OFFSET = -13;
    private static final int PING_BARS_WIDTH = 11;

    public static void renderPingDisplay(MinecraftClient client, PlayerListHud hud, DrawContext context, int width, int x, int y, PlayerListEntry player) {
        String pingString = String.format("%dms", player.getLatency());
        int pingStringWidth = client.textRenderer.getWidth(pingString);
        int pingTextColor = PingColors.getColor(player.getLatency());
        int textX = width + x - pingStringWidth + PING_TEXT_RENDER_OFFSET;

        if (MixSets.getConfig().isBetterPingDisplay()) {
            textX += PING_BARS_WIDTH;
        }

        // Draw the ping text for the given player
        if (MixSets.getConfig().isBetterPingDisplay()) {
            context.drawTextWithShadow(client.textRenderer, pingString, textX, y, pingTextColor);
        }

        if (!MixSets.getConfig().isBetterPingDisplay()) {
            ((TabPlayerListInvokerMixin) hud).invokeRenderLatencyIcon(context, width, x, y, player);
        }
        else {
            // If we don't render ping bars, we need to reset the render system color so the rest
            // of the player list renders properly
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}