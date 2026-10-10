package net.EFTLM.EF.Register;

import net.EFTLM.EFTLM;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EFTLM_Tab {
    public static final DeferredRegister<CreativeModeTab> TABS;
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SKILL;

    static {
        TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EFTLM.MODID);
        SKILL = TABS.register("skills", () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.eftlm.skills"))
                // 1.21.1: the vanilla CreativeModeTabs constants are private, order by tab id instead.
                .withTabsBefore(ResourceLocation.withDefaultNamespace("spawn_eggs"))
                .icon(() -> new ItemStack(EFTLM_Item.SKILLBOOK.get())).build());
    }
}
