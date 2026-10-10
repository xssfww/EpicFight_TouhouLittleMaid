package net.EFTLM.EF.Register;

import net.EFTLM.EF.Item.MaidSkillBookItem;
import net.EFTLM.EFTLM;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EFTLM_Item {
    public static final DeferredRegister<Item> ITEMS;
    public static final DeferredHolder<Item, Item> SKILLBOOK;

    static {
        ITEMS = DeferredRegister.create(Registries.ITEM, EFTLM.MODID);
        SKILLBOOK = ITEMS.register("skillbook", () -> new MaidSkillBookItem((new Item.Properties()).rarity(Rarity.RARE).stacksTo(1)));
    }
}
