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

public class ReaperGeneratorMenu extends AbstractContainerMenu {
    public final Container inventory;
    public final ContainerData data;

    public ReaperGeneratorMenu(Container container, ContainerData data, int i, Inventory inventory) {
        super(ReaperRegistry.REAPER_GEN_MENU.get(), i);
        this.inventory = container;
        this.data = data;
    }

    public ReaperGeneratorMenu(int syncId, Inventory inventory, FriendlyByteBuf byteBuf) {
        this(new SimpleContainer(8), new SimpleContainerData(2), syncId, inventory);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
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
}
