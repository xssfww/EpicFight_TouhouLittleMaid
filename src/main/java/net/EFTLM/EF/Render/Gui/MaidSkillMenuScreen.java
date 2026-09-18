package net.EFTLM.EF.Render.Gui;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Inventory.MaidSkillContainer;
import net.EFTLM.EF.Render.Gui.Widget.MaidSkillBookButton;
import net.EFTLM.EF.Render.Gui.Widget.MaidSkillTabButton;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
public class MaidSkillMenuScreen extends AbstractMaidContainerGui<MaidSkillContainer> {
    private static final int GRID_COLUMNS = 3;
    private static final int GRID_ROWS = 3;
    private static final int PAGE_SIZE = GRID_COLUMNS * GRID_ROWS;
    private static final int SKILL_LIST_X = 87;
    private static final int SKILL_LIST_Y = 36;
    private static final int SKILL_ICON_SIZE = 24;
    private static final int CELL_WIDTH = 50;
    private static final int CELL_HEIGHT = 40;
    private static final int GRID_WIDTH = GRID_COLUMNS * CELL_WIDTH;
    private static final int GRID_HEIGHT = GRID_ROWS * CELL_HEIGHT;
    private static final int PAGE_BUTTON_SIZE = 12;
    private static final int PAGE_BUTTON_GAP = 10;
    private int page = 0;
    @Nullable
    private Button previousPageButton;
    @Nullable
    private Button nextPageButton;
    private final List<MaidSkillBookButton> skillButtons = new ArrayList<>();
    public MaidSkillMenuScreen(MaidSkillContainer container, Inventory inventory, Component title) {
        super(container, inventory, title);
    }
    @Override
    protected void init() {
        super.init();
        if (maid != null) {
            MaidPatch<?> patch = EpicFightCapabilities.getEntityPatch(maid, MaidPatch.class);
            if (patch != null) {
                patch.deserializeNBT(this.getMenu().getNBT());
            }
        }
        updatePageButtons();
        updateSkillButtons();
    }
    @Override
    protected void initAdditionWidgets() {
        super.initAdditionWidgets();
        this.addRenderableWidget(new MaidSkillTabButton(leftPos, topPos, false, button -> {}));
        int pageControlY = getPageControlY() - 12;
        int nextPageX = leftPos + SKILL_LIST_X + GRID_WIDTH - PAGE_BUTTON_SIZE + 14;
        int previousPageX = nextPageX - PAGE_BUTTON_SIZE - PAGE_BUTTON_GAP;
        this.previousPageButton = this.addRenderableWidget(
                Button.builder(Component.literal("<"), button -> changePage(-1))
                        .pos(previousPageX, pageControlY)
                        .size(PAGE_BUTTON_SIZE, PAGE_BUTTON_SIZE)
                        .build());
        this.nextPageButton = this.addRenderableWidget(
                Button.builder(Component.literal(">"), button -> changePage(1))
                        .pos(nextPageX, pageControlY)
                        .size(PAGE_BUTTON_SIZE, PAGE_BUTTON_SIZE)
                        .build());
        skillButtons.clear();
        for (int i = 0; i < PAGE_SIZE; i++) {
            MaidSkillBookButton skillBtn = new MaidSkillBookButton(0, 0, SKILL_ICON_SIZE, SKILL_ICON_SIZE, this::onSkillButtonPress);
            skillBtn.visible = false;
            skillBtn.active = false;
            this.addRenderableWidget(skillBtn);
            skillButtons.add(skillBtn);
        }
        updatePageButtons();
        updateSkillButtons();
    }
    private void onSkillButtonPress(Button button) {
        if (maid == null) return;
        MaidPatch<?> patch = EpicFightCapabilities.getEntityPatch(maid, MaidPatch.class);
        if (patch == null) return;
        List<ResourceLocation> learnedSkills = patch.getLearnedSkills();
        if (button instanceof MaidSkillBookButton BookButton) {
            int index = skillButtons.indexOf(BookButton);
            if (index < 0) return;
            int skillIndex = page * PAGE_SIZE + index;
            if (skillIndex >= learnedSkills.size()) return;
            ResourceLocation rl = learnedSkills.get(skillIndex);
            MaidSkill skill = MaidSkillManager.getSkillFor(rl);
            if (skill != null) {
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().setScreen(new MaidSkillBookScreen(skill,this));
                }
            }
        }
    }
    @Override
    protected void renderAddition(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderAddition(graphics, mouseX, mouseY, partialTicks);
        if (maid == null) return;
        MaidPatch<?> patch = EpicFightCapabilities.getEntityPatch(maid, MaidPatch.class);
        if (patch == null) return;
        List<ResourceLocation> learnedSkills = patch.getLearnedSkills();
        if (learnedSkills.isEmpty()) return;
        int totalSkills = learnedSkills.size();
        int pageCount = Math.max((totalSkills - 1) / PAGE_SIZE + 1, 1);
        page = Mth.clamp(page, 0, pageCount - 1);
        String pageText = (page + 1) + "/" + pageCount;
        int pageTextX = leftPos + SKILL_LIST_X + GRID_WIDTH / 2 - font.width(pageText) / 2;
        graphics.drawString(font, pageText, pageTextX, getPageControlY() - 8, 0x666666, false);
        int gridX = leftPos + SKILL_LIST_X;
        int gridY = topPos + SKILL_LIST_Y;
        int start = page * PAGE_SIZE;
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLUMNS; col++) {
                int index = start + row * GRID_COLUMNS + col;
                if (index >= totalSkills) return;
                ResourceLocation rl = learnedSkills.get(index);
                MaidSkill skill = MaidSkillManager.getSkillFor(rl);
                if (skill == null) continue;
                int cellX = gridX + col * CELL_WIDTH;
                int cellY = gridY + row * CELL_HEIGHT;
                Component name = skill.getTitle();
                int textWidth = font.width(name);
                int textX = cellX + (CELL_WIDTH - textWidth) / 2;
                int textY = cellY + SKILL_ICON_SIZE + 2;
                graphics.drawString(font, name, textX, textY, 0x666666, false);
            }
        }
    }
    private void updateSkillButtons() {
        if (maid == null) return;
        MaidPatch<?> patch = EpicFightCapabilities.getEntityPatch(maid, MaidPatch.class);
        if (patch == null) return;
        List<ResourceLocation> learnedSkills = patch.getLearnedSkills();
        int totalSkills = learnedSkills.size();
        int start = page * PAGE_SIZE;
        int gridX = leftPos + SKILL_LIST_X;
        int gridY = topPos + SKILL_LIST_Y;
        for (int i = 0; i < PAGE_SIZE; i++) {
            MaidSkillBookButton btn = skillButtons.get(i);
            int skillIndex = start + i;
            if (skillIndex < totalSkills) {
                ResourceLocation rl = learnedSkills.get(skillIndex);
                MaidSkill skill = MaidSkillManager.getSkillFor(rl);
                int row = i / GRID_COLUMNS;
                int col = i % GRID_COLUMNS;
                int cellX = gridX + col * CELL_WIDTH;
                int cellY = gridY + row * CELL_HEIGHT;
                int iconX = cellX + (CELL_WIDTH - SKILL_ICON_SIZE) / 2;
                btn.setX(iconX);
                btn.setY(cellY);
                btn.setIcon(skill != null ? skill.getIcon() : null);
                btn.visible = true;
                btn.active = true;
            } else {
                btn.visible = false;
                btn.active = false;
            }
        }
    }
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollY) {
        if (isMouseInsideSkillList(mouseX, mouseY)) {
            int totalSkills = getSkillCount();
            if (totalSkills > 0) {
                int pageCount = Math.max((totalSkills - 1) / getPageSize() + 1, 1);
                if (pageCount > 1) {
                    int direction = scrollY > 0 ? -1 : 1;
                    int nextPage = Mth.clamp(page + direction, 0, pageCount - 1);
                    if (nextPage != page) {
                        page = nextPage;
                        updatePageButtons();
                        updateSkillButtons();
                        return true;
                    }
                }
            }
        }
        return super.mouseScrolled(mouseX, mouseY, scrollY);
    }
    private int getPageSize() {
        return PAGE_SIZE;
    }
    private int getSkillCount() {
        if (maid == null) return 0;
        MaidPatch<?> patch = EpicFightCapabilities.getEntityPatch(maid, MaidPatch.class);
        return patch != null ? patch.getLearnedSkills().size() : 0;
    }
    private int getPageControlY() {
        return topPos + SKILL_LIST_Y + GRID_HEIGHT + 4;
    }
    private void changePage(int offset) {
        int totalSkills = getSkillCount();
        if (totalSkills == 0) return;
        int pageCount = Math.max((totalSkills - 1) / getPageSize() + 1, 1);
        int nextPage = Mth.clamp(page + offset, 0, pageCount - 1);
        if (nextPage != page) {
            page = nextPage;
            updatePageButtons();
            updateSkillButtons();
        }
    }
    private void updatePageButtons() {
        int totalSkills = getSkillCount();
        int pageCount = Math.max((totalSkills - 1) / getPageSize() + 1, 1);
        if (previousPageButton != null) {
            previousPageButton.visible = pageCount > 1;
            previousPageButton.active = page >= 0;
        }
        if (nextPageButton != null) {
            nextPageButton.visible = pageCount > 1;
            nextPageButton.active = page <= pageCount - 1;
        }
    }
    private boolean isMouseInsideSkillList(double mouseX, double mouseY) {
        int x = leftPos + SKILL_LIST_X;
        int y = topPos + SKILL_LIST_Y;
        return mouseX >= x && mouseX < x + GRID_WIDTH && mouseY >= y && mouseY < y + GRID_HEIGHT;
    }
}