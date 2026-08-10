package net.EFTLM.EF.Skill.WeaponInnate.EFN;

import com.hm.efn.EFN;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillBuilder;
import net.EFTLM.EF.Skill.WeaponInnate.WeaponInnateSkill;
import net.minecraft.resources.ResourceLocation;
public class KusabimaruSkill extends WeaponInnateSkill {
    public KusabimaruSkill(MaidSkillBuilder<? extends MaidSkill> builder) {
        super(builder);
    }
    @Override
    public ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(EFN.MODID, "textures/item/kusabimaru.png");
    }
}

