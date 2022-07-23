package test.gc.switchele;

import emu.grasscutter.game.player.Player;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

public class VersionSupportHelper {
    /**
     * 1.2.3 changed the player api, so we need to use reflections to get the proper method for the server instance
     *
     * @return method reference for getting the current position from a player object. Return null if it was unable to find it
     */
    @Nullable
    public static Method getPositionMethod() {
        try {
            return Player.class.getMethod("getPos");
        } catch (NoSuchMethodException ignored) {
        }
        try {
            return Player.class.getMethod("getPosition");
        } catch (NoSuchMethodException ignored) {
        }
        return null;
    }
}
