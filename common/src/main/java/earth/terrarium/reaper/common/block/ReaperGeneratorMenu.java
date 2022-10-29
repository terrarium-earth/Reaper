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
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ReaperGeneratorMenu extends AbstractContainerMenu {
    private final Container inventory;
    private final ContainerData data;

    public ReaperGeneratorMenu(Container container, ContainerData data, int i, Inventory inventory) {
        super(ReaperRegistry.REAPER_GEN_MENU.get(), i);
        this.inventory = inventory;
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
}
