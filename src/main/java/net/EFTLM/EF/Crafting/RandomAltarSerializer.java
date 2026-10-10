package net.EFTLM.EF.Crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Optional;
// 1.21.1: RecipeSerializer is codec based now. The old fromJson/fromNetwork/toNetwork trio is replaced by
// codec() (which used to be fromJson) plus streamCodec() (the old network methods).
public class RandomAltarSerializer implements RecipeSerializer<RandomAltarRecipe> {
    // 1.21.1: both the mod's own "output": {"type": ..., "nbt": ...} shape and TLM's new
    // "entity"/"result" shape are accepted, so either style of altar data file keeps working
    public static final MapCodec<RandomAltarRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Output.CODEC.optionalFieldOf("output").forGetter(RandomAltarSerializer::outputOf),
            ItemStack.OPTIONAL_CODEC.optionalFieldOf("result").forGetter(recipe -> Optional.of(recipe.getResult()).filter(stack -> !stack.isEmpty())),
            ResourceLocation.CODEC.optionalFieldOf("entity").forGetter(recipe -> Optional.ofNullable(recipe.getEntityType())),
            Codec.FLOAT.fieldOf("power").forGetter(RandomAltarRecipe::getPower),
            Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(RandomAltarRecipe::getIngredients)
    ).apply(instance, RandomAltarSerializer::createRecipe));
    private static final StreamCodec<RegistryFriendlyByteBuf, RandomAltarRecipe> STREAM_CODEC = StreamCodec.of(
            RandomAltarSerializer::toNetwork, RandomAltarSerializer::fromNetwork);
    @Override
    public @NotNull MapCodec<RandomAltarRecipe> codec() {
        return CODEC;
    }
    @Override
    public @NotNull StreamCodec<RegistryFriendlyByteBuf, RandomAltarRecipe> streamCodec() {
        return STREAM_CODEC;
    }
    private static void toNetwork(RegistryFriendlyByteBuf buffer, RandomAltarRecipe recipe) {
        buffer.writeResourceLocation(recipe.getEntityType());
        buffer.writeNbt(recipe.getExtraData());
        ItemStack.STREAM_CODEC.encode(buffer, recipe.getResult());
        buffer.writeFloat(recipe.getPower());
        NonNullList<Ingredient> inputs = recipe.getIngredients();
        buffer.writeVarInt(inputs.size());
        for (Ingredient input : inputs) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, input);
        }
    }
    private static RandomAltarRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        ResourceLocation entityType = buffer.readResourceLocation();
        CompoundTag extraData = buffer.readNbt();
        ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
        float powerCost = buffer.readFloat();
        NonNullList<Ingredient> inputs = NonNullList.withSize(buffer.readVarInt(), Ingredient.EMPTY);
        inputs.replaceAll(ingredient -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
        return new RandomAltarRecipe(entityType, result, extraData, powerCost, inputs);
    }
    private static Optional<Output> outputOf(RandomAltarRecipe recipe) {
        CompoundTag extraData = recipe.getExtraData();
        return extraData == null || recipe.getEntityType() == null
                ? Optional.empty()
                : Optional.of(new Output(recipe.getEntityType(), Optional.of(extraData)));
    }
    private static RandomAltarRecipe createRecipe(Optional<Output> output, Optional<ItemStack> result, Optional<ResourceLocation> entity, float power, List<Ingredient> ingredients) {
        ResourceLocation entityType = entity.orElseGet(() -> output.map(Output::type).orElse(null));
        if (entityType == null) {
            throw new IllegalStateException("Random altar recipe needs an \"entity\" or \"output.type\" field");
        }
        CompoundTag extraData = output.flatMap(Output::nbt).orElse(null);
        ItemStack outputStack = result.orElseGet(() -> RandomAltarRecipe.readResultStack(extraData));
        return new RandomAltarRecipe(entityType, outputStack, extraData, power, NonNullList.of(Ingredient.EMPTY, ingredients.toArray(Ingredient[]::new)));
    }
    // 1.21.1: the mod's altar data files still use the 1.20.1 EntityCraftingHelper shape, "output": {"type": ..., "nbt": ...}
    private record Output(ResourceLocation type, Optional<CompoundTag> nbt) {
        static final Codec<Output> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("type").forGetter(Output::type),
                CompoundTag.CODEC.optionalFieldOf("nbt").forGetter(Output::nbt)
        ).apply(instance, Output::new));
    }
}
