package net.EFTLM.EF.API.Event;

import com.google.common.collect.Maps;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillBuilder;
import net.EFTLM.EF.Skill.WeaponInnate.WeaponInnateSkill;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.event.IModBusEvent;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import java.util.Map;
import java.util.function.Function;
public class MaidSkillBuildEvent extends Event implements IModBusEvent {
    private final Map<ResourceLocation, MaidSkill> MaidSkillRegister;
    private final Map<Item, WeaponInnateSkill> WeaponInnateRegister;
    private final Map<Item, Map<Style, WeaponInnateSkill>> ItemStyleInnateRegister;
    private final Map<WeaponCategory, Map<Style, WeaponInnateSkill>> CategoryStyleInnateRegister;
    public MaidSkillBuildEvent(Map<ResourceLocation, MaidSkill> MaidSkillRegister, Map<Item, WeaponInnateSkill> WeaponInnateRegister, Map<Item, Map<Style, WeaponInnateSkill>> ItemStyleInnateRegister, Map<WeaponCategory, Map<Style, WeaponInnateSkill>> CategoryStyleInnateRegister) {
        this.MaidSkillRegister = MaidSkillRegister;
        this.WeaponInnateRegister = WeaponInnateRegister;
        this.ItemStyleInnateRegister = ItemStyleInnateRegister;
        this.CategoryStyleInnateRegister = CategoryStyleInnateRegister;
    }
    public <S extends MaidSkill, B extends MaidSkillBuilder<?>> void build(ResourceLocation RegisterName, Function<B, S> constructor, B builder) {
        MaidSkill skill = createSkill(RegisterName, constructor, builder);
        MaidSkillRegister.put(RegisterName, skill);
    }
    public <S extends WeaponInnateSkill, B extends MaidSkillBuilder<?>> void build(ResourceLocation RegisterName, Function<B, S> constructor, B builder, Item... item) {
        S skill = createSkill(RegisterName, constructor, builder);
        for (Item i : item) {
            if (i != null) {
                WeaponInnateRegister.put(i, skill);
            }
        }
        MaidSkillRegister.put(RegisterName, skill);
    }
    public <S extends WeaponInnateSkill, B extends MaidSkillBuilder<?>> void build(ResourceLocation RegisterName, Function<B, S> constructor, B builder, Item item, Style style) {
        S skill = createSkill(RegisterName, constructor, builder);
        if (item != null && style != null) {
            ItemStyleInnateRegister.computeIfAbsent(item, k -> Maps.newHashMap()).put(style, skill);
        }
        MaidSkillRegister.put(RegisterName, skill);
    }
    public <S extends WeaponInnateSkill, B extends MaidSkillBuilder<?>> void build(ResourceLocation RegisterName, Function<B, S> constructor, B builder, WeaponCategory category, Style style) {
        S skill = createSkill(RegisterName, constructor, builder);
        if (category != null && style != null) {
            CategoryStyleInnateRegister.computeIfAbsent(category, k -> Maps.newHashMap()).put(style, skill);
        }
        MaidSkillRegister.put(RegisterName, skill);
    }
    private <S extends MaidSkill, B extends MaidSkillBuilder<?>> S createSkill(ResourceLocation RegisterName, Function<B, S> constructor, B builder) {
        builder.setRegistryName(RegisterName);
        ModLoader.get().postEvent(new SkillCreateEvent<>(builder));
        return constructor.apply(builder);
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
