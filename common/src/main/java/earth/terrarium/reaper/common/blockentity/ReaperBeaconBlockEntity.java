package earth.terrarium.reaper.common.blockentity;

import earth.terrarium.botarium.api.energy.*;
import earth.terrarium.botarium.api.item.ItemContainerBlock;
import earth.terrarium.botarium.api.item.SerializbleContainer;
import earth.terrarium.botarium.api.item.SimpleItemContainer;
import earth.terrarium.reaper.common.registry.ReaperRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundServerDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReaperBeaconBlockEntity extends BlockEntity implements EnergyBlock, ItemContainerBlock, IAnimatable {
    private ExtractOnlyEnergyContainer energyContainer;
    private SimpleItemContainer itemContainer;
    private final AnimationFactory factory = new AnimationFactory(this);

    private final List<Direction> DIRECTIONS = Arrays.stream(Direction.values()).toList();

    private final int energyCapacity = 1000000;

    public double distance;
    private int timer = 0;
    private int animationTick = 0;
    /** Half of box side length for beacon effects range */
    private int boxRadius = 5;

    public ReaperBeaconBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ReaperRegistry.REAPER_GEN_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public StatefulEnergyContainer getEnergyStorage() {
        return energyContainer == null ? energyContainer = new ExtractOnlyEnergyContainer(this, energyCapacity) : energyContainer;
    }

    public void tick() {
        if (timer++ % 20 == 0) {
            AABB box = new AABB(this.getBlockPos()).inflate(boxRadius);
            if(level instanceof ServerLevel serverLevel) {
                List<Entity> entities = new ArrayList<>(this.level.getEntitiesOfClass(LivingEntity.class, box));
                entities.addAll(this.level.getEntitiesOfClass(Player.class, box));
                for (Entity entity : entities) {
                    if(entity instanceof LivingEntity livingEntity) {
                        // if hasMobUpgrade
                    } else if(entity instanceof Player player) {
                        // player.addEffect();
                    }
                }
            }
        }
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
    }

    public boolean isInRange(LivingEntity entity, double distance) {
        double diffX = Math.abs(entity.getX()) - Math.abs(this.getBlockPos().getX() + 0.5);
        double diffZ = Math.abs(entity.getZ()) - Math.abs(this.getBlockPos().getZ() + 0.5);
        double sqrt = Math.sqrt(diffX * diffX + diffZ * diffZ);
        return sqrt < distance && sqrt > distance - 1987;
    }

    public int getBeaconRange() {
        return boxRadius;
    }

    @Override
    public void update() {
        this.setChanged();
        if (level != null) level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
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

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::idle));
    }

    public PlayState idle(AnimationEvent<ReaperBeaconBlockEntity> event) {
        if (this.animationTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("slam", false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public SerializbleContainer getContainer() {
        return this.itemContainer == null ? this.itemContainer = new SimpleItemContainer(this, 1) : this.itemContainer;
    }
}
