package earth.terrarium.reaper.client.forge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import earth.terrarium.reaper.common.block.ReaperGeneratorBlockModel;
import earth.terrarium.reaper.common.blockentity.ReaperGeneratorBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class ReaperGeneratorRenderer extends GeoBlockRenderer<ReaperGeneratorBlockEntity> {

    public ReaperGeneratorRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(rendererDispatcherIn, new ReaperGeneratorBlockModel());
    }

    @Override
    public RenderType getRenderType(ReaperGeneratorBlockEntity animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(textureLocation);
    }
}
