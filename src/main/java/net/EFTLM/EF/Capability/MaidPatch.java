package net.EFTLM.EF.Capability;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Maps;
import net.EFTLM.EF.Animation.CombatBehavior.*;
import net.EFTLM.EF.Api.Event.CombatBehaviorsEvent;
import net.EFTLM.TLM.Task.FightModeTask;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.api.utils.math.MathUtils;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.MobCombatBehaviors;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.*;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageTypeTags;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.DodgeLocationIndicator;
import yesman.epicfight.world.entity.ai.goal.AnimatedAttackGoal;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;
import yesman.epicfight.world.entity.ai.goal.TargetChasingGoal;
import java.util.*;
public class MaidPatch<T extends EntityMaid> extends HumanoidMobPatch<T> {
    protected Map<Item, CombatBehaviors.Builder<HumanoidMobPatch<?>>> ItemAttackMotions;
    protected Map<Item, Map<Style, CombatBehaviors.Builder<HumanoidMobPatch<?>>>> ItemStyleAttackMotions;
    protected Map<Item, HumanoidArmature> ItemArmatures;
    protected Item CurrentItem;
    protected boolean hasFightAi;
    protected static final String DEFAULT_MODEL_ID = "geckolib:winefox";
    public MaidPatch() {
        super(Factions.NEUTRAL);
    }
    @Override
    public OpenMatrix4f getModelMatrix(float partialTicks) {
        Entity Ride = this.original.getVehicle();
        float yRotO;
        float yRot;
        if (Ride instanceof LivingEntity ridingEntity) {
            yRotO = ridingEntity.yBodyRotO;
            yRot = ridingEntity.yBodyRot;
        } else {
            yRotO = this.isLogicalClient() ? this.original.yBodyRotO : this.original.getYRot();
            yRot = this.isLogicalClient() ? this.original.yBodyRot : this.original.getYRot();
        }
        return MathUtils.getModelMatrixIntegral(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, yRotO, yRot, partialTicks, 0.8F, 0.8F, 0.8F);
    }
    @Override
    public HumanoidArmature getArmature() {
        ItemStack ItemStack = this.getOriginal().getMainHandItem();
        if (this.ItemArmatures.containsKey(ItemStack.getItem())) {
            return this.ItemArmatures.get(ItemStack.getItem());
        }
        return (HumanoidArmature) this.armature;
    }
    @Override
    public void serverTick(LivingEvent.LivingTickEvent event) {
        super.serverTick(event);
        if (!this.getOriginal().level().isClientSide()) {
            if (this.getOriginal().isDeadOrDying()) {
                this.tickDeath();
            }
            if (this.isFightMode()) {
                ItemStack ItemStack = this.getOriginal().getMainHandItem();
                if (this.CurrentItem != ItemStack.getItem() || this.isHugByOwner() || this.isSleep() || this.isSit()) {
                    this.resetAnimation();
                    this.resetAi();
                    this.CurrentItem = ItemStack.getItem();
                }
                if (!this.hasFightAi && !isHugByOwner() && !isSleep() && !isSit()) {
                    this.resetAi();
                }
            } else {
                this.resetAnimation();
            }
        }
    }
    @Override
    protected void initAI() {
        if (!isHugByOwner() && !isSleep() && !isSit()) {
            super.initAI();
            this.hasFightAi = true;
        } else {
            this.hasFightAi = false;
        }
    }
    @Override
    public AttackResult attack(EpicFightDamageSource damageSource, Entity target, InteractionHand hand) {
        if(target instanceof DodgeLocationIndicator LocationIndicator) {
            LivingEntityPatch<?> DodgePatch = EpicFightCapabilities.getEntityPatch(LocationIndicator,LivingEntityPatch.class);
            if (DodgePatch != null && DodgePatch.equals(this.getOwnerPatch())) {
                return AttackResult.missed(0);
            }
        }
        if (target instanceof Player player) {
            if (this.getOwnerPatch() != null) {
                PlayerPatch<?> Owner = EpicFightCapabilities.getPlayerPatch(player);
                if (Owner != null && Owner.equals(this.getOwnerPatch())) {
                    return AttackResult.missed(0);
                }
            }
        }
        return super.attack(damageSource, target, hand);
    }
    @Override
    public AttackResult tryHurt(DamageSource damageSource, float amount) {
        if (damageSource.getEntity() instanceof Player player) {
            if (this.getOwnerPatch() != null) {
                PlayerPatch<?> Owner = EpicFightCapabilities.getPlayerPatch(player);
                if (Owner != null && Owner.equals(this.getOwnerPatch())) {
                    return AttackResult.missed(amount);
                }
            }
        }
        if (damageSource.getEntity() instanceof EntityMaid maid) {
            if (this.getOwnerPatch() != null) {
                if (maid.getOwner() != null) {
                    if (maid.getOwner().equals(this.getOwnerPatch().getOriginal())) {
                        return AttackResult.missed(amount);
                    }
                }
            }
        }
        return super.tryHurt(damageSource,amount);
    }
    @Override
    protected void setWeaponMotions() {
        this.ItemArmatures = Maps.newHashMap();
        this.ItemAttackMotions = Maps.newHashMap();
        this.ItemStyleAttackMotions = Maps.newHashMap();
        this.weaponAttackMotions = Maps.newHashMap();
        EFTLM_Behaviors.SetWeaponMotions(this.weaponAttackMotions,this.ItemArmatures);
        MinecraftForge.EVENT_BUS.post(new CombatBehaviorsEvent(this.weaponAttackMotions,this.ItemStyleAttackMotions,this.ItemAttackMotions,this.ItemArmatures));
    }
    @Override
    protected CombatBehaviors.Builder<HumanoidMobPatch<?>> getHoldingItemWeaponMotionBuilder() {
        ItemStack ItemStack = this.getOriginal().getMainHandItem();
        CapabilityItem ItemCap = this.getHoldingItemCapability(InteractionHand.MAIN_HAND);
        Style style = ItemCap.getStyle(this);
        if (this.ItemAttackMotions.containsKey(ItemStack.getItem())) {
            return this.ItemAttackMotions.get(ItemStack.getItem());
        } else {
            if (this.ItemStyleAttackMotions.containsKey(ItemStack.getItem())) {
                Map<Style, CombatBehaviors.Builder<HumanoidMobPatch<?>>> motionByStyle = this.ItemStyleAttackMotions.get(ItemStack.getItem());
                if (motionByStyle.containsKey(style) || motionByStyle.containsKey(CapabilityItem.Styles.COMMON)) {
                    return motionByStyle.getOrDefault(style,motionByStyle.get(CapabilityItem.Styles.COMMON));
                }
            } else {
                if (this.weaponAttackMotions.containsKey(ItemCap.getWeaponCategory())) {
                    Map<Style, CombatBehaviors.Builder<HumanoidMobPatch<?>>> motionByStyle = this.weaponAttackMotions.get(ItemCap.getWeaponCategory());
                    if (motionByStyle.containsKey(style) || motionByStyle.containsKey(CapabilityItem.Styles.COMMON)) {
                        return motionByStyle.getOrDefault(style, motionByStyle.get(CapabilityItem.Styles.COMMON));
                    }
                }
            }
        }
        return this.getOriginal().getMainHandItem().isEmpty() ? MobCombatBehaviors.HUMANOID_FIST : MobCombatBehaviors.HUMANOID_ONEHAND_TOOLS;
    }
    @Override
    public AnimationManager.AnimationAccessor<? extends StaticAnimation> getHitAnimation(StunType stunType) {
        if (this.getFavorRank() >= 1) {
            return switch (stunType) {
                case LONG, SHORT, HOLD, NONE -> null;
                case KNOCKDOWN -> Animations.BIPED_KNOCKDOWN;
                case NEUTRALIZE -> Animations.BIPED_COMMON_NEUTRALIZED;
                case FALL -> Animations.BIPED_LANDING;
            };
        } else {
            return switch (stunType) {
                case LONG -> Animations.BIPED_HIT_LONG;
                case SHORT, HOLD -> Animations.BIPED_HIT_SHORT;
                case KNOCKDOWN -> Animations.BIPED_KNOCKDOWN;
                case NEUTRALIZE -> Animations.BIPED_COMMON_NEUTRALIZED;
                case FALL -> Animations.BIPED_LANDING;
                case NONE -> null;
            };
        }
    }
    @Override
    public void initAnimator(Animator animator) {
        super.initAnimator(animator);
        animator.addLivingAnimation(LivingMotions.IDLE, Animations.BIPED_IDLE);
        animator.addLivingAnimation(LivingMotions.WALK, Animations.BIPED_WALK);
        animator.addLivingAnimation(LivingMotions.RUN, Animations.BIPED_RUN);
        animator.addLivingAnimation(LivingMotions.SNEAK, Animations.BIPED_SNEAK);
        animator.addLivingAnimation(LivingMotions.SWIM, Animations.BIPED_SWIM);
        animator.addLivingAnimation(LivingMotions.FLOAT, Animations.BIPED_FLOAT);
        animator.addLivingAnimation(LivingMotions.KNEEL, Animations.BIPED_KNEEL);
        animator.addLivingAnimation(LivingMotions.FALL, Animations.BIPED_FALL);
        animator.addLivingAnimation(LivingMotions.MOUNT, Animations.BIPED_MOUNT);
        animator.addLivingAnimation(LivingMotions.SIT, Animations.BIPED_SIT);
        animator.addLivingAnimation(LivingMotions.FLY, Animations.BIPED_FLYING);
        animator.addLivingAnimation(LivingMotions.DEATH, Animations.BIPED_DEATH);
        animator.addLivingAnimation(LivingMotions.JUMP, Animations.BIPED_JUMP);
        animator.addLivingAnimation(LivingMotions.CLIMB, Animations.BIPED_CLIMBING);
        animator.addLivingAnimation(LivingMotions.SLEEP, Animations.BIPED_SLEEPING);
    }
    @Override
    public void updateMotion(boolean considerInaction) {
    }
    @Override
    public boolean shouldCancelKnockback() {
        return true;
    }
    protected void tickDeath() {
        this.getOriginal().deathTime++;
        if (this.getOriginal().deathTime >= 20 && !this.getOriginal().isRemoved()) {
            this.getOriginal().level().broadcastEntityEvent(this.getOriginal(), (byte)60);
            this.getOriginal().discard();
        }
    }
    public PlayerPatch<?> getOwnerPatch() {
        if (this.getOriginal().getOwner() != null) {
            return EpicFightCapabilities.getEntityPatch(this.getOriginal().getOwner(), PlayerPatch.class);
        }
        return null;
    }
    public void resetAi() {
        this.initAI();
    }
    public void resetAnimation() {
        List<WrappedGoal> toRemove = new ArrayList<>();
        for (WrappedGoal wrappedGoal : this.getOriginal().goalSelector.getAvailableGoals()) {
            if (wrappedGoal.getGoal() instanceof AnimatedAttackGoal) {
                toRemove.add(wrappedGoal);
            }
            if (wrappedGoal.getGoal() instanceof TargetChasingGoal) {
                toRemove.add(wrappedGoal);
            }
        }
        for (WrappedGoal wrappedGoal : toRemove) {
            this.getOriginal().goalSelector.removeGoal(wrappedGoal.getGoal());
        }
        if (this.getEntityState().inaction()) {
            this.playAnimationSynchronized(Animations.BIPED_IDLE,0F);
        }
    }
    public boolean isHugByOwner() {
        if (this.getOriginal().getVehicle() != null) {
            if (this.getOwnerPatch() != null) {
                return this.getOriginal().getVehicle().equals(this.getOwnerPatch().getOriginal());
            }
        }
        return false;
    }
    public boolean isSleep() {
        return this.getOriginal().isSleeping();
    }
    public boolean isSit() {
        return this.getOriginal().isMaidInSittingPose();
    }
    public String getModelID() {
        IMaid maid = IMaid.convert(this.getOriginal());
        String modelId;
        if (maid != null) {
            modelId = maid.getModelId();
        } else {
            modelId = DEFAULT_MODEL_ID;
        }
        if (modelId.contains(":")) {
            return modelId.substring(modelId.indexOf(':') + 1);
        }
        return modelId;
    }
    public boolean isFightMode() {
        IMaid maid = IMaid.convert(this.getOriginal());
        if (maid != null) {
            IMaidTask task = maid.getTask();
            return task instanceof FightModeTask;
        }
        return false;
    }
    public int getFavorRank() {
        IMaid maid = IMaid.convert(this.getOriginal());
        if (maid != null) {
            return maid.getFavorability();
        }
        return 0;
    }
    public boolean canUseSkill() {
        return !this.getOriginal().getCooldowns().isOnCooldown(this.CurrentItem);
    }
    public void setCoolDown(int tick) {
        this.getOriginal().getCooldowns().addCooldown(this.CurrentItem, tick);
    }
    public boolean isBlockableSource(DamageSource damageSource) {
        return !damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !damageSource.is(EpicFightDamageTypeTags.UNBLOCKALBE) && !damageSource.is(DamageTypeTags.BYPASSES_ARMOR) && !damageSource.is(DamageTypeTags.IS_EXPLOSION) && !damageSource.is(DamageTypes.MAGIC) && !damageSource.is(DamageTypeTags.IS_FIRE);
    }
    public boolean isFrontAttack(DamageSource damageSource) {
        Vec3 sourceLocation = damageSource.getSourcePosition();
        Vec3 viewVector = this.getOriginal().getViewVector(1.0F);
        if (sourceLocation != null) {
            Vec3 toSourceLocation = sourceLocation.subtract(this.getOriginal().position()).normalize();
            return toSourceLocation.dot(viewVector) > 0.0;
        }
        return false;
    }
}
