package net.EFTLM.EF.Item;

import net.EFTLM.EF.Render.Gui.MaidSkillBookScreen;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
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
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.util.List;
public class MaidSkillBookItem extends Item {
    public MaidSkillBookItem(Properties Properties) {
        super(Properties);
    }
    public static void setContainingSkill(ResourceLocation name, ItemStack stack) {
        stack.getOrCreateTag().put("skill", StringTag.valueOf(String.valueOf(name)));
    }
    public static void setContainingSkill(MaidSkill skill, ItemStack stack) {
        setContainingSkill(skill.getRegistryName(), stack);
    }
    public static MaidSkill getContainSkill(ItemStack stack) {
        if (stack.getTag() != null && stack.getTag().contains("skill")) {
            String skillName = stack.getTag().getString("skill");
            return MaidSkillManager.getSkillFor(ResourceLocation.parse(skillName));
        } else {
            return null;
        }
    }
    @Override
    public boolean isFoil(ItemStack stack) {
        return (stack.getTag() != null && stack.getTag().contains("skill"));
    }
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        MaidSkill Skill = getContainSkill(stack);
        if (Skill != null) {
            tooltip.add(Skill.getTitle().withStyle(ChatFormatting.DARK_GRAY));
        }
    }
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (world.isClientSide) {
            MaidSkill skill = getContainSkill(itemstack);
            if (skill != null) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> openScreen(skill));
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
