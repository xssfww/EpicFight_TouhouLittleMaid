package net.EFTLM.EF.Register;

import net.EFTLM.EF.Inventory.MaidSkillContainer;
import net.EFTLM.EFTLM;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
public class EFTLM_Menu {
    public static final DeferredRegister<MenuType<?>> MENUS;
    public static final RegistryObject<MenuType<MaidSkillContainer>> MaidSkillMenu;
    static {
        MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, EFTLM.MODID);
        MaidSkillMenu = MENUS.register("maid_skill_menu", () -> MaidSkillContainer.TYPE);
    }
}
