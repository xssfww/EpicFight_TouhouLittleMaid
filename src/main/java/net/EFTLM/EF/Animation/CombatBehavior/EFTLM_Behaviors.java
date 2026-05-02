package net.EFTLM.EF.Animation.CombatBehavior;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.Item;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;
import yesman.epicfight.world.item.EpicFightItems;
import java.util.Map;
public class EFTLM_Behaviors {
    public static final CombatBehaviors.Builder<HumanoidMobPatch<?>> Sword_OneHand;
    public static final CombatBehaviors.Builder<HumanoidMobPatch<?>> Sword_TwoHand;
    public static final CombatBehaviors.Builder<HumanoidMobPatch<?>> LongSword_OneHand;
    public static final CombatBehaviors.Builder<HumanoidMobPatch<?>> LongSword_TwoHand;
    public static final CombatBehaviors.Builder<HumanoidMobPatch<?>> GreatSword;
    public static final CombatBehaviors.Builder<HumanoidMobPatch<?>> Tachi;
    public static final CombatBehaviors.Builder<HumanoidMobPatch<?>> Uchigatana;
    public static final CombatBehaviors.Builder<HumanoidMobPatch<?>> Spear_OneHand;
    public static final CombatBehaviors.Builder<HumanoidMobPatch<?>> Spear_TwoHand;
    public static void SetWeaponMotions(Map<WeaponCategory, Map<Style, CombatBehaviors.Builder<HumanoidMobPatch<?>>>> WeaponAttackMotions, Map<Item, HumanoidArmature> ItemArmatures) {
        WeaponAttackMotions.put(CapabilityItem.WeaponCategories.SWORD, ImmutableMap.of(
                CapabilityItem.Styles.ONE_HAND, EFTLM_Behaviors.Sword_OneHand,
                CapabilityItem.Styles.TWO_HAND, EFTLM_Behaviors.Sword_TwoHand)
        );
        WeaponAttackMotions.put(CapabilityItem.WeaponCategories.LONGSWORD, ImmutableMap.of(
                CapabilityItem.Styles.ONE_HAND, EFTLM_Behaviors.LongSword_OneHand,
                CapabilityItem.Styles.TWO_HAND, EFTLM_Behaviors.LongSword_TwoHand)
        );
        WeaponAttackMotions.put(CapabilityItem.WeaponCategories.GREATSWORD, ImmutableMap.of(
                CapabilityItem.Styles.TWO_HAND, EFTLM_Behaviors.GreatSword)
        );
        WeaponAttackMotions.put(CapabilityItem.WeaponCategories.TACHI, ImmutableMap.of(
                CapabilityItem.Styles.TWO_HAND, EFTLM_Behaviors.Tachi)
        );
        WeaponAttackMotions.put(CapabilityItem.WeaponCategories.UCHIGATANA, ImmutableMap.of(
                CapabilityItem.Styles.TWO_HAND, EFTLM_Behaviors.Uchigatana)
        );
        WeaponAttackMotions.put(CapabilityItem.WeaponCategories.SPEAR, ImmutableMap.of(
                CapabilityItem.Styles.ONE_HAND, EFTLM_Behaviors.Spear_OneHand,
                CapabilityItem.Styles.TWO_HAND, EFTLM_Behaviors.Spear_TwoHand)
        );
        ItemArmatures.put(EpicFightItems.UCHIGATANA.get(), Armatures.BIPED.get());
    }
    static {
        Sword_OneHand = CombatBehaviors.<HumanoidMobPatch<?>>builder()
                .newBehaviorSeries(
                        CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                .cooldown(40)
                                .weight(100.0F)
                                .canBeInterrupted(false)
                                .looping(false)
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.SWEEPING_EDGE).withinDistance(0.0D, 3.5D))
                )
                .newBehaviorSeries(
                        CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                .cooldown(20)
                                .weight(100.0F)
                                .canBeInterrupted(false)
                                .looping(false)
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.SWORD_DASH).withinDistance(1.0D, 3.5D))
                )
                .newBehaviorSeries(
                        CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                .cooldown(10)
                                .weight(100.0F)
                                .canBeInterrupted(false)
                                .looping(false)
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.SWORD_AUTO1).withinDistance(0.0D, 3.5D))
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.SWORD_AUTO2).withinDistance(0.0D, 3.5D))
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.SWORD_AUTO3).withinDistance(0.0D, 3.5D))
                );
        Sword_TwoHand = CombatBehaviors.<HumanoidMobPatch<?>>builder()
                .newBehaviorSeries(
                        CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                .cooldown(40)
                                .weight(100.0F)
                                .canBeInterrupted(false)
                                .looping(false)
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.DANCING_EDGE).withinDistance(0.0D, 3.5D))
                )
                .newBehaviorSeries(
                        CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                .cooldown(20)
                                .weight(100.0F)
                                .canBeInterrupted(false)
                                .looping(false)
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.SWORD_DUAL_DASH).withinDistance(1.0D, 3.5D))
                )
                .newBehaviorSeries(
                        CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                .cooldown(10)
                                .weight(100.0F)
                                .canBeInterrupted(false)
                                .looping(false)
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.SWORD_DUAL_AUTO1).withinDistance(0.0D, 3.5D))
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.SWORD_DUAL_AUTO2).withinDistance(0.0D, 3.5D))
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.SWORD_DUAL_AUTO3).withinDistance(0.0D, 3.5D))
                );
        LongSword_OneHand = CombatBehaviors.<HumanoidMobPatch<?>>builder()
                .newBehaviorSeries(
                        CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                .cooldown(40)
                                .weight(100.0F)
                                .canBeInterrupted(false)
                                .looping(false)
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.SHARP_STAB).withinDistance(0.0D, 3.5D))
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.EVISCERATE_SECOND).withinDistance(0.0D, 3.5D))
                )
                .newBehaviorSeries(
                        CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                .cooldown(20)
                                .weight(100.0F)
                                .canBeInterrupted(false)
                                .looping(false)
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.LONGSWORD_DASH).withinDistance(1.0D, 3.5D))
                )
                .newBehaviorSeries(
                        CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                .cooldown(10)
                                .weight(100.0F)
                                .canBeInterrupted(false)
                                .looping(false)
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.LONGSWORD_AUTO1).withinDistance(0.0D, 3.5D))
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.LONGSWORD_AUTO2).withinDistance(0.0D, 3.5D))
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.LONGSWORD_AUTO3).withinDistance(0.0D, 3.5D))
                );
        LongSword_TwoHand = CombatBehaviors.<HumanoidMobPatch<?>>builder()
                .newBehaviorSeries(
                        CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                .cooldown(20)
                                .weight(100.0F)
                                .canBeInterrupted(false)
                                .looping(false)
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.LONGSWORD_DASH).withinDistance(1.0D, 3.5D))
                )
                .newBehaviorSeries(
                        CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                .cooldown(10)
                                .weight(100.0F)
                                .canBeInterrupted(false)
                                .looping(false)
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.LONGSWORD_AUTO1).withinDistance(0.0D, 3.5D))
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.LONGSWORD_AUTO2).withinDistance(0.0D, 3.5D))
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.LONGSWORD_AUTO3).withinDistance(0.0D, 3.5D))
                );
        GreatSword = CombatBehaviors.<HumanoidMobPatch<?>>builder()
                .newBehaviorSeries(
                        CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                .cooldown(20)
                                .weight(100.0F)
                                .canBeInterrupted(false)
                                .looping(true)
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.GREATSWORD_DASH).withinDistance(1.5D, 4.5D))
                )
                .newBehaviorSeries(
                        CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                .cooldown(10)
                                .weight(100.0F)
                                .canBeInterrupted(false)
                                .looping(true)
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.GREATSWORD_AUTO1).withinDistance(0.0D, 4.5D))
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.GREATSWORD_AUTO2).withinDistance(0.0D, 4.5D))
                );
        Tachi = CombatBehaviors.<HumanoidMobPatch<?>>builder()
                .newBehaviorSeries(
                        CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                .weight(100.0F)
                                .canBeInterrupted(false)
                                .looping(true)
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.TACHI_AUTO1).withinDistance(0.0D, 3.5D))
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.RUSHING_TEMPO1).withinDistance(0.0D, 3.5D))
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.TACHI_AUTO2).withinDistance(0.0D, 3.5D))
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.RUSHING_TEMPO2).withinDistance(0.0D, 3.5D))
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.TACHI_AUTO3).withinDistance(0.0D, 3.5D))
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.RUSHING_TEMPO3).withinDistance(0.0D, 3.5D))
                );
        Uchigatana = CombatBehaviors.<HumanoidMobPatch<?>>builder()
                .newBehaviorSeries(
                        CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                .cooldown(60)
                                .weight(100.0F)
                                .canBeInterrupted(false)
                                .looping(false)
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.BATTOJUTSU_DASH).withinDistance(3.0D, 6.0D))
                )
                .newBehaviorSeries(
                        CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                .cooldown(40)
                                .weight(100.0F)
                                .canBeInterrupted(false)
                                .looping(false)
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.BATTOJUTSU).withinDistance(0.0D, 3.0D))
                )
                .newBehaviorSeries(
                        CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                .cooldown(20)
                                .weight(100.0F)
                                .canBeInterrupted(false)
                                .looping(false)
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.UCHIGATANA_DASH).withinDistance(1.5D, 4.0D))
                )
                .newBehaviorSeries(
                        CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                .cooldown(20)
                                .weight(100.0F)
                                .canBeInterrupted(false)
                                .looping(false)
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.UCHIGATANA_AUTO1).withinDistance(0.0D, 3.5D))
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.UCHIGATANA_AUTO2).withinDistance(0.0D, 3.5D))
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.UCHIGATANA_AUTO3).withinDistance(0.0D, 3.5D))
                );
        Spear_OneHand = CombatBehaviors.<HumanoidMobPatch<?>>builder()
                .newBehaviorSeries(
                        CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                .cooldown(40)
                                .weight(100.0F)
                                .canBeInterrupted(false)
                                .looping(false)
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.HEARTPIERCER).withinDistance(0.0D, 5.0D))
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.EVISCERATE_SECOND).withinDistance(0.0D, 5.0D))
                )
                .newBehaviorSeries(
                        CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                .cooldown(20)
                                .weight(100.0F)
                                .canBeInterrupted(false)
                                .looping(false)
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.SPEAR_ONEHAND_AUTO).withinDistance(0.0D, 4.0D))
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.SPEAR_ONEHAND_AUTO).withinDistance(0.0D, 4.0D))
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.SPEAR_ONEHAND_AUTO).withinDistance(0.0D, 4.0D))
                );
        Spear_TwoHand = CombatBehaviors.<HumanoidMobPatch<?>>builder()
                .newBehaviorSeries(
                        CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                .cooldown(40)
                                .weight(100.0F)
                                .canBeInterrupted(false)
                                .looping(false)
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.GRASPING_SPIRAL_FIRST).withinDistance(0.0D, 5.0D))
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.GRASPING_SPIRAL_SECOND).withinDistance(0.0D, 5.0D))
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.EVISCERATE_SECOND).withinDistance(0.0D, 5.0D))
                )
                .newBehaviorSeries(
                        CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                .cooldown(20)
                                .weight(100.0F)
                                .canBeInterrupted(false)
                                .looping(false)
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.SPEAR_TWOHAND_AUTO1).withinDistance(0.0D, 4.0D))
                                .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                        .animationBehavior(Animations.SPEAR_TWOHAND_AUTO2).withinDistance(0.0D, 4.0D))
                );
    }
}
