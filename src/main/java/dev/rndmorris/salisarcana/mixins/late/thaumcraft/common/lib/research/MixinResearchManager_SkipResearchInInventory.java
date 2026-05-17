package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib.research;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReceiver;

import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.research.ResearchManager;

@Mixin(value = ResearchManager.class, remap = false)
public class MixinResearchManager_SkipResearchInInventory {

    @ModifyReceiver(
        method = "findHiddenResearch",
        at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;size()I", ordinal = 0))
    private static ArrayList<String> removeInventoryResearch(ArrayList<String> keys, EntityPlayer player) {
        for (final ItemStack slot : player.inventory.mainInventory) {
            if (slot == null || slot.getItem() != ConfigItems.itemResearchNotes) continue;

            final var data = ResearchManager.getData(slot);

            if (data != null) {
                keys.remove(data.key);
            }
        }

        return keys;
    }
}
