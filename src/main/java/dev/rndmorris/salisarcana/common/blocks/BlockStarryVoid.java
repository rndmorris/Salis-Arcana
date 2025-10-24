package dev.rndmorris.salisarcana.common.blocks;

import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dev.rndmorris.salisarcana.SalisArcana;
import dev.rndmorris.salisarcana.client.ParticleStarryVoid;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.common.tiles.TileEldritchNothing;

public class BlockStarryVoid extends Block {

    private static class MaterialStarryVoid extends Material {

        public MaterialStarryVoid(MapColor par1MapColor) {
            super(par1MapColor);
        }

        @Override
        public boolean isSolid() {
            return false;
        }

        @Override
        public boolean blocksMovement() {
            return false;
        }
    }

    static class MultiBlockHandler {

        private final Map<Entity, Double> lastMovementX = new WeakHashMap<>();
        private final Map<Entity, Double> lastMovementZ = new WeakHashMap<>();
        private final Map<Entity, Boolean> lastDamage = new WeakHashMap<>();
        private final Map<Entity, Long> lastTick = new WeakHashMap<>();

        void resetIfNeeded(Entity entity) {
            if (lastTick.getOrDefault(entity, -1L) != entity.worldObj.getTotalWorldTime()) {
                lastTick.put(entity, entity.worldObj.getTotalWorldTime());
                lastDamage.remove(entity);
                lastMovementX.remove(entity);
                lastMovementZ.remove(entity);
            }
        }

        public void damage(EntityPlayer player, int meta) {
            resetIfNeeded(player);
            if (!lastDamage.containsKey(player)) {
                player.attackEntityFrom(DamageSource.outOfWorld, (meta == 0) ? 10.0F : 1.0F);
                lastDamage.put(player, true);
            }
        }

        private double getMovement(Map<Entity, Double> map, Entity entity, double v) {
            double last = map.getOrDefault(entity, 0.0);
            if ((last >= 0 && v > last) || (last <= 0 && v < last)) {
                map.put(entity, v);
                return v - last;
            }
            return 0;
        }

        public void move(Entity entity, double x, double z) {
            resetIfNeeded(entity);
            entity.motionX += getMovement(lastMovementX, entity, x);
            entity.motionZ += getMovement(lastMovementZ, entity, z);
        }
    }

    private static final MultiBlockHandler handlerClient = new MultiBlockHandler();
    private static final MultiBlockHandler handlerServer = new MultiBlockHandler();

    private MultiBlockHandler getMultiBlockHandler(World world) {
        return world.isRemote ? handlerClient : handlerServer;
    }

    public static final int range = 12;

    private static final Material material = new MaterialStarryVoid(MapColor.airColor);

    static boolean disableUpdates;

    public BlockStarryVoid() {
        super(material);
        setBlockUnbreakable();
        setLightOpacity(255);
        setBlockName("blockStarryVoid");
        setBlockTextureName(SalisArcana.MODID + ":starry_void");
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1; // No standard render type
    }

    @Override
    public boolean isAir(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        return meta > 0;
    }

    @Override
    public boolean isReplaceable(IBlockAccess world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z) > 0;
    }

    private void handleUpdate(World world, int x, int y, int z) {
        if (world.isRemote || disableUpdates) {
            return;
        }

        int meta = world.getBlockMetadata(x, y, z);

        // Delete if needed
        if (meta > 0) {
            boolean foundSource = false;
            for (EnumFacing face : EnumFacing.values()) {
                int otherX = x + face.getFrontOffsetX();
                int otherY = y + face.getFrontOffsetY();
                int otherZ = z + face.getFrontOffsetZ();
                Block blockOther = world.getBlock(otherX, otherY, otherZ);
                int metaOther = world.getBlockMetadata(otherX, otherY, otherZ);

                if (blockOther == this && metaOther < meta) {
                    foundSource = true;
                    break;
                }
            }
            if (!foundSource) {
                world.setBlockToAir(x, y, z);
                return;
            }
        }

        // Spread if needed
        if (meta < range) {
            for (EnumFacing face : EnumFacing.values()) {
                int otherX = x + face.getFrontOffsetX();
                int otherY = y + face.getFrontOffsetY();
                int otherZ = z + face.getFrontOffsetZ();
                Block blockOther = world.getBlock(otherX, otherY, otherZ);
                int metaOther = world.getBlockMetadata(otherX, otherY, otherZ);

                if (blockOther.isAir(world, otherX, otherY, otherZ)) {
                    if (blockOther != this || metaOther > meta + 1) {
                        disableUpdates = true;
                        world.setBlock(otherX, otherY, otherZ, this, meta + 1, 3);
                        disableUpdates = false;
                        world.scheduleBlockUpdate(otherX, otherY, otherZ, this, 3);
                    }
                }
            }

        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
        handleUpdate(world, x, y, z);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        handleUpdate(world, x, y, z);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        handleUpdate(world, x, y, z);
    }

    private void pullEntity(World world, int x, int y, int z, Entity entity) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta > 0 && (entity.canBePushed() || entity instanceof EntityItem)) {
            for (EnumFacing face : EnumFacing.values()) {
                int otherX = x + face.getFrontOffsetX();
                int otherY = y + face.getFrontOffsetY();
                int otherZ = z + face.getFrontOffsetZ();
                Block blockOther = world.getBlock(otherX, otherY, otherZ);
                int metaOther = world.getBlockMetadata(otherX, otherY, otherZ);
                if (blockOther == this && metaOther < meta) {
                    double moveX = face.getFrontOffsetX() / 300.0f * (range - meta);
                    double moveZ = face.getFrontOffsetZ() / 300.0f * (range - meta);
                    getMultiBlockHandler(world).move(entity, moveX, moveZ);
                }
            }
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (entity instanceof EntityPlayer player) {
            if (player.isEntityInvulnerable() || player.capabilities.isCreativeMode) {
                return;
            }

            if (!world.isRemote) {
                int meta = world.getBlockMetadata(x, y, z);
                if (meta == 0 || world.rand.nextInt(1000) < range - meta) {
                    getMultiBlockHandler(world).damage(player, meta);
                }
            }
            pullEntity(world, x, y, z, entity);
        } else {
            pullEntity(world, x, y, z, entity);
        }
    }

    private static final BlockPos displayPos = new BlockPos();

    private void getOriginalPos(BlockPos pos, int x, int y, int z, World world, int meta) {
        if (meta == 0) {
            pos.set(x, y, z);
            return;
        }
        for (EnumFacing face : EnumFacing.values()) {
            int otherX = x + face.getFrontOffsetX();
            int otherY = y + face.getFrontOffsetY();
            int otherZ = z + face.getFrontOffsetZ();
            int metaOther = world.getBlockMetadata(otherX, otherY, otherZ);
            Block blockOther = world.getBlock(otherX, otherY, otherZ);
            if (blockOther == this && metaOther < meta) {
                getOriginalPos(pos, otherX, otherY, otherZ, world, metaOther);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == 0 || random.nextInt(500) > meta) {
            return;
        }
        displayPos.set(x, y, z);
        getOriginalPos(displayPos, x, y, z, world, meta);

        ParticleStarryVoid particle = new ParticleStarryVoid(
            world,
            x + random.nextFloat(),
            y + random.nextFloat(),
            z + random.nextFloat(),
            displayPos.x - x + 0.5,
            displayPos.y - y + 0.5,
            displayPos.z - z + 0.5);

        particle.setAlphaF(0.45f);
        particle.setParticles(48, 14, 1);
        particle.setLoop(true);
        ParticleEngine.instance.addEffect(world, particle);
        if (random.nextInt(40) == 0) {
            world.playSound(x, y, z, "thaumcraft:wind", 0.33F, 1.0F, true);
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == 0) {
            return super.getCollisionBoundingBoxFromPool(world, x, y, z);
        }
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == 0) {
            return super.collisionRayTrace(world, x, y, z, start, end);
        }
        return null;
    }

    public boolean hasTileEntity(int meta) {
        return meta == 0;
    }

    public TileEntity createTileEntity(World world, int meta) {
        return meta == 0 ? new TileEldritchNothing() : null;
    }
}
