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

        particleGravity = 0.2f;
        particleTextureJitterX = 0.0F;
        particleTextureJitterY = 0.0F;
        setScale(3);

        // Initial random motion
        motionX = rand.nextGaussian() * 0.01;
        motionY = rand.nextGaussian() * 0.01;
        motionZ = rand.nextGaussian() * 0.01;

        double length = Math.sqrt(velX * velX + velY * velY + velZ * velZ);
        double factor = rand.nextGaussian() + 0.5; // 0.5 - 1.5
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

        motionX *= 0.98;
        motionY *= 0.98;
        motionZ *= 0.98;

        double velX = sourceX - posX;
        double velY = sourceY - posY;
        double velZ = sourceZ - posZ;

        double length = Math.sqrt(velX * velX + velY * velY + velZ * velZ);

        double strength = 0.3;
        if (length < 5) {
            strength += (5 - length) / 5 * 0.3;
            particleScale *= 0.9F;
        }

        motionX += velX / length * strength;
        motionY += velY / length * strength;
        motionZ += velZ / length * strength;

        motionX = MathHelper.clamp_double(motionX, -0.4, 0.4);
        motionY = MathHelper.clamp_double(motionY, -0.4, 0.4);
        motionZ = MathHelper.clamp_double(motionZ, -0.4, 0.4);
    }
}
