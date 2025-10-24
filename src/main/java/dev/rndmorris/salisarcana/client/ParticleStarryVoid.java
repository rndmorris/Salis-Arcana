package dev.rndmorris.salisarcana.client;

import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import thaumcraft.client.fx.particles.FXGeneric;

public class ParticleStarryVoid extends FXGeneric {

    private final double sourceX;
    private final double sourceY;
    private final double sourceZ;

    public ParticleStarryVoid(World world, double x, double y, double z, double velX, double velY, double velZ) {
        super(world, x, y, z, velX, velY, velZ);

        particleGravity = 0.3f;
        particleTextureJitterX = 0.0f;
        particleTextureJitterY = 0.0f;
        setScale(5);

        // Initial random motion
        motionX = rand.nextGaussian() * 0.01;
        motionY = rand.nextGaussian() * 0.01;
        motionZ = rand.nextGaussian() * 0.01;

        double length = Math.sqrt(velX * velX + velY * velY + velZ * velZ);
        double factor = rand.nextFloat() + 0.5; // 0.5 - 1.5
        particleMaxAge = (int) (Math.max(1, length) * 4 * factor);

        sourceX = x + velX;
        sourceY = y + velY;
        sourceZ = z + velZ;
    }

    @Override
    public void onUpdate() {
        if (particleAge++ >= particleMaxAge) {
            setDead();
        }

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        moveEntity(motionX, motionY, motionZ);

        motionX *= 0.9;
        motionY *= 0.9;
        motionZ *= 0.9;

        double distanceX = sourceX - posX;
        double distanceY = sourceY - posY;
        double distanceZ = sourceZ - posZ;

        double length = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);

        if (length < 12) {
            particleScale *= 0.9f;
        }

        if (length < 1.5) {
            this.setDead();
        }

        motionX += distanceX / length;
        motionY += distanceY / length;
        motionZ += distanceZ / length;

        motionX = MathHelper.clamp_double(motionX, -0.4, 0.4);
        motionY = MathHelper.clamp_double(motionY, -0.4, 0.4);
        motionZ = MathHelper.clamp_double(motionZ, -0.4, 0.4);
    }
}
