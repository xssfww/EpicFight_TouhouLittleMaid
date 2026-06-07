package net.EFTLM.EF.Skill;

import com.google.common.collect.Maps;
import net.EFTLM.EF.API.Event.MaidSkillBuildEvent;
import net.EFTLM.EF.Skill.WeaponInnate.WeaponInnateSkill;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModLoader;
import java.util.Map;
import java.util.Set;
public class MaidSkillManager {
    protected static Map<ResourceLocation, MaidSkill> MaidSkillRegister = Maps.newHashMap();
    protected static Map<Item, WeaponInnateSkill> WeaponSkillRegister = Maps.newHashMap();
    public static void MaidSkillBuild() {
        ModLoader.get().postEvent(new MaidSkillBuildEvent(MaidSkillRegister,WeaponSkillRegister));
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
    public static Set<ResourceLocation> getSkillRegisterName() {
        return MaidSkillRegister.keySet();
    }
}
