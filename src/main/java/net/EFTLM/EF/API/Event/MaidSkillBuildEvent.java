package net.EFTLM.EF.API.Event;

import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillBuilder;
import net.EFTLM.EF.Skill.WeaponInnate.WeaponInnateSkill;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.fml.ModLoader;
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
    public <S extends MaidSkill, B extends MaidSkillBuilder<?>> void build(ResourceLocation RegisterName, Function<B, S> constructor, B builder) {
        builder.setRegistryName(RegisterName);
        MaidSkillBuildEvent.SkillCreateEvent<B> CreateEvent = new SkillCreateEvent<>(builder);
        ModLoader.get().postEvent(CreateEvent);
        MaidSkill skill = constructor.apply(builder);
        MaidSkillRegister.put(RegisterName, skill);
    }
    public <S extends WeaponInnateSkill, B extends MaidSkillBuilder<?>> void build(ResourceLocation RegisterName, Function<B, S> constructor, B builder,Item item) {
        builder.setRegistryName(RegisterName);
        MaidSkillBuildEvent.SkillCreateEvent<B> CreateEvent = new SkillCreateEvent<>(builder);
        ModLoader.get().postEvent(CreateEvent);
        WeaponInnateSkill skill = constructor.apply(builder);
        WeaponInnateRegister.put(item,skill);
        MaidSkillRegister.put(RegisterName,skill);
    }
    @SuppressWarnings("unchecked")
    public static class SkillCreateEvent<B extends MaidSkillBuilder<?>> extends GenericEvent<B> implements IModBusEvent {
        private final B builder;
        private SkillCreateEvent(B builder) {
            super((Class<B>) builder.getClass());
            this.builder = builder;
        }
        public B getSkillBuilder() {
            return this.builder;
        }
    }
}
