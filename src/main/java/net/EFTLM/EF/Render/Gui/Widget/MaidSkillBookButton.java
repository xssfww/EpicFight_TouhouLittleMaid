package net.EFTLM.EF.Render.Gui.Widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.EFTLM.EFTLM;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
public class MaidSkillBookButton extends ImageButton {
    private static final ResourceLocation SLOT_TEXTURE = ResourceLocation.fromNamespaceAndPath(EFTLM.MODID, "textures/gui/skill_slot.png");
    @Nullable
    private ResourceLocation iconTexture;
    public MaidSkillBookButton(int x, int y, int width, int height, OnPress onPress) {
        // 1.21.1: the ImageButton texture-uv constructor was replaced by WidgetSprites; renderWidget is
        // overridden below, so the sprite is never actually drawn
        super(x, y, width, height, new WidgetSprites(SLOT_TEXTURE, SLOT_TEXTURE), onPress);
    }
    public void setIcon(@Nullable ResourceLocation icon) {
        this.iconTexture = icon;
    }
    @Override
    public void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.enableDepthTest();
        graphics.blit(SLOT_TEXTURE, getX(), getY(), width, height, 0, 0, 128, 128, 128, 128);
        if (iconTexture != null) {
            graphics.blit(iconTexture, getX(), getY(), width, height, 0, 0, 128, 128, 128, 128);
        }
        if (isHovered) {
            RenderSystem.disableDepthTest();
            graphics.fill(getX(), getY(), getX() + width, getY() + height, 0x80FFFFFF);
            RenderSystem.enableDepthTest();
        }
    }
}
