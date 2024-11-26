package dev.rndmorris.tfixins.common.commands.arguments.handlers.named;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import dev.rndmorris.tfixins.common.commands.CommandErrors;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.IArgumentHandler;

public class PlayerHandler implements INamedArgumentHandler {

    public static final IArgumentHandler INSTANCE = new PlayerHandler();

    @Override
    public Object parse(ICommandSender sender, String current, Iterator<String> args) {
        if (!args.hasNext()) {
            CommandErrors.invalidSyntax();
        }
        final var player = CommandBase.getPlayer(sender, args.next());
        if (player == null) {
            CommandErrors.playerNotFound();
        }
        return player;
    }

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, String current, Iterator<String> args) {
        if (args.hasNext()) {
            args.next();
        }
        if (args.hasNext()) {
            return null;
        }
        return Arrays.asList(
            MinecraftServer.getServer()
                .getAllUsernames());
    }
}
