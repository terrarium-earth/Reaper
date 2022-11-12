package earth.terrarium.reaper.common.blockentity;

import earth.terrarium.botarium.api.energy.EnergyBlock;
import earth.terrarium.botarium.api.energy.EnergyHooks;
import earth.terrarium.botarium.api.energy.ExtractOnlyEnergyContainer;
import earth.terrarium.botarium.api.item.ItemContainerBlock;
import earth.terrarium.botarium.api.item.SerializableContainer;
import earth.terrarium.botarium.api.item.SimpleItemContainer;
import earth.terrarium.botarium.api.menu.ExtraDataMenuProvider;
import earth.terrarium.reaper.common.block.ReaperGeneratorData;
import earth.terrarium.reaper.common.block.ReaperGeneratorMenu;
import earth.terrarium.reaper.common.registry.ReaperRegistry;
import me.codexadrian.spirit.Corrupted;
import me.codexadrian.spirit.entity.EntityRarity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
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

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public class ReaperGeneratorBlockEntity extends BlockEntity implements EnergyBlock, ItemContainerBlock, IAnimatable, ExtraDataMenuProvider {
    private ExtractOnlyEnergyContainer energyContainer;
    private SimpleItemContainer itemContainer;
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final Predicate<Entity> ISNT_CORRUPTED = (entity) -> entity instanceof Corrupted corrupted && !corrupted.isCorrupted();

    public double distance;
    public int cooldown = 100;
    private int animationTick = 0;

    public ReaperGeneratorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ReaperRegistry.REAPER_GEN_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public ExtractOnlyEnergyContainer getEnergyStorage() {
        return energyContainer == null ? energyContainer = new ExtractOnlyEnergyContainer(this, 1000000) : energyContainer;
    }

    public void tick() {
        if (level instanceof ServerLevel serverLevel) {
            if (this.getEnergyStorage().extractEnergy(1, true) == 1) {
                EnergyHooks.distributeEnergyNearby(this);
            }
            if (distance < getMaxRange() && cooldown == 0) {
                if (distance == 0) {
                    ContainerHelper.clearOrCountMatchingItems(this.getContainer(), (itemStack) -> itemStack.is(ReaperRegistry.SOUL_CATALYST.get()), 1, false);
                    serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, worldPosition.getX() + 0.5, worldPosition.getY() + 0.3, worldPosition.getZ() + 0.5, 15, 0.4, .75, 0.4, 0.1);
                }
                distance += 0.49;
                double percent = distance / getMaxRange();
                for (double i = 0; i < 2 * Math.PI; i += .3 * (1 - (percent * .5))) {
                    double size = getMaxRange() * percent;
                    double x = this.getBlockPos().getX() + 0.5;
                    double z = this.getBlockPos().getZ() + 0.5;
                    serverLevel.sendParticles(ParticleTypes.CRIT, x + size * Math.cos(i), this.getBlockPos().getY() + 0.5, z + size * Math.sin(i), 1, 0.1, 0.1, 0.1, 0.1);
                }
                List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, new AABB(getBlockPos()).inflate(getMaxRange()), EntitySelector.NO_SPECTATORS.and(ISNT_CORRUPTED).and(entity -> !(entity instanceof Player) || this.getContainer().hasAnyMatching(stack -> stack.is(ReaperRegistry.RUNE_PLAYER.get()))).and(entity -> isInRange(entity, distance)));
                for (LivingEntity entity : entities) {
                    double healthBefore = entity.getHealth();
                    double entityX = entity.getX();
                    double entityY = entity.getY();
                    double entityZ = entity.getZ();
                    boolean hurt;
                    EntityType<?> entityType = entity.getType();
                    DamageSource reaperDamage = ReaperRegistry.REAPER_DAMAGE;
                    boolean spiritus = false;
                    if (this.getContainer().hasAnyMatching(stack -> stack.is(ReaperRegistry.RUNE_SPIRIT.get()))) {
                        reaperDamage = ReaperRegistry.playerSource(serverLevel);
                        spiritus = true;
                    }
                    hurt = entity.hurt(reaperDamage, getDamage() > 0 ? getDamage() : Float.MAX_VALUE);
                    if (hurt) {
                        double healthLost;
                        if (this.getDamage() == -1) {
                            serverLevel.sendParticles(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, entityX, entityY, entityZ, 5, 0.1, 0.1, 0.1, 0.1);
                            healthLost = healthBefore;
                        } else {
                            healthLost = healthBefore - entity.getHealth();
                        }
                        if (!spiritus)
                            this.getEnergyStorage().internalInsert((int) (healthLost * getEnergyGeneration() * EntityRarity.getRarity(entityType).energyModifer), false);
                        for (double mobDis = 0; mobDis < 1; mobDis += .2) {
                            if (this.getDamage() > 0)
                                entity.setDeltaMovement(entity.getDeltaMovement().add(0, .065, 0));
                            double x = this.getBlockPos().getX() + 0.5 - entityX;
                            double y = this.getBlockPos().getY() + 0.5 - entityY;
                            double z = this.getBlockPos().getZ() + 0.5 - entityZ;
                            serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, this.getBlockPos().getX() + 0.5 - x * mobDis, this.getBlockPos().getY() + 0.75 - y * mobDis, this.getBlockPos().getZ() + 0.5 - z * mobDis, 5, 0.01, 0.01, 0.01, 0.005);
                        }
                        serverLevel.sendParticles(ParticleTypes.SOUL, entityX, entityY + 0.5, entityZ, 10, 0.5, .5, 0.5, 0.05);
                    }
                }
            } else {
                if (cooldown == 0) {
                    distance = 0;
                    cooldown = getMaxCooldown();
                    this.update();
                }
                if (cooldown > 0 && this.getContainer().hasAnyMatching(stack -> stack.is(ReaperRegistry.SOUL_CATALYST.get())) && this.getEnergyStorage().internalInsert(1, true) == 1) {
                    cooldown--;
                    this.update();
                    if (cooldown == 36) {
                        level.playSound(null, worldPosition, ReaperRegistry.REAPER_GEN_SOUND.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                    }
                }
            }
        } else {
            if (level == null) return;
            if (cooldown < 36) {
                animationTick++;
            } else {
                if (animationTick > 36 && animationTick < 60) {
                    animationTick++;
                } else {
                    animationTick = 0;
                }
            }
        }
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);
        this.cooldown = compoundTag.getInt("Cooldown");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.putInt("Cooldown", cooldown);
    }

    public boolean isInRange(Entity entity, double distance) {
        double diffX = Math.abs(entity.getX()) - Math.abs(this.getBlockPos().getX() + 0.5);
        double diffZ = Math.abs(entity.getZ()) - Math.abs(this.getBlockPos().getZ() + 0.5);
        double sqrt = Math.sqrt(diffX * diffX + diffZ * diffZ);
        return sqrt < distance && sqrt > distance - 1987;
    }

    public int getMaxRange() {
        return this.getContainer().hasAnyMatching(stack -> stack.is(ReaperRegistry.RUNE_RANGE.get())) ? 8 : 5;
    }

    public int getDamage() {
        return this.getContainer().hasAnyMatching(stack -> stack.is(ReaperRegistry.RUNE_INSTADEATH.get())) ? -1 : 5;
    }

    public int getEnergyGeneration() {
        return this.getContainer().hasAnyMatching(stack -> stack.is(ReaperRegistry.RUNE_MORE_ENERGY.get())) ? 100 : 50;
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

    public PlayState idle(AnimationEvent<ReaperGeneratorBlockEntity> event) {
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
    public SimpleItemContainer getContainer() {
        return this.itemContainer == null ? this.itemContainer = new SimpleItemContainer(this, 8) : this.itemContainer;
    }

    @Override
    public void writeExtraData(ServerPlayer player, FriendlyByteBuf buf) {
        buf.writeBlockPos(this.getBlockPos());
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Reaper Generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ReaperGeneratorMenu(this.getContainer(), new ReaperGeneratorData(this), i, inventory, this);
    }

    public int getMaxCooldown() {
        return this.getContainer().hasAnyMatching(stack -> stack.is(ReaperRegistry.RUNE_SPEED.get())) ? 60 : 120;
    }
}
