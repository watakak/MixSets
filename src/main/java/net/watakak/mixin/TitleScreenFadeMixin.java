package net.watakak.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import net.watakak.MixSets;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenFadeMixin {
    @Shadow @Final @Mutable private boolean doBackgroundFade;

    @Inject(method = "<init>(Z)V", at = @At("RETURN"))
    private void onInit(boolean doBackgroundFade, CallbackInfo ci) {
        if (MixSets.getConfig().isDisableFadeTransitions()) {
            this.doBackgroundFade = false;
        }
    }
}