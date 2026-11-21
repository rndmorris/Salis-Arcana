package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.entities.monster;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.monster.EntityPech;
import thaumcraft.common.items.ItemManaBean;

@Mixin(value = EntityPech.class)
public abstract class MixinEntityPech_MergeMobDrops extends EntityMob {

    /**
     * @author nimuck
     * @reason Faster mana bean drop logic equivalent
     */
    @Overwrite
    protected void dropFewItems(boolean flag, int i) {
        ThreadLocalRandom rand = ThreadLocalRandom.current();

        for (int a = 0; a < this.loot.length; a++) {
            if (this.loot[a] != null && rand.nextFloat() < 0.88F) {
                this.entityDropItem(this.loot[a].copy(), 1.5F);
            }
        }

        ItemStack[] templates = salisarcana$getPrimalBeanTemplates();
        int kLen = templates.length;
        int remaining = salisarcana$sampleBinomialHalf(rand, 1 + i);

        for (int k = 0; k < kLen && remaining > 0; k++) {
            int count;
            if (k == kLen - 1) {
                count = remaining;
            } else {
                count = salisarcana$sampleBinomial(rand, remaining, 1.0 / (kLen - k));
                remaining -= count;
            }

            if (count > 0) {
                ItemStack is = templates[k].copy();
                is.stackSize = count;
                this.entityDropItem(is, 1.5F);
            }
        }

        if (rand.nextInt(10) < 1 + i) {
            this.entityDropItem(new ItemStack(ConfigItems.itemResource, 1, 18), 1.5F);
        }
        super.dropFewItems(flag, i);
    }

    @Unique
    private static ItemStack[] salisarcana$PRIMAL_BEAN_TEMPLATES;

    @Unique
    private static ItemStack[] salisarcana$getPrimalBeanTemplates() {
        if (salisarcana$PRIMAL_BEAN_TEMPLATES == null) {
            List<Aspect> primals = Aspect.getPrimalAspects();
            ItemStack[] templates = new ItemStack[primals.size()];
            for (int k = 0; k < primals.size(); k++) {
                ItemStack is = new ItemStack(ConfigItems.itemManaBean, 1);
                ((ItemManaBean) is.getItem()).setAspects(is, new AspectList().add(primals.get(k), 1));
                templates[k] = is;
            }
            salisarcana$PRIMAL_BEAN_TEMPLATES = templates;
        }
        return salisarcana$PRIMAL_BEAN_TEMPLATES;
    }

    @Unique
    private static int salisarcana$sampleBinomialHalf(ThreadLocalRandom rand, int n) {
        if (n <= 0) return 0; // Negative looting guard
        if (n <= 256) {
            int hits = 0;
            while (n >= 64) {
                hits += Long.bitCount(rand.nextLong());
                n -= 64;
            }
            if (n > 0) {
                hits += Long.bitCount(rand.nextLong() & ((1L << n) - 1L));
            }
            return hits;
        }
        double mean = n * 0.5;
        double stdDev = Math.sqrt(n) * 0.5;
        int sample = (int) Math.round(mean + rand.nextGaussian() * stdDev);
        return Math.max(0, Math.min(n, sample));
    }

    @Unique
    private static int salisarcana$sampleBinomial(ThreadLocalRandom rand, int n, double p) {
        if (p >= 1.0) return n;
        if (n <= 64) {
            int hits = 0;
            for (int j = 0; j < n; j++) {
                if (rand.nextDouble() < p) hits++;
            }
            return hits;
        }
        double mean = n * p;
        double stdDev = Math.sqrt(mean * (1.0 - p));
        int sample = (int) Math.round(mean + rand.nextGaussian() * stdDev);
        return Math.max(0, Math.min(n, sample));
    }

    @Shadow(remap = false)
    private ItemStack[] loot;

    private MixinEntityPech_MergeMobDrops(World world) {
        super(world);
    }
}
