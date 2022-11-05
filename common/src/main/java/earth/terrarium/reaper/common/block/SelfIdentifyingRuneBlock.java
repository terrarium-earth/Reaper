package earth.terrarium.reaper.common.block;

import earth.terrarium.reaper.common.blockentity.SelfIdentifyingRuneBlockEntity;
import me.codexadrian.spirit.registry.SpiritItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class SelfIdentifyingRuneBlock extends BaseEntityBlock {
    public SelfIdentifyingRuneBlock(Properties properties) {
        super(properties);
    }

    //override use method to switch the whitelist mode


    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if(!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if(blockEntity instanceof SelfIdentifyingRuneBlockEntity runeBlockEntity) {
                if(player.getItemInHand(interactionHand).is(SpiritItems.SOUL_STEEL_WAND.get())) {
                    runeBlockEntity.toggleWhitelist();
                    player.displayClientMessage(Component.nullToEmpty("Whitelist mode: " + runeBlockEntity.isWhitelist()), true);
                    return InteractionResult.SUCCESS;
                } else {
                    Player playerByUUID = level.getPlayerByUUID(runeBlockEntity.getOwner());
                    if (playerByUUID != null) {
                        player.sendSystemMessage(Component.translatable(runeBlockEntity.isWhitelist() ? "block.possessio_rune.whitelist" : "block.possessio_rune.blacklist", playerByUUID.getDisplayName()));
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SelfIdentifyingRuneBlockEntity(blockPos, blockState);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(level, blockPos, blockState, livingEntity, itemStack);
        if(level.getBlockEntity(blockPos) instanceof SelfIdentifyingRuneBlockEntity rune && livingEntity != null && livingEntity instanceof Player player) {
            rune.setOwner(player);
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState arg) {
        return RenderShape.MODEL;
    }
}
