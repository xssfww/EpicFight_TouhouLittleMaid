package net.EFTLM.EF.Skill.WeaponInnate.EFN;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTickEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.hm.efn.EFN;
import com.hm.efn.gameasset.animations.EFNHfBladeAnimations;
import com.hm.efn.gameasset.animations.EFNZansetsuAnimations_B;
import com.merlin204.avalon.epicfight.animations.AvalonAttackAnimation;
import net.EFTLM.EF.API.Event.MaidSkillInitEvent;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Compat.CompatModList;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillBuilder;
import net.EFTLM.EF.Skill.MaidSkillDataManager;
import net.EFTLM.EF.Skill.WeaponInnate.WeaponInnateSkill;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import yesman.epicfight.api.animation.AnimationManager;
import java.util.List;
import com.google.common.collect.Lists;
public class HF_BladeSkill extends WeaponInnateSkill {
    public static List<AnimationManager.AnimationAccessor<? extends AvalonAttackAnimation>> BladeZansetsuList = Lists.newArrayList();
    public static final MaidSkillDataManager.SkillDataKey<Boolean> isZansetsu_Blade =
            MaidSkillDataManager.SkillDataKey.createDataKey(MaidSkillDataManager.SkillDataKey.BOOLEAN);
    public static final MaidSkillDataManager.SkillDataKey<Integer> ZansetsuDelay_Blade =
            MaidSkillDataManager.SkillDataKey.createDataKey(MaidSkillDataManager.SkillDataKey.INTEGER);
    public static final MaidSkillDataManager.SkillDataKey<Integer> ZansetsuIndex_Blade =
            MaidSkillDataManager.SkillDataKey.createDataKey(MaidSkillDataManager.SkillDataKey.INTEGER);
    static {
        if (CompatModList.LoadedEFN()) {
            BladeZansetsuList = List.of(
                    EFNZansetsuAnimations_B.HF_BLADE_SLASH_DIAGONAL_LR_UP,
                    EFNZansetsuAnimations_B.HF_BLADE_SLASH_DIAGONAL_LR_DOWN,
                    EFNZansetsuAnimations_B.HF_BLADE_SLASH_DIAGONAL_RL_UP,
                    EFNZansetsuAnimations_B.HF_BLADE_SLASH_DIAGONAL_RL_DOWN,
                    EFNZansetsuAnimations_B.HF_BLADE_SLASH_HORIZONTAL_LR_UP,
                    EFNZansetsuAnimations_B.HF_BLADE_SLASH_HORIZONTAL_LR_DOWN,
                    EFNZansetsuAnimations_B.HF_BLADE_SLASH_HORIZONTAL_RL_UP,
                    EFNZansetsuAnimations_B.HF_BLADE_SLASH_HORIZONTAL_RL_DOWN,
                    EFNHfBladeAnimations.HF_BLADE_ZANDATSU
            );
        }
    }
    public HF_BladeSkill(MaidSkillBuilder<? extends MaidSkill> builder) {
        super(builder);
    }
    @Override
    public void onInit(MaidSkillInitEvent event) {
        super.onInit(event);
        event.registerData(this, isZansetsu_Blade, false);
        event.registerData(this, ZansetsuDelay_Blade, 0);
        event.registerData(this, ZansetsuIndex_Blade, 0);
    }
    @Override
    public void onMaidTick(MaidTickEvent event,MaidPatch<?> patch) {
        super.onMaidTick(event, patch);
        EntityMaid maid = event.getMaid();
        if (patch == null) return;
        Integer delay = patch.getDataValue(this, ZansetsuDelay_Blade);
        if (delay == null) return;
        LivingEntity target = patch.getTarget();
        if (target == null) return;
        if (maid.distanceToSqr(target) >= 2.0F) return;
        Boolean isActive = patch.getDataValue(this, isZansetsu_Blade);
        if (isActive == null || !isActive) return;
        if (maid.tickCount - delay > 5) {
            patch.setData(this, ZansetsuDelay_Blade, maid.tickCount);
            playNextZansetsuAnimation(patch);
        }
    }
    private void playNextZansetsuAnimation(MaidPatch<?> patch) {
        if (BladeZansetsuList.isEmpty()) return;
        Integer index = patch.getDataValue(this, ZansetsuIndex_Blade);
        if (index == null) return;
        patch.playAnimationSynchronized(BladeZansetsuList.get(index), 0F);
        index++;
        if (index >= BladeZansetsuList.size()) {
            patch.setData(this, isZansetsu_Blade, false);
            patch.setData(this, ZansetsuIndex_Blade, 0);
        } else {
            patch.setData(this, ZansetsuIndex_Blade, index);
        }
    }
    @Override
    public ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(EFN.MODID, "textures/item/hf_blade.png");
    }
}

