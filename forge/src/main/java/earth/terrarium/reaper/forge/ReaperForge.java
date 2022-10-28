package earth.terrarium.reaper.forge;

import earth.terrarium.reaper.Reaper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Reaper.MOD_ID)
public class ReaperForge {
    public ReaperForge() {
        Reaper.init();
        Reaper.register();
    }
}