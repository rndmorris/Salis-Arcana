package dev.rndmorris.salisarcana.lib;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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

    public static ItemStack[] getItemsInInventory(EntityPlayer player, Class<? extends Item> itemClass) {
        // We don't just use player.inventory.mainInventory here because we want to include anything
        // that's not part of the main inventory, like armor slots
        return player.inventoryContainer.inventorySlots.stream()
            .filter(
                slot -> slot.getHasStack() && itemClass.isInstance(
                    slot.getStack()
                        .getItem()))
            .map(Slot::getStack)
            .toArray(ItemStack[]::new);
    }
}
