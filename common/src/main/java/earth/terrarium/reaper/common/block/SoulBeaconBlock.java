package earth.terrarium.reaper.common.block;

import earth.terrarium.reaper.common.blockentity.SoulBeaconBlockEntity;
import earth.terrarium.reaper.common.registry.ReaperRegistry;
import me.codexadrian.spirit.blocks.blockentity.SoulCageBlockEntity;
import me.codexadrian.spirit.registry.SpiritItems;
import me.codexadrian.spirit.utils.SoulUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
public class SoulBeaconBlock extends BaseEntityBlock {
    public static final VoxelShape SHAPE = box(0, 0, 0, 16, 8, 16);

    public SoulBeaconBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (interactionHand != InteractionHand.OFF_HAND) {
            ItemStack itemStack = player.getMainHandItem();
            if (level.getBlockEntity(blockPos) instanceof SoulBeaconBlockEntity beacon) {
                if (beacon.getContainer().isEmpty()) {
                    if (itemStack.getItem().equals(SpiritItems.SOUL_CRYSTAL.get()) && itemStack.hasTag() && SoulUtils.getTier(itemStack, level) != null) {
                        beacon.getContainer().setItem(0, itemStack.copy());

                        if (!player.getAbilities().instabuild) {
                            itemStack.shrink(1);
                        }

                        beacon.update();
                        return InteractionResult.SUCCESS;
                    }
                } else if (player.isShiftKeyDown()) {
                    ItemStack DivineCrystal = beacon.getContainer().removeItemNoUpdate(0);
                    if (itemStack.isEmpty()) {
                        player.setItemInHand(interactionHand, DivineCrystal);
                    } else if (!player.addItem(DivineCrystal)) {
                        player.drop(DivineCrystal, false);
                    }

                    beacon.setChanged();
                    level.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_ALL);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SoulBeaconBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ReaperRegistry.SOUL_BEACON_BLOCK_ENTITY.get(), (level1, blockPos, blockState1, blockEntity) -> ((SoulBeaconBlockEntity) blockEntity).tick());
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    public @NotNull List<ItemStack> getDrops(@NotNull BlockState blockState, LootContext.@NotNull Builder builder) {
        List<ItemStack> drops = super.getDrops(blockState, builder);
        BlockEntity blockE = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockE instanceof SoulBeaconBlockEntity blockEntity) {
            drops.addAll(blockEntity.getContainer().getItems());
        }

        return drops;
    }
}
