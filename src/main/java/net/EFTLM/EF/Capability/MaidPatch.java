package net.EFTLM.EF.Capability;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.google.common.collect.Maps;
import net.EFTLM.EF.API.Event.MaidChangeItemEvent;
import net.EFTLM.EF.API.Event.MaidSkillInitEvent;
import net.EFTLM.EF.Animation.CombatBehavior.*;
import net.EFTLM.EF.API.Event.CombatBehaviorsEvent;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillDataManager;
import net.EFTLM.EF.Utils.CompoundTagManager;
import net.EFTLM.TLM.Task.FightModeTask;
import net.minecraft.nbt.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
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
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.DodgeLocationIndicator;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import yesman.epicfight.world.entity.ai.goal.AnimatedAttackGoal;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;
import yesman.epicfight.world.entity.ai.goal.TargetChasingGoal;
import java.util.*;
public class MaidPatch<T extends EntityMaid> extends HumanoidMobPatch<T> implements INBTSerializable<CompoundTag> {
    public static final EntityDataAccessor<Float> Stamina = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.FLOAT);
    protected static final String DEFAULT_MODEL_ID = "geckolib:winefox";
    protected Map<Item, CombatBehaviors.Builder<HumanoidMobPatch<?>>> ItemAttackMotions;
    protected Map<Item, Map<Style, CombatBehaviors.Builder<HumanoidMobPatch<?>>>> ItemStyleAttackMotions;
    protected Map<Item, HumanoidArmature> ItemArmatures;
    protected Map<MaidSkill, Map<MaidSkillDataManager.SkillDataKey<?>, MaidSkillDataManager.Data>> SkillDataKey = Maps.newHashMap();
    public Item CurrentMain;
    public Item CurrentOff;
    protected List<ResourceLocation> LearnedSkills = new ArrayList<>();
    protected boolean hasFightAi;
    public MaidPatch() {
        super(Factions.NEUTRAL);
    }
    public static void initAttribute(EntityAttributeModificationEvent event) {
        event.add(InitEntities.MAID.get(), EpicFightAttributes.ARMOR_NEGATION.get(), 0);
        event.add(InitEntities.MAID.get(), EpicFightAttributes.IMPACT.get(),0);
        event.add(InitEntities.MAID.get(), EpicFightAttributes.MAX_STRIKES.get(),999);
        event.add(InitEntities.MAID.get(), EpicFightAttributes.STUN_ARMOR.get(),20);
        event.add(InitEntities.MAID.get(), EpicFightAttributes.OFFHAND_ATTACK_SPEED.get(), 0);
        event.add(InitEntities.MAID.get(), EpicFightAttributes.OFFHAND_MAX_STRIKES.get(),0);
        event.add(InitEntities.MAID.get(), EpicFightAttributes.OFFHAND_ARMOR_NEGATION.get(),0);
        event.add(InitEntities.MAID.get(), EpicFightAttributes.OFFHAND_IMPACT.get(),0);
        event.add(InitEntities.MAID.get(), EpicFightAttributes.MAX_STAMINA.get(),20);
        event.add(InitEntities.MAID.get(), EpicFightAttributes.STAMINA_REGEN.get(),1);
    }
    public float getMaxStamina() {
        AttributeInstance maxStamina = this.getOriginal().getAttribute(EpicFightAttributes.MAX_STAMINA.get());
        return (float)(maxStamina == null ? 0.0 : maxStamina.getValue());
    }
    public float getStamina() {
        return this.getMaxStamina() <= 0.0F ? 0.0F : this.getOriginal().getEntityData().hasItem(Stamina) ? this.getOriginal().getEntityData().get(Stamina) : 0.0F;
    }
    public boolean hasStamina(float amount) {
        return this.getStamina() >= amount;
    }
    public void setStamina(float value) {
        if (this.getOriginal().getEntityData().hasItem(Stamina)) {
            float amount = Mth.clamp(value, 0.0F, this.getMaxStamina());
            this.getOriginal().getEntityData().set(Stamina, amount);
        }
    }
    public List<ResourceLocation> getLearnedSkills() {
        return this.LearnedSkills;
    }
    public void addLearnedSkill(ResourceLocation RegistryName) {
        if (!this.hasLearnedSkill(RegistryName)) {
            this.LearnedSkills.add(RegistryName);
            this.saveToPersistent();
            MinecraftForge.EVENT_BUS.post(new MaidSkillInitEvent(this));
        }
    }
    public void removeLearnedSkill(ResourceLocation RegistryName) {
        if (this.hasLearnedSkill(RegistryName)) {
            this.LearnedSkills.remove(RegistryName);
            this.saveToPersistent();
        }
    }
    public void clearLearnedSkills() {
        this.LearnedSkills.clear();
        this.saveToPersistent();
    }
    public boolean hasLearnedSkill(ResourceLocation RegistryName) {
        return this.LearnedSkills.contains(RegistryName);
    }
    protected void saveToPersistent() {
        EntityMaid Maid = this.getOriginal();
        if (Maid != null && !Maid.level().isClientSide()) {
            CompoundTag PersistentData = Maid.getPersistentData();
            PersistentData.remove(CompoundTagManager.MaidCap);
            PersistentData.put(CompoundTagManager.MaidCap, this.serializeNBT());
        }
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
    public void onConstructed(T entity) {
        super.onConstructed(entity);
        entity.getEntityData().define(Stamina, 0F);
    }
    @Override
    public void onJoinWorld(T entity, EntityJoinLevelEvent event) {
        super.onJoinWorld(entity, event);
        CompoundTag NBT = entity.getPersistentData();
        if (NBT.contains(CompoundTagManager.MaidCap)) {
            this.deserializeNBT(NBT);
        }
        Item Main = entity.getMainHandItem().getItem();
        Item Off = entity.getOffhandItem().getItem();
        this.CurrentMain = Main;
        this.CurrentOff = Off;
        MinecraftForge.EVENT_BUS.post(new MaidSkillInitEvent(this));
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
            float stamina = this.getStamina();
            float maxStamina = this.getMaxStamina();
            float staminaRegen = (float) this.getOriginal().getAttributeValue(EpicFightAttributes.STAMINA_REGEN.get());
            if (this.getOriginal().isDeadOrDying()) {
                this.tickDeath();
            }
            if (staminaRegen > 0.0F && !this.getEntityState().inaction()) {
                if (stamina < maxStamina) {
                    float staminaFactor = 1.0F + (float)Math.pow(stamina / (maxStamina - stamina * 0.5F), 2.0);
                    this.setStamina(stamina + maxStamina * 0.01F * staminaFactor * staminaRegen);
                }
            }
            if (this.isFightMode()) {
                Item Main = this.getOriginal().getMainHandItem().getItem();
                Item Off = this.getOriginal().getOffhandItem().getItem();
                if (this.CurrentMain != Main || this.CurrentOff != Off || this.CheckState()) {
                    this.resetAnimation();
                    this.resetAi();
                    this.CurrentMain = Main;
                    this.CurrentOff = Off;
                    MinecraftForge.EVENT_BUS.post(new MaidChangeItemEvent(this));
                }
                if (!this.hasFightAi && !this.CheckState()) {
                    this.resetAi();
                }
            } else {
                this.resetAnimation();
            }
        }
    }
    @Override
    protected void initAI() {
        if (!CheckState()) {
            super.initAI();
            this.hasFightAi = true;
        } else {
            this.hasFightAi = false;
        }
    }
    @Override
    public void setAIAsInfantry(boolean holdingRanedWeapon) {
        CombatBehaviors.Builder<HumanoidMobPatch<?>> builder = this.getHoldingItemWeaponMotionBuilder();
        if (builder != null) {
            this.original.goalSelector.addGoal(0, new AnimatedAttackGoal<>(this, builder.build(this)));
            this.original.goalSelector.addGoal(1, new TargetChasingGoal(this, this.getOriginal(), 1.0, true));
        }
    }
    @Override
    public AttackResult attack(EpicFightDamageSource damageSource, Entity target, InteractionHand hand) {
        if (target instanceof DodgeLocationIndicator LocationIndicator) {
            LivingEntityPatch<?> DodgePatch = EpicFightCapabilities.getEntityPatch(LocationIndicator, LivingEntityPatch.class);
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
        return super.tryHurt(damageSource, amount);
    }
    @Override
    protected void setWeaponMotions() {
        this.ItemArmatures = Maps.newHashMap();
        this.ItemAttackMotions = Maps.newHashMap();
        this.ItemStyleAttackMotions = Maps.newHashMap();
        this.weaponAttackMotions = Maps.newHashMap();
        EFTLM_Behaviors.SetWeaponMotions(this.weaponAttackMotions, this.ItemArmatures);
        MinecraftForge.EVENT_BUS.post(new CombatBehaviorsEvent(this.weaponAttackMotions, this.ItemStyleAttackMotions, this.ItemAttackMotions, this.ItemArmatures));
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
                    return motionByStyle.getOrDefault(style, motionByStyle.get(CapabilityItem.Styles.COMMON));
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
        return switch (stunType) {
            case LONG -> Animations.BIPED_HIT_LONG;
            case SHORT, HOLD -> Animations.BIPED_HIT_SHORT;
            case KNOCKDOWN -> Animations.BIPED_KNOCKDOWN;
            case NEUTRALIZE -> Animations.BIPED_COMMON_NEUTRALIZED;
            case FALL -> Animations.BIPED_LANDING;
            case NONE -> null;
        };
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
    protected void tickDeath() {
        this.getOriginal().deathTime++;
        if (this.getOriginal().deathTime >= 20 && !this.getOriginal().isRemoved()) {
            this.getOriginal().level().broadcastEntityEvent(this.getOriginal(), (byte) 60);
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
            this.playAnimationSynchronized(Animations.BIPED_IDLE, 0F);
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
        return !this.getOriginal().getCooldowns().isOnCooldown(this.CurrentMain);
    }
    public void setCoolDown(int tick) {
        this.getOriginal().getCooldowns().addCooldown(this.CurrentMain, tick);
    }
    public boolean CheckState() {
        return this.isHugByOwner() || this.isSleep() || this.isSit();
    }
    public <V> void registerData(MaidSkill skill, MaidSkillDataManager.SkillDataKey<V> key, V data) {
        Map<MaidSkillDataManager.SkillDataKey<?>, MaidSkillDataManager.Data> inner = this.SkillDataKey.computeIfAbsent(skill, k -> new HashMap<>());
        MaidSkillDataManager.Data Container = key.getValueType().create();
        key.getValueType().set(Container, data);
        inner.put(key, Container);
    }
    public void removeData(MaidSkill skill) {
        this.SkillDataKey.remove(skill);
    }
    public <V> void setData(MaidSkill skill, MaidSkillDataManager.SkillDataKey<V> key, V data) {
        if (this.hasData(skill,key)) {
            key.getValueType().set(this.SkillDataKey.get(skill).get(key), data);
        }
    }
    public <V> V getDataValue(MaidSkill skill, MaidSkillDataManager.SkillDataKey<V> key) {
        return this.hasData(skill,key) ? key.getValueType().get(this.SkillDataKey.get(skill).get(key)) : null;
    }
    public boolean hasData(MaidSkill skill, MaidSkillDataManager.SkillDataKey<?> key) {
        Map<MaidSkillDataManager.SkillDataKey<?>, ?> inner = this.SkillDataKey.get(skill);
        return inner != null && inner.containsKey(key);
    }
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag NBT = new CompoundTag();
        ListTag SkillsList = new ListTag();
        for (ResourceLocation SkillRegisterId : this.LearnedSkills) {
            SkillsList.add(StringTag.valueOf(SkillRegisterId.toString()));
        }
        NBT.put(CompoundTagManager.LearnedSkills, SkillsList);
        return NBT;
    }
    @Override
    public void deserializeNBT(CompoundTag NBT) {
        CompoundTag MaidCap = NBT.getCompound(CompoundTagManager.MaidCap);
        if (MaidCap.contains(CompoundTagManager.LearnedSkills)) {
            ListTag SkillsList = MaidCap.getList(CompoundTagManager.LearnedSkills, StringTag.TAG_STRING);
            this.LearnedSkills = new ArrayList<>();
            for (int i = 0; i < SkillsList.size(); i++) {
                this.LearnedSkills.add(ResourceLocation.parse(SkillsList.getString(i)));
            }
        } else {
            ListTag SkillsList = NBT.getList(CompoundTagManager.LearnedSkills, StringTag.TAG_STRING);
            this.LearnedSkills = new ArrayList<>();
            for (int i = 0; i < SkillsList.size(); i++) {
                this.LearnedSkills.add(ResourceLocation.parse(SkillsList.getString(i)));
            }
        }
    }
}