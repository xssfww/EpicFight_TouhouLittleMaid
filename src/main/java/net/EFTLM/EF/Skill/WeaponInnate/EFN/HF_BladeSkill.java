package net.EFTLM.EF.Skill.WeaponInnate.EFN;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTickEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
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
import net.minecraft.server.level.ServerLevel;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
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
        MaidPatch<?> MaidPatch = event.getMaidPatch();
        MaidPatch.registerData(this, isZansetsu_Blade, false);
        MaidPatch.registerData(this, ZansetsuDelay_Blade, 0);
        MaidPatch.registerData(this, ZansetsuIndex_Blade, 0);
    }
    @Override
    public void MaidTick(MaidTickEvent event) {
        EntityMaid Maid = event.getMaid();
        MaidPatch<?> MaidPatch = EpicFightCapabilities.getEntityPatch(Maid, MaidPatch.class);
        if (Maid.level() instanceof ServerLevel) {
            if (MaidPatch != null) {
                if (MaidPatch.getDataValue(this, ZansetsuDelay_Blade) != null) {
                    int Delay = MaidPatch.getDataValue(this, ZansetsuDelay_Blade);
                    if (MaidPatch.getDataValue(this, isZansetsu_Blade) != null) {
                        if (MaidPatch.getDataValue(this, isZansetsu_Blade)) {
                            if (Maid.tickCount - Delay > 5) {
                                MaidPatch.setData(this, ZansetsuDelay_Blade, Maid.tickCount);
                                if (!BladeZansetsuList.isEmpty()) {
                                    if (MaidPatch.getDataValue(this, ZansetsuIndex_Blade) != null) {
                                        int index = MaidPatch.getDataValue(this, ZansetsuIndex_Blade);
                                        MaidPatch.playAnimationSynchronized(BladeZansetsuList.get(index), 0F);
                                        index++;
                                        if (index >= BladeZansetsuList.size()) {
                                            MaidPatch.setData(this, isZansetsu_Blade, false);
                                            MaidPatch.setData(this, ZansetsuIndex_Blade, 0);
                                        } else {
                                            MaidPatch.setData(this, ZansetsuIndex_Blade, index);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

