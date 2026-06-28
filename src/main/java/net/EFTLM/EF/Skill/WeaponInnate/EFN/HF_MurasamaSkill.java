package net.EFTLM.EF.Skill.WeaponInnate.EFN;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTickEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
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
import net.minecraft.server.level.ServerLevel;
import org.apache.commons.compress.utils.Lists;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import java.util.List;
public class HF_MurasamaSkill extends WeaponInnateSkill {
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
    public void onInit(MaidSkillInitEvent event) {
        MaidPatch<?> MaidPatch = event.getMaidPatch();
        MaidPatch.registerData(this,isZansetsu,false);
        MaidPatch.registerData(this,ZansetsuDelay,0);
        MaidPatch.registerData(this,ZansetsuIndex,0);
    }
    @Override
    public void MaidTick(MaidTickEvent event) {
        EntityMaid Maid = event.getMaid();
        MaidPatch<?> MaidPatch = EpicFightCapabilities.getEntityPatch(Maid, MaidPatch.class);
        if (Maid.level() instanceof ServerLevel) {
            if (MaidPatch != null) {
                if (MaidPatch.getDataValue(this,ZansetsuDelay) != null) {
                    int Delay = MaidPatch.getDataValue(this, ZansetsuDelay);
                    if (MaidPatch.getDataValue(this, isZansetsu) != null) {
                        if (MaidPatch.getDataValue(this, isZansetsu)) {
                            if (Maid.tickCount - Delay > 5) {
                                MaidPatch.setData(this, ZansetsuDelay, Maid.tickCount);
                                if (!ZansetsuList.isEmpty()) {
                                    if (MaidPatch.getDataValue(this, ZansetsuIndex) != null) {
                                        int index = MaidPatch.getDataValue(this, ZansetsuIndex);
                                        MaidPatch.playAnimationSynchronized(ZansetsuList.get(index), 0F);
                                        index++;
                                        if (index >= ZansetsuList.size()) {
                                            MaidPatch.setData(this, isZansetsu, false);
                                            MaidPatch.setData(this, ZansetsuIndex, 0);
                                        } else {
                                            MaidPatch.setData(this, ZansetsuIndex, index);
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
