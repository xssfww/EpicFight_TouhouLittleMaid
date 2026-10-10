package net.EFTLM;

import net.EFTLM.EF.API.Data.BehaviorReloadListener;
import net.EFTLM.EF.API.Data.SkillDataReloadListener;
import net.EFTLM.EF.Animation.EFTLM_LivingMotions;
import net.EFTLM.EF.Command.MaidSkillCommand;
import net.EFTLM.EF.Event.EventBus;
import net.EFTLM.EF.Item.MaidSkillBookItem;
import net.EFTLM.EF.Network.PacketHandler;
import net.EFTLM.EF.Register.EFTLM_Item;
import net.EFTLM.EF.Register.EFTLM_Menu;
import net.EFTLM.EF.Register.EFTLM_Recipe;
import net.EFTLM.EF.Register.EFTLM_Tab;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillManager;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import yesman.epicfight.api.animation.LivingMotion;

@Mod(EFTLM.MODID)
public class EFTLM {
    public static final String MODID = "ef_tlm";

    public EFTLM(IEventBus modBus) {
        PacketHandler.RegisterManager(modBus);
        EFTLM_Item.ITEMS.register(modBus);
        EFTLM_Menu.MENUS.register(modBus);
        EFTLM_Tab.TABS.register(modBus);
        EFTLM_Recipe.SERIALIZERS.register(modBus);
        LivingMotion.ENUM_MANAGER.registerEnumCls(MODID, EFTLM_LivingMotions.class);
        // Epic Fight 21 uses its own event registry, so its hooks must be registered before Epic
        // Fight builds the entity patch / animator registries (i.e. during mod construction).
        EventBus.RegisterEpicFightHooks();
        NeoForge.EVENT_BUS.addListener(this::addReloadListenerEvent);
        NeoForge.EVENT_BUS.addListener(MaidSkillCommand::RegisterCommands);
        modBus.addListener(this::BuildCreativeTabWithSkillBooks);
    }

    protected void addReloadListenerEvent(AddReloadListenerEvent event) {
        event.addListener(new BehaviorReloadListener());
        event.addListener(new SkillDataReloadListener());
    }

    protected void BuildCreativeTabWithSkillBooks(BuildCreativeModeTabContentsEvent event) {
        MaidSkillManager.getNonWeaponSkillName().forEach((rl) -> {
            MaidSkill Skill = MaidSkillManager.getSkillFor(rl);
            if (Skill != null) {
                if (Skill.getCreativeTab() != null) {
                    if (Skill.getCreativeTab().equals(event.getTab())) {
                        ItemStack stack = new ItemStack(EFTLM_Item.SKILLBOOK.get());
                        MaidSkillBookItem.setContainingSkill(Skill, stack);
                        event.accept(stack);
                    }
                } else {
                    if (event.getTab().equals(EFTLM_Tab.SKILL.get())) {
                        ItemStack stack = new ItemStack(EFTLM_Item.SKILLBOOK.get());
                        MaidSkillBookItem.setContainingSkill(Skill, stack);
                        event.accept(stack);
                    }
                }
            }
        });
    }
}
