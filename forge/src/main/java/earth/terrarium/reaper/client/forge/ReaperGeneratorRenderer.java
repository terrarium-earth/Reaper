package earth.terrarium.reaper.client.forge;

import earth.terrarium.reaper.client.block.ReaperGeneratorBlockModel;
import earth.terrarium.reaper.common.blockentity.ReaperGeneratorBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class ReaperGeneratorRenderer extends GeoBlockRenderer<ReaperGeneratorBlockEntity> {

    public ReaperGeneratorRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(rendererDispatcherIn, new ReaperGeneratorBlockModel());
    }
}
