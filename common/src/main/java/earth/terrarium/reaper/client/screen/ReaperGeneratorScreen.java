package earth.terrarium.reaper.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.components.selection.SelectionList;
import earth.terrarium.reaper.common.block.ReaperGeneratorData;
import earth.terrarium.reaper.common.block.ReaperGeneratorMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class ReaperGeneratorScreen extends AbstractContainerScreen<ReaperGeneratorMenu> {
    public static final ResourceLocation TEXTURE = new ResourceLocation("reaper", "textures/gui/reaper_generator.png");
    private final ReaperGeneratorMenu menu;

    public ReaperGeneratorScreen(ReaperGeneratorMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        this.menu = abstractContainerMenu;
        this.imageWidth = 176;
        this.imageHeight = 186;
    }

    @Override
    protected void init() {
        var boxComponents = addRenderableWidget(new SelectionList<DeferredInfographic>(leftPos + 49, topPos + 24, 63, 66, 10, entry -> {}));
        boxComponents.updateEntries(List.of(
                new DeferredInfographic(() -> Component.translatable(true ? "gui.overcharged.active" : "gui.overcharged.inactive" ).withStyle(ChatFormatting.GRAY)),
                new DeferredInfographic(() -> Component.translatable("gui.overcharged.energy", this.menu.data.get(ReaperGeneratorData.PRODUCTION)).withStyle(ChatFormatting.GRAY)),
                new DeferredInfographic(() -> Component.translatable("gui.overcharged.damage", this.menu.data.get(ReaperGeneratorData.DAMAGE)).withStyle(ChatFormatting.GRAY))
        ));
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

    @Override
    protected void renderLabels(@NotNull PoseStack poseStack, int i, int j) {
        this.font.draw(poseStack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 0xffffb62e);
        this.font.draw(poseStack, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY, 0xffffb62e);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }

}
