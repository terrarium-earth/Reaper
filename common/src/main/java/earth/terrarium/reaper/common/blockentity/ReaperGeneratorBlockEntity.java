package earth.terrarium.reaper.common.blockentity;

import earth.terrarium.botarium.api.energy.EnergyBlock;
import earth.terrarium.botarium.api.energy.ExtractOnlyEnergyContainer;
import earth.terrarium.botarium.api.energy.StatefulEnergyContainer;
import earth.terrarium.reaper.common.registry.ReaperRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class ReaperGeneratorBlockEntity extends BlockEntity implements EnergyBlock, IAnimatable {
    private ExtractOnlyEnergyContainer energyContainer;
    private final AnimationFactory factory = new AnimationFactory(this);

    public double distance;
    private int hurtMobData;
    private int cooldown = 0;
    private int animationTick = 0;

    public ReaperGeneratorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ReaperRegistry.REAPER_GEN_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public StatefulEnergyContainer getEnergyStorage() {
        return energyContainer == null ? energyContainer = new ExtractOnlyEnergyContainer(this, 1000) : energyContainer;
    }

    public void tick() {
        if (level instanceof ServerLevel serverLevel) {
            if (hurtMobData > 0) {
                serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, worldPosition.getX() + 0.5, worldPosition.getY() + 0.3, worldPosition.getZ() + 0.5, 1, 0.3, 0.0, 0.3, 0.001);
                serverLevel.sendParticles(ParticleTypes.SOUL, worldPosition.getX() + 0.5, worldPosition.getY() + 0.3, worldPosition.getZ() + 0.5, 1, 0.3, 0.0, 0.3, 0.001);
            }
            if (distance < getMaxRange() && cooldown == 0) {
                animationTick = 0;
                this.setChanged();
                distance += 0.4;
                double percent = distance / getMaxRange();
                for (double i = 0; i < 2 * Math.PI; i += .4 * (1 - (percent * .5))) {
                    double size = getMaxRange() * percent;
                    double x = this.getBlockPos().getX() + 0.5;
                    double z = this.getBlockPos().getZ() + 0.5;
                    double disperse = 0.003;
                    serverLevel.sendParticles(ParticleTypes.CRIT, x + size * Math.cos(i), this.getBlockPos().getY() + 0.5, z + size * Math.sin(i), 1, 0.01, 0.01, 0.01, disperse);
                    //serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x + size * Math.cos(i), this.getBlockPos().getY() + 0.5, z + size * Math.sin(i), 1, 0.01, 0.01, 0.01, disperse);
                }
                List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, new AABB(getBlockPos()).inflate(5));
                for (LivingEntity entity : entities) {
                    if (isInRange(entity, distance)) {
                        boolean hurt = entity.hurt(DamageSource.ANVIL, getDamage());
                        if (hurt) {
                            hurtMobData += 100;
                            for (double mobDis = 0; mobDis < 1; mobDis += .2) {
                                entity.setDeltaMovement(entity.getDeltaMovement().add(0, .065, 0));
                                double x = this.getBlockPos().getX() + 0.5 - entity.getX();
                                double y = this.getBlockPos().getY() + 0.5 - entity.getY();
                                double z = this.getBlockPos().getZ() + 0.5 - entity.getZ();
                                serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, this.getBlockPos().getX() + 0.5 - x * mobDis, this.getBlockPos().getY() + 0.75 - y * mobDis, this.getBlockPos().getZ() + 0.5 - z * mobDis, 5, 0.01, 0.01, 0.01, 0.005);
                            }
                            serverLevel.sendParticles(ParticleTypes.SOUL, entity.getX(), entity.getY() + 0.5, entity.getZ(), 10, 0.5, .5, 0.5, 0.05);
                        }
                    }
                }
            } else {
                if (hurtMobData > 0) {
                    hurtMobData--;
                    this.getEnergyStorage();
                    this.energyContainer.insertEnergy(30, false);
                } else if (cooldown == 0) {
                    distance = 0;
                    cooldown = 100;
                }
                if (cooldown > 0) cooldown--;
                if (cooldown > 63) {
                    animationTick++;
                    this.setChanged();
                }
            }
        }
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);
        this.animationTick = compoundTag.getInt("AnimationTick");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.putInt("AnimationTick", animationTick);
    }

    public boolean isInRange(LivingEntity entity, double distance) {
        double diffX = Math.abs(entity.getX()) - Math.abs(this.getBlockPos().getX() + 0.5);
        double diffZ = Math.abs(entity.getZ()) - Math.abs(this.getBlockPos().getZ() + 0.5);
        double sqrt = Math.sqrt(diffX * diffX + diffZ * diffZ);
        return sqrt < distance && sqrt > distance - 1987;
    }

    public int getMaxRange() {
        return 5;
    }

    public float getDamage() {
        return 1.5F;
    }

    public int getEnergyGeneration() {
        return 30;
    }

    @Override
    public void update() {
        this.setChanged();
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::idle));
    }

    public PlayState idle(AnimationEvent<ReaperGeneratorBlockEntity> event) {
        if (this.animationTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.reaper_generator.slam").addAnimation("animation.reaper_generator.idle", true));
            return PlayState.CONTINUE;
        } else {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.reaper_generator.idle", true));
            return PlayState.CONTINUE;
        }
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
