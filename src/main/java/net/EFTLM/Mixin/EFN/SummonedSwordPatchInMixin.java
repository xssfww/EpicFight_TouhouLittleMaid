package net.EFTLM.Mixin.EFN;

import com.hm.efn.entity.effect.BlastSummonedSwordEntity;
import com.hm.efn.entity.effect.SummonedSwordEntity_In;
import com.hm.efn.entity.effect.SummonedSwordPatch_In;
import com.merlin204.avalon.entity.vfx.VFXEntityPatch;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;

@Mixin(value = SummonedSwordPatch_In.class, remap = false)
public abstract class SummonedSwordPatchInMixin {
    // 1.21.1: this mixin no longer extends VFXEntityPatch — Epic Fight 21's patch constructors need the
    // entity, and Mixin would then have to merge a constructor into the target. The parent API is reached
    // through a cast instead.
    @Inject(method = "attack", at = @At("HEAD"), cancellable = true, remap = false)
    private void InjectAttack(EpicFightDamageSource damageSource, Entity target, InteractionHand hand, CallbackInfoReturnable<AttackResult> cir) {
        VFXEntityPatch<SummonedSwordEntity_In> self = (VFXEntityPatch<SummonedSwordEntity_In>) (Object) this;
        LivingEntityPatch<?> ownerPatch = self.getOwnerPatch();
        if (ownerPatch == null || ownerPatch instanceof ServerPlayerPatch) return;
        if (!self.shouldUseOwnerAttack()) return;
        SummonedSwordEntity_In sword = self.getOriginal();
        LivingEntity owner = sword.getOwner();
        if (owner != null && target == owner) {
            cir.setReturnValue(AttackResult.missed(0.0F));
            return;
        }
        if (target instanceof SummonedSwordEntity_In otherSword && otherSword.getOwner() != null && otherSword.getOwner() == owner) {
            cir.setReturnValue(AttackResult.missed(0.0F));
            return;
        }
        if (target instanceof BlastSummonedSwordEntity otherBlast && otherBlast.getOwner() != null && otherBlast.getOwner() == owner) {
            cir.setReturnValue(AttackResult.missed(0.0F));
            return;
        }
        cir.setReturnValue(ownerPatch.attack(damageSource, target, hand));
    }
}
