package net.EFTLM.EF.Skill;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
public class MaidSkillBuilder {
    protected ResourceLocation registryName;
    protected CreativeModeTab tab;
    public MaidSkillBuilder() {
    }
    public void setRegistryName(ResourceLocation registryName) {
        this.registryName = registryName;
    }
    public void setCreativeTab(CreativeModeTab tab) {
        this.tab = tab;
    }
}
