package net.EFTLM.EFN.CombatBehavior;

import com.hm.efn.gameasset.EFNAnimations;
import com.hm.efn.gameasset.animations.EFNYamatoAnimations;
import net.EFTLM.EF.Animation.BehaviorsBuild;
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
                                    .weight(110.0F)
                                    .canBeInterrupted(false)
                                    .looping(false)
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_NORMAL_AUTO1)
                                                    .withinDistance(0.0D, 4.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_NORMAL_AUTO2)
                                                    .withinDistance(0.0D, 4.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_NORMAL_AUTO3)
                                                    .withinDistance(0.0D, 4.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_EXTEND_AUTO3)
                                                    .withinDistance(0.0D, 4.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_EXTEND_AUTO4)
                                                    .withinDistance(0.0D, 4.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_EXTEND_AUTO5)
                                                    .withinDistance(0.0D, 4.0D))
                    )
                    .newBehaviorSeries(
                            CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                    .cooldown(45)
                                    .weight(80.0F)
                                    .canBeInterrupted(false)
                                    .looping(false)
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_FLARECUT_RISING)
                                                    .withinDistance(0.0D, 4.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_ORBIT_1)
                                                    .withinDistance(0.0D, 4.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_ORBIT_2)
                                                    .withinDistance(0.0D, 4.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_HELMBREAKER)
                                                    .withinDistance(0.0D, 4.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_NORMAL_AUTO1)
                                                    .withinDistance(0.0D, 4.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_NORMAL_AUTO2)
                                                    .withinDistance(0.0D, 4.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_NORMAL_AUTO3)
                                                    .withinDistance(0.0D, 4.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_FLARECUT)
                                                    .withinDistance(0.0D, 4.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_EXTEND_AUTO3)
                                                    .withinDistance(0.0D, 4.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_EXTEND_AUTO4)
                                                    .withinDistance(0.0D, 4.0D))
                    )
                    .newBehaviorSeries(
                            CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                    .cooldown(45)
                                    .weight(80.0F)
                                    .canBeInterrupted(false)
                                    .looping(false)
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_DIVORCE_AUTO1)
                                                    .withinDistance(0.0D, 3.5D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_DIVORCE_AUTO2)
                                                    .withinDistance(0.0D, 3.5D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_DIVORCE_AUTO3)
                                                    .withinDistance(0.0D, 3.5D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_FLARECUT_RISING)
                                                    .withinDistance(0.0D, 4.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_HELMBREAKER)
                                                    .withinDistance(0.0D, 4.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_FLARECUT)
                                                    .withinDistance(0.0D, 4.0D))
                    )
                    .newBehaviorSeries(
                            CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                    .cooldown(70)
                                    .weight(60.0F)
                                    .canBeInterrupted(false)
                                    .looping(false)
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_FLARECUT_RISING)
                                                    .withinDistance(0.0D, 3.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_AERIALRAVE_AUTO1)
                                                    .withinDistance(0.0D, 4.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_AERIALRAVE_AUTO2)
                                                    .withinDistance(0.0D, 4.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_AERIALRAVE_AUTO3)
                                                    .withinDistance(0.0D, 4.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_ORBIT_1)
                                                    .withinDistance(0.0D, 4.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_ORBIT_2)
                                                    .withinDistance(0.0D, 4.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_KILLERBEE)
                                                    .withinDistance(0.0D, 4.0D))
                    )
                    .newBehaviorSeries(
                            CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                    .cooldown(35)
                                    .weight(70.0F)
                                    .canBeInterrupted(false)
                                    .looping(false)
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_REPAIDSLASH_MOB)
                                                    .withinDistance(4.0D, 16.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_NORMAL_AUTO1)
                                                    .withinDistance(0.0D, 4.5D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_NORMAL_AUTO2)
                                                    .withinDistance(0.0D, 4.5D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_NORMAL_AUTO3)
                                                    .withinDistance(0.0D, 4.5D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_STOMP)
                                                    .withinDistance(0.0D, 4.5D))
                    )
                    .newBehaviorSeries(
                            CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                    .cooldown(60)
                                    .weight(70.0F)
                                    .canBeInterrupted(false)
                                    .looping(false)
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_JUDEMENCUT_JUST_MOB)
                                                    .withinDistance(4.0D, 14.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_JUDEMENCUT_JUST_MOB)
                                                    .withinDistance(0.0D, 14.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_JUDEMENCUT_JUST_MOB)
                                                    .withinDistance(0.0D, 14.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .animationBehavior(EFNYamatoAnimations.YAMATO_REPAIDSLASH_MOB)
                                                    .withinDistance(0.0D, 14.0D))
                    )
                    .newBehaviorSeries(
                            CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                    .cooldown(20)
                                    .weight(300.0F)
                                    .canBeInterrupted(false)
                                    .looping(false)
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .custom(Patch -> BehaviorsBuild.hasStack(Patch,BehaviorsBuild.getMaxStack(Patch)))
                                                    .behavior(Patch -> {
                                                            Patch.playAnimationSynchronized(EFNAnimations.DMC5_V_JC, 0F);
                                                            BehaviorsBuild.setStack(Patch,0);
                                                    })
                                                    .withinDistance(0.0D, 24.0D))
                    );
        }
    }
}
