package earth.terrarium.reaper.client.fabric;

import earth.terrarium.reaper.client.block.ReaperGeneratorBlockModel;
import earth.terrarium.reaper.common.blockentity.ReaperGeneratorBlockEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class ReaperGeneratorRenderer extends GeoBlockRenderer<ReaperGeneratorBlockEntity> {
    public ReaperGeneratorRenderer() {
        super(new ReaperGeneratorBlockModel());
    }
}
