package dev.rndmorris.tfixins.common.commands;

import dev.rndmorris.tfixins.common.commands.arguments.ArgumentProcessor;
import dev.rndmorris.tfixins.common.commands.arguments.annotations.NamedArg;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.PlayerHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.SearchHandler;
import dev.rndmorris.tfixins.config.FixinsConfig;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

public class PlayerResearchCommand extends FixinsCommandBase<PlayerResearchCommand.Arguments>{

    public PlayerResearchCommand() {
        super(FixinsConfig.commandsModule.playerResearch);
    }

    @Override
    protected ArgumentProcessor<Arguments> initializeProcessor() {
        return new ArgumentProcessor<>(Arguments.class, Arguments::new, new IArgumentHandler[] {
            PlayerHandler.INSTANCE, SearchHandler.INSTANCE
        });
    }

    @Override
    protected void process(ICommandSender sender, String[] args) {
        final var arguments = argumentProcessor.process(sender, args);

        var t = false;
    }

    public static class Arguments {

        @NamedArg(name = "--player", handler = PlayerHandler.class, descLangKey = "player")
        public EntityPlayerMP forPlayer;

        @NamedArg(name = "--search", handler = SearchHandler.class, descLangKey = "search")
        public String searchTerm;

    }
}
