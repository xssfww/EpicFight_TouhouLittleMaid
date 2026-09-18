package net.EFTLM.EF.Skill.Dodge;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTickEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Maps;
import net.EFTLM.EF.API.Event.MaidSkillInitEvent;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Compat.EFNCompat;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillBuilder;
import net.EFTLM.EF.Skill.MaidSkillDataManager;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.DodgeAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import java.util.List;
import java.util.Map;
public class Step extends MaidSkill {
    public static final MaidSkillDataManager.SkillDataKey<Integer> STEP_RESTORE_COUNTER =
            MaidSkillDataManager.SkillDataKey.createDataKey(MaidSkillDataManager.SkillDataKey.INTEGER);
    protected final Map<Item, List<AnimationManager.AnimationAccessor<? extends DodgeAnimation>>> ItemDodgeMotions;
    protected final Map<WeaponCategory, List<AnimationManager.AnimationAccessor<? extends DodgeAnimation>>> WeaponDodgeMotions;
    public static Step.Builder createStepBuilder() {
        return new Builder()
                .addDodgeMotions(CapabilityItem.WeaponCategories.SWORD,
                        List.of(Animations.BIPED_STEP_FORWARD, Animations.BIPED_STEP_BACKWARD,
                                Animations.BIPED_STEP_LEFT, Animations.BIPED_STEP_RIGHT))
                .addDodgeMotions(CapabilityItem.WeaponCategories.LONGSWORD,
                        List.of(Animations.BIPED_STEP_FORWARD, Animations.BIPED_STEP_BACKWARD,
                                Animations.BIPED_STEP_LEFT, Animations.BIPED_STEP_RIGHT))
                .addDodgeMotions(CapabilityItem.WeaponCategories.TACHI,
                        List.of(Animations.BIPED_STEP_FORWARD, Animations.BIPED_STEP_BACKWARD,
                                Animations.BIPED_STEP_LEFT, Animations.BIPED_STEP_RIGHT))
                .addDodgeMotions(CapabilityItem.WeaponCategories.UCHIGATANA,
                        List.of(Animations.BIPED_STEP_FORWARD, Animations.BIPED_STEP_BACKWARD,
                                Animations.BIPED_STEP_LEFT, Animations.BIPED_STEP_RIGHT))
                .addDodgeMotions(CapabilityItem.WeaponCategories.GREATSWORD,
                        List.of(Animations.BIPED_STEP_FORWARD, Animations.BIPED_STEP_BACKWARD,
                                Animations.BIPED_STEP_LEFT, Animations.BIPED_STEP_RIGHT))
                .addDodgeMotions(CapabilityItem.WeaponCategories.AXE,
                        List.of(Animations.BIPED_STEP_FORWARD, Animations.BIPED_STEP_BACKWARD,
                                Animations.BIPED_STEP_LEFT, Animations.BIPED_STEP_RIGHT))
                .addDodgeMotions(CapabilityItem.WeaponCategories.DAGGER,
                        List.of(Animations.BIPED_STEP_FORWARD, Animations.BIPED_STEP_BACKWARD,
                                Animations.BIPED_STEP_LEFT, Animations.BIPED_STEP_RIGHT));
    }
    public Step(Builder builder) {
        super(builder);
        this.ItemDodgeMotions = builder.ItemDodgeMotions;
        this.WeaponDodgeMotions = builder.WeaponDodgeMotions;
    }
    @Override
    public void onInit(MaidSkillInitEvent event) {
        event.registerData(this,STEP_RESTORE_COUNTER,0);
    }
    @Override
    public void onMaidTick(MaidTickEvent event,MaidPatch<?> patch) {
        EntityMaid Maid = event.getMaid();
        LivingEntity Target = patch.getTarget();
        if (Target == null) return;
        LivingEntityPatch<?> targetPatch = EpicFightCapabilities.getEntityPatch(Target, LivingEntityPatch.class);
        if (targetPatch == null) return;
        int Phase = targetPatch.getEntityState().getLevel();
        if (patch.getDataValue(this,STEP_RESTORE_COUNTER) != null) {
            int Counter = patch.getDataValue(this, STEP_RESTORE_COUNTER);
            patch.setData(this, STEP_RESTORE_COUNTER, Counter + 1);
            if (Phase > 0 && Phase < 3) {
                List<AnimationManager.AnimationAccessor<? extends DodgeAnimation>> dodgeAnimations = getDodgeAnimations(patch);
                if (dodgeAnimations == null || dodgeAnimations.size() < 4) return;
                float targetYaw = Target.getYRot();
                Vec3 attackDir = new Vec3(-Math.sin(Math.toRadians(targetYaw)), 0, Math.cos(Math.toRadians(targetYaw)));
                Vec3 toMaid = Maid.position().subtract(Target.position()).multiply(1, 0, 1).normalize();
                double dot = attackDir.dot(toMaid);
                double crossY = attackDir.cross(toMaid).y;
                String direction;
                if (Math.abs(dot) > Math.abs(crossY)) {
                    direction = dot > 0 ? "backward" : "forward";
                } else {
                    direction = crossY > 0 ? "right" : "left";
                }
                AnimationManager.AnimationAccessor<? extends DodgeAnimation> anim;
                switch (direction) {
                    case "forward":
                        anim = dodgeAnimations.get(0);
                        break;
                    case "backward":
                        anim = dodgeAnimations.get(1);
                        break;
                    case "left":
                        anim = dodgeAnimations.get(2);
                        break;
                    case "right":
                        anim = dodgeAnimations.get(3);
                        break;
                    default:
                        return;
                }
                if (EFNCompat.isSpecialAnimation(patch)) {
                    return;
                }
                if (patch.getOriginal().tickCount - Counter > 10) {
                    patch.setData(this, STEP_RESTORE_COUNTER, patch.getOriginal().tickCount);
                    AttributeInstance Weight = patch.getOriginal().getAttribute(EpicFightAttributes.WEIGHT.get());
                    if (Weight != null) {
                        patch.setStamina((float) (patch.getStamina() - (Weight.getValue() * 0.1F)));
                    }
                    patch.playAnimationSynchronized(anim, 0F);
                }
            }
        }
    }
    public List<AnimationManager.AnimationAccessor<? extends DodgeAnimation>> getDodgeAnimations(MaidPatch<?> Maid) {
        ItemStack ItemStack = Maid.getOriginal().getMainHandItem();
        CapabilityItem ItemCap = Maid.getHoldingItemCapability(InteractionHand.MAIN_HAND);
        if (this.ItemDodgeMotions.containsKey(ItemStack.getItem())) {
            return this.ItemDodgeMotions.get(ItemStack.getItem());
        } else {
            if (this.WeaponDodgeMotions.containsKey(ItemCap.getWeaponCategory())) {
                return this.WeaponDodgeMotions.get(ItemCap.getWeaponCategory());
            }
        }
        return null;
    }
    public static class Builder extends MaidSkillBuilder<Step> {
        protected final Map<Item, List<AnimationManager.AnimationAccessor<? extends DodgeAnimation>>> ItemDodgeMotions = Maps.newHashMap();
        protected final Map<WeaponCategory, List<AnimationManager.AnimationAccessor<? extends DodgeAnimation>>> WeaponDodgeMotions = Maps.newHashMap();
        public Builder() {
        }
        public Step.Builder addDodgeMotions(WeaponCategory Category, List<AnimationManager.AnimationAccessor<? extends DodgeAnimation>> AnimationList) {
            this.WeaponDodgeMotions.put(Category, AnimationList);
            return this;
        }
        public void addDodgeMotions(Item Item, List<AnimationManager.AnimationAccessor<? extends DodgeAnimation>> AnimationList) {
            this.ItemDodgeMotions.put(Item, AnimationList);
        }
    }
}
