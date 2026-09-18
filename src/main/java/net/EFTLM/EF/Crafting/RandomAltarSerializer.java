package net.EFTLM.EF.Crafting;

import com.github.tartaricacid.touhoulittlemaid.util.EntityCraftingHelper;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Optional;
public class RandomAltarSerializer implements RecipeSerializer<RandomAltarRecipe> {
    @Override
    public @NotNull RandomAltarRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
        EntityCraftingHelper.Output output = EntityCraftingHelper.getEntityData(GsonHelper.getAsJsonObject(json, "output"));
        float powerCost = GsonHelper.getAsFloat(json, "power");
        JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
        List<Ingredient> inputs = Lists.newArrayList();
        for (JsonElement e : ingredients) {
            inputs.add(Ingredient.fromJson(e));
        }
        return new RandomAltarRecipe(recipeId, output.getType(), output.getData(), powerCost, output.getCopyInput(), output.getCopyTag(), inputs.toArray(new Ingredient[0]));
    }
    @Override
    public @Nullable RandomAltarRecipe fromNetwork(@NotNull ResourceLocation recipeId, FriendlyByteBuf buffer) {
        Optional<EntityType<?>> typeOptional = EntityType.byString(buffer.readUtf());
        if (typeOptional.isEmpty()) {
            throw new JsonParseException("Entity Type Tag Not Found");
        }
        EntityType<?> entityType = typeOptional.get();
        CompoundTag extraData = buffer.readNbt();
        float powerCost = buffer.readFloat();
        Ingredient copyInput = Ingredient.fromNetwork(buffer);
        String copyTag = buffer.readUtf();
        Ingredient[] inputs = new Ingredient[buffer.readVarInt()];
        for (int i = 0; i < inputs.length; i++) {
            inputs[i] = Ingredient.fromNetwork(buffer);
        }
        return new RandomAltarRecipe(recipeId, entityType, extraData, powerCost, copyInput, copyTag, inputs);
    }
    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buffer, RandomAltarRecipe recipe) {
        ResourceLocation name = ForgeRegistries.ENTITY_TYPES.getKey(recipe.getEntityType());
        if (name == null) {
            throw new JsonParseException("Entity Type Tag Not Found");
        }
        buffer.writeUtf(name.toString());
        buffer.writeNbt(recipe.getExtraData());
        buffer.writeFloat(recipe.getPowerCost());
        recipe.getCopyInput().toNetwork(buffer);
        if (StringUtils.isEmpty(recipe.getCopyTag())) {
            buffer.writeUtf("");
        } else {
            buffer.writeUtf(recipe.getCopyTag());
        }
        buffer.writeVarInt(recipe.getIngredients().size());
        for (Ingredient input : recipe.getIngredients()) {
            input.toNetwork(buffer);
        }
    }
}

