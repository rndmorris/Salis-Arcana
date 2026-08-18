package dev.rndmorris.salisarcana.common.infusion;

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
    public AspectList availableEssentia;

    public float instability;

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
        int bucket = Math.min(5, Math.max(0, Math.round(instability) / 2));
        return StatCollector.translateToLocal("tc.inst." + bucket);
    }

    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, recipeOutput);
        writeAspectList(buf, recipeEssentia);
        writeAspectList(buf, availableEssentia);
        buf.writeFloat(instability);
        buf.writeInt(enchantmentId);
        buf.writeInt(enchantmentLevel);
        buf.writeInt(enchantmentXpCost);
    }

    public void fromBytes(ByteBuf buf) {
        recipeOutput = ByteBufUtils.readItemStack(buf);
        recipeEssentia = readAspectList(buf);
        availableEssentia = readAspectList(buf);
        instability = buf.readFloat();
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
}
