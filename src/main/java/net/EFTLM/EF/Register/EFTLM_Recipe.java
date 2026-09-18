package net.EFTLM.EF.Register;

import net.EFTLM.EF.Crafting.RandomAltarSerializer;
import net.EFTLM.EFTLM;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
public class EFTLM_Recipe {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS;
    public static final RegistryObject<RecipeSerializer<?>> RANDOM_ALTAR_SERIALIZER;
    static {
        SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, EFTLM.MODID);
        RANDOM_ALTAR_SERIALIZER = SERIALIZERS.register("random_skill_altar", RandomAltarSerializer::new);
    }
}
