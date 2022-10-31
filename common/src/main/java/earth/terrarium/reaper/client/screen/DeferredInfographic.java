package earth.terrarium.reaper.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.components.selection.ListEntry;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class DeferredInfographic extends ListEntry {
    Supplier<Component> component;

    public DeferredInfographic(Supplier<Component> component) {
        this.component = component;
    }

    @Override
    protected void render(@NotNull ScissorBoxStack scissorStack, @NotNull PoseStack stack, int id, int left, int top, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick, boolean selected) {
        Font font = Minecraft.getInstance().font;
        stack.pushPose();
        stack.translate(left, top, 0);
        stack.scale(0.85F, 0.85F, 1);
        GuiComponent.drawString(stack, font, this.component.get(), 0, 1, 0xffffac30);
        stack.popPose();
    }
}