package dev.rndmorris.salisarcana.common.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.research.ScanManager;
import thaumcraft.common.lib.utils.BlockUtils;

public class ForgetScannedCommand extends ArcanaCommandBase<ForgetScannedCommand.Arguments> {

    public ForgetScannedCommand() {
        super(ConfigModuleRoot.commands.forgetScanned);
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
        if (arguments.targetPlayer == null) {
            arguments.targetPlayer = getCommandSenderAsPlayer(sender);
        }

        final var playerKnowledge = Thaumcraft.proxy.getPlayerKnowledge();
        final var toClear = new ArrayList<Map<String, ArrayList<String>>>(3);

        int removedCount = 0;

        if (arguments.hand) {
            if (forgetSingle(sender, arguments.targetPlayer.getHeldItem())) {
                removedCount++;
            }
        }
        if (arguments.inventory) {
            final var inventory = arguments.targetPlayer.inventory;
            for (var i = 0; i < inventory.getSizeInventory(); i++) {
                if (forgetSingle(sender, inventory.getStackInSlot(i))) {
                    removedCount++;
                }
            }
        }
        if (arguments.looking) {
            MovingObjectPosition target = getLookingAt(arguments.targetPlayer);
            Block block = arguments.targetPlayer.worldObj.getBlock(target.blockX, target.blockY, target.blockZ);
            if (block != null) {
                ItemStack item = new ItemStack(
                    block,
                    1,
                    arguments.targetPlayer.worldObj.getBlockMetadata(target.blockX, target.blockY, target.blockZ));
                if (forgetSingle(sender, item)) {
                    removedCount++;
                }
            }
        }
        if (arguments.container) {
            MovingObjectPosition target = getLookingAt(arguments.targetPlayer);
            TileEntity tile = arguments.targetPlayer.worldObj
                .getTileEntity(target.blockX, target.blockY, target.blockZ);
            if (tile instanceof IInventory inventory) {
                for (var i = 0; i < inventory.getSizeInventory(); i++) {
                    if (forgetSingle(sender, inventory.getStackInSlot(i))) {
                        removedCount++;
                    }
                }
            }
        }

        if (arguments.all || arguments.objects) {
            toClear.add(playerKnowledge.objectsScanned);
        }
        if (arguments.all || arguments.entities) {
            toClear.add(playerKnowledge.entitiesScanned);
        }
        if (arguments.all || arguments.nodes) {
            toClear.add(playerKnowledge.phenomenaScanned);
        }

        final var playerName = arguments.targetPlayer.getCommandSenderName();
        if (!toClear.isEmpty()) {
            removedCount = forget(playerName, toClear);
        }
        if (removedCount > 0) {
            sender.addChatMessage(
                new ChatComponentTranslation("salisarcana:command.forget-scanned.success", removedCount, playerName));
        } else {
            sender
                .addChatMessage(new ChatComponentTranslation("salisarcana:command.forget-scanned.failure", playerName));
        }
    }

    private int forget(String playerName, List<Map<String, ArrayList<String>>> toClear) {
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

    private boolean forgetSingle(ICommandSender sender, ItemStack item) {
        if (item == null) {
            return false;
        }
        final var playerKnowledge = Thaumcraft.proxy.getPlayerKnowledge();
        final var playerName = sender.getCommandSenderName();

        final var objectsScanned = playerKnowledge.objectsScanned.get(playerName);
        String key = "@" + ScanManager.generateItemHash(item.getItem(), item.getItemDamage());
        return objectsScanned != null && objectsScanned.remove(key);
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
