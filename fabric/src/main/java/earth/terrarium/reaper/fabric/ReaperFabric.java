package earth.terrarium.reaper.fabric;

import earth.terrarium.reaper.Reaper;
import net.fabricmc.api.ModInitializer;

public class ReaperFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Reaper.init();
    }
}