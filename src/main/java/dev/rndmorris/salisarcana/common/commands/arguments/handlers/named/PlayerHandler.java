package dev.rndmorris.salisarcana.common.commands.arguments.handlers.named;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import com.google.common.collect.PeekingIterator;

import dev.rndmorris.salisarcana.common.commands.CommandErrors;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;

public class PlayerHandler implements INamedArgumentHandler {

    public static final IArgumentHandler INSTANCE = new PlayerHandler();

    @Override
    public Object parse(ICommandSender sender, PeekingIterator<String> args) {
        final var player = CommandBase.getPlayer(sender, args.next());
        if (player == null) {
            CommandErrors.playerNotFound();
        }
        return player;
    }

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, PeekingIterator<String> args) {
        args.next();
        if (args.hasNext()) {
            return null;
        }
        return Arrays.asList(
            MinecraftServer.getServer()
                .getAllUsernames());
    }

    @Nonnull
    @Override
    public Class<?> getOutputType() {
        return EntityPlayerMP.class;
    }
}
