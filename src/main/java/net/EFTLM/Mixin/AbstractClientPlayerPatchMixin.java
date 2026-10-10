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

@Mixin(value = AbstractClientPlayerPatch.class, remap = false)
public abstract class AbstractClientPlayerPatchMixin {
    // 1.21.1: Epic Fight 21 requires the original entity in the patch constructor, so this mixin can
    // neither extend PlayerPatch nor @Shadow the fields that PlayerPatch/EntityPatch declare
    // (Mixin only resolves @Shadow members declared in the target class itself — it failed at runtime
    // with "@Shadow field original was not located in the target class").
    // Instead the mixin casts itself and uses Epic Fight's own accessors / public field.
    @Shadow(remap = false) protected abstract boolean isMoving();

    @Inject(method = "updateMotion", at = @At("TAIL"))
    public void InjectAnimator(boolean considerInaction, CallbackInfo ci) {
        AbstractClientPlayerPatch<?> self = (AbstractClientPlayerPatch<?>) (Object) this;
        AbstractClientPlayer original = self.getOriginal();
        if (original.getFirstPassenger() instanceof EntityMaid) {
            if (this.isMoving()) {
                if (original.isCrouching()) {
                    self.currentLivingMotion = EFTLM_LivingMotions.HUG_SNEAK;
                } else if(original.isSprinting()) {
                    self.currentLivingMotion = EFTLM_LivingMotions.HUG_RUN;
                } else {
                    self.currentLivingMotion = EFTLM_LivingMotions.HUG_WALK;
                }
            } else {
                if (original.isCrouching()) {
                    self.currentLivingMotion = EFTLM_LivingMotions.HUG_KNEEL;
                } else {
                    self.currentLivingMotion = EFTLM_LivingMotions.HUG;
                }
            }
        }
    }
}
