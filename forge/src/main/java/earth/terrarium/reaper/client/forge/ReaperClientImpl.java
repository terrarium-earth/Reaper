package earth.terrarium.reaper.client.forge;

import earth.terrarium.reaper.common.registry.ReaperRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class ReaperClientImpl {

    public static void init() {
        BlockEntityRenderers.register(ReaperRegistry.REAPER_GEN_BLOCK_ENTITY.get(), ReaperGeneratorRenderer::new);
    }
}
