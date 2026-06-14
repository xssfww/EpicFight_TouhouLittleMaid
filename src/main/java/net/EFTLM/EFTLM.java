package net.EFTLM;

import net.EFTLM.EF.Animation.EFTLM_LivingMotions;
import net.EFTLM.EF.Command.MaidSkillCommand;
import net.EFTLM.EF.Item.MaidSkillBookItem;
import net.EFTLM.EF.Network.PacketHandler;
import net.EFTLM.EF.Register.EFTLM_Item;
import net.EFTLM.EF.Register.EFTLM_Menu;
import net.EFTLM.EF.Register.EFTLM_Tab;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillManager;
import net.EFTLM.EF.Skill.WeaponInnate.WeaponInnateSkill;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.world.item.EpicFightCreativeTabs;
@Mod("ef_tlm")
public class EFTLM {
    public static final String MODID = "ef_tlm";
    public EFTLM() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        PacketHandler.RegisterManager();
        EFTLM_Item.ITEMS.register(bus);
        EFTLM_Menu.MENUS.register(bus);
        EFTLM_Tab.TABS.register(bus);
        bus.addListener(this::BuildCreativeTabWithSkillBooks);
        LivingMotion.ENUM_MANAGER.registerEnumCls(MODID, EFTLM_LivingMotions.class);
        MinecraftForge.EVENT_BUS.addListener(MaidSkillCommand::RegisterCommands);
    }
    protected void BuildCreativeTabWithSkillBooks(BuildCreativeModeTabContentsEvent event) {
        MaidSkillManager.getSkillRegisterName().forEach((rl) -> {
            MaidSkill Skill = MaidSkillManager.getSkillFor(rl);
            if (Skill != null) {
                if (!(Skill instanceof WeaponInnateSkill)) {
                    if (Skill.getCreativeTab() != null) {
                        if (Skill.getCreativeTab().equals(event.getTab())) {
                            ItemStack stack = new ItemStack(EFTLM_Item.SKILLBOOK.get());
                            MaidSkillBookItem.setContainingSkill(Skill, stack);
                            event.accept(stack);
                        }
                    } else {
                        if (event.getTab().equals(EpicFightCreativeTabs.ITEMS.get())) {
                            ItemStack stack = new ItemStack(EFTLM_Item.SKILLBOOK.get());
                            MaidSkillBookItem.setContainingSkill(Skill, stack);
                            event.accept(stack);
                        }
                    }
                }
            }
        });
    }
}
