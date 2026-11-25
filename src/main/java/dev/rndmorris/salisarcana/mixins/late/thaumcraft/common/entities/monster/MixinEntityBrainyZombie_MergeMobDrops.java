package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.entities.monster;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.monster.EntityBrainyZombie;

@Mixin(value = EntityBrainyZombie.class)
public abstract class MixinEntityBrainyZombie_MergeMobDrops extends EntityZombie {

    /**
     * @author nimuck
     * @reason Batch ItemStacks instead of looping
     */
    @Overwrite
    protected void dropFewItems(boolean flag, int i) {
        int count = Integer.bitCount(this.rand.nextInt() & 0b111);
        if (count > 0) {
            this.dropItem(Items.rotten_flesh, count);
        }

        if (this.rand.nextInt(10) - i <= 4) {
            this.entityDropItem(new ItemStack(ConfigItems.itemZombieBrain), 1.5F);
        }
    }

    private MixinEntityBrainyZombie_MergeMobDrops(World world) {
        super(world);
    }
}
