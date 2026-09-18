package dev.rndmorris.salisarcana.lib;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;

import com.mojang.authlib.GameProfile;

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

    public static @Nullable EntityPlayerMP getPlayerByUUID(@Nonnull UUID uuid) {
        for (EntityPlayerMP player : MinecraftServer.getServer()
            .getConfigurationManager().playerEntityList) {
            if (uuid.equals(
                player.getGameProfile()
                    .getId())) {
                return player;
            }
        }
        return null;
    }

    public static @Nonnull EntityPlayerMP getOrFakePlayer(@Nonnull GameProfile profile, WorldServer world) {
        UUID uuid = profile.getId();
        if (uuid != null) {
            EntityPlayerMP player = getPlayerByUUID(uuid);
            if (player != null) {
                return player;
            }
        }

        return FakePlayerFactory.get(world, profile);
    }

    public static @Nonnull NBTTagCompound gameProfileToNBT(@Nonnull GameProfile profile) {
        final NBTTagCompound ownerInfo = new NBTTagCompound();
        final String name = profile.getName();
        final UUID uuid = profile.getId();

        if (name != null) {
            ownerInfo.setString("username", name);
        }
        if (uuid != null) {
            ownerInfo.setLong("uuidUpper", uuid.getMostSignificantBits());
            ownerInfo.setLong("uuidLower", uuid.getLeastSignificantBits());
        }

        return ownerInfo;
    }

    public static @Nullable GameProfile gameProfileFromNBT(@Nonnull NBTTagCompound nbt) {
        String username = nbt.getString("username");
        if (username.isEmpty()) username = null;

        UUID uuid = null;
        if (nbt.getTag("uuidLower") instanceof NBTTagLong uuidLower
            && nbt.getTag("uuidUpper") instanceof NBTTagLong uuidUpper) {
            uuid = new UUID(uuidUpper.func_150291_c(), uuidLower.func_150291_c());
        }

        if (username != null || uuid != null) {
            return new GameProfile(uuid, username);
        } else {
            return null;
        }
    }
}
