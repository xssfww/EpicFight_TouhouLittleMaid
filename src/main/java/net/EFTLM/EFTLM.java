package net.EFTLM;

import net.EFTLM.EF.Animation.EFTLM_LivingMotions;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.LivingMotion;
@Mod(EFTLM.MODID)
public class EFTLM {
    public static final String MODID = "ef_tlm";
    public EFTLM() {
        LivingMotion.ENUM_MANAGER.registerEnumCls(MODID, EFTLM_LivingMotions.class);
    }
}
