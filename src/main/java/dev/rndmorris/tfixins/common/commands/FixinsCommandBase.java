package dev.rndmorris.tfixins.common.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;

import dev.rndmorris.tfixins.ThaumicFixins;
import dev.rndmorris.tfixins.common.commands.arguments.ArgumentProcessor;
import dev.rndmorris.tfixins.config.CommandSettings;

public abstract class FixinsCommandBase<T> extends CommandBase {

    protected final CommandSettings settings;
    protected final ArgumentProcessor<T> argumentProcessor;

    public FixinsCommandBase(@Nonnull CommandSettings settings) {
        settings.setCommandGetter(() -> this);
        this.settings = settings;
        this.argumentProcessor = initializeProcessor();
    }

    protected abstract @Nonnull ArgumentProcessor<T> initializeProcessor();

    @Override
    public String getCommandName() {
        return settings.getFullName();
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<>(settings.aliases);
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return String.format("%s:command.%s.usage", ThaumicFixins.MODID, settings.name);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return settings.getPermissionLevel();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        process(sender, args);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return CommandBase.getListOfStringsFromIterableMatchingLastWord(
            args,
            argumentProcessor.getAutocompletionSuggestions(sender, args));
    }

    public void printHelp(ICommandSender sender) {
        final var header = Arrays.stream(
            new String[] { String.format("tfixins:command.%s.desc", settings.name), "tfixins:command.usage",
                String.format("tfixins:command.%s.usage", settings.name), "tfixins:command.args", });
        final var descLangKeys = argumentProcessor.descriptionLangKeys.stream()
            .map(key -> String.format("tfixins:command.%s.args.%s", settings.name, key));
        Stream.concat(header, descLangKeys)
            .map(ChatComponentTranslation::new)
            .forEachOrdered(sender::addChatMessage);
    }

    protected abstract void process(ICommandSender sender, String[] args);
}
