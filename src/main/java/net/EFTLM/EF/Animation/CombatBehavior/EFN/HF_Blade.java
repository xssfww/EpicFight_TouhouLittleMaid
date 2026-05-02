package net.EFTLM.EF.Animation.CombatBehavior.EFN;

import com.hm.efn.gameasset.animations.EFNDodgeAnimations;
import com.hm.efn.gameasset.animations.EFNHfBladeAnimations;
import com.hm.efn.gameasset.animations.EFNZansetsuAnimations_B;
import net.EFTLM.EF.Compat.CompatModList;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;
public class HF_Blade {
    public static CombatBehaviors.Builder<HumanoidMobPatch<?>> Instance;
    static {
        if (CompatModList.LoadedEFN()) {
            Instance = CombatBehaviors.<HumanoidMobPatch<?>>builder()
                    .newBehaviorSeries(CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                            .cooldown(10)
                            .weight(100.0F)
                            .canBeInterrupted(false)
                            .looping(false)
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNDodgeAnimations.MURASAMA_ROLL_F)
                                            .withinDistance(8.0D, 20.0D))
                    )
                    .newBehaviorSeries(CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                            .cooldown(20)
                            .weight(100.0F)
                            .canBeInterrupted(false)
                            .looping(false)
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNHfBladeAnimations.HF_BLADE_X)
                                            .withinDistance(0.0D, 5.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNHfBladeAnimations.HF_BLADE_XX)
                                            .withinDistance(0.0D, 5.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNHfBladeAnimations.HF_BLADE_DASH_Y_SP)
                                            .withinDistance(0.0D, 5.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNHfBladeAnimations.HF_BLADE_XXY_CHARGE)
                                            .withinDistance(0.0D, 5.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNHfBladeAnimations.HF_BLADE_XXXY)
                                            .withinDistance(0.0D, 5.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNHfBladeAnimations.HF_BLADE_XXXY_CHARGE)
                                            .withinDistance(0.0D, 5.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNHfBladeAnimations.HF_BLADE_XXXX)
                                            .withinDistance(0.0D, 5.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNDodgeAnimations.MURASAMA_ROLL_F)
                                            .withinDistance(0.0D, 5.0D))
                    )
                    .newBehaviorSeries(CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                            .cooldown(20)
                            .weight(100.0F)
                            .canBeInterrupted(false)
                            .looping(false)
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNHfBladeAnimations.HF_BLADE_DASH_Y_SP)
                                            .withinDistance(0.0D, 2.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNHfBladeAnimations.HF_BLADE_XY_CHARGE)
                                            .withinDistance(0.0D, 2.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNHfBladeAnimations.HF_BLADE_X_AIR)
                                            .withinDistance(0.0D, 2.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNHfBladeAnimations.HF_BLADE_XX_AIR)
                                            .withinDistance(0.0D, 2.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNHfBladeAnimations.HF_BLADE_Y_CHARGE_AIR)
                                            .withinDistance(0.0D, 2.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNDodgeAnimations.MURASAMA_ROLL_B)
                                            .withinDistance(0.0D, 20.0D))
                    )
                    .newBehaviorSeries(CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                            .cooldown(400)
                            .weight(100.0F)
                            .canBeInterrupted(false)
                            .looping(false)
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNHfBladeAnimations.HF_BLADE_DASH_Y_SP)
                                            .withinDistance(0.0D, 2.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNHfBladeAnimations.HF_BLADE_XY_CHARGE)
                                            .withinDistance(0.0D, 2.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNZansetsuAnimations_B.HF_BLADE_SLASH_DIAGONAL_LR_UP_AIR)
                                            .withinDistance(0.0D, 2.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNZansetsuAnimations_B.HF_BLADE_SLASH_DIAGONAL_LR_DOWN_AIR)
                                            .withinDistance(0.0D, 2.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNZansetsuAnimations_B.HF_BLADE_SLASH_DIAGONAL_RL_UP_AIR)
                                            .withinDistance(0.0D, 2.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNZansetsuAnimations_B.HF_BLADE_SLASH_DIAGONAL_RL_DOWN_AIR)
                                            .withinDistance(0.0D, 2.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNZansetsuAnimations_B.HF_BLADE_SLASH_HORIZONTAL_LR_UP_AIR)
                                            .withinDistance(0.0D, 2.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNZansetsuAnimations_B.HF_BLADE_SLASH_HORIZONTAL_LR_DOWN_AIR)
                                            .withinDistance(0.0D, 2.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNZansetsuAnimations_B.HF_BLADE_SLASH_HORIZONTAL_RL_UP_AIR)
                                            .withinDistance(0.0D, 2.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNZansetsuAnimations_B.HF_BLADE_SLASH_HORIZONTAL_RL_DOWN_AIR)
                                            .withinDistance(0.0D, 2.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNHfBladeAnimations.HF_BLADE_ZANDATSU_AIR)
                                            .withinDistance(0.0D, 2.0D))
                    )
                    .newBehaviorSeries(CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                            .cooldown(400)
                            .weight(100.0F)
                            .canBeInterrupted(false)
                            .looping(false)
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNZansetsuAnimations_B.HF_BLADE_SLASH_DIAGONAL_LR_UP)
                                            .withinDistance(0.0D, 2.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNZansetsuAnimations_B.HF_BLADE_SLASH_DIAGONAL_LR_DOWN)
                                            .withinDistance(0.0D, 2.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNZansetsuAnimations_B.HF_BLADE_SLASH_DIAGONAL_RL_UP)
                                            .withinDistance(0.0D, 2.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNZansetsuAnimations_B.HF_BLADE_SLASH_DIAGONAL_RL_DOWN)
                                            .withinDistance(0.0D, 2.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNZansetsuAnimations_B.HF_BLADE_SLASH_HORIZONTAL_LR_UP)
                                            .withinDistance(0.0D, 2.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNZansetsuAnimations_B.HF_BLADE_SLASH_HORIZONTAL_LR_DOWN)
                                            .withinDistance(0.0D, 2.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNZansetsuAnimations_B.HF_BLADE_SLASH_HORIZONTAL_RL_UP)
                                            .withinDistance(0.0D, 2.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNZansetsuAnimations_B.HF_BLADE_SLASH_HORIZONTAL_RL_DOWN)
                                            .withinDistance(0.0D, 2.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNHfBladeAnimations.HF_BLADE_ZANDATSU)
                                            .withinDistance(0.0D, 2.0D))
                    );
        }
    }
}
