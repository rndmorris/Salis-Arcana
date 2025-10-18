package dev.rndmorris.salisarcana.client.infusion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectSource;
import thaumcraft.api.crafting.IInfusionStabiliser;
import thaumcraft.api.crafting.InfusionEnchantmentRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.common.items.wands.WandManager;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.tiles.TileInfusionMatrix;
import thaumcraft.common.tiles.TileMirrorEssentia;
import thaumcraft.common.tiles.TilePedestal;

public class InfusionData {

    public TileInfusionMatrix matrix;
    public World world;
    public boolean unformed;

    private ItemStack recipeOutput;
    private AspectList recipeEssentia;
    private final List<ChunkCoordinates> pedestals = new ArrayList<>();
    private String recipeOutputString;

    // positive values -> more instability
    private float itemSymmetry;
    private float structureSymmetry;
    private int recipeInstability;
    private float instability;

    public int ticks;

    private final Set<IAspectSource> essentiaSources = new HashSet<>();

    private static final int INFUSION_RANGE = 12;
    private static final int MIRROR_RANGE = 8;

    public InfusionData() {

    }

    public void update(World world, TileInfusionMatrix matrix) {
        if (world != this.world || matrix != this.matrix) {
            this.world = world;
            this.matrix = matrix;
            computeData();
        }
    }

    public AspectList getEssentia() {
        return recipeEssentia;
    }

    private static boolean isUnformed(World world, int xCoord, int yCoord, int zCoord) {
        return WandManager.fitInfusionAltar(world, xCoord - 1, yCoord - 2, zCoord - 1);
    }

    private void computeData() {
        if (!matrix.active) {
            unformed = isUnformed(world, matrix.xCoord, matrix.yCoord, matrix.zCoord);
            return;
        }
        unformed = false;
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
                EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
                InfusionRecipe recipe = ThaumcraftCraftingManager
                    .findMatchingInfusionRecipe(components, recipeInput, Minecraft.getMinecraft().thePlayer);
                if (recipe != null) {

                    if (recipe.getRecipeOutput(recipeInput) instanceof Object[]) {
                        Object[] obj = (Object[]) (recipe.getRecipeOutput(recipeInput));
                        recipeOutput = (ItemStack) obj[1];
                    } else {
                        recipeOutput = (ItemStack) recipe.getRecipeOutput(recipeInput);
                    }

                    recipeInstability = recipe.getInstability(recipeInput);
                    recipeEssentia = recipe.getAspects(recipeInput)
                        .copy();
                    instability = itemSymmetry + structureSymmetry + recipeInstability;
                    EnumChatFormatting color = recipeOutput.getRarity().rarityColor;
                    this.recipeOutputString = color.toString() + recipeOutput.stackSize
                        + "x "
                        + recipeOutput.getDisplayName();
                } else {
                    InfusionEnchantmentRecipe enchantmentRecipe = ThaumcraftCraftingManager
                        .findMatchingInfusionEnchantmentRecipe(components, recipeInput, player);
                    if (enchantmentRecipe != null) {
                        Enchantment enchantment = enchantmentRecipe.getEnchantment();

                        this.recipeOutput = new ItemStack(Items.enchanted_book, 1);

                        this.recipeInstability = enchantmentRecipe.calcInstability(recipeInput);
                        AspectList esscost = enchantmentRecipe.aspects.copy();
                        float essmod = enchantmentRecipe.getEssentiaMod(recipeInput);

                        for (Aspect as : esscost.getAspects()) {
                            esscost.add(as, (int) ((float) esscost.getAmount(as) * essmod));
                        }

                        this.recipeEssentia = esscost;
                        int levels = enchantmentRecipe.calcXP(recipeInput);
                        int enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(enchantment.effectId, recipeInput)
                            + 1;
                        this.recipeOutputString = EnumChatFormatting.YELLOW
                            + enchantment.getTranslatedName(enchantmentLevel)
                            + " ("
                            + StatCollector
                                .translateToLocalFormatted("salisarcana:infusion.preview.enchantment", levels)
                            + ')';
                        instability = itemSymmetry + structureSymmetry + recipeInstability;
                    }
                }
            }
        }
    }

    public ItemStack getOutputStack() {
        return recipeOutput;
    }

    public String getOutputString() {
        return this.recipeOutputString;
    }

    private void getRecipe(int xCoord, int yCoord, int zCoord) {
        pedestals.clear();
        List<ChunkCoordinates> stabilizers = new ArrayList<>();
        for (int xx = -INFUSION_RANGE; xx <= INFUSION_RANGE; xx++) {
            for (int zz = -INFUSION_RANGE; zz <= INFUSION_RANGE; zz++) {
                boolean skip = false;

                for (int yy = -5; yy <= 10; yy++) {
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

    public void fetchEssentiaSources() {
        essentiaSources.clear();
        for (int x = -INFUSION_RANGE; x <= INFUSION_RANGE; x++) {
            for (int y = -INFUSION_RANGE; y <= INFUSION_RANGE; y++) {
                for (int z = -INFUSION_RANGE; z < INFUSION_RANGE; z++) {
                    if (x != 0 || y != 0 || z != 0) {
                        final int posX = matrix.xCoord + x;
                        final int posY = matrix.yCoord + y;
                        final int posZ = matrix.zCoord + z;

                        TileEntity te = world.getTileEntity(posX, posY, posZ);
                        if (te instanceof IAspectSource source) {
                            if (te instanceof TileMirrorEssentia mirror) {
                                getSourcesFromMirror(mirror);
                            } else {
                                essentiaSources.add(source);
                            }
                        }
                    }
                }
            }
        }
    }

    private void getSourcesFromMirror(TileMirrorEssentia mirror) {
        int posX = mirror.linkX;
        int posY = mirror.linkY;
        int posZ = mirror.linkZ;
        ForgeDirection dir = mirror.linkedFacing;
        for (int aa = -MIRROR_RANGE; aa <= MIRROR_RANGE; aa++) {
            for (int bb = -MIRROR_RANGE; bb <= MIRROR_RANGE; bb++) {
                for (int cc = -MIRROR_RANGE; cc < MIRROR_RANGE; cc++) {
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
                    TileEntity te = world.getTileEntity(x, y, z);
                    if (te instanceof IAspectSource source && !(te instanceof TileMirrorEssentia)) {
                        essentiaSources.add(source);
                    }
                }
            }
        }
    }

    public AspectList buildAspectList() {
        AspectList list = new AspectList();
        for (IAspectSource te : essentiaSources) {
            list.add(te.getAspects());
        }
        return list;
    }

    public void invalidate() {
        world = null;
        matrix = null;
        essentiaSources.clear();
    }

    public void onTick() {
        if (++ticks >= 4) {
            computeData();
            fetchEssentiaSources();
            ticks = 0;
        }
    }

    public static boolean hasEnoughEssentia(AspectList source, Aspect required, int amount) {
        return source.getAmount(required) >= amount;
    }
}
