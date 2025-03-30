package dev.rndmorris.salisarcana.mixins.late.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.items.wands.foci.ItemFocusExcavation;

@Mixin(ItemFocusExcavation.class)
public class MixinItemFocusExcavation_HarvestLevel extends ItemFocusBasic {

    @WrapMethod(method = "excavate", remap = false)
    private boolean wrapExcavate(World world, ItemStack stack, EntityPlayer player, Block block, int md, int x, int y,
        int z, Operation<Boolean> original) {
        int requiredLevel = block.getHarvestLevel(md);
        int harvestLevel = ConfigModuleRoot.enhancements.excavationFocusHarvestLevel.getValue();
        int modifiedHarvestLevel = harvestLevel;
        if (ConfigModuleRoot.enhancements.potencyModifiesHarvestLevel.isEnabled()) {
            ItemWandCasting wandCasting = (ItemWandCasting) stack.getItem();
            @SuppressWarnings("DataFlowIssue") // idea doesn't know that wandCasting.getFocusItem(stack) can't be null
            ItemStack focus = wandCasting.getFocusItem(stack);
            modifiedHarvestLevel += this.getUpgradeLevel(focus, FocusUpgradeType.potency);
        }
        if (harvestLevel < 0 || modifiedHarvestLevel >= requiredLevel) {
            return original.call(world, stack, player, block, md, x, y, z);
        }
        player.worldObj.playSoundEffect(player.posX, player.posY, player.posZ, "thaumcraft:craftfail", 1.0F, 1.0F);
        return false;
    }
}
