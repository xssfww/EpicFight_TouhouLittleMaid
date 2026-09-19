package net.EFTLM.EF.Skill;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
public class MaidSkillBuilder<T extends MaidSkill> {
    protected ResourceLocation registryName;
    protected ResourceLocation iconName;
    protected CreativeModeTab tab;
    public MaidSkillBuilder() {
    }
    public void setRegistryName(ResourceLocation registryName) {
        this.registryName = registryName;
    }
    @SuppressWarnings("unchecked")
    public <B extends MaidSkillBuilder<T>> B setCreativeTab(CreativeModeTab tab) {
        this.tab = tab;
        return (B)this;
    }
    @SuppressWarnings("unchecked")
    public <B extends MaidSkillBuilder<T>> B setIcon(ResourceLocation icon) {
        this.iconName = icon;
        return (B)this;
    }
}
