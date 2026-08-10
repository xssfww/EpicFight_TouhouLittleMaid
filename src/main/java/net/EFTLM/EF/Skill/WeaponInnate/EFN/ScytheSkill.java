package net.EFTLM.EF.Skill.WeaponInnate.EFN;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidAttackEvent;
import com.hm.efn.EFN;
import com.hm.efn.gameasset.EFNAnimations;
import com.hm.efn.gameasset.EFNExtraDamageInstance;
import com.hm.efn.gameasset.animations.EFNScytheAnimations;
import com.hm.efn.registries.EFNMobEffectRegistry;
import net.EFTLM.EF.API.Event.MaidHurtTargetEvent;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillBuilder;
import net.EFTLM.EF.Skill.WeaponInnate.WeaponInnateSkill;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageTypeTags;
import yesman.epicfight.world.damagesource.StunType;
public class ScytheSkill extends WeaponInnateSkill {
    private float HEAL_RATIO;
    private float HEAL_RATIO_END;
    public ScytheSkill(MaidSkillBuilder<? extends MaidSkill> builder) {
        super(builder);
    }
    @Override
    public void setParams(CompoundTag parameters) {
        super.setParams(parameters);
        HEAL_RATIO = parameters.getFloat("heal_ratio");
        HEAL_RATIO_END = parameters.getFloat("heal_ratio_end");
    }
    @Override
    public void onHurtTargetPre(MaidHurtTargetEvent.Pre event) {
        super.onHurtTargetPre(event);
        if (!(event.getSource() instanceof EpicFightDamageSource source)) return;
        LivingEntity target = event.getTarget();
        MaidPatch<?> maid = event.getMaidPatch();
        LivingEntity maidEntity = maid.getOriginal();
        AnimationManager.AnimationAccessor<? extends StaticAnimation> animation = source.getAnimation();
        if (animation == EFNScytheAnimations.SCYTHE_SCARLET_END) {
            source.addRuntimeTag(EpicFightDamageTypeTags.BYPASS_DODGE);
            source.addRuntimeTag(EpicFightDamageTypeTags.GUARD_PUNCTURE);
            source.addRuntimeTag(EpicFightDamageTypeTags.UNBLOCKALBE);
            source.addRuntimeTag(EpicFightDamageTypeTags.FINISHER);
            source.addRuntimeTag(EpicFightDamageTypeTags.IS_MELEE);
            source.addRuntimeTag(EpicFightDamageTypeTags.COUNTER);
            AnimationPlayer animPlayer = maid.getAnimator().getPlayerFor(null);
            if (animPlayer != null) {
                AttackAnimation attackAnim = (AttackAnimation) animation.get();
                int length = attackAnim.phases.length;
                int phase = attackAnim.getPhaseOrderByTime(animPlayer.getElapsedTime());
                if (phase != length - 1) {
                    source.addExtraDamage(EFNExtraDamageInstance.EX_DAMAGE_BY_COB_EFFECT.create(0.064F, 0.0F));
                } else {
                    source.addExtraDamage(EFNExtraDamageInstance.EX_DAMAGE_BY_COB_EFFECT.create(0.111999996F, 0.003F));
                }
            }
        }
        if (animation == EFNScytheAnimations.SCYTHE_HARVEST) {
            boolean hasCurse = target.hasEffect(EFNMobEffectRegistry.CURSE_OF_BLOOD.get());
            boolean hasBlessing = maidEntity.hasEffect(EFNMobEffectRegistry.BLOOD_BLESSINGS.get());
            if (!hasCurse && !hasBlessing) {
                source.attachDamageModifier(ValueModifier.multiplier(0.25F));
            } else {
                source.attachDamageModifier(ValueModifier.multiplier(1.25F));
            }
        }
    }
    @Override
    public void onHurtTargetPost(MaidHurtTargetEvent.Post event) {
        super.onHurtTargetPost(event);
        LivingEntity target = event.getTarget();
        MaidPatch<?> maid = event.getMaidPatch();
        if (!(event.getSource() instanceof EpicFightDamageSource efSource)) return;
        LivingEntity maidEntity = maid.getOriginal();
        AnimationManager.AnimationAccessor<? extends StaticAnimation> animation = efSource.getAnimation();
        maidEntity.heal(event.getAmount() * HEAL_RATIO);
        if (animation == EFNScytheAnimations.SCYTHE_SCARLET_END) {
            MobEffectInstance curse = target.getEffect(EFNMobEffectRegistry.CURSE_OF_BLOOD.get());
            MobEffectInstance blessing = maidEntity.getEffect(EFNMobEffectRegistry.BLOOD_BLESSINGS.get());
            int amplifier = 0;
            if (curse != null) {
                amplifier = curse.getAmplifier() + 1;
            } else if (blessing != null) {
                amplifier = blessing.getAmplifier() + 1;
            }
            if (amplifier > 0) {
                maidEntity.heal(maidEntity.getMaxHealth() * HEAL_RATIO_END * amplifier);
                Vec3 pos = maidEntity.position().add(0.0, maidEntity.getBbHeight() / 2.0, 0.0);
                if (maidEntity.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.END_ROD, pos.x, pos.y, pos.z, 10, 0.35, 1.0, 0.35, 1.0);
                }
                AnimationPlayer animPlayer = maid.getAnimator().getPlayerFor(null);
                if (animPlayer != null) {
                    AttackAnimation attackAnim = (AttackAnimation) animation.get();
                    int phaseIndex = attackAnim.getPhaseOrderByTime(animPlayer.getElapsedTime());
                    if (phaseIndex == attackAnim.phases.length - 1) {
                        if (curse != null) {
                            target.removeEffect(EFNMobEffectRegistry.CURSE_OF_BLOOD.get());
                        } else {
                            maidEntity.removeEffect(EFNMobEffectRegistry.BLOOD_BLESSINGS.get());
                        }
                    }
                }
            }
            return;
        }
        boolean canBeAffected = target.canBeAffected(new MobEffectInstance(EFNMobEffectRegistry.CURSE_OF_BLOOD.get()));
        MobEffect effectToApply = canBeAffected ? EFNMobEffectRegistry.CURSE_OF_BLOOD.get() : EFNMobEffectRegistry.BLOOD_BLESSINGS.get();
        LivingEntity effectCarrier = canBeAffected ? target : maidEntity;
        if (animation == EFNScytheAnimations.SCYTHE_AUTO1 ||
                animation == EFNScytheAnimations.SCYTHE_AUTO3 ||
                animation == EFNScytheAnimations.SCYTHE_AUTO5 ||
                animation == EFNAnimations.CRIMSON_SLASH ||
                animation == EFNAnimations.CRIMSON_SLASH_ANTI) {
            int level = -1;
            MobEffectInstance existing = effectCarrier.getEffect(effectToApply);
            if (existing != null) {
                level = existing.getAmplifier();
            }
            level = Mth.clamp(level + 1, 0, 255);
            effectCarrier.addEffect(new MobEffectInstance(effectToApply, 400, level));
        } else if (animation == EFNScytheAnimations.SCYTHE_DASH ||
                animation == EFNScytheAnimations.SCYTHE_AIR_SLASH ||
                animation == EFNScytheAnimations.SCYTHE_HARVEST) {
            MobEffectInstance existing = effectCarrier.getEffect(effectToApply);
            boolean hadEffect = existing != null;
            if (hadEffect) {
                int newAmp = existing.getAmplifier() - 1;
                if (newAmp >= 0) {
                    effectCarrier.forceAddEffect(new MobEffectInstance(effectToApply, existing.getDuration(), newAmp), target);
                } else {
                    effectCarrier.removeEffect(effectToApply);
                }
            }
            if (hadEffect) {
                if (animation == EFNScytheAnimations.SCYTHE_DASH) {
                    int levelx = -1;
                    MobEffectInstance dmgRed = maidEntity.getEffect(EFNMobEffectRegistry.DAMAGE_REDUCTION.get());
                    if (dmgRed != null) levelx = dmgRed.getAmplifier();
                    maidEntity.addEffect(new MobEffectInstance(EFNMobEffectRegistry.DAMAGE_REDUCTION.get(), 300, Mth.clamp(levelx + 25, 0, 75)));
                } else if (animation == EFNScytheAnimations.SCYTHE_AIR_SLASH) {
                    int levelx = -1;
                    MobEffectInstance atkSpeed = maidEntity.getEffect(EFNMobEffectRegistry.ATTACK_SPEED_INCREASE.get());
                    if (atkSpeed != null) levelx = atkSpeed.getAmplifier();
                    maidEntity.addEffect(new MobEffectInstance(EFNMobEffectRegistry.ATTACK_SPEED_INCREASE.get(), 300, Mth.clamp(levelx + 15, 0, 44)));
                } else if (animation == EFNScytheAnimations.SCYTHE_HARVEST) {
                    int levelx = -1;
                    MobEffectInstance atkDmg = maidEntity.getEffect(EFNMobEffectRegistry.ATTACK_DAMAGE_INCREASE.get());
                    if (atkDmg != null) levelx = atkDmg.getAmplifier();
                    maidEntity.addEffect(new MobEffectInstance(EFNMobEffectRegistry.ATTACK_DAMAGE_INCREASE.get(), 300, Mth.clamp(levelx + 15, 0, 89)));
                    maidEntity.addEffect(new MobEffectInstance(EFNMobEffectRegistry.SIN_STUN_IMMUNITY.get(), 60));
                    target.level().playSound(null, target.blockPosition(), EpicFightSounds.BLADE_RUSH_FINISHER.get(), SoundSource.HOSTILE);
                }
            }
        }
    }
    @Override
    public void onMaidAttack(MaidAttackEvent event, MaidPatch<?> MaidPatch) {
        AnimationPlayer animation = MaidPatch.getAnimator().getPlayerFor(null);
        if (animation == null) return;
        if (MaidPatch.getOriginal().hasEffect(EFNMobEffectRegistry.DAMAGE_REDUCTION.get()) && animation.getRealAnimation().equals(EFNScytheAnimations.SCYTHE_HARVEST)) {
            if (event.getSource() instanceof EpicFightDamageSource EFSource) {
                EFSource.setStunType(StunType.NONE);
            }
        }
        if (animation.getRealAnimation().equals(EFNScytheAnimations.SCYTHE_SCARLET_END)) {
            event.setCanceled(true);
        }
    }
    @Override
    public ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(EFN.MODID, "textures/item/crimson_moon.png");
    }
}
