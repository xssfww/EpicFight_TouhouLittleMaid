package net.EFTLM.EF.Register;

import net.EFTLM.EF.Item.MaidSkillBookItem;
import net.EFTLM.EFTLM;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
public class EFTLM_Item {
    public static final DeferredRegister<Item> ITEMS;
    public static final RegistryObject<Item> SKILLBOOK;
    static {
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EFTLM.MODID);
        SKILLBOOK = ITEMS.register("skillbook", () -> new MaidSkillBookItem((new Item.Properties()).rarity(Rarity.RARE).stacksTo(1)));
    }
}
