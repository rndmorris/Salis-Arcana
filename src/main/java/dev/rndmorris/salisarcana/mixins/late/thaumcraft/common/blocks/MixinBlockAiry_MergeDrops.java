package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.mitchej123.hodgepodge.util.MergeDropsHelper;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import dev.rndmorris.salisarcana.config.SalisConfig;
import fox.spiteful.avaritia.items.ItemMatterCluster;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.INode;
import thaumcraft.common.blocks.BlockAiry;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.ItemWispEssence;

@Mixin(BlockAiry.class)
public abstract class MixinBlockAiry_MergeDrops extends BlockContainer {

    /**
     * @author nimuck
     * @reason Merge ethereal essence drops from broken nodes to prevent lags
     */
    @Overwrite
    public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player) {
        if (meta == 0 && !world.isRemote) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof INode) {
                AspectList aspects = ((INode) te).getAspects();
                if (aspects != null && aspects.size() > 0) {
                    List<ItemStack> drops = new ArrayList<>(aspects.size());
                    long totalCount = 0;

                    for (Aspect aspect : aspects.getAspects()) {
                        int amount = aspects.getAmount(aspect);
                        if (amount >= 5) {
                            int count = amount / 10 + 1;
                            ItemStack essenceStack = new ItemStack(ConfigItems.itemWispEssence, count);
                            ((ItemWispEssence) essenceStack.getItem())
                                .setAspects(essenceStack, new AspectList().add(aspect, 2));
                            drops.add(essenceStack);
                            totalCount += count;
                        }
                    }

                    if (Loader.isModLoaded("hodgepodge")) mergeNodeDrops(drops, totalCount);

                    for (ItemStack drop : drops) {
                        this.dropBlockAsItem(world, x, y, z, drop);
                    }
                }
            }
        }
        super.onBlockHarvested(world, x, y, z, meta, player);
    }

    @Optional.Method(modid = "hodgepodge")
    private static void mergeNodeDrops(List<ItemStack> drops, long totalCount) {
        if (totalCount < SalisConfig.thaum.mergeNodeDropsThresholdFirst.getValue()) {
            // mode 0
            MergeDropsHelper.respectMaxStackSize(drops, null, false, (int) (totalCount / 64L) + 1);
        } else if (totalCount < SalisConfig.thaum.mergeNodeDropsThresholdSecond.getValue()
            && Loader.isModLoaded("Avaritia")) {
                // mode 1
                MergeDropsHelper
                    .convertToClusters(drops, (int) (totalCount / ItemMatterCluster.MAX_NORMAL_CAPACITY) + 1);
            }
        // Otherwise (>= second threshold, or Avaritia absent): mode 2 (FORCE_MERGE_INTO_ONE)
    }

    private MixinBlockAiry_MergeDrops(Material material) {
        super(material);
    }
}
