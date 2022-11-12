package earth.terrarium.reaper.common.block;

import earth.terrarium.reaper.common.blockentity.ReaperGeneratorBlockEntity;
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
    private final Container container;
    private final ContainerData data;
    private final ReaperGeneratorBlockEntity reaperGenerator;

    public ReaperGeneratorMenu(Container container, ContainerData data, int i, Inventory inventory, ReaperGeneratorBlockEntity reaperGenerator) {
        super(ReaperRegistry.REAPER_GEN_MENU.get(), i);
        this.container = container;
        this.data = data;
        this.reaperGenerator = reaperGenerator;
        int slot = 0;
        for (int y = 0; y < 4; y++) {
            this.addSlot(new CatalystSlot(this.container, this.reaperGenerator, slot, 96, 17 + y * 18));
            slot++;
        }
        for (int y = 0; y < 4; y++) {
            this.addSlot(new RuneSlot(this.container, slot, 152, 17 + y * 18));
            slot++;
        }
        addPlayerInvSlots(inventory);
        addDataSlots(data);
    }

    public ReaperGeneratorMenu(int syncId, Inventory inventory, FriendlyByteBuf buf) {
        this(new SimpleContainer(8), new SimpleContainerData(6), syncId, inventory, (ReaperGeneratorBlockEntity) inventory.player.level.getBlockEntity(buf.readBlockPos()));
    }

    @Override
    public ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (index < 8) {
                if (!this.moveItemStackTo(originalStack, 8, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 0, 8, false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return newStack;
    }

    public Container getContainer() {
        return container;
    }

    public ContainerData getContainerData() {
        return data;
    }

    public ReaperGeneratorBlockEntity getBlockEntity() {
        return reaperGenerator;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
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
        private final ReaperGeneratorBlockEntity reaperGenerator;
        public CatalystSlot(Container container, ReaperGeneratorBlockEntity reaperGenerator, int index, int x, int y) {
            super(container, index, x, y);
            this.reaperGenerator = reaperGenerator;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.is(ReaperRegistry.SOUL_CATALYST.get());
        }

        @Override
        public boolean mayPickup(@NotNull Player player) {
            return reaperGenerator.cooldown > 30;
        }
    }
}
