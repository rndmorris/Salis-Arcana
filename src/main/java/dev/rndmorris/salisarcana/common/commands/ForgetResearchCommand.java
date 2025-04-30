package dev.rndmorris.salisarcana.common.commands;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Objects;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;

import dev.rndmorris.salisarcana.common.commands.arguments.ArgumentProcessor;
import dev.rndmorris.salisarcana.common.commands.arguments.annotations.FlagArg;
import dev.rndmorris.salisarcana.common.commands.arguments.annotations.NamedArg;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.ResearchKeyHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.flag.FlagHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.named.PlayerHandler;
import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.network.MessageForgetResearch;
import dev.rndmorris.salisarcana.network.NetworkHandler;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketSyncWarp;

public class ForgetResearchCommand extends ArcanaCommandBase<ForgetResearchCommand.Arguments> {

    public ForgetResearchCommand() {
        super(SalisConfig.commands.forgetResearch);
    }

    @Override
    protected @Nonnull ArgumentProcessor<Arguments> initializeProcessor() {
        return new ArgumentProcessor<>(
            Arguments.class,
            Arguments::new,
            new IArgumentHandler[] { PlayerHandler.INSTANCE, ResearchKeyHandler.INSTANCE, FlagHandler.INSTANCE, });
    }

    @Override
    protected void process(ICommandSender sender, Arguments arguments, String[] args) {
        if (arguments.targetPlayer == null) {
            arguments.targetPlayer = getCommandSenderAsPlayer(sender);
        }
        if (arguments.researchKeys.isEmpty() && !arguments.allResearch) {
            CommandErrors.invalidSyntax();
        }

        // required to be a reference to the original list
        final var playerResearch = Thaumcraft.proxy.getPlayerKnowledge().researchCompleted
            .get(arguments.targetPlayer.getCommandSenderName());
        if (playerResearch == null) {
            return;
        }
        final var toForget = new ArrayDeque<String>();
        if (arguments.allResearch) {
            toForget.addAll(playerResearch);
        } else {
            toForget.addAll(arguments.researchKeys);
        }

        final var researchMap = playerResearch.stream()
            .filter(Objects::nonNull)
            .map(ResearchCategories::getResearch)
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(r -> r.key, r -> r));
        final var visited = new TreeSet<>(toForget);

        var permWarp = 0;
        var stickyWarp = 0;
        var removedCount = 0;

        final ArrayList<String> removed = new ArrayList<>();
        while (!toForget.isEmpty()) {
            final var key = toForget.poll();
            final var data = researchMap.get(key);

            if (data == null) {
                continue;
            }

            if (!data.isAutoUnlock()) {
                removedCount += 1;

                final var removeIndex = playerResearch.indexOf(key);
                if (removeIndex >= 0) {
                    playerResearch.remove(removeIndex);
                    removed.add(key);
                }

                final var warp = ThaumcraftApi.getWarp(key);
                if (warp > 0 && !Config.wuss && !arguments.targetPlayer.worldObj.isRemote) {
                    final var sticky = warp / 2;
                    permWarp += warp - sticky;
                    stickyWarp += sticky;
                }
            }

            if (arguments.scalpel) {
                continue;
            }

            final Predicate<ResearchItem> isChildResearch = (r) -> {
                if (r.parents != null) {
                    for (var parent : r.parents) {
                        if (key.equals(parent)) {
                            return true;
                        }
                    }
                }
                if (r.parentsHidden != null) {
                    for (var parent : r.parentsHidden) {
                        if (key.equals(parent)) {
                            return true;
                        }
                    }
                }
                if (r.siblings != null) {
                    for (var sibling : r.siblings) {
                        if (key.equals(sibling)) {
                            return true;
                        }
                    }
                }
                return false;
            };

            researchMap.values()
                .stream()
                .filter(r -> !visited.contains(r.key) && isChildResearch.test(r))
                .forEach(r -> {
                    toForget.add(r.key);
                    visited.add(r.key);
                });
        }

        final var target = arguments.targetPlayer;
        if (!arguments.retainWarp) {
            final var knowledge = Thaumcraft.proxy.getPlayerKnowledge();
            if (permWarp > 0) {
                knowledge.addWarpPerm(target.getCommandSenderName(), -permWarp);
                PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(target, (byte) 0), target);
            }
            if (stickyWarp > 0) {
                knowledge.addWarpSticky(target.getCommandSenderName(), -stickyWarp);
                PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(target, (byte) 1), target);
            }
            if (permWarp > 0 || stickyWarp > 0) {
                knowledge.setWarpCounter(
                    target.getCommandSenderName(),
                    knowledge.getWarpTotal(target.getCommandSenderName()));
            }
        }
        NetworkHandler.instance.sendTo(new MessageForgetResearch(removed), arguments.targetPlayer);
        sender.addChatMessage(
            new ChatComponentTranslation(
                arguments.retainWarp ? "salisarcana:command.forget-research.retain"
                    : "salisarcana:command.forget-research.remove",
                removedCount,
                permWarp,
                stickyWarp));
    }

    @Override
    protected int minimumRequiredArgs() {
        return 1;
    }

    public static class Arguments {

        @FlagArg(name = "--all", excludes = { "--research-key", "--scalpel" }, descLangKey = "all")
        public boolean allResearch;

        @NamedArg(
            name = "--research-key",
            handler = ResearchKeyHandler.class,
            excludes = "--all",
            descLangKey = "research")
        public ArrayList<String> researchKeys = new ArrayList<>();

        @NamedArg(name = "--player", handler = PlayerHandler.class, descLangKey = "player")
        public EntityPlayerMP targetPlayer;

        @FlagArg(name = "--scalpel", excludes = { "--all" }, descLangKey = "scalpel")
        public boolean scalpel;

        @FlagArg(name = "--retain-warp", descLangKey = "retain-warp")
        public boolean retainWarp;
    }
}
