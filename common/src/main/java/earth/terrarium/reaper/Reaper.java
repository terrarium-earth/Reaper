package earth.terrarium.reaper;

import earth.terrarium.reaper.common.registry.ReaperRegistry;

public class Reaper {
    public static final String MOD_ID = "reaper";

    public static void init() {
        ReaperRegistry.init();
    }

    public static void register() {
        ReaperRegistry.register();
    }
}