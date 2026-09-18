package net.EFTLM.Mixin;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.EFTLM.EF.Animation.EFTLM_LivingMotions;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.client.world.capabilites.entitypatch.player.AbstractClientPlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
@Mixin(value = AbstractClientPlayerPatch.class,remap = false)
public abstract class AbstractClientPlayerPatchMixin <T extends AbstractClientPlayer> extends PlayerPatch<T> {
    @Shadow protected abstract boolean isMoving();
    @Inject(method = "updateMotion", at = @At("TAIL"))
    public void InjectAnimator(boolean considerInaction, CallbackInfo ci) {
        if (this.original.getFirstPassenger() instanceof EntityMaid) {
            if (this.isMoving()) {
                if (this.original.isCrouching()) {
                    this.currentLivingMotion = EFTLM_LivingMotions.HUG_SNEAK;
                } else if(this.original.isSprinting()) {
                    this.currentLivingMotion = EFTLM_LivingMotions.HUG_RUN;
                } else {
                    this.currentLivingMotion = EFTLM_LivingMotions.HUG_WALK;
                }
            } else {
                if (original.isCrouching()) {
                    this.currentLivingMotion = EFTLM_LivingMotions.HUG_KNEEL;
                } else {
                    this.currentLivingMotion = EFTLM_LivingMotions.HUG;
                }
            }
        }
    }
}

