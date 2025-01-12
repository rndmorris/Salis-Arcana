package dev.rndmorris.salisarcana.common.commands;

import net.minecraft.command.CommandException;

public class CommandErrors {

    public static void commandNotFound() {
        throw new CommandException("commands.generic.notFound");
    }

    public static void generic() {
        throw new CommandException("commands.generic.exception");
    }

    public static void insufficientPermission() {
        throw new CommandException("commands.generic.permission");
    }

    public static void invalidSyntax() {
        throw new CommandException("commands.generic.syntax");
    }

    public static void noItem() {
        throw new CommandException("commands.enchant.noItem");
    }

    public static void playerNotFound() {
        throw new CommandException("commands.generic.player.notFound");
    }

}
