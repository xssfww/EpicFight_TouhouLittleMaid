package net.EFTLM.EF.API.Event;

import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillBuilder;
import net.EFTLM.EF.Skill.WeaponInnate.WeaponInnateSkill;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import java.util.Map;
import java.util.function.Function;
public class MaidSkillBuildEvent extends Event implements IModBusEvent {
    private final Map<ResourceLocation, MaidSkill> MaidSkillRegister;
    private final Map<Item, WeaponInnateSkill> WeaponInnateRegister;
    public MaidSkillBuildEvent(Map<ResourceLocation, MaidSkill> MaidSkillRegister,Map<Item, WeaponInnateSkill> WeaponInnateRegister) {
        this.MaidSkillRegister = MaidSkillRegister;
        this.WeaponInnateRegister = WeaponInnateRegister;
    }
    public void build(ResourceLocation RegisterName, Function<MaidSkillBuilder, MaidSkill> constructor, MaidSkillBuilder builder) {
        builder.setRegistryName(RegisterName);
        MaidSkill skill = constructor.apply(builder);
        MaidSkillRegister.put(RegisterName,skill);
    }
    public void build(ResourceLocation RegisterName, Function<MaidSkillBuilder, WeaponInnateSkill> constructor, MaidSkillBuilder builder,Item item) {
        builder.setRegistryName(RegisterName);
        WeaponInnateSkill skill = constructor.apply(builder);
        WeaponInnateRegister.put(item,skill);
        MaidSkillRegister.put(RegisterName,skill);
    }
}
