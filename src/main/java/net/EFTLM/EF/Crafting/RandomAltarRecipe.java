package net.EFTLM.EF.Crafting;

import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipe;
import net.EFTLM.EF.Item.MaidSkillBookItem;
import net.EFTLM.EF.Register.EFTLM_Recipe;
import net.EFTLM.EF.Skill.MaidSkillManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
public class RandomAltarRecipe extends AltarRecipe {
    private static final String SKILL_BOOK_ID = "ef_tlm:skillbook";
    private final CompoundTag extraData;
    // 1.21.1: AltarRecipe is a ShapelessRecipe again and takes (group, category, ingredients, power, result, entity, lang);
    // the old Forge "id" argument and the EntityCraftingHelper copy input/tag are gone
    public RandomAltarRecipe(ResourceLocation entityType, ItemStack result, @Nullable CompoundTag extraData, float powerCost, NonNullList<Ingredient> inputs) {
        super("", CraftingBookCategory.MISC, inputs, powerCost, result, entityType, "");
        this.extraData = extraData;
    }
    @Nullable
    public CompoundTag getExtraData() {
        return this.extraData;
    }
    @Override
    public void spawnOutputEntity(@NotNull ServerLevel world, @NotNull BlockPos pos, @Nullable List<ItemStack> inventory) {
        if (this.extraData == null) {
            super.spawnOutputEntity(world, pos, inventory);
            return;
        }
        CompoundTag nbt = this.extraData.copy();
        if (nbt.contains("Item", Tag.TAG_COMPOUND)) {
            CompoundTag itemTag = nbt.getCompound("Item");
            ItemStack stack = readItemStack(world.registryAccess(), itemTag);
            if (!stack.isEmpty()) {
                if (SKILL_BOOK_ID.equals(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString()) && !hasPresetSkill(itemTag, stack)) {
                    Set<ResourceLocation> pool = MaidSkillManager.getNonWeaponSkillName();
                    if (!pool.isEmpty()) {
                        List<ResourceLocation> list = new ArrayList<>(pool);
                        ResourceLocation chosen = list.get(world.random.nextInt(list.size()));
                        MaidSkillBookItem.setContainingSkill(chosen, stack);
                    }
                }
                // 1.21.1: item "tag" compounds were replaced by data components, so re-encode the stack through the
                // item codec before EntityType.loadEntityRecursive reads it back
                nbt.put("Item", stack.save(world.registryAccess()));
            }
        }
        nbt.putString("id", getEntityType().toString());
        Entity resultEntity = EntityType.loadEntityRecursive(nbt, world, (e) -> {
            e.moveTo(pos.getX(), pos.getY(), pos.getZ(), e.getYRot(), e.getXRot());
            return e;
        });
        if (resultEntity != null) {
            world.tryAddFreshEntityWithPassengers(resultEntity);
        }
    }
    static ItemStack readResultStack(@Nullable CompoundTag extraData) {
        if (extraData == null || !extraData.contains("Item", Tag.TAG_COMPOUND)) {
            return ItemStack.EMPTY;
        }
        return readItemStack(null, extraData.getCompound("Item"));
    }
    private static ItemStack readItemStack(@Nullable HolderLookup.Provider registries, CompoundTag itemTag) {
        if (registries != null) {
            ItemStack parsed = ItemStack.parseOptional(registries, itemTag);
            if (!parsed.isEmpty()) {
                return parsed;
            }
        }
        // 1.21.1 fallback: the 1.20.1-era altar data files spell the stack size "Count" instead of "count"
        if (!itemTag.contains("id", Tag.TAG_STRING)) {
            return ItemStack.EMPTY;
        }
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemTag.getString("id")));
        return new ItemStack(item, itemTag.contains("Count") ? itemTag.getInt("Count") : 1);
    }
    private static boolean hasPresetSkill(CompoundTag itemTag, ItemStack stack) {
        return (itemTag.contains("tag", Tag.TAG_COMPOUND) && itemTag.getCompound("tag").contains("skill"))
                || MaidSkillBookItem.getContainSkill(stack) != null;
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
