package net.watakak.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.watakak.MixSets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMouseMovingMixin {
    @Inject(method = "onPassengerLookAround", at = @At("HEAD"))
    private void applyPlayerFacingOnPassengerLookAround(Entity passenger, CallbackInfo ci) {
        if (MixSets.getConfig().isEntityMouseMovingFix()) {
            if (!(passenger instanceof ClientPlayerEntity)) return;
            Entity vehicle = (Entity) (Object) this;

            float f = MathHelper.wrapDegrees(vehicle.getYaw() - vehicle.getYaw());
            float g = MathHelper.clamp(f, -180f, 180f);
            passenger.prevYaw += g - f;
            passenger.setYaw(passenger.getYaw() + g - f);
            passenger.setHeadYaw(passenger.getYaw());
        }
    }
}