package earth.terrarium.reaper.common.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.NotImplementedException;

public class Utils {

    @ExpectPlatform
    public static Player makeFakePlayer(ServerLevel level) {
        throw new NotImplementedException();
    }
}
