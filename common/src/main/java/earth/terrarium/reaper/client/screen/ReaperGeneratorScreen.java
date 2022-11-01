package earth.terrarium.reaper.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.components.selection.SelectionList;
import earth.terrarium.reaper.Reaper;
import earth.terrarium.reaper.common.block.ReaperGeneratorData;
import earth.terrarium.reaper.common.block.ReaperGeneratorMenu;
import me.codexadrian.spirit.Spirit;
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
        this.inventoryLabelY = 92;
    }

    @Override
    protected void init() {
        super.init();
        var boxComponents = addRenderableWidget(new SelectionList<DeferredInfographic>(leftPos + 11, topPos + 20, 79, 68, 10, entry -> {}));
        boxComponents.updateEntries(List.of(
                new DeferredInfographic(() -> Component.translatable("gui.reaper.energy", this.menu.data.get(ReaperGeneratorData.PRODUCTION)).withStyle(ChatFormatting.GRAY)),
                new DeferredInfographic(() -> Component.translatable("gui.reaper.work", this.menu.data.get(ReaperGeneratorData.MAX_COOLDOWN)).withStyle(ChatFormatting.GRAY)),
                new DeferredInfographic(() -> Component.translatable("gui.reaper.range", this.menu.data.get(ReaperGeneratorData.RANGE)).withStyle(ChatFormatting.GRAY)),
                new DeferredInfographic(() -> Component.translatable(this.menu.data.get(ReaperGeneratorData.DAMAGE) > 0 ? "gui.reaper.damage" : "gui.reaper.instadeath", this.menu.data.get(ReaperGeneratorData.DAMAGE)).withStyle(ChatFormatting.GRAY))
        ));
    }

    @Override
    protected void renderBg(@NotNull PoseStack matrixStack, float f, int i, int j) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1, 1, 1, 1f);
        blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        int energyBarHeight = Mth.clamp((int) (66F * (getEnergyLevel().get() / (float) 1000000)), 0, 66);
        blit(matrixStack, leftPos + 133, topPos + 19 + (66 - energyBarHeight), 176, 66 - energyBarHeight, 12, energyBarHeight);
        int tickBarHeight = Mth.clamp((int) (66F * (getTicks().get() / (getMaxTicks().get() + 0.001f))), 0, 66);
        blit(matrixStack, leftPos + 119, topPos + 19 + (66 - tickBarHeight), 188, 66 - tickBarHeight, 6, tickBarHeight);
    }

    public Supplier<Integer> getEnergyLevel() {
        return () -> menu.data.get(ReaperGeneratorData.ENERGY);
    }

    public Supplier<Integer> getTicks() {
        return () -> menu.data.get(ReaperGeneratorData.COOLDOWN);
    }

    public Supplier<Integer> getMaxTicks() {
        return () -> menu.data.get(ReaperGeneratorData.MAX_COOLDOWN);
    }

    @Override
    protected void renderLabels(@NotNull PoseStack poseStack, int i, int j) {
        this.font.draw(poseStack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, Spirit.SOUL_COLOR);
        this.font.draw(poseStack, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY, Spirit.SOUL_COLOR);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float f) {
        this.renderBackground(poseStack);
        this.renderBg(poseStack, f, mouseX, mouseY);
        super.render(poseStack, mouseX, mouseY, f);
        this.renderTooltip(poseStack, mouseX, mouseY);
        if(mouseX > leftPos + 130 && mouseX < 147 + leftPos && mouseY > 16 + topPos && mouseY < 87 + topPos) {
            this.renderTooltip(poseStack, Component.translatable("gui." + Reaper.MODID + ".energy_tooltip", Component.literal(String.valueOf(getEnergyLevel().get())).withStyle(ChatFormatting.GOLD), Component.literal("1000000").withStyle(ChatFormatting.GOLD)).withStyle(ChatFormatting.AQUA), mouseX, mouseY);
        } else if(mouseX > leftPos + 116 && mouseX < 127 + leftPos && mouseY > 16 + topPos && mouseY < 87 + topPos) {
            this.renderTooltip(poseStack, Component.translatable("gui." + Reaper.MODID + ".work_tooltip", Component.literal(String.valueOf(getTicks().get())).withStyle(ChatFormatting.GOLD), Component.literal(String.valueOf(getMaxTicks().get())).withStyle(ChatFormatting.GOLD)).withStyle(ChatFormatting.AQUA), mouseX, mouseY);
        }
    }
}
