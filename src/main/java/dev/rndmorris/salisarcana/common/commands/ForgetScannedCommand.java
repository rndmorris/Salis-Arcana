package dev.rndmorris.salisarcana.common.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MovingObjectPosition;

import dev.rndmorris.salisarcana.common.commands.arguments.ArgumentProcessor;
import dev.rndmorris.salisarcana.common.commands.arguments.annotations.FlagArg;
import dev.rndmorris.salisarcana.common.commands.arguments.annotations.NamedArg;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.flag.FlagHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.named.AspectHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.named.PlayerHandler;
import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.lib.InventoryHelper;
import dev.rndmorris.salisarcana.network.MessageForgetScannedCategory;
import dev.rndmorris.salisarcana.network.MessageForgetScannedObjects;
import dev.rndmorris.salisarcana.network.NetworkHandler;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.research.ScanManager;
import thaumcraft.common.lib.utils.BlockUtils;

public class ForgetScannedCommand extends ArcanaCommandBase<ForgetScannedCommand.Arguments> {

    public ForgetScannedCommand() {
        super(SalisConfig.commands.forgetScanned);
    }

    @Override
    protected @Nonnull ArgumentProcessor<Arguments> initializeProcessor() {
        return new ArgumentProcessor<>(
            Arguments.class,
            Arguments::new,
            new IArgumentHandler[] { PlayerHandler.INSTANCE, FlagHandler.INSTANCE, AspectHandler.INSTANCE, });
    }

    @Override
    protected void process(ICommandSender sender, Arguments arguments, String[] args) {
        final var targetPlayer = arguments.targetPlayer != null ? arguments.targetPlayer
            : getCommandSenderAsPlayer(sender);

        final var playerName = targetPlayer.getCommandSenderName();
        final var playerKnowledge = Thaumcraft.proxy.getPlayerKnowledge();
        // aggregate all forgotten item hashes to send to affected client
        final var forgottenHashes = new HashSet<Integer>();

        if (arguments.hand) {
            forgottenHashes.addAll(forgetItems(targetPlayer, Collections.singletonList(targetPlayer.getHeldItem())));
        }
        if (arguments.inventory) {
            forgottenHashes.addAll(forgetItems(targetPlayer, InventoryHelper.getItemStacks(targetPlayer.inventory)));
        }
        if (arguments.looking || arguments.container) {
            MovingObjectPosition target = getLookingAt(targetPlayer);
            if (arguments.looking) {
                Block block = targetPlayer.worldObj.getBlock(target.blockX, target.blockY, target.blockZ);
                if (block != null) {
                    final var item = new ItemStack(
                        block,
                        1,
                        targetPlayer.worldObj.getBlockMetadata(target.blockX, target.blockY, target.blockZ));
                    forgottenHashes.addAll(forgetItems(targetPlayer, Collections.singletonList(item)));
                }
            }
            if (arguments.container) {
                TileEntity tile = targetPlayer.worldObj.getTileEntity(target.blockX, target.blockY, target.blockZ);
                if (tile instanceof IInventory inventory) {
                    forgottenHashes.addAll(forgetItems(targetPlayer, InventoryHelper.getItemStacks(inventory)));
                }
            }
        }

        if (!forgottenHashes.isEmpty()) {
            NetworkHandler.instance.sendTo(new MessageForgetScannedObjects(forgottenHashes), targetPlayer);
        }

        final var removeFromMaps = new ArrayList<Map<String, ArrayList<String>>>(3);
        final var forgetObjects = arguments.all || arguments.objects;
        if (forgetObjects) {
            removeFromMaps.add(playerKnowledge.objectsScanned);
        }
        final var forgetEntities = arguments.all || arguments.entities;
        if (forgetEntities) {
            removeFromMaps.add(playerKnowledge.entitiesScanned);
        }
        final var forgetNodes = arguments.all || arguments.nodes;
        if (forgetNodes) {
            removeFromMaps.add(playerKnowledge.phenomenaScanned);
        }

        int removedCount = forgottenHashes.size();
        if (!removeFromMaps.isEmpty()) {
            removedCount += forgetCategories(playerName, removeFromMaps);
            NetworkHandler.instance
                .sendTo(new MessageForgetScannedCategory(forgetObjects, forgetEntities, forgetNodes), targetPlayer);
        }

        if (removedCount > 0) {
            sender.addChatMessage(
                new ChatComponentTranslation("salisarcana:command.forget-scanned.success", removedCount, playerName));
        } else {
            sender
                .addChatMessage(new ChatComponentTranslation("salisarcana:command.forget-scanned.failure", playerName));
        }
    }

    private int forgetCategories(String playerName, List<Map<String, ArrayList<String>>> toClear) {
        var count = 0;
        for (var map : toClear) {
            if (map == null) {
                continue;
            }
            final var list = map.get(playerName);
            if (list == null) {
                continue;
            }
            count += list.size();
            list.clear();
        }
        return count;
    }

    private Set<Integer> forgetItems(EntityPlayerMP targetPlayer, Collection<ItemStack> items) {
        if (items.isEmpty()) {
            return Collections.emptySet();
        }
        final var playerKnowledge = Thaumcraft.proxy.getPlayerKnowledge();
        final var playerName = targetPlayer.getCommandSenderName();
        final var objectsScanned = playerKnowledge.objectsScanned.get(playerName);

        if (objectsScanned == null || objectsScanned.isEmpty()) {
            return Collections.emptySet();
        }

        final var forgottenHashes = new HashSet<Integer>(items.size());
        for (var item : items) {
            if (item == null || item.getItem() == null) {
                continue;
            }
            final var hash = ScanManager.generateItemHash(item.getItem(), item.getItemDamage());
            final var key = "@" + hash;
            if (objectsScanned.remove(key)) {
                forgottenHashes.add(hash);
            }
        }

        return forgottenHashes;
    }

    private MovingObjectPosition getLookingAt(EntityPlayer player) {
        return BlockUtils.getTargetBlock(player.worldObj, player, false);
    }

    @Override
    protected int minimumRequiredArgs() {
        return 1;
    }

    public static class Arguments {

        @NamedArg(name = "--player", handler = PlayerHandler.class, descLangKey = "player")
        public EntityPlayerMP targetPlayer;

        @FlagArg(name = "--objects", excludes = "--all", descLangKey = "objects")
        public boolean objects;

        @FlagArg(name = "--entities", excludes = "--all", descLangKey = "entities")
        public boolean entities;

        @FlagArg(name = "--nodes", excludes = "--all", descLangKey = "nodes")
        public boolean nodes;

        @FlagArg(name = "--all", excludes = { "--objects", "--entities", "--nodes" }, descLangKey = "all")
        public boolean all;

        @FlagArg(name = "--hand", excludes = "--all", descLangKey = "hand")
        public boolean hand;

        @FlagArg(name = "--inventory", excludes = "--all", descLangKey = "research")
        public boolean inventory;

        @FlagArg(name = "--looking", excludes = "--all", descLangKey = "looking")
        public boolean looking;

        @FlagArg(name = "--container", excludes = "--all", descLangKey = "container")
        public boolean container;

    }
}
