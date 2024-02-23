package earth.terrarium.reaper;

import com.teamresourceful.resourcefulconfig.common.config.Configurator;
import earth.terrarium.reaper.common.config.ReaperConfig;
import earth.terrarium.reaper.common.registry.ReaperRegistry;

public class Reaper {
    public static final String MODID = "reaper";
    public static final Configurator CONFIGURATOR = new Configurator();

    public static void init() {
        ReaperRegistry.init();
        CONFIGURATOR.registerConfig(ReaperConfig.class);
    }

    public static void register() {
        ReaperRegistry.register();
    }
}