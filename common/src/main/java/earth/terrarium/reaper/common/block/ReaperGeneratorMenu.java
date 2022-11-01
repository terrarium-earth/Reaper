package earth.terrarium.reaper.common.block;

import earth.terrarium.reaper.common.registry.ReaperRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ReaperGeneratorMenu extends AbstractContainerMenu {
    public final Container container;
    public final ContainerData data;

    public ReaperGeneratorMenu(Container container, ContainerData data, int i, Inventory inventory) {
        super(ReaperRegistry.REAPER_GEN_MENU.get(), i);
        this.container = container;
        this.data = data;
        addPlayerInvSlots(inventory);
        addDataSlots(data);
        int slot = 0;
        for (int y = 0; y < 4; y++) {
            this.addSlot(new CatalystSlot(this.container, slot, 96, 17 + y * 18));
            slot++;
        }
        for (int y = 0; y < 4; y++) {
            this.addSlot(new RuneSlot(this.container, slot, 152, 17 + y * 18));
            slot++;
        }
    }

    public ReaperGeneratorMenu(int syncId, Inventory inventory, FriendlyByteBuf byteBuf) {
        this(new SimpleContainer(8), new SimpleContainerData(6), syncId, inventory);
    }

    @Override
    public ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack slotItem = slot.getItem();
            itemStack = slotItem.copy();

            if (index < 12) {
                if (!this.moveItemStackTo(slotItem, 12, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotItem, 0, 12, false)) {
                return ItemStack.EMPTY;
            }

            if (slotItem.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    protected void addPlayerInvSlots(Inventory inventory) {
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(inventory, x + y * 9 + 9, 8 + x * 18, 104 + y * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 104 + 58));
        }
    }

    public static class RuneSlot extends Slot {
        public RuneSlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.is(ReaperRegistry.RUNES);
        }
    }

    public static class CatalystSlot extends Slot {
        public CatalystSlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.is(ReaperRegistry.SOUL_CATALYST.get());
        }
    }
}
