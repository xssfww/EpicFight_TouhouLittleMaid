package net.EFTLM.EF.Render.Gui;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexSorting;
import net.EFTLM.EF.Skill.MaidSkill;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.client.ForgeHooksClient;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import yesman.epicfight.main.EpicFightMod;
import java.util.List;
public class MaidSkillBookScreen extends Screen {
    protected final MaidSkill Skill;
    protected final Screen ParentScreen;
    protected final MaidSkillBookScreen.SkillTooltipList skillTooltipList;
    protected double customScale;
    private static final ResourceLocation SKILLBOOK_BACKGROUND = EpicFightMod.identifier("textures/gui/screen/skillbook.png");
    public MaidSkillBookScreen(MaidSkill Skill, Screen ParentScreen) {
        super(Component.empty());
        this.Skill = Skill;
        this.ParentScreen = ParentScreen;
        this.skillTooltipList = new SkillTooltipList(Minecraft.getInstance(), 0, 0, 0, 0, 9);
        List<FormattedCharSequence> list = Minecraft.getInstance().font.split(this.Skill.getDesc(), 148);
        list.forEach(skillTooltipList::add);
    }
    @Override
    protected void init() {
        Window window = Minecraft.getInstance().getWindow();
        if (window.getGuiScaledHeight() < 270 && window.getGuiScale() > 1.0) {
            this.customScale = window.getGuiScale() - 1.0;
            this.width = (int)((double)window.getWidth() / this.customScale);
            this.height = (int)((double)window.getHeight() / this.customScale);
        } else {
            this.customScale = window.getGuiScale();
        }
        this.skillTooltipList.updateSize(210, 400, this.height / 2 - 100, (this.height + 80) / 2);
        this.skillTooltipList.setLeftPos(this.width / 2 - 40);
        this.addRenderableWidget(this.skillTooltipList);
    }
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.render(guiGraphics, mouseX, mouseY, partialTicks, false);
    }
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks, boolean asBackground) {
        guiGraphics.pose().pushPose();
        Window window = Minecraft.getInstance().getWindow();
        double originalScale = window.getGuiScale();
        if (originalScale != this.customScale) {
            window.setGuiScale(this.customScale);
            Matrix4f matrix4f = (new Matrix4f()).setOrtho(0.0F, (float)((double)window.getWidth() / window.getGuiScale()), (float)((double)window.getHeight() / window.getGuiScale()), 0.0F, 1000.0F, ForgeHooksClient.getGuiFarPlane());
            RenderSystem.setProjectionMatrix(matrix4f, VertexSorting.ORTHOGRAPHIC_Z);
        }
        if (!asBackground) {
            this.renderBackground(guiGraphics);
        }
        int posX = (this.width - 284) / 2;
        int posY = (this.height - 165) / 2;
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        guiGraphics.blit(SKILLBOOK_BACKGROUND, this.width / 2 - 192, this.height / 2 - 140, 384, 279, 0.0F, 0.0F, 256, 186, 256, 256);
        guiGraphics.pose().pushPose();
        RenderSystem.enableBlend();
        guiGraphics.blit(this.Skill.getIcon(), this.width / 2 - 122, this.height / 2 - 99, 68, 68, 0.0F, 0.0F, 128, 128, 128, 128);
        RenderSystem.disableBlend();
        String skillName = Skill.getTitle().getString();
        int width = this.font.width(skillName);
        guiGraphics.drawString(this.font, skillName, posX + 56 - width / 2, posY + 75, 0, false);
        super.render(guiGraphics, (int)((double)mouseX * originalScale / this.customScale), (int)((double)mouseY * originalScale / this.customScale), partialTicks);
        if (asBackground) {
            this.renderBackground(guiGraphics);
        }
        guiGraphics.pose().popPose();
        if (originalScale != this.customScale) {
            window.setGuiScale(originalScale);
            Matrix4f matrix4f = (new Matrix4f()).setOrtho(0.0F, (float)((double)window.getWidth() / window.getGuiScale()), (float)((double)window.getHeight() / window.getGuiScale()), 0.0F, 1000.0F, ForgeHooksClient.getGuiFarPlane());
            RenderSystem.setProjectionMatrix(matrix4f, VertexSorting.ORTHOGRAPHIC_Z);
        }
    }
    @Override
    public void onClose() {
        if (this.ParentScreen != null) {
            if (this.minecraft != null) {
                this.minecraft.setScreen(this.ParentScreen);
            }
        } else {
            super.onClose();
        }
    }
    @Override
    public boolean mouseClicked(double x, double y, int button) {
        Window window = Minecraft.getInstance().getWindow();
        return super.mouseClicked((int)(x * window.getGuiScale() / this.customScale), (int)(y * window.getGuiScale() / this.customScale), button);
    }
    @Override
    public boolean mouseReleased(double x, double y, int button) {
        Window window = Minecraft.getInstance().getWindow();
        return super.mouseReleased((int)(x * window.getGuiScale() / this.customScale), (int)(y * window.getGuiScale() / this.customScale), button);
    }
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        Window window = Minecraft.getInstance().getWindow();
        return super.mouseDragged((int)(mouseX * window.getGuiScale() / this.customScale), (int)(mouseY * window.getGuiScale() / this.customScale), button, dx, dy);
    }
    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        Window window = Minecraft.getInstance().getWindow();
        return super.mouseScrolled((int)(pMouseX * window.getGuiScale() / this.customScale), (int)(pMouseY * window.getGuiScale() / this.customScale), pDelta);
    }
    protected class SkillTooltipList extends ObjectSelectionList<SkillTooltipList.TooltipLine> {
        protected SkillTooltipList(Minecraft minecraft, int width, int height, int y0, int y1, int itemHeight) {
            super(minecraft, width, height, y0, y1, itemHeight);
            this.setRenderBackground(false);
            this.setRenderHeader(false, 0);
            this.setRenderTopAndBottom(false);
        }
        protected void add(FormattedCharSequence tooltip) {
            this.addEntry(new SkillTooltipList.TooltipLine(tooltip));
        }
        protected int getScrollbarPosition() {
            return this.x1 - 6;
        }
        protected class TooltipLine extends ObjectSelectionList.Entry<SkillTooltipList.TooltipLine> {
            protected final FormattedCharSequence tooltip;
            protected TooltipLine(FormattedCharSequence string) {
                this.tooltip = string;
            }
            public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
                guiGraphics.drawString(MaidSkillBookScreen.this.font, this.tooltip, left + 59, top, 0, false);
            }
            public @NotNull Component getNarration() {
                return Component.empty();
            }
        }
    }
}
