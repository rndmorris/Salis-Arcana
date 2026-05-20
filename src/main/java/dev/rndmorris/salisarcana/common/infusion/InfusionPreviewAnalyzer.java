package dev.rndmorris.salisarcana.common.infusion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;

import dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles.AccessorTileInfusionMatrix;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectSource;
import thaumcraft.api.crafting.InfusionEnchantmentRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.tiles.TileInfusionMatrix;
import thaumcraft.common.tiles.TileMirrorEssentia;
import thaumcraft.common.tiles.TilePedestal;

public final class InfusionPreviewAnalyzer {

    private static final int INFUSION_RANGE = 12;
    private static final int MIRROR_RANGE = 8;

    private InfusionPreviewAnalyzer() {}

    public static InfusionPreviewInfo analyze(World world, TileInfusionMatrix matrix, EntityPlayer player) {
        InfusionPreviewInfo info = computeData(world, matrix, player);
        info.availableEssentia = fetchEssentiaSources(world, matrix);
        return info;
    }

    private static InfusionPreviewInfo computeInfusionRecipe(InfusionRecipe recipe, ItemStack recipeInput,
        int matrixSymmetry) {
        InfusionPreviewInfo info = new InfusionPreviewInfo();
        Object output = recipe.getRecipeOutput(recipeInput);
        if (output instanceof Object[]obj && obj.length >= 2 && obj[0] instanceof String && obj[1] instanceof NBTBase) {
            info.recipeOutput = recipeInput.copy();
            info.recipeOutput.setTagInfo((String) obj[0], ((NBTBase) obj[1]).copy());
        } else if (output instanceof ItemStack) {
            info.recipeOutput = (ItemStack) output;
        } else {
            return info;
        }

        int recipeInstability = recipe.getInstability(recipeInput);
        info.recipeEssentia = recipe.getAspects(recipeInput)
            .copy();
        info.instability = matrixSymmetry + recipeInstability;
        return info;
    }

    private static InfusionPreviewInfo computeEnchantmentRecipe(InfusionEnchantmentRecipe enchantmentRecipe,
        ItemStack recipeInput, int matrixSymmetry) {
        InfusionPreviewInfo info = new InfusionPreviewInfo();
        Enchantment enchantment = enchantmentRecipe.getEnchantment();

        info.recipeOutput = new ItemStack(Items.enchanted_book, 1);

        AspectList esscost = enchantmentRecipe.aspects.copy();
        float essmod = enchantmentRecipe.getEssentiaMod(recipeInput);

        for (Aspect as : esscost.getAspects()) {
            esscost.add(as, (int) ((float) esscost.getAmount(as) * essmod));
        }

        info.recipeEssentia = esscost;
        info.enchantmentId = enchantment.effectId;
        info.enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(enchantment.effectId, recipeInput) + 1;
        info.enchantmentXpCost = enchantmentRecipe.calcXP(recipeInput);
        info.instability = matrixSymmetry + enchantmentRecipe.calcInstability(recipeInput);
        return info;
    }

    private static InfusionPreviewInfo computeData(World world, TileInfusionMatrix matrix, EntityPlayer player) {
        if (!matrix.active) {
            return new InfusionPreviewInfo();
        }
        final int xCoord = matrix.xCoord;
        final int yCoord = matrix.yCoord;
        final int zCoord = matrix.zCoord;
        ItemStack recipeInput = null;
        TileEntity pedestal = world.getTileEntity(xCoord, yCoord - 2, zCoord);
        if (pedestal instanceof TilePedestal ped) {
            ItemStack stack = ped.getStackInSlot(0);
            if (stack != null) {
                recipeInput = stack.copy();
            }
        }

        if (recipeInput == null) {
            return new InfusionPreviewInfo();
        }
        AccessorTileInfusionMatrix matrixAccessor = (AccessorTileInfusionMatrix) matrix;
        matrixAccessor.invokeGetSurroundings();
        ArrayList<ItemStack> components = new ArrayList<>();

        for (ChunkCoordinates cc : matrixAccessor.getPedestals()) {
            TileEntity te = world.getTileEntity(cc.posX, cc.posY, cc.posZ);
            if (te instanceof TilePedestal ped) {
                if (ped.getStackInSlot(0) != null) {
                    components.add(
                        ped.getStackInSlot(0)
                            .copy());
                }
            }
        }
        if (components.isEmpty()) {
            return new InfusionPreviewInfo();
        }
        InfusionRecipe recipe = ThaumcraftCraftingManager.findMatchingInfusionRecipe(components, recipeInput, player);
        if (recipe != null) {
            return computeInfusionRecipe(recipe, recipeInput, matrix.symmetry);
        }
        InfusionEnchantmentRecipe enchantmentRecipe = ThaumcraftCraftingManager
            .findMatchingInfusionEnchantmentRecipe(components, recipeInput, player);
        if (enchantmentRecipe != null) {
            return computeEnchantmentRecipe(enchantmentRecipe, recipeInput, matrix.symmetry);
        }
        return new InfusionPreviewInfo();
    }

    private static AspectList fetchEssentiaSources(World world, TileInfusionMatrix matrix) {
        Set<IAspectSource> essentiaSources = new HashSet<>();
        for (int x = -INFUSION_RANGE; x <= INFUSION_RANGE; x++) {
            for (int y = -INFUSION_RANGE; y < INFUSION_RANGE; y++) {
                for (int z = -INFUSION_RANGE; z <= INFUSION_RANGE; z++) {
                    if (x != 0 || y != 0 || z != 0) {
                        final int posX = matrix.xCoord + x;
                        final int posY = matrix.yCoord + y;
                        final int posZ = matrix.zCoord + z;

                        TileEntity te = world.getTileEntity(posX, posY, posZ);
                        if (te instanceof IAspectSource source) {
                            if (te instanceof TileMirrorEssentia mirror) {
                                addSourcesFromMirror(mirror, essentiaSources);
                            } else {
                                essentiaSources.add(source);
                            }
                        }
                    }
                }
            }
        }

        AspectList list = new AspectList();
        for (IAspectSource te : essentiaSources) {
            list.add(te.getAspects());
        }
        return list;
    }

    private static void addSourcesFromMirror(TileMirrorEssentia mirror, Set<IAspectSource> essentiaSources) {
        if (!mirror.isLinkValidSimple()) {
            return;
        }

        World linkedWorld = DimensionManager.getWorld(mirror.linkDim);
        if (linkedWorld == null) {
            return;
        }

        int posX = mirror.linkX;
        int posY = mirror.linkY;
        int posZ = mirror.linkZ;
        if (!(linkedWorld.getTileEntity(posX, posY, posZ) instanceof TileMirrorEssentia)) {
            return;
        }
        ForgeDirection dir = mirror.linkedFacing;
        int start = 0;
        if (dir == ForgeDirection.UNKNOWN) {
            start = -MIRROR_RANGE;
            dir = ForgeDirection.UP;
        }
        for (int aa = -MIRROR_RANGE; aa <= MIRROR_RANGE; aa++) {
            for (int bb = -MIRROR_RANGE; bb <= MIRROR_RANGE; bb++) {
                for (int cc = start; cc < MIRROR_RANGE; cc++) {
                    int x = posX;
                    int y = posY;
                    int z = posZ;
                    if (dir.offsetX != 0) {
                        x = x + cc * dir.offsetX;
                        y = y + aa;
                        z = z + bb;
                    } else if (dir.offsetY != 0) {
                        x = x + aa;
                        y = y + cc * dir.offsetY;
                        z = z + bb;
                    } else if (dir.offsetZ != 0) {
                        x = x + aa;
                        y = y + bb;
                        z = z + cc * dir.offsetZ;
                    }
                    TileEntity te = linkedWorld.getTileEntity(x, y, z);
                    if (te instanceof IAspectSource source && !(te instanceof TileMirrorEssentia)) {
                        essentiaSources.add(source);
                    }
                }
            }
        }
    }
}
