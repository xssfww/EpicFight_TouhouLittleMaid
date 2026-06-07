package net.EFTLM.EF.API.Event;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.Event;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;
import java.util.Map;
public class CombatBehaviorsEvent extends Event {
    private final Map<WeaponCategory, Map<Style, CombatBehaviors.Builder<HumanoidMobPatch<?>>>> WeaponStyleAttackMotions;
    private final Map<Item, Map<Style, CombatBehaviors.Builder<HumanoidMobPatch<?>>>> ItemStyleAttackMotions;
    private final Map<Item, CombatBehaviors.Builder<HumanoidMobPatch<?>>> ItemAttackMotions;
    private final Map<Item, HumanoidArmature> ItemArmatures;
    public CombatBehaviorsEvent(Map<WeaponCategory, Map<Style, CombatBehaviors.Builder<HumanoidMobPatch<?>>>> WeaponStyleAttackMotions, Map<Item, Map<Style, CombatBehaviors.Builder<HumanoidMobPatch<?>>>> ItemStyleAttackMotions,Map<Item, CombatBehaviors.Builder<HumanoidMobPatch<?>>> ItemAttackMotions, Map<Item, HumanoidArmature> ItemArmatures) {
        this.WeaponStyleAttackMotions = WeaponStyleAttackMotions;
        this.ItemStyleAttackMotions = ItemStyleAttackMotions;
        this.ItemAttackMotions = ItemAttackMotions;
        this.ItemArmatures = ItemArmatures;
    }
    public Map<WeaponCategory, Map<Style, CombatBehaviors.Builder<HumanoidMobPatch<?>>>> getWeaponStyleAttackMotions() {
        return this.WeaponStyleAttackMotions;
    }
    public Map<Item, Map<Style, CombatBehaviors.Builder<HumanoidMobPatch<?>>>> getItemStyleAttackMotions() {
        return this.ItemStyleAttackMotions;
    }
    public Map<Item, CombatBehaviors.Builder<HumanoidMobPatch<?>>> getItemAttackMotions() {
        return this.ItemAttackMotions;
    }
    public Map<Item, HumanoidArmature> getItemArmatures() {
        return this.ItemArmatures;
    }
}
