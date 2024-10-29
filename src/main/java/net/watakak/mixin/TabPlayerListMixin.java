package net.watakak.mixin;

import net.watakak.MixSets;
import net.watakak.hud.CustomPlayerListHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerListHud.class)
public abstract class TabPlayerListMixin {
    @Unique
    @Final
    private static final int PLAYER_SLOT_EXTRA_WIDTH = 24;

    @Shadow
    @Final
    private MinecraftClient client;

    @ModifyConstant(method = "render", constant = @Constant(intValue = 13))
    private int modifySlotWidthConstant(int original) {
        if (MixSets.getConfig().isBetterPingDisplay()) {
            return original + PLAYER_SLOT_EXTRA_WIDTH;
        }
        return original;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/hud/PlayerListHud.renderLatencyIcon(Lnet/minecraft/client/gui/DrawContext;IIILnet/minecraft/client/network/PlayerListEntry;)V"))
    private void redirectRenderLatencyIconCall(PlayerListHud instance, DrawContext context, int width, int x, int y, @NotNull PlayerListEntry entry) {
        CustomPlayerListHud.renderPingDisplay(client, instance, context, width, x, y, entry);
    }
}