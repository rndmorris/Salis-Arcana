package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.entities.monster;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.monster.EntityTaintSpore;
import thaumcraft.common.entities.monster.EntityTaintSporeSwarmer;

@Mixin(value = EntityTaintSporeSwarmer.class)
public abstract class MixinEntityTaintSporeSwarmer_MergeMobDrops extends EntityTaintSpore {

    /**
     * @author nimuck
     * @reason Batch ItemStacks instead of looping
     */
    @Overwrite
    protected void dropFewItems(boolean flag, int i) {
        int count11 = Integer.bitCount(this.rand.nextInt() & 0b11);
        int count12 = 2 - count11;
        float offsetY = this.height / 2.0F;

        if (count11 > 0) {
            this.entityDropItem(new ItemStack(ConfigItems.itemResource, count11, 11), offsetY);
        }
        if (count12 > 0) {
            this.entityDropItem(new ItemStack(ConfigItems.itemResource, count12, 12), offsetY);
        }
    }

    private MixinEntityTaintSporeSwarmer_MergeMobDrops(World world) {
        super(world);
    }
}
