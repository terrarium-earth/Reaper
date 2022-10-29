package earth.terrarium.reaper.common.registry;

import earth.terrarium.botarium.api.registry.RegistryHelpers;
import earth.terrarium.botarium.api.registry.RegistryHolder;
import earth.terrarium.reaper.Reaper;
import earth.terrarium.reaper.common.block.ReaperGeneratorBlock;
import earth.terrarium.reaper.common.blockentity.ReaperGeneratorBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public class ReaperRegistry {
    public static RegistryHolder<Item> ITEMS = new RegistryHolder<>(Registry.ITEM, Reaper.MOD_ID);
    public static RegistryHolder<Block> BLOCKS = new RegistryHolder<>(Registry.BLOCK, Reaper.MOD_ID);
    public static RegistryHolder<BlockEntityType<?>> BLOCK_ENTITIES = new RegistryHolder<>(Registry.BLOCK_ENTITY_TYPE, Reaper.MOD_ID);
    //registry holder of sound events
    public static final RegistryHolder<SoundEvent> SOUNDS = new RegistryHolder<>(Registry.SOUND_EVENT, Reaper.MOD_ID);

    public static final Supplier<Block> REAPER_GEN_BLOCK = registerBlockWithItem("reaper_generator", () -> new ReaperGeneratorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final Supplier<BlockEntityType<?>> REAPER_GEN_BLOCK_ENTITY = BLOCK_ENTITIES.register("reaper_generator", () -> RegistryHelpers.createBlockEntityType(ReaperGeneratorBlockEntity::new, REAPER_GEN_BLOCK.get()));

    public static final Supplier<SoundEvent> REAPER_GEN_SOUND = SOUNDS.register("block.reaper.slam", () -> new SoundEvent(new ResourceLocation(Reaper.MOD_ID, "block.reaper.slam")));

    public static Supplier<Block> registerBlockWithItem(String name, Supplier<Block> block) {
        var blockRegistry = BLOCKS.register(name, block);
        ITEMS.register(name, () -> new BlockItem(blockRegistry.get(), new Item.Properties()));
        return blockRegistry;
    }

    public static void init() {}

    public static void register() {
        ITEMS.initialize();
        BLOCKS.initialize();
        BLOCK_ENTITIES.initialize();
        SOUNDS.initialize();
    }
}
