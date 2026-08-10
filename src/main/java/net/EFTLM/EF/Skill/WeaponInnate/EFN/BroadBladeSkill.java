package net.EFTLM.EF.Skill.WeaponInnate.EFN;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidHurtEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.hm.efn.EFN;
import com.hm.efn.gameasset.EFNExtraDamageInstance;
import com.hm.efn.item.custom.BroadBladeItem;
import com.hm.efn.registries.EFNMobEffectRegistry;
import com.hm.efn.skill.weapon_innate.BroadBladeInnate;
import net.EFTLM.EF.API.Event.MaidHurtTargetEvent;
import net.EFTLM.EF.API.Event.MaidKilledEvent;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillBuilder;
import net.EFTLM.EF.Skill.WeaponInnate.WeaponInnateSkill;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
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
    public void onMaidHurt(MaidHurtEvent event, MaidPatch<?> patch) {
        if (patch.getEntityState().getLevel() == 1) {
            if (event.getSource() instanceof EpicFightDamageSource efSource) {
                efSource.setStunType(StunType.NONE);
            }
        }
    }
    @Override
    public void onHurtTargetPre(MaidHurtTargetEvent.Pre event) {
        MaidPatch<?> maidPatch = event.getMaidPatch();
        EntityMaid maid = maidPatch.getOriginal();
        LivingEntity target = event.getTarget();
        RandomSource rand = maid.level().getRandom();
        if (event.getSource() instanceof EpicFightDamageSource efSource) {
            if (rand.nextFloat() < 0.3F && !efSource.is(BroadBladeInnate.EFN_BROADBLADE_EXTRA_DAMAGE)) {
                efSource.addRuntimeTag(EpicFightDamageTypeTags.BYPASS_DODGE);
                efSource.addRuntimeTag(EpicFightDamageTypeTags.GUARD_PUNCTURE);
                efSource.addRuntimeTag(EpicFightDamageTypeTags.UNBLOCKALBE);
                efSource.addRuntimeTag(EpicFightDamageTypeTags.FINISHER);
                addBypassTags(efSource);
                float extraDamage = target.getMaxHealth() * 0.05F;
                DamageSource damageSource = createExtraDamageSource(maid);
                target.hurt(damageSource, extraDamage);
                maidPatch.playSound(SoundEvents.PLAYER_ATTACK_CRIT, 0.8F, 1.2F);
                maidPatch.playSound(SoundEvents.WITHER_HURT, 0.2F, 0.4F + 0.25F * rand.nextFloat());
                if (maid.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.ENCHANTED_HIT,
                            target.getX(), target.getY() + 1.0, target.getZ(),
                            20, 0.5, 0.5, 0.5, 0.1);
                    EpicFightParticles.BLADE_RUSH_SKILL.get()
                            .spawnParticleWithArgument(serverLevel, null, null, target, maid);
                }
            }
        }
    }
    @Override
    public void onHurtTargetPost(MaidHurtTargetEvent.Post event) {
        MaidPatch<?> maidPatch = event.getMaidPatch();
        EntityMaid maid = maidPatch.getOriginal();
        LivingEntity target = event.getTarget();
        RandomSource rand = maid.level().getRandom();
        Animator animator = maidPatch.getAnimator();
        AnimationPlayer animationPlayer = animator.getPlayerFor(null);
        if (event.getSource() instanceof EpicFightDamageSource efSource) {
            if (animationPlayer != null && animationPlayer.getAnimation() instanceof AttackAnimation attackAnimation) {
                float elapsedTime = animationPlayer.getElapsedTime();
                float prevElapsedTime = animationPlayer.getPrevElapsedTime();
                AttackAnimation.Phase phase = attackAnimation.getPhaseByTime(elapsedTime);
                List<Entity> collidingEntities = phase.getCollidingEntities(maidPatch, attackAnimation,
                        prevElapsedTime, elapsedTime, attackAnimation.getPlaySpeed(maidPatch, attackAnimation));
                int hitCount = collidingEntities.size();
                if (hitCount > 0 && !efSource.is(BroadBladeInnate.EFN_BROADBLADE_EXTRA_DAMAGE)) {
                    if (hitCount == 1) {
                        efSource.attachDamageModifier(ValueModifier.multiplier(1.5F));
                        maidPatch.playSound(SoundEvents.TRIDENT_THROW, 1.1F, 0.625F + 0.1F * rand.nextFloat());
                    } else if (hitCount > 2) {
                        float extraDamage = target.getMaxHealth() * 0.02F;
                        DamageSource damageSource = createExtraDamageSource(maid);
                        target.hurt(damageSource, extraDamage);
                        maidPatch.playSound(SoundEvents.PLAYER_ATTACK_CRIT, 0.8F, 1.2F);
                    }
                }
            }
            if (maid.hasEffect(EFNMobEffectRegistry.BATTLE_CONTINUATION.get())) {
                efSource.addExtraDamage(EFNExtraDamageInstance.EXTRA_PERCENTAGE_DAMAGE.create(1.0F));
            }
        }
    }
    @Override
    public void onKillTarget(MaidKilledEvent event) {
        MaidPatch<?> maidPatch = event.getMaidPatch();
        EntityMaid maid = maidPatch.getOriginal();
        maid.addEffect(new MobEffectInstance(EFNMobEffectRegistry.GRADUAL_HEAL.get(), 40, 4));
        maid.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1));
        ItemStack item = maid.getMainHandItem();
        if (item.getItem() instanceof BroadBladeItem) {
            CompoundTag tag = item.getOrCreateTag();
            int currentCount = BroadBladeItem.getKillCount(item);
            tag.putInt("KillCount", currentCount + 1);
        }
    }
    @Override
    public ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(EFN.MODID, "textures/item/broadblade.png");
    }
    private void addBypassTags(EpicFightDamageSource source) {
        source.addRuntimeTag(DamageTypeTags.BYPASSES_ARMOR);
        source.addRuntimeTag(DamageTypeTags.BYPASSES_INVULNERABILITY);
        source.addRuntimeTag(DamageTypeTags.BYPASSES_RESISTANCE);
        source.addRuntimeTag(DamageTypeTags.BYPASSES_ENCHANTMENTS);
        source.addRuntimeTag(DamageTypeTags.BYPASSES_EFFECTS);
        source.addRuntimeTag(DamageTypeTags.BYPASSES_COOLDOWN);
        source.addRuntimeTag(DamageTypeTags.BYPASSES_SHIELD);
    }
    private DamageSource createExtraDamageSource(EntityMaid maid) {
        EpicFightDamageSource source = EpicFightDamageSources.mobAttack(maid)
                .setAnimation(null)
                .setInitialPosition(maid.position())
                .addRuntimeTag(BroadBladeInnate.EFN_BROADBLADE_EXTRA_DAMAGE)
                .setStunType(StunType.NONE)
                .setBaseImpact(0.0F);
        addBypassTags(source);
        source.addRuntimeTag(EpicFightDamageTypeTags.FINISHER);
        return source;
    }
}