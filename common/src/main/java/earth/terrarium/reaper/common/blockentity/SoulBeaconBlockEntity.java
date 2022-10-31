package earth.terrarium.reaper.common.blockentity;

import earth.terrarium.botarium.api.energy.EnergyBlock;
import earth.terrarium.botarium.api.energy.InsertOnlyEnergyContainer;
import earth.terrarium.botarium.api.energy.StatefulEnergyContainer;
import earth.terrarium.botarium.api.item.ItemContainerBlock;
import earth.terrarium.botarium.api.item.SerializableContainer;
import earth.terrarium.botarium.api.item.SimpleItemContainer;
import earth.terrarium.reaper.common.registry.ReaperRegistry;
import me.codexadrian.spirit.data.MobTraitData;
import me.codexadrian.spirit.data.ToolType;
import me.codexadrian.spirit.registry.SpiritItems;
import me.codexadrian.spirit.utils.SoulUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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

public class SoulBeaconBlockEntity extends BlockEntity implements EnergyBlock, ItemContainerBlock, IAnimatable {
    private InsertOnlyEnergyContainer energyContainer;
    private SimpleItemContainer itemContainer;
    private final AnimationFactory factory = new AnimationFactory(this);

    private static final int ENERGY_CAPACITY = 1000000;

    private int timer = 0;
    /**
     * Half of box side length for beacon effects range
     */
    private static final int boxRadius = 10;


    public SoulBeaconBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ReaperRegistry.SOUL_BEACON_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public StatefulEnergyContainer getEnergyStorage() {
        return energyContainer == null ? energyContainer = new InsertOnlyEnergyContainer(this, ENERGY_CAPACITY) : energyContainer;
    }

    public void tick() {
        if (!(level instanceof ServerLevel serverLevel) || timer++ % 20 != 0 || this.getContainer().isEmpty() || !this.getContainer().getItem(0).is(SpiritItems.SOUL_CRYSTAL.get())) return;
        AABB box = new AABB(this.getBlockPos()).inflate(boxRadius, 5, boxRadius);
        String soulCrystalType = SoulUtils.getSoulCrystalType(this.getContainer().getItem(0));
        if(soulCrystalType == null) return;
        EntityType.byString(soulCrystalType).ifPresent(entityType -> {
            for (LivingEntity livingEntity : this.level.getEntitiesOfClass(LivingEntity.class, box)) {
                MobTraitData.getEffectForEntity(entityType, serverLevel.getRecipeManager())
                        .ifPresent(mobTraitData -> mobTraitData.traits().forEach(trait -> trait.onHitEntity(ToolType.SWORD, null, livingEntity)));
            }
        });
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
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

    public PlayState idle(AnimationEvent<SoulBeaconBlockEntity> event) {
        if (!this.getContainer().isEmpty() && this.getEnergyStorage().getStoredEnergy() > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("active", true));
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
    public SerializableContainer getContainer() {
        return this.itemContainer == null ? this.itemContainer = new SimpleItemContainer(this, 1) : this.itemContainer;
    }
}
