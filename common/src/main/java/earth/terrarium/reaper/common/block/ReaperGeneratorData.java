package earth.terrarium.reaper.common.block;

import earth.terrarium.reaper.common.blockentity.ReaperGeneratorBlockEntity;
import net.minecraft.world.inventory.ContainerData;

public class ReaperGeneratorData implements ContainerData {
    public final ReaperGeneratorBlockEntity gen;
    public static final int ENERGY = 0;
    public static final int COOLDOWN = 1;

    public ReaperGeneratorData(ReaperGeneratorBlockEntity reaperGeneratorBlockEntity) {
        this.gen = reaperGeneratorBlockEntity;
    }

    @Override
    public int get(int i) {
        return switch (i) {
            case ENERGY -> (int) gen.getEnergyStorage().getStoredEnergy();
            case COOLDOWN -> gen.cooldown;
            default -> 0;
        };
    }

    @Override
    public void set(int i, int j) {
    }

    @Override
    public int getCount() {
        return 2;
    }
}
