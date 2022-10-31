package earth.terrarium.reaper.common.util.forge;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.FakePlayerFactory;

public class UtilsImpl {
    public static Player makeFakePlayer(ServerLevel level) {
        return FakePlayerFactory.getMinecraft(level);
    }
}
