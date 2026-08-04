package net.EFTLM.EF.Skill;

import com.github.tartaricacid.touhoulittlemaid.api.event.*;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.EFTLM.EF.API.Event.*;
import net.EFTLM.EF.Capability.MaidPatch;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.CreativeModeTab;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
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
        EntityMaid maid = event.getMaid();
        if (!(maid.level() instanceof ServerLevel)) return;
        MaidPatch<?> patch = EpicFightCapabilities.getEntityPatch(maid, MaidPatch.class);
        if (patch == null) return;
        onMaidTick(event,patch);
    }
    public void MaidAttack(MaidAttackEvent event) {
        EntityMaid maid = event.getMaid();
        if (!(maid.level() instanceof ServerLevel)) return;
        MaidPatch<?> patch = EpicFightCapabilities.getEntityPatch(maid, MaidPatch.class);
        if (patch == null) return;
        onMaidAttack(event,patch);
    }
    public void MaidHurt(MaidHurtEvent event) {
        EntityMaid maid = event.getMaid();
        if (!(maid.level() instanceof ServerLevel)) return;
        MaidPatch<?> patch = EpicFightCapabilities.getEntityPatch(maid, MaidPatch.class);
        if (patch == null) return;
        onMaidHurt(event,patch);
    }
    public void MaidDamage(MaidDamageEvent event) {
        EntityMaid maid = event.getMaid();
        if (!(maid.level() instanceof ServerLevel)) return;
        MaidPatch<?> patch = EpicFightCapabilities.getEntityPatch(maid, MaidPatch.class);
        if (patch == null) return;
        onMaidDamage(event,patch);
    }
    public void MaidDeath(MaidDeathEvent event) {
        EntityMaid maid = event.getMaid();
        if (!(maid.level() instanceof ServerLevel)) return;
        MaidPatch<?> patch = EpicFightCapabilities.getEntityPatch(maid, MaidPatch.class);
        if (patch == null) return;
        onMaidDeath(event,patch);
    }
    public void onHurtTargetPost(MaidHurtTargetEvent.Post event) {
    }
    public void onHurtTargetPre(MaidHurtTargetEvent.Pre event) {
    }
    public void onKillTarget(MaidKilledEvent event) {
    }
    public void onMaidTick(MaidTickEvent event,MaidPatch<?> MaidPatch) {
    }
    public void onMaidAttack(MaidAttackEvent event,MaidPatch<?> MaidPatch) {
    }
    public void onMaidHurt(MaidHurtEvent event,MaidPatch<?> MaidPatch) {
    }
    public void onMaidDamage(MaidDamageEvent event,MaidPatch<?> MaidPatch) {
    }
    public void onMaidDeath(MaidDeathEvent event,MaidPatch<?> MaidPatch) {
    }
    public void onInit(MaidSkillInitEvent event) {
    }
    public boolean canExecute(MaidPatch<?> MaidPatch) {
        return MaidPatch.isFightMode();
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
