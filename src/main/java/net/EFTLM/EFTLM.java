package net.EFTLM;

import net.EFTLM.EF.Animation.EFTLM_LivingMotions;
import net.EFTLM.EF.Command.MaidSkillCommand;
import net.EFTLM.EF.Network.PacketHandler;
import net.EFTLM.EF.Register.EFTLM_Menu;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import yesman.epicfight.api.animation.LivingMotion;
@Mod(EFTLM.MODID)
public class EFTLM {
    public static final String MODID = "ef_tlm";
    public EFTLM() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        PacketHandler.RegisterManager();
        EFTLM_Menu.MENUS.register(bus);
        LivingMotion.ENUM_MANAGER.registerEnumCls(MODID, EFTLM_LivingMotions.class);
        MinecraftForge.EVENT_BUS.addListener(MaidSkillCommand::RegisterCommands);
    }
}
