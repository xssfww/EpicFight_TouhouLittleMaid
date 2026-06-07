package net.EFTLM.EF.Inventory;

import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeMenuType;
import org.jetbrains.annotations.NotNull;
public class MaidSkillContainer extends AbstractMaidContainer {
    protected final CompoundTag NBT;
    public static final MenuType<MaidSkillContainer> TYPE = IForgeMenuType.create((windowId, inv, data) -> new MaidSkillContainer(windowId, inv, data.readVarInt(),data.readNbt()));
    public MaidSkillContainer(int id, Inventory inventory, int entityId, CompoundTag nbt) {
        super(TYPE, id, inventory, entityId);
        this.NBT = nbt;
    }
    public static MenuProvider create(int entityId, CompoundTag nbt) {
        return new SimpleMenuProvider((windowId, inventory, player) -> new MaidSkillContainer(windowId, inventory, entityId, nbt),
                Component.translatable("screen.ef_tlm.maid_skill")
        );
    }
    public CompoundTag getNBT() {
        return this.NBT;
    }
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        return ItemStack.EMPTY;
    }
}
