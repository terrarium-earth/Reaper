package earth.terrarium.reaper.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import earth.terrarium.reaper.common.block.ReaperGeneratorMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ReaperGeneratorScreen extends AbstractContainerScreen<ReaperGeneratorMenu> {

    public ReaperGeneratorScreen(ReaperGeneratorMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float f, int i, int j) {

    }
}
