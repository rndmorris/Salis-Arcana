package dev.rndmorris.salisarcana.mixins.late.lib;

import dev.rndmorris.salisarcana.api.IMultipleResearchArcaneRecipe;
import dev.rndmorris.salisarcana.lib.WandHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;
import thaumcraft.common.lib.crafting.ArcaneWandRecipe;

import java.util.List;

@Mixin(ArcaneWandRecipe.class)
public abstract class MixinArcaneWandRecipe implements IMultipleResearchArcaneRecipe {
    @Override
    public List<String> salisArcana$getResearches(IInventory inv, World world, EntityPlayer player) {
        WandCap cap = WandHelper.getWandCapFromItem(inv.getStackInSlot(2));
        WandRod rod = WandHelper.getWandRodFromItem(inv.getStackInSlot(4));

        if(cap != null && rod != null) {
            return List.of(cap.getResearch(), rod.getResearch());
        } else {
            return List.of();
        }
    }
}
