package earth.terrarium.reaper.client.fabric;

import com.mojang.blaze3d.systems.RenderSystem;
import earth.terrarium.reaper.common.registry.ReaperRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.renderer.RenderType;
import reborncore.client.RenderUtil;

public class ReaperClientImpl implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ReaperRegistry.REAPER_GEN_BLOCK.get(), RenderType.translucent());
        BlockEntityRendererRegistry.register(ReaperRegistry.REAPER_GEN_BLOCK_ENTITY.get(), context -> new ReaperGeneratorRenderer());
    }
}
