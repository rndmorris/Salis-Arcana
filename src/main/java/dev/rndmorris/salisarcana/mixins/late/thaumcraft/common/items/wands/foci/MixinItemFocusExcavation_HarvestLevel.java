package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.wands.foci;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import dev.rndmorris.salisarcana.config.SalisConfig;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.items.wands.foci.ItemFocusExcavation;

@Mixin(ItemFocusExcavation.class)
public class MixinItemFocusExcavation_HarvestLevel extends ItemFocusBasic {

    // i dunno why this has to be static, but it does
    // the definition just wouldn't be embedded into the class otherwise
    @Unique
    private static final int sa$harvestLevel = SalisConfig.features.excavationFocusHarvestLevel.getValue();

    @Unique
    private final boolean sa$potencyEnabled = SalisConfig.features.potencyModifiesHarvestLevel.isEnabled();

    @WrapMethod(method = "excavate", remap = false)
    private boolean wrapExcavate(World world, ItemStack stack, EntityPlayer player, Block block, int md, int x, int y,
        int z, Operation<Boolean> original) {
        int requiredLevel = block.getHarvestLevel(md);
        int harvestLevel = sa$harvestLevel;
        if (sa$potencyEnabled) {
            ItemWandCasting wandCasting = (ItemWandCasting) stack.getItem();
            @SuppressWarnings("DataFlowIssue") // idea doesn't know that wandCasting.getFocusItem(stack) can't be null
            ItemStack focus = wandCasting.getFocusItem(stack);
            harvestLevel += this.getUpgradeLevel(focus, FocusUpgradeType.potency);
        }
        if (harvestLevel >= requiredLevel) {
            return original.call(world, stack, player, block, md, x, y, z);
        }
        player.worldObj.playSoundEffect(player.posX, player.posY, player.posZ, "thaumcraft:craftfail", 1.0F, 1.0F);
        return false;
    }
}
