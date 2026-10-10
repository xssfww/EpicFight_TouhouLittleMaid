package net.EFTLM.EF.Register;

import net.EFTLM.EF.Inventory.MaidSkillContainer;
import net.EFTLM.EFTLM;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EFTLM_Menu {
    public static final DeferredRegister<MenuType<?>> MENUS;
    public static final DeferredHolder<MenuType<?>, MenuType<MaidSkillContainer>> MaidSkillMenu;

    static {
        MENUS = DeferredRegister.create(Registries.MENU, EFTLM.MODID);
        MaidSkillMenu = MENUS.register("maid_skill_menu", () -> MaidSkillContainer.TYPE);
    }
}
