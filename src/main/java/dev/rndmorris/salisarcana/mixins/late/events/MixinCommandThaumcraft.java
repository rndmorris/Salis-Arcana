package dev.rndmorris.salisarcana.mixins.late.events;

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

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategoryList;
import thaumcraft.common.lib.events.CommandThaumcraft;
import thaumcraft.common.lib.research.ResearchManager;

@Mixin(CommandThaumcraft.class)
public abstract class MixinCommandThaumcraft extends CommandBase {

    /**
     * @author Midnight145
     * @reason Implement tab completion for Thaumcraft commands
     */
    @Overwrite
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        List<String> playerNames = Arrays.asList(
            MinecraftServer.getServer()
                .getAllUsernames());
        ArrayList<String> list = new ArrayList<>();
        switch (args.length - 1) {
            case 0 -> {
                list.add("research");
                list.add("warp");
                list.add("aspect");
            }
            case 1 -> {
                if (Arrays.asList(new String[] { "research", "warp", "aspect" })
                    .contains(args[0])) {
                    if (args[0].equals("research")) {
                        list.add("list");
                    }
                    list.addAll(playerNames);
                }
            }
            case 2 -> list.addAll(sa$getThirdArgOptions(sender, args));

            case 4 -> {
                if (args[0].equals("warp")) {
                    list.add("PERM");
                    list.add("TEMP");
                }
            }
        }
        return sa$getTrueArgs(args[args.length - 1], list);
    }

    @Unique
    private static List<String> sa$getThirdArgOptions(ICommandSender sender, String[] args) {
        ArrayList<String> potentialArgs = new ArrayList<>();
        switch (args[0]) {
            case "research" -> {
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
            }
            case "warp" -> {
                potentialArgs.add("add");
                potentialArgs.add("set");
            }
            case "aspect" -> {
                potentialArgs.add("all");
                potentialArgs.addAll(Aspect.aspects.keySet());
            }
        }
        return sa$getTrueArgs(args[args.length - 1], potentialArgs);
    }

    @Unique
    private static List<String> sa$getTrueArgs(String arg, List<String> potentialArgs) {
        ArrayList<String> list = new ArrayList<>();
        for (String potentialArg : potentialArgs) {
            if (potentialArg.startsWith(arg)) {
                list.add(potentialArg);
            }
        }
        return list;
    }
}
