package dev.rndmorris.salisarcana.common.commands;

import java.util.TreeSet;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

import dev.rndmorris.salisarcana.common.commands.arguments.ArgumentProcessor;
import dev.rndmorris.salisarcana.common.commands.arguments.annotations.NamedArg;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.named.PlayerHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.named.SearchHandler;
import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.lib.ResearchHelper;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.Thaumcraft;

public class ListResearchCommand extends ArcanaCommandBase<ListResearchCommand.Arguments> {

    public static final String listOthersReserach = "permissionLevel_ListOthersResearch";

    public ListResearchCommand() {
        super(SalisConfig.commands.playerResearch);
    }

    @Override
    protected @Nonnull ArgumentProcessor<Arguments> initializeProcessor() {
        return new ArgumentProcessor<>(
            Arguments.class,
            Arguments::new,
            new IArgumentHandler[] { PlayerHandler.INSTANCE, SearchHandler.INSTANCE });
    }

    @Override
    protected void process(ICommandSender sender, Arguments arguments, String[] args) {
        Predicate<ResearchItem> predicate = (r) -> true;

        if (arguments.forPlayer != null) {
            if (!sender.canCommandSenderUseCommand(
                settings.getChildPermissionLevel(listOthersReserach),
                settings.getFullName())) {
                CommandErrors.insufficientPermission();
            }
            final var playerKnowledge = new TreeSet<>(
                Thaumcraft.proxy.playerKnowledge.researchCompleted.get(arguments.forPlayer.getCommandSenderName()));

            predicate = arguments.searchTerm != null
                ? (r) -> playerKnowledge.contains(r.key) && ResearchHelper.matchesTerm(r, arguments.searchTerm)
                : (r) -> playerKnowledge.contains(r.key);
        } else if (arguments.searchTerm != null) {
            predicate = (r) -> ResearchHelper.matchesTerm(r, arguments.searchTerm);
        }

        var results = ResearchHelper.printResearchToChat(predicate);
        if (results.isEmpty()) {
            throw new CommandException("salisarcana:command.list-research.no_results");
        }
        results.forEach(sender::addChatMessage);
    }

    @Override
    protected int minimumRequiredArgs() {
        return 0;
    }

    public static class Arguments {

        @NamedArg(name = "--player", handler = PlayerHandler.class, descLangKey = "player")
        public EntityPlayerMP forPlayer;

        @NamedArg(name = "--search", handler = SearchHandler.class, descLangKey = "search")
        public String searchTerm;

    }
}
