package dev.rndmorris.salisarcana.common;

import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import thaumcraft.common.entities.projectile.EntityPrimalArrow;

public class BehaviorDispensePrimalArrow implements IBehaviorDispenseItem {

    @Override
    public ItemStack dispense(IBlockSource source, ItemStack stack) {
        // This is implemented similarly to how potions are dispensed
        return new BehaviorProjectileDispense() {

            @Override
            // Required to access both the exact item being dispensed and
            // the arrow entity without relying on state
            protected IProjectile getProjectileEntity(World worldIn, IPosition position) {
                final var entity = new EntityPrimalArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entity.type = stack.getItemDamage() % 6;
                return entity;
            }
        }.dispense(source, stack);
    }
}
