package net.EFTLM.EF.Skill.WeaponInnate.EFN;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidHurtEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.hm.efn.gameasset.EFNExtraDamageInstance;
import com.hm.efn.item.custom.BroadBladeItem;
import com.hm.efn.registries.EFNMobEffectRegistry;
import com.hm.efn.skill.weapon_innate.BroadBladeInnate;
import net.EFTLM.EF.API.Event.MaidChangeItemEvent;
import net.EFTLM.EF.API.Event.MaidHurtTargetEvent;
import net.EFTLM.EF.API.Event.MaidKilledEvent;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillBuilder;
import net.EFTLM.EF.Skill.WeaponInnate.WeaponInnateSkill;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageSources;
import yesman.epicfight.world.damagesource.EpicFightDamageTypeTags;
import yesman.epicfight.world.damagesource.StunType;
import java.util.List;
public class BroadBladeSkill extends WeaponInnateSkill {
    public BroadBladeSkill(MaidSkillBuilder<? extends MaidSkill> builder) {
        super(builder);
    }
    @Override
    public void MaidHurt(MaidHurtEvent event) {
        MaidPatch<?> MaidPatch = EpicFightCapabilities.getEntityPatch(event.getMaid(), MaidPatch.class);
        if (MaidPatch != null) {
            if (MaidPatch.getOriginal().level() instanceof ServerLevel) {
                if ((MaidPatch.getEntityState().getLevel() == 1)) {
                    if (event.getSource() instanceof EpicFightDamageSource EFSource) {
                        EFSource.setStunType(StunType.NONE);
                    }
                }
            }
        }
    }
    @Override
    public void MaidHurtTargetPre(MaidHurtTargetEvent.Pre event) {
        MaidPatch<?> MaidPatch = event.getMaidPatch();
        EntityMaid Maid = MaidPatch.getOriginal();
        LivingEntity target = event.getTarget();
        RandomSource rand = Maid.level().getRandom();
        if (event.getSource() instanceof EpicFightDamageSource EFSource) {
            if (rand.nextFloat() < 0.3F && !EFSource.is(BroadBladeInnate.EFN_BROADBLADE_EXTRA_DAMAGE)) {
                EFSource.addRuntimeTag(EpicFightDamageTypeTags.BYPASS_DODGE);
                EFSource.addRuntimeTag(EpicFightDamageTypeTags.GUARD_PUNCTURE);
                EFSource.addRuntimeTag(EpicFightDamageTypeTags.UNBLOCKALBE);
                EFSource.addRuntimeTag(EpicFightDamageTypeTags.FINISHER);
                EFSource.addRuntimeTag(DamageTypeTags.BYPASSES_ARMOR);
                EFSource.addRuntimeTag(DamageTypeTags.BYPASSES_INVULNERABILITY);
                EFSource.addRuntimeTag(DamageTypeTags.BYPASSES_RESISTANCE);
                EFSource.addRuntimeTag(DamageTypeTags.BYPASSES_ENCHANTMENTS);
                EFSource.addRuntimeTag(DamageTypeTags.BYPASSES_EFFECTS);
                EFSource.addRuntimeTag(DamageTypeTags.BYPASSES_COOLDOWN);
                EFSource.addRuntimeTag(DamageTypeTags.BYPASSES_SHIELD);
                float extraDamage = target.getMaxHealth() * 0.05F;
                DamageSource damageSource = EpicFightDamageSources.mobAttack(Maid).setAnimation(null)
                        .addRuntimeTag(BroadBladeInnate.EFN_BROADBLADE_EXTRA_DAMAGE).setInitialPosition(Maid.position())
                        .setStunType(StunType.NONE).setBaseImpact(0.0F)
                        .addRuntimeTag(DamageTypeTags.BYPASSES_ARMOR).addRuntimeTag(DamageTypeTags.BYPASSES_INVULNERABILITY)
                        .addRuntimeTag(DamageTypeTags.BYPASSES_COOLDOWN).addRuntimeTag(DamageTypeTags.BYPASSES_RESISTANCE)
                        .addRuntimeTag(DamageTypeTags.BYPASSES_ENCHANTMENTS).addRuntimeTag(DamageTypeTags.BYPASSES_EFFECTS)
                        .addRuntimeTag(DamageTypeTags.BYPASSES_SHIELD).addRuntimeTag(EpicFightDamageTypeTags.FINISHER);
                target.hurt(damageSource, extraDamage);
                target.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 0.8F, 1.2F);
                Maid.playSound(SoundEvents.WITHER_HURT, 0.2F, 0.4F + 0.25F * rand.nextFloat());
                if (Maid.level() instanceof ServerLevel Level) {
                    Level.sendParticles(ParticleTypes.ENCHANTED_HIT, target.getX(), target.getY() + 1.0, target.getZ(), 20, 0.5, 0.5, 0.5, 0.1);
                    EpicFightParticles.BLADE_RUSH_SKILL.get().spawnParticleWithArgument(Level, null, null, target, Maid);
                }
            }
        }
    }
    @Override
    public void MaidHurtTargetPost(MaidHurtTargetEvent.Post event) {
        MaidPatch<?> MaidPatch = event.getMaidPatch();
        EntityMaid Maid = MaidPatch.getOriginal();
        LivingEntity target = event.getTarget();
        RandomSource rand = Maid.level().getRandom();
        Animator animator = MaidPatch.getAnimator();
        AnimationPlayer animationPlayer = animator.getPlayerFor(null);
        if (event.getSource() instanceof EpicFightDamageSource EFSource) {
            if (animationPlayer != null) {
                if (animationPlayer.getAnimation() instanceof AttackAnimation attackAnimation) {
                    float elapsedTime = animationPlayer.getElapsedTime();
                    float prevElapsedTime = animationPlayer.getPrevElapsedTime();
                    AttackAnimation.Phase phase = attackAnimation.getPhaseByTime(elapsedTime);
                    List<Entity> collidingEntities = phase.getCollidingEntities(MaidPatch, attackAnimation, prevElapsedTime, elapsedTime, attackAnimation.getPlaySpeed(MaidPatch, attackAnimation));
                    int hitCount = collidingEntities.size();
                    if (hitCount > 0) {
                        if (hitCount == 1 && !event.getSource().is(BroadBladeInnate.EFN_BROADBLADE_EXTRA_DAMAGE)) {
                            EFSource.attachDamageModifier(ValueModifier.multiplier(1.5F));
                            Maid.playSound(SoundEvents.TRIDENT_THROW, 1.1F, 0.625F + 0.1F * rand.nextFloat());
                        }
                        if (hitCount > 2 && !EFSource.is(BroadBladeInnate.EFN_BROADBLADE_EXTRA_DAMAGE)) {
                            float extraDamage = target.getMaxHealth() * 0.02F;
                            DamageSource damageSource = EpicFightDamageSources.mobAttack(Maid)
                                    .setAnimation(null).setInitialPosition(Maid.position())
                                    .addRuntimeTag(BroadBladeInnate.EFN_BROADBLADE_EXTRA_DAMAGE)
                                    .setStunType(StunType.NONE).setBaseImpact(0.0F)
                                    .addRuntimeTag(DamageTypeTags.BYPASSES_ARMOR).addRuntimeTag(DamageTypeTags.BYPASSES_INVULNERABILITY)
                                    .addRuntimeTag(DamageTypeTags.BYPASSES_RESISTANCE).addRuntimeTag(DamageTypeTags.BYPASSES_ENCHANTMENTS)
                                    .addRuntimeTag(DamageTypeTags.BYPASSES_EFFECTS).addRuntimeTag(DamageTypeTags.BYPASSES_COOLDOWN)
                                    .addRuntimeTag(DamageTypeTags.BYPASSES_SHIELD).addRuntimeTag(EpicFightDamageTypeTags.FINISHER);
                            target.hurt(damageSource, extraDamage);
                            target.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 0.8F, 1.2F);
                        }
                    }
                }
            }
            if (Maid.hasEffect(EFNMobEffectRegistry.BATTLE_CONTINUATION.get())) {
                EFSource.addExtraDamage(EFNExtraDamageInstance.EXTRA_PERCENTAGE_DAMAGE.create(1.0F));
            }
        }
    }
    @Override
    public void MaidKillTarget(MaidKilledEvent event) {
        MaidPatch<?> MaidPatch = event.getMaidPatch();
        EntityMaid Maid = MaidPatch.getOriginal();
        Maid.addEffect(new MobEffectInstance(EFNMobEffectRegistry.GRADUAL_HEAL.get(), 40, 4));
        Maid.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1));
        ItemStack item = Maid.getMainHandItem();
        if (item.getItem() instanceof BroadBladeItem) {
            CompoundTag tag = item.getOrCreateTag();
            int currentCount = BroadBladeItem.getKillCount(item);
            tag.putInt("KillCount", currentCount + 1);
        }
    }
    @Override
    public void onRemove(MaidChangeItemEvent event) {
        super.onRemove(event);
    }
}
