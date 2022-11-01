package earth.terrarium.reaper.forge;

import earth.terrarium.reaper.Reaper;
import net.minecraftforge.fml.common.Mod;

@Mod(Reaper.MODID)
public class ReaperForge {
    public ReaperForge() {
        Reaper.init();
        Reaper.register();
    }
}