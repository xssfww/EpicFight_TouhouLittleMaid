package net.EFTLM.EF.Skill;

import com.github.tartaricacid.touhoulittlemaid.api.event.*;
import net.EFTLM.EF.API.Event.*;
import net.EFTLM.EF.Capability.MaidPatch;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
public abstract class MaidSkill {
    protected final ResourceLocation registryName;
    protected final CreativeModeTab creativeTab;
    public MaidSkill(MaidSkillBuilder<? extends MaidSkill> builder) {
        this.registryName = builder.registryName;
        this.creativeTab = builder.tab;
    }
    public static MaidSkillBuilder<MaidSkill> createBuilder() {
        return new MaidSkillBuilder<>();
    }
    public void MaidTick(MaidTickEvent event) {
    }
    public void MaidAttack(MaidAttackEvent event) {
    }
    public void MaidHurt(MaidHurtEvent event) {
    }
    public void MaidDamage(MaidDamageEvent event) {
    }
    public void MaidDeath(MaidDeathEvent event) {
    }
    public void MaidHurtTargetPost(MaidHurtTargetEvent.Post event) {
    }
    public void MaidHurtTargetPre(MaidHurtTargetEvent.Pre event) {
    }
    public void MaidKillTarget(MaidKilledEvent event) {
    }
    public void MaidChangeItemOnHand(MaidChangeItemEvent event) {
    }
    public void onInit(MaidSkillInitEvent event) {
    }
    public boolean canExecute(MaidPatch<?> MaidPatch) {
        return true;
    }
    public ResourceLocation getRegistryName() {
        return this.registryName;
    }
    public CreativeModeTab getCreativeTab() {
        return this.creativeTab;
    }
    public MutableComponent getTitle() {
        return Component.translatable(String.format("maid_skill.%s.%s", this.getRegistryName().getNamespace(), this.getRegistryName().getPath()));
    }
    public MutableComponent getDesc() {
        return Component.translatable(String.format("maid_skill.%s.%s.desc", this.getRegistryName().getNamespace(), this.getRegistryName().getPath()));
    }
    public ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(this.getRegistryName().getNamespace(), String.format("textures/gui/skill/%s.png", this.getRegistryName().getPath()));
    }
}
