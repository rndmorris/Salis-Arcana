package dev.rndmorris.salisarcana.lib;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dev.rndmorris.salisarcana.SalisArcana;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.common.lib.research.ResearchManager;

public class KnowItAll extends EntityPlayer {

    private static @Nullable KnowItAll knowItAll = null;
    private static final String USERNAME = SalisArcana.MODID + ":KnowItAll";
    public static final EventCollector EVENT_COLLECTOR = new EventCollector();

    public static String getUsername() {
        teachKnowItAll();
        return USERNAME;
    }

    public static KnowItAll getInstance() {
        if (knowItAll == null) {
            knowItAll = new KnowItAll(SalisArcana.proxy.getFakePlayerWorld());
        }

        return knowItAll;
    }

    public static class EventCollector {

        @SubscribeEvent
        public void onWorldUnload(WorldEvent.Unload event) {
            if (knowItAll != null && knowItAll.worldObj == event.world) {
                knowItAll = null;
            }
        }
    }

    private static void teachKnowItAll() {
        if (ResearchManager.getResearchForPlayerSafe(USERNAME) == null) {
            for (final var category : ResearchCategories.researchCategories.values()) {
                for (final var researchItem : category.research.values()) {
                    ResearchManager.completeResearchUnsaved(USERNAME, researchItem.key);
                }
            }
        }
    }

    public KnowItAll(World world) {
        super(world, new GameProfile(null, USERNAME));
        KnowItAll.teachKnowItAll();
    }

    @Override
    public void addChatMessage(IChatComponent message) {}

    @Override
    public boolean canCommandSenderUseCommand(int i, String s) {
        return false;
    }

    @Override
    public ChunkCoordinates getPlayerCoordinates() {
        return new ChunkCoordinates(0, 0, 0);
    }

    @Override
    public void openGui(Object mod, int modGuiId, World world, int x, int y, int z) {}

    @Override
    public boolean isEntityInvulnerable() {
        return true;
    }

    @Override
    public boolean canAttackPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void onDeath(DamageSource source) {}

    @Override
    public void onUpdate() {}

    @Override
    public void travelToDimension(int dim) {}
}
