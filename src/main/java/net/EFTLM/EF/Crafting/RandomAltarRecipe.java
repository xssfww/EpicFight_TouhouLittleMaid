package net.EFTLM.EF.Crafting;

import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipe;
import com.github.tartaricacid.touhoulittlemaid.inventory.AltarRecipeInventory;
import net.EFTLM.EF.Register.EFTLM_Recipe;
import net.EFTLM.EF.Skill.MaidSkillManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
public class RandomAltarRecipe extends AltarRecipe {
    private static final String SKILL_BOOK_ID = "ef_tlm:skillbook";
    public RandomAltarRecipe(ResourceLocation id, EntityType<?> entityType, @Nullable CompoundTag extraData, float powerCost, Ingredient copyInput, @Nullable String copyTag, Ingredient... inputs) {
        super(id, entityType, extraData, powerCost, copyInput, copyTag, inputs);
    }
    @Override
    public void spawnOutputEntity(@NotNull ServerLevel world, @NotNull BlockPos pos, @Nullable AltarRecipeInventory inventory) {
        CompoundTag extra = getExtraData();
        if (extra == null) {
            super.spawnOutputEntity(world, pos, inventory);
            return;
        }
        CompoundTag nbt = extra.copy();
        if (nbt.contains("Item")) {
            CompoundTag itemTag = nbt.getCompound("Item");
            if (SKILL_BOOK_ID.equals(itemTag.getString("id"))) {
                CompoundTag tag = itemTag.contains("tag") ? itemTag.getCompound("tag") : new CompoundTag();
                if (!tag.contains("skill")) {
                    Set<ResourceLocation> pool = MaidSkillManager.getNonWeaponSkillName();
                    if (!pool.isEmpty()) {
                        List<ResourceLocation> list = new ArrayList<>(pool);
                        ResourceLocation chosen = list.get(world.random.nextInt(list.size()));
                        tag.putString("skill", chosen.toString());
                        itemTag.put("tag", tag);
                        nbt.put("Item", itemTag);
                    }
                }
            }
        }
        nbt.putString("id", Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(getEntityType())).toString());
        Entity resultEntity = EntityType.loadEntityRecursive(nbt, world, (e) -> {
            e.moveTo(pos.getX(), pos.getY(), pos.getZ(), e.getYRot(), e.getXRot());
            return e;
        });
        if (resultEntity != null) {
            world.tryAddFreshEntityWithPassengers(resultEntity);
        }
    }
    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return EFTLM_Recipe.RANDOM_ALTAR_SERIALIZER.get();
    }
    @Override
    public @NotNull RecipeType<?> getType() {
        return super.getType();
    }
}
