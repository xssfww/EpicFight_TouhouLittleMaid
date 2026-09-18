package net.EFTLM.EF.Register;

import net.EFTLM.EFTLM;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
public class EFTLM_Tab {
    public static final DeferredRegister<CreativeModeTab> TABS;
    public static final RegistryObject<CreativeModeTab> SKILL;
    static {
        TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EFTLM.MODID);
        SKILL = TABS.register("skills", () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.eftlm.skills"))
                .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                .icon(() -> new ItemStack(EFTLM_Item.SKILLBOOK.get())).build());
    }
}

