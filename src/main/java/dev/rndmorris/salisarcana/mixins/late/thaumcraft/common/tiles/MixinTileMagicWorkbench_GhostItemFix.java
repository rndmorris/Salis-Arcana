package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.rndmorris.salisarcana.SalisArcana;
import thaumcraft.common.tiles.TileMagicWorkbench;

@Mixin(value = TileMagicWorkbench.class, remap = false)
public class MixinTileMagicWorkbench_GhostItemFix {

    @Inject(method = "readCustomNBT", at = @At("HEAD"))
    public void copyWandNBTTagOnClient(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
        if (!SalisArcana.proxy.isSingleplayerClient()) return;
        NBTTagList inventory = par1NBTTagCompound.getTagList("Inventory", 10);

        // Normally, the wand is the last tag in the list, but that is not strictly required by the format of Inventory
        // NBTs
        for (int i = inventory.tagCount() - 1; i >= 0; i--) {
            NBTTagCompound item = inventory.getCompoundTagAt(i);
            if (item.getByte("Slot") == 10) {
                NBTBase tag = item.getTag("tag");
                if (tag != null) {
                    // Replace the NBT with a copy, so vis spending on the client doesn't affect the server's NBT tag
                    item.setTag("tag", tag.copy());
                }
                return;
            }
        }
    }
}
