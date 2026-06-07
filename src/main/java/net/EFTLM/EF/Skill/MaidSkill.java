package net.EFTLM.EF.Skill;

import com.github.tartaricacid.touhoulittlemaid.api.event.*;
import net.EFTLM.EF.API.Event.*;
import net.EFTLM.EF.Capability.MaidPatch;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
public abstract class MaidSkill {
    protected final ResourceLocation registryName;
    protected final CreativeModeTab creativeTab;
    public MaidSkill(MaidSkillBuilder builder) {
        this.registryName = builder.registryName;
        this.creativeTab = builder.tab;
    }
    public static MaidSkillBuilder createBuilder() {
        return new MaidSkillBuilder();
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
    public void MaidHurtTargetPost(MaidHurtTarget.Post event) {
    }
    public void MaidHurtTargetPre(MaidHurtTarget.Pre event) {
    }
    public void MaidKillTarget(LivingDeathEvent event) {
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
    public Component getTitle() {
        return Component.translatable(String.format("maid_skill.%s.%s", this.getRegistryName().getNamespace(), this.getRegistryName().getPath()));
    }
    public Component getDesc() {
        return Component.translatable(String.format("maid_skill.%s.%s.desc", this.getRegistryName().getNamespace(), this.getRegistryName().getPath()));
    }
    public ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(this.getRegistryName().getNamespace(), String.format("textures/gui/skill/%s.png", this.getRegistryName().getPath()));
    }
}
