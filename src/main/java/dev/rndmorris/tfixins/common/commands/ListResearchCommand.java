package dev.rndmorris.tfixins.common.commands;

import java.util.TreeSet;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

import dev.rndmorris.tfixins.common.commands.arguments.ArgumentProcessor;
import dev.rndmorris.tfixins.common.commands.arguments.annotations.NamedArg;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.named.PlayerHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.named.SearchHandler;
import dev.rndmorris.tfixins.config.ConfigModuleRoot;
import dev.rndmorris.tfixins.lib.ResearchHelper;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.Thaumcraft;

public class ListResearchCommand extends FixinsCommandBase<ListResearchCommand.Arguments> {

    public static final String listOthersReserach = "permissionLevel_ListOthersResearch";

    public ListResearchCommand() {
        super(ConfigModuleRoot.commandsModule.playerResearch);
    }

    @Override
    protected @Nonnull ArgumentProcessor<Arguments> initializeProcessor() {
        return new ArgumentProcessor<>(
            Arguments.class,
            Arguments::new,
            new IArgumentHandler[] { PlayerHandler.INSTANCE, SearchHandler.INSTANCE });
    }

    @Override
    protected void process(ICommandSender sender, String[] args) {
        final var arguments = argumentProcessor.process(sender, args);

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
            throw new CommandException("tfixins:command.player-research.no_results");
        }
        results.forEach(sender::addChatMessage);
    }

    public static class Arguments {

        @NamedArg(name = "--player", handler = PlayerHandler.class, descLangKey = "player")
        public EntityPlayerMP forPlayer;

        @NamedArg(name = "--search", handler = SearchHandler.class, descLangKey = "search")
        public String searchTerm;

    }
}
