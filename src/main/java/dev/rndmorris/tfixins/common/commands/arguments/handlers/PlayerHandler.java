package dev.rndmorris.tfixins.common.commands.arguments.handlers;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class PlayerHandler implements IArgumentHandler {

    public static final IArgumentHandler INSTANCE = new PlayerHandler();

    @Override
    public Object parse(ICommandSender sender, String current, Iterator<String> args) {
        if (!args.hasNext()) {
            throw new CommandException("commands.generic.syntax");
        }
        final var player = CommandBase.getPlayer(sender, args.next());
        if (player == null) {
            throw new CommandException("commands.generic.player.notFound");
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
