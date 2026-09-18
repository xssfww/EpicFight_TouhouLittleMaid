package net.EFTLM.EF.Skill.Guard;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidAttackEvent;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTickEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.EFTLM.EF.API.Event.MaidSkillInitEvent;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Compat.EFNCompat;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillBuilder;
import net.EFTLM.EF.Skill.MaidSkillDataManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageTypeTags;
public class BladeClash extends MaidSkill {
    public static final MaidSkillDataManager.SkillDataKey<Float> CLASH_PENALTY =
            MaidSkillDataManager.SkillDataKey.createDataKey(MaidSkillDataManager.SkillDataKey.FLOAT);
    public static final MaidSkillDataManager.SkillDataKey<Integer> CLASH_RESTORE_COUNTER =
            MaidSkillDataManager.SkillDataKey.createDataKey(MaidSkillDataManager.SkillDataKey.INTEGER);
    public BladeClash(MaidSkillBuilder<? extends MaidSkill> builder) {
        super(builder);
    }
    @Override
    public void onInit(MaidSkillInitEvent event) {
        event.registerData(this,CLASH_PENALTY,0F);
        event.registerData(this,CLASH_RESTORE_COUNTER,0);
    }
    @Override
    public void onMaidTick(MaidTickEvent event,MaidPatch<?> patch) {
        EntityMaid Maid = event.getMaid();
        MaidPatch<?> MaidPatch = EpicFightCapabilities.getEntityPatch(Maid, MaidPatch.class);
        if (Maid.level() instanceof ServerLevel) {
            if (MaidPatch != null) {
                if (MaidPatch.getDataValue(this,CLASH_RESTORE_COUNTER) != null) {
                    int Counter = MaidPatch.getDataValue(this,CLASH_RESTORE_COUNTER);
                    if (MaidPatch.getDataValue(this,CLASH_PENALTY) != null) {
                        float Penalty = MaidPatch.getDataValue(this,CLASH_PENALTY);
                        if (Penalty > 0.0F) {
                            if (Maid.tickCount - Counter > 40) {
                                MaidPatch.setData(this,CLASH_PENALTY, 0.0F);
                            }
                        }
                    }
                }
            }
        }
    }
    @Override
    public void onMaidAttack(MaidAttackEvent event,MaidPatch<?> MaidPatch) {
        EntityMaid Maid = event.getMaid();
        DamageSource Source = event.getSource();
        PlayerPatch<?> OwnerPatch = MaidPatch.getOwnerPatch();
        if (Source.getEntity() != null) {
            LivingEntityPatch<?> SourcePatch = EpicFightCapabilities.getEntityPatch(Source.getEntity(), LivingEntityPatch.class);
            int phaseLevel = MaidPatch.getEntityState().getLevel();
            if (OwnerPatch != null && OwnerPatch.getOriginal().equals(Source.getEntity())) {
                return;
            }
            if (EFNCompat.isSpecialAnimation(MaidPatch)) {
                return;
            }
            if (phaseLevel > 0 && phaseLevel < 3 && this.isFrontAttack(Source, Maid) && this.isBlockableSource(Source)) {
                float impact = 0.5F;
                float knockback = 0.1F;
                if (Source instanceof EpicFightDamageSource EFSource) {
                    impact = EFSource.calculateImpact();
                    knockback += Math.min(impact * 0.1F, 1.0F);
                }
                if (MaidPatch.getDataValue(this, CLASH_PENALTY) != null) {
                    float penalty = MaidPatch.getDataValue(this, CLASH_PENALTY);
                    float consumeAmount = penalty * impact;
                    if (MaidPatch.hasStamina(consumeAmount)) {
                        if (Source.getSourcePosition() != null) {
                            MaidPatch.knockBackEntity(Source.getSourcePosition(), knockback);
                            if (SourcePatch != null) {
                                SourcePatch.knockBackEntity(Maid.position(), knockback);
                            }
                        }
                        MaidPatch.playSound(EpicFightSounds.CLASH.get(), -0.05F, 0.1F);
                        MaidPatch.setStamina(MaidPatch.getStamina() - consumeAmount);
                        MaidPatch.setData(this, CLASH_PENALTY, penalty + 0.1F);
                        MaidPatch.setData(this, CLASH_RESTORE_COUNTER, Maid.tickCount);
                        EpicFightParticles.HIT_BLUNT.get().spawnParticleWithArgument((ServerLevel) Maid.level(), HitParticleType.FRONT_OF_EYES, HitParticleType.ZERO, Maid, Source.getDirectEntity());
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
    public boolean isBlockableSource(DamageSource damageSource) {
        return !damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !damageSource.is(EpicFightDamageTypeTags.UNBLOCKALBE) && !damageSource.is(DamageTypeTags.BYPASSES_ARMOR) && !damageSource.is(DamageTypeTags.IS_EXPLOSION) && !damageSource.is(DamageTypes.MAGIC) && !damageSource.is(DamageTypeTags.IS_FIRE);
    }
    public boolean isFrontAttack(DamageSource damageSource, Entity entity) {
        Vec3 sourceLocation = damageSource.getSourcePosition();
        Vec3 viewVector = entity.getViewVector(1.0F);
        if (sourceLocation != null) {
            Vec3 toSourceLocation = sourceLocation.subtract(entity.position()).normalize();
            return toSourceLocation.dot(viewVector) > 0.0;
        }
        return false;
    }
}
