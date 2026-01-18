package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib.crafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;

import dev.rndmorris.salisarcana.api.IMultipleResearchArcaneRecipe;
import dev.rndmorris.salisarcana.lib.WandHelper;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;
import thaumcraft.common.lib.crafting.ArcaneSceptreRecipe;

@Mixin(ArcaneSceptreRecipe.class)
public abstract class MixinArcaneSceptreRecipe_MissingResearchWorkbench implements IMultipleResearchArcaneRecipe {

    @Override
    public String[] salisArcana$getResearches(IInventory inv, World world, EntityPlayer player) {
        WandCap cap = WandHelper.getWandCapFromItem(inv.getStackInSlot(1));
        WandRod rod = WandHelper.getWandRodFromItem(inv.getStackInSlot(4));

        if (cap != null && rod != null) {
            return new String[] { "SCEPTRE", cap.getResearch(), rod.getResearch() };
        } else {
            return new String[] { "SCEPTRE" };
        }
    }
}
