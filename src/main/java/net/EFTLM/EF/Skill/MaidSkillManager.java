package net.EFTLM.EF.Skill;

import com.google.common.collect.Maps;
import net.EFTLM.EF.API.Event.MaidSkillBuildEvent;
import net.EFTLM.EF.Skill.WeaponInnate.WeaponInnateSkill;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.fml.ModLoader;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
public class MaidSkillManager {
    protected static Map<ResourceLocation, MaidSkill> MaidSkillRegister = Maps.newHashMap();
    protected static Map<Item, WeaponInnateSkill> WeaponSkillRegister = Maps.newHashMap();
    protected static Map<Item, Map<Style, WeaponInnateSkill>> ItemStyleWeaponSkills = Maps.newHashMap();
    protected static Map<WeaponCategory, Map<Style, WeaponInnateSkill>> CategoryStyleWeaponSkills = Maps.newHashMap();
    public static void MaidSkillBuild() {
        // 1.21.1: ModLoader.get() is gone, ModLoader.postEvent is static
        ModLoader.postEvent(new MaidSkillBuildEvent(MaidSkillRegister, WeaponSkillRegister, ItemStyleWeaponSkills, CategoryStyleWeaponSkills));
    }
    public static MaidSkill getSkillFor(ResourceLocation RegisterName) {
        return MaidSkillRegister.get(RegisterName);
    }
    public static boolean hasSkillFor(ResourceLocation RegisterName) {
        return MaidSkillRegister.containsKey(RegisterName);
    }
    public static WeaponInnateSkill getSkillFor(Item Item) {
        return WeaponSkillRegister.get(Item);
    }
    public static boolean hasSkillFor(Item Item) {
        return WeaponSkillRegister.containsKey(Item);
    }
    public static WeaponInnateSkill getWeaponSkillFor(Item item, WeaponCategory category, Style style) {
        WeaponInnateSkill exact = item == null ? null : WeaponSkillRegister.get(item);
        if (exact != null) return exact;
        if (item != null) {
            WeaponInnateSkill byItemStyle = getStyleSkill(ItemStyleWeaponSkills.get(item), style);
            if (byItemStyle != null) return byItemStyle;
        }
        if (category != null) {
            return getStyleSkill(CategoryStyleWeaponSkills.get(category), style);
        }
        return null;
    }
    private static WeaponInnateSkill getStyleSkill(Map<Style, WeaponInnateSkill> byStyle, Style style) {
        if (byStyle == null || style == null) return null;
        WeaponInnateSkill skill = byStyle.get(style);
        return skill != null ? skill : byStyle.get(CapabilityItem.Styles.COMMON);
    }
    public static Set<ResourceLocation> getSkillRegisterName() {
        return MaidSkillRegister.keySet();
    }
    public static Set<ResourceLocation> getNonWeaponSkillName() {
        Set<ResourceLocation> result = new HashSet<>();
        for (Map.Entry<ResourceLocation, MaidSkill> entry : MaidSkillRegister.entrySet()) {
            if (!(entry.getValue() instanceof WeaponInnateSkill)) {
                result.add(entry.getKey());
            }
        }
        return result;
    }
}
