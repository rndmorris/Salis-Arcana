package dev.rndmorris.salisarcana.lib;

import net.minecraft.entity.player.EntityPlayer;

public final class PlayerHelper {

    private PlayerHelper() {}

    // This function is copied from
    // https://github.com/GTNewHorizons/EnderIO/blob/master/src/main/java/crazypants/enderio/xp/XpUtil.java#L30
    // The original is licensed under the CC0 1.0 Universal License.
    public static int getExperienceForLevel(int level) {
        if (level == 0) {
            return 0;
        }
        if (level > 0 && level < 16) {
            return level * 17;
        } else if (level > 15 && level < 31) {
            return (int) (1.5 * Math.pow(level, 2) - 29.5 * level + 360);
        } else {
            return (int) (3.5 * Math.pow(level, 2) - 151.5 * level + 2220);
        }
    }

    public static int getExperienceTotal(EntityPlayer player) {
        return getExperienceForLevel(player.experienceLevel) + (int) (player.experience * player.xpBarCap());
    }
}
