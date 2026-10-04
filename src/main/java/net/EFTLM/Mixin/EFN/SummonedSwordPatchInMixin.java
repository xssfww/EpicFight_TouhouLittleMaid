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
public abstract class SummonedSwordPatchInMixin extends VFXEntityPatch<SummonedSwordEntity_In> {
    @Inject(method = "attack", at = @At("HEAD"), cancellable = true, remap = false)
    private void InjectAttack(EpicFightDamageSource damageSource, Entity target, InteractionHand hand, CallbackInfoReturnable<AttackResult> cir) {
        LivingEntityPatch<?> ownerPatch = this.getOwnerPatch();
        if (ownerPatch == null || ownerPatch instanceof ServerPlayerPatch) return;
        if (!this.shouldUseOwnerAttack()) return;
        SummonedSwordEntity_In sword = this.getOriginal();
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
