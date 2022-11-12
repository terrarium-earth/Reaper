package earth.terrarium.reaper.common.blockentity;

import earth.terrarium.botarium.api.energy.EnergyBlock;
import earth.terrarium.botarium.api.energy.InsertOnlyEnergyContainer;
import earth.terrarium.botarium.api.item.ItemContainerBlock;
import earth.terrarium.botarium.api.item.SerializableContainer;
import earth.terrarium.botarium.api.item.SimpleItemContainer;
import earth.terrarium.reaper.common.registry.ReaperRegistry;
import me.codexadrian.spirit.data.MobTraitData;
import me.codexadrian.spirit.data.Tier;
import me.codexadrian.spirit.data.ToolType;
import me.codexadrian.spirit.entity.EntityRarity;
import me.codexadrian.spirit.registry.SpiritBlocks;
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
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
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
import java.util.List;
import java.util.UUID;

public class SoulBeaconBlockEntity extends BlockEntity implements EnergyBlock, ItemContainerBlock, IAnimatable {
    private InsertOnlyEnergyContainer energyContainer;
    private SimpleItemContainer itemContainer;
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final int ENERGY_CAPACITY = 1000000;
    private int timer = 0;
    private static final List<BlockPos> offsets = new ArrayList<>(
            List.of(
                    new BlockPos(1, -1, 0),
                    new BlockPos(1, -1, -1),
                    new BlockPos(1, -1, 1),
                    new BlockPos(0, -1, 1),
                    new BlockPos(0, -1, -1),
                    new BlockPos(-1, -1, 1),
                    new BlockPos(-1, -1, 0),
                    new BlockPos(-1, -1, -1)
            )
    );

    private final List<BlockPos> runePositions = new ArrayList<>();

    public SoulBeaconBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ReaperRegistry.SOUL_BEACON_BLOCK_ENTITY.get(), blockPos, blockState);
        runePositions.addAll(offsets.stream().map(pos -> pos.offset(blockPos)).toList());
    }

    @Override
    public InsertOnlyEnergyContainer getEnergyStorage() {
        return energyContainer == null ? energyContainer = new InsertOnlyEnergyContainer(this, ENERGY_CAPACITY) : energyContainer;
    }

    public void tick() {
        if (!level.isClientSide()) if (timer > 0) timer--;
        if (!(level instanceof ServerLevel serverLevel) || timer != 0 || this.getContainer().isEmpty() || !this.getContainer().getItem(0).is(SpiritItems.SOUL_CRYSTAL.get()))
            return;

        boolean beastia = false;
        float dilato = 1;
        boolean duplici = false;
        boolean efficio = false;
        boolean hostilis = false;
        List<UUID> whitelist = new ArrayList<>();
        List<UUID> blacklist = new ArrayList<>();
        for (BlockPos runePosition : runePositions) {
            var block = this.getLevel().getBlockState(runePosition);

            if (block.is(ReaperRegistry.RUNE_BLOCK_NEUTRAL_FILTER.get())) {
                beastia = true;
            } else if (block.is(ReaperRegistry.RUNE_BLOCK_RANGE.get())) {
                dilato = 1.2f;
            } else if (block.is(ReaperRegistry.RUNE_BLOCK_DOUBLE.get())) {
                duplici = true;
            } else if (block.is(ReaperRegistry.RUNE_BLOCK_EFFICIENCY.get())) {
                efficio = true;
            } else if (block.is(ReaperRegistry.RUNE_BLOCK_HOSTILE_FILTER.get())) {
                hostilis = true;
            } else if (block.is(ReaperRegistry.RUNE_BLOCK_PERSONAL_FILTER.get()) && this.getLevel().getBlockEntity(runePosition) instanceof SelfIdentifyingRuneBlockEntity runeBlockEntity) {
                if(runeBlockEntity.isWhitelist()) {
                    whitelist.add(runeBlockEntity.getOwner());
                } else {
                    blacklist.add(runeBlockEntity.getOwner());
                }
            } else if (!block.is(SpiritBlocks.SOUL_STEEL_BLOCK.get())) {
                return;
            }
        }

        String soulCrystalType = SoulUtils.getSoulCrystalType(this.getContainer().getItem(0));
        Tier tier = SoulUtils.getTier(this.getContainer().getItem(0), level);
        float range = tier.spawnRange() * dilato;
        AABB box = new AABB(this.getBlockPos()).inflate(range, 5, range);
        if (soulCrystalType == null) return;
        boolean finalHostilis = hostilis;
        boolean finalBeastia = beastia;
        boolean finalDuplici = duplici;
        boolean finalEfficio = efficio;
        EntityType.byString(soulCrystalType).ifPresent(entityType -> {
            int energyDrain = (int) ((finalEfficio ? 1000 : 2000) * EntityRarity.getRarity(entityType).energyModifer);
            if (this.getEnergyStorage().internalExtract(energyDrain, true) < energyDrain) return;
            timer += tier.minSpawnDelay();
            for (LivingEntity livingEntity : this.level.getEntitiesOfClass(LivingEntity.class, box)) {
                if (livingEntity instanceof Player && ((!whitelist.isEmpty() && !whitelist.contains(livingEntity.getUUID())) || blacklist.contains(livingEntity.getUUID()))) continue;
                if (livingEntity instanceof Enemy && !finalHostilis) continue;
                if ((livingEntity instanceof NeutralMob || livingEntity instanceof Animal) && !finalBeastia) continue;
                MobTraitData.getEffectForEntity(entityType, serverLevel.getRecipeManager())
                        .ifPresent(mobTraitData -> mobTraitData.traits().forEach(trait -> trait.onHitEntity(ToolType.SWORD, null, livingEntity)));
                if (finalDuplici) {
                    MobTraitData.getEffectForEntity(entityType, serverLevel.getRecipeManager())
                            .ifPresent(mobTraitData -> mobTraitData.traits().forEach(trait -> trait.onHitEntity(ToolType.SWORD, null, livingEntity)));
                }
                this.getEnergyStorage().internalExtract(energyDrain, false);
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
    public SimpleItemContainer getContainer() {
        return this.itemContainer == null ? this.itemContainer = new SimpleItemContainer(this, 1) : this.itemContainer;
    }
}
