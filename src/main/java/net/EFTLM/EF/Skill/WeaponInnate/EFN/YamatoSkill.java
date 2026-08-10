package net.EFTLM.EF.Skill.WeaponInnate.EFN;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTickEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.hm.efn.EFN;
import com.hm.efn.item.custom.YamatoItem;
import net.EFTLM.EF.API.Event.MaidHurtTargetEvent;
import net.EFTLM.EF.API.Event.MaidKilledEvent;
import net.EFTLM.EF.API.Event.MaidSkillInitEvent;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillBuilder;
import net.EFTLM.EF.Skill.MaidSkillDataManager;
import net.EFTLM.EF.Skill.WeaponInnate.WeaponInnateSkill;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
public class YamatoSkill extends WeaponInnateSkill {
    public static final MaidSkillDataManager.SkillDataKey<Integer> formationTime =
            MaidSkillDataManager.SkillDataKey.createDataKey(MaidSkillDataManager.SkillDataKey.INTEGER);
    public YamatoSkill(MaidSkillBuilder<? extends MaidSkill> builder) {
        super(builder);
    }
    @Override
    public void onInit(MaidSkillInitEvent event) {
        super.onInit(event);
        event.registerData(this, formationTime, 0);
    }
    @Override
    public void onMaidTick(MaidTickEvent event,MaidPatch<?> patch) {
        super.onMaidTick(event,patch);
        if (patch == null) return;
        Integer time = patch.getDataValue(this, formationTime);
        if (time == null) return;
        if (time > 0) {
            patch.setData(this, formationTime, time - 1);
        }
    }
    @Override
    public void onHurtTargetPost(MaidHurtTargetEvent.Post event) {
        super.onHurtTargetPost(event);
        MaidPatch<?> MaidPatch = event.getMaidPatch();
        EntityMaid Maid = MaidPatch.getOriginal();
        ItemStack item = Maid.getMainHandItem();
        if (item.getItem() instanceof YamatoItem) {
            CompoundTag tag = item.getOrCreateTag();
            float totalDamage = YamatoItem.getTotalDamage(item);
            tag.putFloat("TotalDamage",totalDamage + event.getAmount());
        }
    }
    @Override
    public void onKillTarget(MaidKilledEvent event) {
        MaidPatch<?> MaidPatch = event.getMaidPatch();
        EntityMaid Maid = MaidPatch.getOriginal();
        ItemStack item = Maid.getMainHandItem();
        if (item.getItem() instanceof YamatoItem) {
            CompoundTag tag = item.getOrCreateTag();
            int currentCount = YamatoItem.getKillCount(item);
            tag.putInt("KillCount", currentCount + 1);
        }
    }
    @Override
    public ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(EFN.MODID, "textures/item/yamato_dmc4.png");
    }
}
