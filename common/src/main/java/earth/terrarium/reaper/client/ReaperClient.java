package earth.terrarium.reaper.client;

import earth.terrarium.reaper.client.screen.ReaperGeneratorScreen;
import earth.terrarium.reaper.common.registry.ReaperRegistry;
import net.minecraft.client.gui.screens.MenuScreens;

public class ReaperClient {
    public static void init() {
        MenuScreens.register(ReaperRegistry.REAPER_GEN_MENU.get(), ReaperGeneratorScreen::new);
    }
}
