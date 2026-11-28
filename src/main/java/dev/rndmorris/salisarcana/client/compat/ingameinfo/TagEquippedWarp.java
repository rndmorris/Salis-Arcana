package dev.rndmorris.salisarcana.client.compat.ingameinfo;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import com.github.lunatrius.ingameinfo.tag.Tag;

import baubles.api.BaublesApi;
import thaumcraft.api.IWarpingGear;

public class TagEquippedWarp extends Tag {

    @Override
    public String getCategory() {
        return "SalisArcana";
    }

    @Override
    public String getValue() {
        if (player == null) return "0";

        int total = 0;

        for (int i = 0; i < 4; i++) {
            ItemStack armor = player.getCurrentArmor(i);
            if (armor != null && armor.getItem() instanceof IWarpingGear) {
                total += ((IWarpingGear) armor.getItem()).getWarp(armor, player);
            }
        }

        IInventory baubles = BaublesApi.getBaubles(player);
        if (baubles != null) {
            for (int i = 0; i < baubles.getSizeInventory(); i++) {
                ItemStack bauble = baubles.getStackInSlot(i);
                if (bauble != null && bauble.getItem() instanceof IWarpingGear) {
                    total += ((IWarpingGear) bauble.getItem()).getWarp(bauble, player);
                }
            }
        }

        return String.valueOf(total);
    }
}
