package dev.rndmorris.salisarcana.common.recipes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import dev.rndmorris.salisarcana.lib.AspectHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.ItemShard;

public class RecipeForgivingPrimalCharm implements IArcaneRecipe {

    private final ItemShard shardItem;
    private final ItemStack primalCharmItemStack;

    public RecipeForgivingPrimalCharm() {
        shardItem = (ItemShard) ConfigItems.itemShard;
        primalCharmItemStack = new ItemStack(ConfigItems.itemResource, 1, 15);
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

            if (shardId >= 0 && shardId < 6) {
                foundShards[shardId] = true;
            }
        }

        for (var foundShard : foundShards) {
            if (!foundShard) {
                return false;
            }
        }

        for (var slot : SLOT_GOLD) {
            final var itemStack = craftingTable.getStackInSlot(slot);
            if (itemStack == null) {
                return false;
            }
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
