package earth.terrarium.reaper.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import earth.terrarium.reaper.common.block.ReaperGeneratorData;
import earth.terrarium.reaper.common.block.ReaperGeneratorMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ReaperGeneratorScreen extends AbstractContainerScreen<ReaperGeneratorMenu> {
    public static final ResourceLocation TEXTURE = new ResourceLocation("reaper", "textures/gui/reaper_generator.png");
    private final ReaperGeneratorMenu menu;

    public ReaperGeneratorScreen(ReaperGeneratorMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        this.menu = abstractContainerMenu;
    }

    @Override
    protected void renderBg(@NotNull PoseStack matrixStack, float f, int i, int j) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1, 1, 1, 1f);
        blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        int energyBarHeight = Mth.clamp((int) (66F * (getEnergyLevel().get() / (float) 1000000)), 0, 66);
        blit(matrixStack, leftPos + 186, topPos + 19 + (66 - energyBarHeight), 208, 66 - energyBarHeight, 12, energyBarHeight);
        int tickBarHeight = Mth.clamp((int) (66F * (getTicks().get() / 100F)), 0, 66);
        blit(matrixStack, leftPos + 172, topPos + 19 + (66 - tickBarHeight), 220, 66 - tickBarHeight, 6, tickBarHeight);
    }

    public Supplier<Integer> getEnergyLevel() {
        return () -> menu.data.get(ReaperGeneratorData.ENERGY);
    }

    public Supplier<Integer> getTicks() {
        return () -> menu.data.get(ReaperGeneratorData.COOLDOWN);
    }
}
