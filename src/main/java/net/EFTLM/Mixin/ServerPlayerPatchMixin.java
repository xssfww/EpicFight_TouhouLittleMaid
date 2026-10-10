package net.EFTLM.Mixin;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.EFTLM.EF.Capability.MaidPatch;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

@Mixin(value = ServerPlayerPatch.class, remap = false)
public abstract class ServerPlayerPatchMixin {
    // 1.21.1: no longer extends PlayerPatch — the Epic Fight 21 patch constructor requires the
    // original player, and this mixin only needs the injected method.
    @Inject(method = "tryHurt", at = @At("HEAD"), cancellable = true)
    public void InjectTryHurt(DamageSource damageSource, float amount, CallbackInfoReturnable<AttackResult> cir) {
        if (damageSource.getEntity() instanceof EntityMaid Maid) {
            if (!Maid.level().isClientSide()) {
                MaidPatch<?> MaidPatch = EpicFightCapabilities.getEntityPatch(Maid, MaidPatch.class);
                if (MaidPatch != null) {
                    PlayerPatch<?> OwnerPatch = MaidPatch.getOwnerPatch();
                    if (OwnerPatch != null) {
                        if (OwnerPatch.equals(this)) {
                            cir.setReturnValue(AttackResult.missed(amount));
                        }
                    }
                }
            }
        }
    }
}
