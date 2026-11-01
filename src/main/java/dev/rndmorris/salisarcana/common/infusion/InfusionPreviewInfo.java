package dev.rndmorris.salisarcana.common.infusion;

import java.util.HashSet;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class InfusionPreviewInfo {

    public ItemStack recipeOutput;
    public AspectList recipeEssentia;
    public HashSet<Aspect> availableEssentia;

    public int instability;

    // For enchantment recipes the client formats the localized display name from these.
    // enchantmentId < 0 means "not an enchantment recipe".
    public int enchantmentId = -1;
    public int enchantmentLevel;
    public int enchantmentXpCost;

    public String getOutputString() {
        if (enchantmentId >= 0) {
            Enchantment enchantment = Enchantment.enchantmentsList[enchantmentId];
            if (enchantment == null) return null;
            return EnumChatFormatting.YELLOW + enchantment.getTranslatedName(enchantmentLevel)
                + " ("
                + StatCollector.translateToLocalFormatted("salisarcana:infusion.preview.enchantment", enchantmentXpCost)
                + ')';
        }
        if (recipeOutput == null) return null;
        EnumChatFormatting color = recipeOutput.getRarity().rarityColor;
        return color.toString() + recipeOutput.stackSize + "x " + recipeOutput.getDisplayName();
    }

    public String getInstabilityLevelString() {
        int bucket = Math.min(5, Math.max(0, instability / 2));
        return StatCollector.translateToLocal("tc.inst." + bucket);
    }

    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, recipeOutput);
        writeAspectList(buf, recipeEssentia);
        writeAspectSet(buf, availableEssentia);
        buf.writeInt(instability);
        buf.writeInt(enchantmentId);
        buf.writeInt(enchantmentLevel);
        buf.writeInt(enchantmentXpCost);
    }

    public void fromBytes(ByteBuf buf) {
        recipeOutput = ByteBufUtils.readItemStack(buf);
        recipeEssentia = readAspectList(buf);
        availableEssentia = readAspectSet(buf);
        instability = buf.readInt();
        enchantmentId = buf.readInt();
        enchantmentLevel = buf.readInt();
        enchantmentXpCost = buf.readInt();
    }

    private static void writeAspectList(ByteBuf buf, AspectList list) {
        if (list == null) {
            buf.writeShort(-1);
            return;
        }
        Aspect[] aspects = list.getAspects();
        int count = 0;
        for (Aspect a : aspects) {
            if (a != null) count++;
        }
        buf.writeShort(count);
        for (Aspect a : aspects) {
            if (a == null) continue;
            ByteBufUtils.writeUTF8String(buf, a.getTag());
            buf.writeInt(list.getAmount(a));
        }
    }

    private static AspectList readAspectList(ByteBuf buf) {
        short n = buf.readShort();
        if (n < 0) return null;
        AspectList list = new AspectList();
        for (int i = 0; i < n; i++) {
            String tag = ByteBufUtils.readUTF8String(buf);
            int amount = buf.readInt();
            Aspect a = Aspect.getAspect(tag);
            if (a != null) list.add(a, amount);
        }
        return list;
    }

    private static void writeAspectSet(ByteBuf buf, HashSet<Aspect> aspects) {
        buf.writeInt(aspects.size());
        for (var aspect : aspects) {
            ByteBufUtils.writeUTF8String(buf, aspect.getTag());
        }
    }

    private static HashSet<Aspect> readAspectSet(ByteBuf buf) {
        var result = new HashSet<Aspect>();
        var count = buf.readInt();
        for (int i = 0; i < count; ++i) {
            final var tag = ByteBufUtils.readUTF8String(buf);
            final var aspect = Aspect.getAspect(tag);
            if (aspect != null) {
                result.add(aspect);
            }
        }
        return result;
    }
}
