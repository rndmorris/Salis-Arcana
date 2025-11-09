package dev.rndmorris.salisarcana.common.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dev.rndmorris.salisarcana.SalisArcana;
import dev.rndmorris.salisarcana.config.SalisConfig;
import thaumcraft.common.entities.monster.EntityWisp;

public class EntityEventHandlers {

    public static void preInit() {
        if (SalisConfig.features.netherPortalWispSpawns.isEnabled()) {
            MinecraftForge.EVENT_BUS.register(new EntityEventHandlers());
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.world.isRemote || !(event.entity instanceof EntityPigZombie pigZombie)) {
            return;
        }
        var properties = pigZombie.getExtendedProperties(PigZombieProperties.KEY) instanceof PigZombieProperties _p ? _p
            : null;
        if (properties == null) {
            pigZombie.registerExtendedProperties(PigZombieProperties.KEY, properties = new PigZombieProperties());
        }
        if (properties.checkedWispReplacement) {
            return;
        }
        if (event.world.provider.dimensionId != 0) {
            // we only care about pigzombies that spawn in the overworld
            properties.checkedWispReplacement = true;
            return;
        }

        final var inBlock = event.world.getBlock(
            MathHelper.floor_double(event.entity.posX),
            MathHelper.floor_double(event.entity.posY),
            MathHelper.floor_double(event.entity.posZ));
        if (inBlock != Blocks.portal) {
            properties.checkedWispReplacement = true;
            return;
        }

        if (SalisConfig.features.netherPortalWispSpawns.roll(event.world.rand)) {
            final var newWisp = new EntityWisp(event.world);
            newWisp.setPosition(pigZombie.posX, pigZombie.posY, pigZombie.posZ);
            // prevent the wisp from being immediately sucked into the nether
            newWisp.timeUntilPortal = newWisp.getPortalCooldown();
            event.world.spawnEntityInWorld(newWisp);
            // move the pigzombie out of sight and kill it
            pigZombie.setPosition(pigZombie.posX, -50, pigZombie.posZ);
            pigZombie.setDead();
        }
    }

    public static class PigZombieProperties implements IExtendedEntityProperties {

        public static final String KEY = SalisArcana.MODID + ":" + EntityPigZombie.class.getName();

        /**
         * If we check a zombie pigman and determine they won't be replaced with a wisp, we flag them so we never check
         * them again.
         */
        public boolean checkedWispReplacement = false;

        @Override
        public void saveNBTData(NBTTagCompound compound) {
            compound.setBoolean("noWisp", checkedWispReplacement);
        }

        @Override
        public void loadNBTData(NBTTagCompound compound) {
            checkedWispReplacement = compound.getBoolean("noWisp");
        }

        @Override
        public void init(Entity entity, World world) {
            // we can't find the information we need (the block(s) the entity is currently in) at this point in the
            // entity's lifecycle
        }
    }
}
