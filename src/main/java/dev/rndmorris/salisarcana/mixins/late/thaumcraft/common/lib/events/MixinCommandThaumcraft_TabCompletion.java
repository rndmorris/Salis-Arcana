package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import dev.rndmorris.salisarcana.config.SalisConfig;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategoryList;
import thaumcraft.common.lib.events.CommandThaumcraft;
import thaumcraft.common.lib.research.ResearchManager;

@Mixin(CommandThaumcraft.class)
public abstract class MixinCommandThaumcraft_TabCompletion extends CommandBase {

    /**
     * @author Midnight145
     * @reason Implement tab completion for Thaumcraft commands
     */
    @Overwrite
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        switch (args[0]) {
            case "research" -> {
                return sa$handleResearchArgs(sender, args);
            }
            case "warp" -> {
                return sa$handleWarpArgs(args);
            }
            case "aspect" -> {
                return sa$handleAspectArgs(args);
            }
            default -> {
                return getListOfStringsMatchingLastWord(args, "research", "warp", "aspect");
            }
        }
    }

    @Unique
    private static List<String> sa$getResearchThirdArgOptions(ICommandSender sender, String[] args) {
        List<String> potentialArgs = new ArrayList<>();
        String lastArg = args[args.length - 1];
        if (lastArg.equals("list")) {
            return potentialArgs;
        }
        potentialArgs.add("all");
        potentialArgs.add("reset");
        Collection<ResearchCategoryList> researchCategories = ResearchCategories.researchCategories.values();
        for (ResearchCategoryList category : researchCategories) {
            for (String key : category.research.keySet()) {
                if (!ResearchManager.isResearchComplete(sender.getCommandSenderName(), key)) {
                    potentialArgs.add(key);
                }
            }
        }
        return getListOfStringsMatchingLastWord(args, potentialArgs.toArray(new String[0]));
    }

    @Unique
    private static List<String> sa$handleResearchArgs(ICommandSender sender, String[] commandArgs) {
        switch (commandArgs.length) {
            case 2 -> {
                List<String> args = new ArrayList<>(
                    Arrays.asList(
                        MinecraftServer.getServer()
                            .getAllUsernames()));
                args.add(0, "list");
                return getListOfStringsMatchingLastWord(commandArgs, args.toArray(new String[0]));
            }
            case 3 -> {
                return sa$getResearchThirdArgOptions(sender, commandArgs);
            }
            default -> {
                return new ArrayList<>();
            }
        }
    }

    @Unique
    private static List<String> sa$handleWarpArgs(String[] commandArgs) {
        switch (commandArgs.length) {
            case 2 -> {
                return getListOfStringsMatchingLastWord(
                    commandArgs,
                    MinecraftServer.getServer()
                        .getAllUsernames());
            }
            case 3 -> {
                return getListOfStringsMatchingLastWord(commandArgs, "add", "set");
            }
            // case 4 is an integer, so no tab completion needed
            case 5 -> {
                if (SalisConfig.features.thaumcraftCommandWarpArgAll.isEnabled()) {
                    return getListOfStringsMatchingLastWord(commandArgs, "PERM", "TEMP", "ALL");
                }
                return getListOfStringsMatchingLastWord(commandArgs, "PERM", "TEMP");
            }
            default -> {
                return new ArrayList<>();
            }
        }
    }

    @Unique
    private static List<String> sa$handleAspectArgs(String[] commandArgs) {
        switch (commandArgs.length) {
            case 2 -> {
                return getListOfStringsMatchingLastWord(
                    commandArgs,
                    MinecraftServer.getServer()
                        .getAllUsernames());
            }
            case 3 -> {
                List<String> potentialArgs = new ArrayList<>(Aspect.aspects.keySet());
                potentialArgs.add(0, "all");
                return getListOfStringsMatchingLastWord(commandArgs, potentialArgs.toArray(new String[0]));
            }
            default -> {
                return new ArrayList<>();
            }
        }
    }
}
