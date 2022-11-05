package earth.terrarium.reaper.common.util.fabric;

import dev.cafeteria.fakeplayerapi.server.FakePlayerBuilder;
import dev.cafeteria.fakeplayerapi.server.FakeServerPlayer;
import earth.terrarium.reaper.Reaper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class UtilsImpl {
    public static Player makeFakePlayer(ServerLevel level) {
        return new FakePlayerBuilder(new ResourceLocation(Reaper.MODID, "reaper_generator_fake"))
                .create(level.getServer(), level, "Reaper Generator Fake Player");
    }
}
