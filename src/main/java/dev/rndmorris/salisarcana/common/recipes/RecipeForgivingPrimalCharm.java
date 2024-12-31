package dev.rndmorris.salisarcana.common.recipes;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import dev.rndmorris.salisarcana.lib.ArrayHelper;
import dev.rndmorris.salisarcana.lib.AspectHelper;
import thaumcraft.api.ItemApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.common.items.ItemResource;
import thaumcraft.common.items.ItemShard;

public class RecipeForgivingPrimalCharm implements IArcaneRecipe {

    private final ItemShard shardItem;
    private final ItemStack primalCharmItemStack;
    public final boolean initializedSuccessfully;

    public RecipeForgivingPrimalCharm() {
        final var shardStack = ItemApi.getItem("itemShard", 0);
        if (shardStack == null || !(shardStack.getItem() instanceof ItemShard shard)) {
            LOG.error("Failed to look up TC4's shard item in {}.", RecipeForgivingPrimalCharm.class.getName());
            shardItem = null;
        } else {
            shardItem = shard;
        }
        final var charmStack = ItemApi.getItem("itemResource", 15);
        if (charmStack == null || !(charmStack.getItem() instanceof ItemResource)) {
            LOG.error("Failed to look up TC4's primal charm in {}.", RecipeForgivingPrimalCharm.class.getName());
            primalCharmItemStack = null;
        } else {
            primalCharmItemStack = charmStack;
        }

        initializedSuccessfully = shardItem != null && primalCharmItemStack != null;
    }

    private final int[] SLOT_PRIMAL = new int[] { 0, 1, 2, 6, 7, 8, };
    private final int[] SLOT_GOLD = new int[] { 3, 5, };
    private final int SLOT_BALANCED = 4;

    @Override
    public boolean matches(IInventory craftingTable, World world, EntityPlayer player) {
        final var foundShards = new boolean[6];

        for (var slot : SLOT_PRIMAL) {
            final var itemStack = craftingTable.getStackInSlot(slot);
            final var shardId = shardId(itemStack);
            ArrayHelper.tryAssign(foundShards, shardId, true);
        }

        for (var foundShard : foundShards) {
            if (!foundShard) {
                return false;
            }
        }

        for (var slot : SLOT_GOLD) {
            final var itemStack = craftingTable.getStackInSlot(slot);
            if (itemStack.getItem() != Items.gold_ingot) {
                return false;
            }
        }

        final var balancedSlot = craftingTable.getStackInSlot(SLOT_BALANCED);
        return shardId(balancedSlot) == 6;
    }

    private int shardId(ItemStack itemStack) {
        if (itemStack == null) {
            return -1;
        }
        if (itemStack.getItem() != shardItem) {
            return -1;
        }
        return itemStack.getItemDamage();
    }

    @Override
    public ItemStack getCraftingResult(IInventory var1) {
        return primalCharmItemStack.copy();
    }

    @Override
    public int getRecipeSize() {
        return 9;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return primalCharmItemStack.copy();
    }

    @Override
    public AspectList getAspects() {
        return AspectHelper.primalList(25);
    }

    @Override
    public AspectList getAspects(IInventory var1) {
        return AspectHelper.primalList(25);
    }

    @Override
    public String getResearch() {
        return "BASICARTIFACE";
    }
}
