package net.EFTLM.EF.Render.Gui.Widget;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.client.gui.ITooltipButton;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.world.item.EpicFightItems;
import java.util.List;
public class MaidSkillTabButton extends Button implements ITooltipButton {
    private static final ResourceLocation SIDE = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_side.png");
    private static final int TAB_X = 169;
    private static final int TAB_Y = 5;
    private static final int SELECTED_BG_U = 107;
    private final ItemStack icon = EpicFightItems.SKILLBOOK.get().getDefaultInstance();
    private final List<Component> tooltips = Lists.newArrayList(
            Component.translatable("gui.ef_tlm.button.skill_book"),
            Component.translatable("gui.ef_tlm.button.skill_book.desc")
    );
    public MaidSkillTabButton(int leftPos, int topPos, boolean active, OnPress onPressIn) {
        super(Button.builder(Component.empty(), onPressIn).pos(leftPos + TAB_X, topPos + TAB_Y).size(24, 26));
        this.active = active;
    }
    @Override
    public void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.enableDepthTest();
        if (!this.active) {
            graphics.blit(SIDE, this.getX(), this.getY(), SELECTED_BG_U, 21, this.width, this.height, 256, 256);
        }
        graphics.renderItem(icon, this.getX() + 4, this.getY() + 6);
    }
    @Override
    public boolean isTooltipHovered() {
        return this.isHovered();
    }
    @Override
    public void renderTooltip(GuiGraphics graphics, Minecraft mc, int mouseX, int mouseY) {
        Font font = mc.font;
        graphics.renderComponentTooltip(font, tooltips, mouseX, mouseY);
    }
}
