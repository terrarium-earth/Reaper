package earth.terrarium.reaper.common.block;

import earth.terrarium.reaper.common.blockentity.ReaperGeneratorBlockEntity;
import earth.terrarium.reaper.common.registry.ReaperRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ReaperGeneratorBlock extends BaseEntityBlock {

    public ReaperGeneratorBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ReaperGeneratorBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ReaperRegistry.REAPER_GEN_BLOCK_ENTITY.get(), (level1, blockPos, blockState1, blockEntity) -> ((ReaperGeneratorBlockEntity) blockEntity).tick());
    }
}
