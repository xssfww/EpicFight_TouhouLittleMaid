package net.EFTLM.EF.Skill.WeaponInnate.EFN;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTickEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.hm.efn.EFN;
import com.hm.efn.gameasset.animations.EFNMurasamaAnimations;
import com.hm.efn.gameasset.animations.EFNZansetsuAnimations;
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
public class HF_MurasamaSkill extends WeaponInnateSkill {
    private static final float ENERGY_PER_STACK = 40.0F;
    private static final int MAX_STACKS = 3;
    public static List<AnimationManager.AnimationAccessor<? extends AvalonAttackAnimation>> ZansetsuList = Lists.newArrayList();
    public static final MaidSkillDataManager.SkillDataKey<Boolean> isZansetsu =
            MaidSkillDataManager.SkillDataKey.createDataKey(MaidSkillDataManager.SkillDataKey.BOOLEAN);
    public static final MaidSkillDataManager.SkillDataKey<Integer> ZansetsuDelay =
            MaidSkillDataManager.SkillDataKey.createDataKey(MaidSkillDataManager.SkillDataKey.INTEGER);
    public static final MaidSkillDataManager.SkillDataKey<Integer> ZansetsuIndex =
            MaidSkillDataManager.SkillDataKey.createDataKey(MaidSkillDataManager.SkillDataKey.INTEGER);
    static {
        if (CompatModList.LoadedEFN()) {
            ZansetsuList = List.of(
                    EFNZansetsuAnimations.HF_MURASAMA_SLASH_DIAGONAL_LR_UP,
                    EFNZansetsuAnimations.HF_MURASAMA_SLASH_DIAGONAL_LR_DOWN,
                    EFNZansetsuAnimations.HF_MURASAMA_SLASH_DIAGONAL_RL_UP,
                    EFNZansetsuAnimations.HF_MURASAMA_SLASH_DIAGONAL_RL_DOWN,
                    EFNZansetsuAnimations.HF_MURASAMA_SLASH_HORIZONTAL_LR_UP,
                    EFNZansetsuAnimations.HF_MURASAMA_SLASH_HORIZONTAL_LR_DOWN,
                    EFNZansetsuAnimations.HF_MURASAMA_SLASH_HORIZONTAL_RL_UP,
                    EFNZansetsuAnimations.HF_MURASAMA_SLASH_HORIZONTAL_RL_DOWN,
                    EFNMurasamaAnimations.HF_MURASAMA_ZANDATSU
            );
        }
    }
    public HF_MurasamaSkill(MaidSkillBuilder<? extends MaidSkill> builder) {
        super(builder);
    }
    @Override
    protected float getEnergyCharge() {
        return ENERGY_PER_STACK;
    }
    @Override
    protected int getMaxStack() {
        return MAX_STACKS;
    }
    @Override
    public void onInit(MaidSkillInitEvent event) {
        super.onInit(event);
        event.registerData(this,isZansetsu,false);
        event.registerData(this,ZansetsuDelay,0);
        event.registerData(this,ZansetsuIndex,0);
    }
    @Override
    public void onMaidTick(MaidTickEvent event,MaidPatch<?> patch) {
        super.onMaidTick(event, patch);
        EntityMaid maid = event.getMaid();
        if (patch == null) return;
        Integer delay = patch.getDataValue(this, ZansetsuDelay);
        if (delay == null) return;
        LivingEntity target = patch.getTarget();
        if (target == null) return;
        if (maid.distanceToSqr(target) >= 2.0F) return;
        Boolean isActive = patch.getDataValue(this, isZansetsu);
        if (isActive == null || !isActive) return;
        if (maid.tickCount - delay > 5) {
            patch.setData(this, ZansetsuDelay, maid.tickCount);
            playNextZansetsuAnimation(patch);
        }
    }
    private void playNextZansetsuAnimation(MaidPatch<?> patch) {
        if (ZansetsuList.isEmpty()) return;
        Integer index = patch.getDataValue(this, ZansetsuIndex);
        if (index == null) return;
        patch.playAnimationSynchronized(ZansetsuList.get(index), 0F);
        index++;
        if (index >= ZansetsuList.size()) {
            patch.setData(this, isZansetsu, false);
            patch.setData(this, ZansetsuIndex, 0);
        } else {
            patch.setData(this, ZansetsuIndex, index);
        }
    }
    @Override
    public ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(EFN.MODID, "textures/item/hf_murasama.png");
    }
}
