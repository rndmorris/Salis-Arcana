package dev.rndmorris.salisarcana.mixins.late.lib;

import dev.rndmorris.salisarcana.api.IMultipleResearchArcaneRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import thaumcraft.common.lib.crafting.ArcaneSceptreRecipe;

import java.util.List;

@Mixin(ArcaneSceptreRecipe.class)
public abstract class MixinArcaneSceptreRecipe implements IMultipleResearchArcaneRecipe {
    @Override
    public List<String> getResearches(IInventory inv, World world, EntityPlayer player) {
        return null;
    }
}
