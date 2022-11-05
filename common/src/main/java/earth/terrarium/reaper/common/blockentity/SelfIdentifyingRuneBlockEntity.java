package earth.terrarium.reaper.common.blockentity;

import earth.terrarium.reaper.common.registry.ReaperRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SelfIdentifyingRuneBlockEntity extends BlockEntity {
    private UUID owner;
    private String ownerName;
    private boolean whitelist = true;

    public SelfIdentifyingRuneBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ReaperRegistry.RUNE_BLOCK_PERSONAL_FILTER_ENTITY.get(), blockPos, blockState);
    }

    public void setOwner(Player player) {
        this.owner = player.getUUID();
        this.ownerName = player.getDisplayName().getString();
    }

    public UUID getOwner() {
        return owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        if (owner != null && ownerName != null) {
            compoundTag.putUUID("Owner", owner);
            compoundTag.putString("OwnerName", ownerName);
        }
        compoundTag.putBoolean("Whitelist", whitelist);
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);
        this.owner = compoundTag.getUUID("Owner");
        this.ownerName = compoundTag.getString("OwnerName");
        this.whitelist = compoundTag.getBoolean("Whitelist");
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public boolean isWhitelist() {
        return whitelist;
    }

    public void toggleWhitelist() {
        this.whitelist = !whitelist;
    }
}
