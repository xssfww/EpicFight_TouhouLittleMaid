package net.EFTLM.EF.Item;

import net.EFTLM.EF.Render.Gui.MaidSkillBookScreen;
import net.EFTLM.EF.Render.SkillBookRenderer;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class MaidSkillBookItem extends Item {
    public MaidSkillBookItem(Properties Properties) {
        super(Properties);
    }
    public static void setContainingSkill(ResourceLocation name, ItemStack stack) {
        // 1.21.1: getOrCreateTag() is gone; the skill name lives in the minecraft:custom_data component
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.put("skill", StringTag.valueOf(String.valueOf(name))));
    }
    public static void setContainingSkill(MaidSkill skill, ItemStack stack) {
        setContainingSkill(skill.getRegistryName(), stack);
    }
    public static MaidSkill getContainSkill(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data != null && data.contains("skill")) {
            String skillName = data.copyTag().getString("skill");
            return MaidSkillManager.getSkillFor(ResourceLocation.parse(skillName));
        } else {
            return null;
        }
    }
    @Override
    public boolean isFoil(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return (data != null && data.contains("skill"));
    }
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Item.TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        MaidSkill Skill = getContainSkill(stack);
        if (Skill != null) {
            tooltip.add(Skill.getTitle().withStyle(ChatFormatting.DARK_GRAY));
        }
    }
    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return SkillBookRenderer.getInstance();
            }
        });
    }
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (world.isClientSide) {
            MaidSkill skill = getContainSkill(itemstack);
            if (skill != null) {
                // 1.21.1: DistExecutor was removed from NeoForge; world.isClientSide already guarantees the client side
                if (FMLEnvironment.dist == Dist.CLIENT) {
                    openScreen(skill);
                }
            }
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.pass(itemstack);
    }
    @OnlyIn(Dist.CLIENT)
    private void openScreen(MaidSkill skill) {
        Minecraft.getInstance().setScreen(new MaidSkillBookScreen(skill, null));
    }
}
