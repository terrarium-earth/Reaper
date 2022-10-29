package earth.terrarium.reaper.client.forge;

import earth.terrarium.reaper.Reaper;
import earth.terrarium.reaper.common.registry.ReaperRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Reaper.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ReaperClientImpl {

    @SubscribeEvent
    public static void init(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ReaperRegistry.REAPER_GEN_BLOCK_ENTITY.get(), ReaperGeneratorRenderer::new);
    }
}
