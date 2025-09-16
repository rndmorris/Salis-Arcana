package dev.rndmorris.salisarcana.client.handlers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectSource;
import thaumcraft.api.crafting.IInfusionStabiliser;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.tiles.TileInfusionMatrix;
import thaumcraft.common.tiles.TileMirrorEssentia;
import thaumcraft.common.tiles.TilePedestal;

public class InfusionData {

    private int recipeType;
    private String recipeOutputLabel;
    private Object recipeOutput;
    private int recipeInstability;
    private AspectList recipeEssentia;
    private float instability;
    private final List<ChunkCoordinates> pedestals = new ArrayList<>();
    private final TileInfusionMatrix matrix;

    // positive values -> more instability
    private float itemSymmetry;
    private float structureSymmetry;
    private final Set<TileEntity> essentiaSources = new HashSet<>();

    private final World world;

    public InfusionData(World world, TileInfusionMatrix matrix) {
        this.world = world;
        this.matrix = matrix;
    }

    public AspectList getEssentia() {
        return recipeEssentia;
    }

    public InfusionData compute() {
        if (matrix.validLocation()) {
            int xCoord = matrix.xCoord;
            int yCoord = matrix.yCoord;
            int zCoord = matrix.zCoord;
            ItemStack recipeInput = null;
            TileEntity pedestal = world.getTileEntity(xCoord, yCoord - 2, zCoord);
            if (pedestal instanceof TilePedestal) {
                TilePedestal ped = (TilePedestal) pedestal;
                ItemStack stack = ped.getStackInSlot(0);
                if (stack != null) {
                    recipeInput = stack.copy();
                }
            }

            if (recipeInput != null) {
                getRecipe(xCoord, yCoord, zCoord);
                ArrayList<ItemStack> components = new ArrayList<>();

                for (ChunkCoordinates cc : this.pedestals) {
                    TileEntity te = world.getTileEntity(cc.posX, cc.posY, cc.posZ);
                    if (te instanceof TilePedestal ped) {
                        if (ped.getStackInSlot(0) != null) {
                            components.add(
                                ped.getStackInSlot(0)
                                    .copy());
                        }
                    }
                }
                if (!components.isEmpty()) {
                    InfusionRecipe recipe = ThaumcraftCraftingManager
                        .findMatchingInfusionRecipe(components, recipeInput, Minecraft.getMinecraft().thePlayer);
                    if (recipe != null) {
                        recipeType = 0;

                        if (recipe.getRecipeOutput(recipeInput) instanceof Object[]) {
                            Object[] obj = (Object[]) (recipe.getRecipeOutput(recipeInput));
                            recipeOutputLabel = (String) obj[0];
                            recipeOutput = obj[1];
                        } else {
                            recipeOutput = recipe.getRecipeOutput(recipeInput);
                        }

                        recipeInstability = recipe.getInstability(recipeInput);
                        recipeEssentia = recipe.getAspects(recipeInput)
                            .copy();
                        instability = itemSymmetry + structureSymmetry + recipeInstability;
                    } // TODO enchantments
                }
            }
        }
        return this;
    }

    public ItemStack getOutputStack() {
        if (recipeOutput instanceof ItemStack stack) {
            return stack;
        }
        return null;
    }

    public String getOutputString(ItemStack outputStack) {
        EnumChatFormatting color = outputStack.getRarity().rarityColor;
        return color.toString() + outputStack.stackSize + "x " + outputStack.getDisplayName();
    }

    private void getRecipe(int xCoord, int yCoord, int zCoord) {
        List<ChunkCoordinates> stabilizers = new ArrayList<>();
        for (int xx = -12; xx <= 12; ++xx) {
            for (int zz = -12; zz <= 12; ++zz) {
                boolean skip = false;

                for (int yy = -5; yy <= 10; ++yy) {
                    if (xx != 0 || zz != 0) {
                        int x = xCoord + xx;
                        int y = yCoord - yy;
                        int z = zCoord + zz;
                        TileEntity te = world.getTileEntity(x, y, z);
                        if (!skip && yy > 0 && Math.abs(xx) <= 8 && Math.abs(zz) <= 8 && te instanceof TilePedestal) {
                            pedestals.add(new ChunkCoordinates(x, y, z));
                            skip = true;
                        } else {
                            Block bi = world.getBlock(x, y, z);
                            if (bi == Blocks.skull || bi instanceof IInfusionStabiliser
                                && ((IInfusionStabiliser) bi).canStabaliseInfusion(world, x, y, z)) {
                                stabilizers.add(new ChunkCoordinates(x, y, z));
                            }
                        }
                    }
                }
            }
        }

        for (ChunkCoordinates cc : pedestals) {
            boolean items = false;
            int x = xCoord - cc.posX;
            int z = zCoord - cc.posZ;
            TileEntity te = world.getTileEntity(cc.posX, cc.posY, cc.posZ);
            if (te instanceof TilePedestal) {
                structureSymmetry += 2;
                if (((TilePedestal) te).getStackInSlot(0) != null) {
                    itemSymmetry++;
                    items = true;
                }
            }

            int xx = xCoord + x;
            int zz = zCoord + z;
            te = world.getTileEntity(xx, cc.posY, zz);
            if (te instanceof TilePedestal) {
                structureSymmetry -= 2;
                if (((TilePedestal) te).getStackInSlot(0) != null && items) {
                    itemSymmetry--;
                }
            }
        }

        float sym = 0.0F;

        for (ChunkCoordinates cc : stabilizers) {
            structureSymmetry += 0.1F;

            int x = xCoord - cc.posX;
            int z = zCoord - cc.posZ;
            int xx = xCoord + x;
            int zz = zCoord + z;
            Block bi = world.getBlock(xx, cc.posY, zz);
            if (bi == Blocks.skull || bi instanceof IInfusionStabiliser
                && ((IInfusionStabiliser) bi).canStabaliseInfusion(world, xx, cc.posY, zz)) {
                structureSymmetry -= 0.2F;
            }
        }
    }

    private static final int INFUSION_RANGE = 12;
    private static final int MIRROR_RANGE = 8;

    public void fetchEssentiaSources() {
        essentiaSources.clear();
        for (int aa = -INFUSION_RANGE; aa <= INFUSION_RANGE; aa++) {
            for (int bb = -INFUSION_RANGE; bb <= INFUSION_RANGE; bb++) {
                for (int cc = -INFUSION_RANGE; cc < INFUSION_RANGE; cc++) {
                    if (aa != 0 || bb != 0 || cc != 0) {
                        final int xx = matrix.xCoord + aa;
                        final int yy = matrix.yCoord + bb;
                        final int zz = matrix.zCoord + cc;

                        TileEntity te = world.getTileEntity(xx, yy, zz);
                        if (te instanceof IAspectSource) {
                            if (te instanceof TileMirrorEssentia mirror) {
                                getSourcesFromMirror(mirror);
                            } else {
                                essentiaSources.add(te);
                            }
                        }
                    }
                }
            }
        }
    }

    // TODO add compat for mirrors (scan the area of the link for all essentia sources and exclude any mirrors)
    private void getSourcesFromMirror(TileMirrorEssentia mirror) {

    }

    public AspectList buildAspectList() {
        AspectList list = new AspectList();
        for (TileEntity te : essentiaSources) {
            IAspectSource source = (IAspectSource) te;
            list.add(source.getAspects());
        }
        return list;
    }

    public static boolean hasEnoughEssentia(AspectList source, Aspect required, int amount) {
        return source.getAmount(required) >= amount;
    }
}
