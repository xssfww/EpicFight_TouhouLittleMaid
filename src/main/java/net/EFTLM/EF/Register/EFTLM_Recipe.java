package net.EFTLM.EF.Register;

import net.EFTLM.EF.Crafting.RandomAltarSerializer;
import net.EFTLM.EFTLM;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EFTLM_Recipe {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS;
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> RANDOM_ALTAR_SERIALIZER;

    static {
        SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, EFTLM.MODID);
        RANDOM_ALTAR_SERIALIZER = SERIALIZERS.register("random_skill_altar", RandomAltarSerializer::new);
    }
}
