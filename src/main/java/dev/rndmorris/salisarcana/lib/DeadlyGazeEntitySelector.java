package dev.rndmorris.salisarcana.lib;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * This class is used to filter out entities that should not be affected by the
 * Deadly Gaze effect.
 *
 * @author Midnight145
 */
public class DeadlyGazeEntitySelector implements IEntitySelector {

    public static DeadlyGazeEntitySelector INSTANCE = new DeadlyGazeEntitySelector();

    @Override
    public boolean isEntityApplicable(Entity entity) {
        if (entity instanceof EntityLivingBase living) {
            return !(living.isEntityInvulnerable()
                || !living.isPotionApplicable(new PotionEffect(Potion.wither.getId(), 1))
                || (living instanceof EntitySkeleton skeleton && skeleton.getSkeletonType() != 0)
                || living instanceof EntityWither
                || living instanceof EntityDragon);
        }
        return false;
    }
}
