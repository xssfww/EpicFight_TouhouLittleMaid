package net.EFTLM.EF.Animation.CombatBehavior.EFN;

import com.hm.efn.gameasset.animations.EFNMurasamaAnimations;
import net.EFTLM.EF.Animation.CombatBehavior.BehaviorsBuild;
import net.EFTLM.EF.Compat.CompatModList;
import net.EFTLM.EF.Skill.WeaponInnate.EFN.HF_MurasamaSkill;
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
                            .cooldown(20)
                            .weight(150.0F)
                            .canBeInterrupted(false)
                            .looping(false)
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .custom(patch -> BehaviorsBuild.hasStack(patch,2))
                                            .behavior(patch -> {
                                                HF_MurasamaSkill skill = BehaviorsBuild.getWeaponInnateSkill(patch, HF_MurasamaSkill.class);
                                                if (skill == null) return;
                                                BehaviorsBuild.setData(patch,skill,HF_MurasamaSkill.isZansetsu,true);
                                                BehaviorsBuild.setStack(patch, BehaviorsBuild.getStack(patch) - 2);
                                            })
                                            .withinDistance(0.0D, 2.0D))
                    );
        }
    }
}
