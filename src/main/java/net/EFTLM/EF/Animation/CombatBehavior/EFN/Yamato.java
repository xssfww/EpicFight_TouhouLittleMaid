package net.EFTLM.EF.Animation.CombatBehavior.EFN;

import com.hm.efn.gameasset.EFNAnimations;
import com.hm.efn.gameasset.animations.EFNDodgeAnimations;
import com.hm.efn.gameasset.animations.EFNYamatoAnimations;
import net.EFTLM.EF.Animation.CombatBehavior.BehaviorsBuild;
import net.EFTLM.EF.Compat.EFNCompat;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;
public class Yamato {
    public static CombatBehaviors.Builder<HumanoidMobPatch<?>> getInstance() {
        return EFNCompatHolder.Instance;
    }
    private static class EFNCompatHolder {
        static final CombatBehaviors.Builder<HumanoidMobPatch<?>> Instance;
        static {
            Instance = CombatBehaviors.<HumanoidMobPatch<?>>builder()
                    .newBehaviorSeries(
                            CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                    .cooldown(20)
                                    .weight(100.0F)
                                    .canBeInterrupted(false)
                                    .looping(false)
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNDodgeAnimations.YAMATO_STEP_B)
                                                    .withinDistance(0.0D, 10.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_JUDEMENCUT_JUST_MOB)
                                                    .withinDistance(0.0D, 10.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNDodgeAnimations.YAMATO_STEP_L)
                                                    .withinDistance(0.0D, 10.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_JUDEMENCUT_JUST_MOB)
                                                    .withinDistance(0.0D, 10.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNDodgeAnimations.YAMATO_STEP_R)
                                                    .withinDistance(0.0D, 10.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_JUDEMENCUT_JUST_MOB)
                                                    .withinDistance(0.0D, 10.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNDodgeAnimations.YAMATO_STEP_F)
                                                    .withinDistance(0.0D, 10.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_JUDEMENCUT_JUST_MOB)
                                                    .withinDistance(0.0D, 10.0D))
                    )
                    .newBehaviorSeries(
                            CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                    .cooldown(20)
                                    .weight(100.0F)
                                    .canBeInterrupted(false)
                                    .looping(false)
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_NORMAL_AUTO1)
                                                    .withinDistance(0.0D, 5.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_NORMAL_AUTO2)
                                                    .withinDistance(0.0D, 5.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_EXTEND_AUTO3)
                                                    .withinDistance(0.0D, 5.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_EXTEND_AUTO4)
                                                    .withinDistance(0.0D, 5.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_EXTEND_AUTO5)
                                                    .withinDistance(0.0D, 5.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNDodgeAnimations.YAMATO_STEP_B)
                                                    .withinDistance(0.0D, 5.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_JUDEMENCUT_JUST_MOB)
                                                    .withinDistance(0.0D, 10.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNDodgeAnimations.YAMATO_STEP_F)
                                                    .withinDistance(0.0D, 10.0D))
                    )
                    .newBehaviorSeries(
                            CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                    .cooldown(40)
                                    .weight(100.0F)
                                    .canBeInterrupted(false)
                                    .looping(false)
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_DIVORCE_AUTO1)
                                                    .withinDistance(0.0D, 3.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_DIVORCE_AUTO2)
                                                    .withinDistance(0.0D, 3.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_DIVORCE_AUTO3)
                                                    .withinDistance(0.0D, 3.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNDodgeAnimations.YAMATO_STEP_B)
                                                    .withinDistance(0.0D, 10.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_JUDEMENCUT_JUST_MOB)
                                                    .withinDistance(0.0D, 10.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNDodgeAnimations.YAMATO_STEP_F)
                                                    .withinDistance(0.0D, 10.0D))
                    )
                    .newBehaviorSeries(
                            CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                    .cooldown(20)
                                    .weight(100.0F)
                                    .canBeInterrupted(false)
                                    .looping(false)
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNDodgeAnimations.YAMATO_STEP_B)
                                                    .withinDistance(0.0D, 5.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_JUDEMENCUT_JUST_MOB)
                                                    .withinDistance(0.0D, 10.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_JUDEMENCUT_JUST_MOB)
                                                    .withinDistance(0.0D, 10.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_JUDEMENCUT_JUST_MOB)
                                                    .withinDistance(0.0D, 10.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNDodgeAnimations.YAMATO_STEP_F)
                                                    .withinDistance(0.0D, 10.0D))
                    )
                    .newBehaviorSeries(
                            CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                    .cooldown(20)
                                    .weight(100.0F)
                                    .canBeInterrupted(false)
                                    .looping(false)
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_FLARECUT_RISING)
                                                    .withinDistance(0.0D, 2.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_AERIALRAVE_AUTO1)
                                                    .withinDistance(0.0D, 5.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_AERIALRAVE_AUTO2)
                                                    .withinDistance(0.0D, 5.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_AERIALRAVE_AUTO3)
                                                    .withinDistance(0.0D, 5.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_ORBIT_1)
                                                    .withinDistance(0.0D, 5.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_ORBIT_1)
                                                    .withinDistance(0.0D, 5.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_ORBIT_1)
                                                    .withinDistance(0.0D, 5.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_KILLERBEE)
                                                    .withinDistance(0.0D, 5.0D))
                    )
                    .newBehaviorSeries(
                            CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                    .cooldown(20)
                                    .weight(100.0F)
                                    .canBeInterrupted(false)
                                    .looping(false)
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_REPAIDSLASH)
                                                    .withinDistance(0.0D, 10.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_FLARECUT_RISING)
                                                    .withinDistance(0.0D, 3.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_AERIALRAVE_AUTO1)
                                                    .withinDistance(0.0D, 5.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_AERIALRAVE_AUTO2)
                                                    .withinDistance(0.0D, 5.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_AERIALRAVE_AUTO3)
                                                    .withinDistance(0.0D, 5.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_HELMBREAKER)
                                                    .withinDistance(0.0D, 3.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_VOLCANOL_CHARGE)
                                                    .withinDistance(0.0D, 3.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNDodgeAnimations.YAMATO_STEP_B)
                                                    .withinDistance(0.0D, 10.0D))
                    )
                    .newBehaviorSeries(
                            CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                    .cooldown(20)
                                    .weight(100.0F)
                                    .canBeInterrupted(false)
                                    .looping(false)
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_REPAIDSLASH)
                                                    .withinDistance(0.0D, 10.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNDodgeAnimations.YAMATO_STEP_B)
                                                    .withinDistance(0.0D, 10.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_JUDEMENCUT_JUST_MOB)
                                                    .withinDistance(0.0D, 10.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_JUDEMENCUT_JUST_MOB)
                                                    .withinDistance(0.0D, 10.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_JUDEMENCUT_JUST_MOB)
                                                    .withinDistance(0.0D, 10.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNDodgeAnimations.YAMATO_STEP_F)
                                                    .withinDistance(0.0D, 10.0D))
                    )
                    .newBehaviorSeries(
                            CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                    .cooldown(10)
                                    .weight(100.0F)
                                    .canBeInterrupted(false)
                                    .looping(false)
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .behavior(EFNCompat::summonAtWaist)
                                                    .withinDistance(0.0D, 20.0D))
                    )
                    .newBehaviorSeries(
                            CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                    .cooldown(20)
                                    .weight(100.0F)
                                    .canBeInterrupted(false)
                                    .looping(false)
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .custom(BehaviorsBuild::canUseSkill)
                                                    .behavior(Patch -> {
                                                        Patch.playAnimationSynchronized(EFNAnimations.DMC5_V_JC, 0F);
                                                        BehaviorsBuild.setCoolDown(Patch, 1200);
                                                    })
                                                    .withinDistance(0.0D, 24.0D))
                    );
        }
    }
}
