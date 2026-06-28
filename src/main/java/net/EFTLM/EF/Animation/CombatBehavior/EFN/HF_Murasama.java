package net.EFTLM.EF.Animation.CombatBehavior.EFN;

import com.hm.efn.gameasset.animations.EFNMurasamaAnimations;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Compat.CompatModList;
import net.EFTLM.EF.Skill.MaidSkillManager;
import net.EFTLM.EF.Skill.WeaponInnate.EFN.HF_MurasamaSkill;
import net.minecraft.resources.ResourceLocation;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;
public class HF_Murasama {
    public static CombatBehaviors.Builder<HumanoidMobPatch<?>> Instance;
    static {
        if (CompatModList.LoadedEFN()) {
            Instance = CombatBehaviors.<HumanoidMobPatch<?>>builder()
                    .newBehaviorSeries(CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                            .cooldown(20)
                            .weight(100.0F)
                            .canBeInterrupted(false)
                            .looping(false)
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNMurasamaAnimations.HF_MURASAMA_X)
                                            .withinDistance(0.0D, 5.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNMurasamaAnimations.HF_MURASAMA_XX)
                                            .withinDistance(0.0D, 5.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNMurasamaAnimations.HF_MURASAMA_DASH_Y_SP)
                                            .withinDistance(0.0D, 5.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNMurasamaAnimations.HF_MURASAMA_XXY_CHARGE)
                                            .withinDistance(0.0D, 5.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNMurasamaAnimations.HF_MURASAMA_XXXY)
                                            .withinDistance(0.0D, 5.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNMurasamaAnimations.HF_MURASAMA_XXXY_CHARGE)
                                            .withinDistance(0.0D, 5.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNMurasamaAnimations.HF_MURASAMA_XXXX)
                                            .withinDistance(0.0D, 5.0D))
                    )
                    .newBehaviorSeries(CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                            .cooldown(20)
                            .weight(100.0F)
                            .canBeInterrupted(false)
                            .looping(false)
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNMurasamaAnimations.HF_MURASAMA_X_AIR)
                                            .withinDistance(0.0D, 2.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNMurasamaAnimations.HF_MURASAMA_XX_AIR)
                                            .withinDistance(0.0D, 2.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNMurasamaAnimations.HF_MURASAMA_Y_CHARGE_THROUGH)
                                            .withinDistance(0.0D, 4.0D))
                    )
                    .newBehaviorSeries(CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                            .cooldown(20)
                            .weight(100.0F)
                            .canBeInterrupted(false)
                            .looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                    .animationBehavior(EFNMurasamaAnimations.HF_MURASAMA_Y_CHARGE_THROUGH)
                                    .withinDistance(2.0D, 4.0D))
                    )
                    .newBehaviorSeries(CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                            .cooldown(300)
                            .weight(100.0F)
                            .canBeInterrupted(false)
                            .looping(false)
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .behavior(patch -> {
                                                if (patch instanceof MaidPatch<?> maid) {
                                                    for (ResourceLocation RL : maid.getLearnedSkills()) {
                                                        if (MaidSkillManager.getSkillFor(RL) instanceof HF_MurasamaSkill murasama) {
                                                            maid.setData(murasama, HF_MurasamaSkill.isZansetsu,true);
                                                        }
                                                    }
                                                }
                                            })
                                            .withinDistance(0.0D, 2.0D))
                    );
        }
    }
}
