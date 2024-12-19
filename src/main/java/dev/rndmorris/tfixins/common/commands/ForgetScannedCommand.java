package dev.rndmorris.tfixins.common.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;

import dev.rndmorris.tfixins.common.commands.arguments.ArgumentProcessor;
import dev.rndmorris.tfixins.common.commands.arguments.annotations.FlagArg;
import dev.rndmorris.tfixins.common.commands.arguments.annotations.NamedArg;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.flag.FlagHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.named.PlayerHandler;
import dev.rndmorris.tfixins.config.ConfigModuleRoot;
import thaumcraft.common.Thaumcraft;

public class ForgetScannedCommand extends FixinsCommandBase<ForgetScannedCommand.Arguments> {

    public ForgetScannedCommand() {
        super(ConfigModuleRoot.commandsModule.forgetScanned);
    }

    @Override
    protected @Nonnull ArgumentProcessor<Arguments> initializeProcessor() {
        return new ArgumentProcessor<>(
            Arguments.class,
            Arguments::new,
            new IArgumentHandler[] { PlayerHandler.INSTANCE, FlagHandler.INSTANCE, });
    }

    @Override
    protected void process(ICommandSender sender, String[] args) {
        final var arguments = argumentProcessor.process(sender, args);

        if (arguments.targetPlayer == null) {
            arguments.targetPlayer = getCommandSenderAsPlayer(sender);
        }

        final var playerKnowledge = Thaumcraft.proxy.getPlayerKnowledge();
        final var toClear = new ArrayList<Map<String, ArrayList<String>>>(3);

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
        final var removedCount = forget(playerName, toClear);
        if (removedCount > 0) {
            sender.addChatMessage(
                new ChatComponentTranslation("tfixins:command.forget-scanned.success", removedCount, playerName));
        } else {
            sender.addChatMessage(new ChatComponentTranslation("tfixins:command.forget-scanned.failure", playerName));
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

    }
}
