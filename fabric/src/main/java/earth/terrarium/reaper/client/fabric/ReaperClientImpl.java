package earth.terrarium.reaper.client.fabric;

import earth.terrarium.reaper.common.registry.ReaperRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;

public class ReaperClientImpl implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register(ReaperRegistry.REAPER_GEN_BLOCK_ENTITY.get(), context -> new ReaperGeneratorRenderer());
    }
}
