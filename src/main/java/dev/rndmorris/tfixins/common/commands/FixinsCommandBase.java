package dev.rndmorris.tfixins.common.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import dev.rndmorris.tfixins.ThaumicFixins;
import dev.rndmorris.tfixins.common.commands.arguments.ArgumentProcessor;
import dev.rndmorris.tfixins.config.commands.CommandSettings;

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
        final var titleStyle = new ChatStyle();
        titleStyle.setBold(true);
        titleStyle.setColor(EnumChatFormatting.BLUE);

        final var description = new ChatComponentTranslation("tfixins:command.desc");
        description.setChatStyle(titleStyle);
        description.appendText(" ");
        final var descriptionText = new ChatComponentTranslation(
            String.format("tfixins:command.%s.desc", settings.name));
        descriptionText.getChatStyle()
            .setColor(EnumChatFormatting.RESET)
            .setBold(false);
        description.appendSibling(descriptionText);

        final var usageTitle = new ChatComponentTranslation("tfixins:command.usage");
        usageTitle.setChatStyle(titleStyle);

        final var argumentsTitle = new ChatComponentTranslation("tfixins:command.args");
        argumentsTitle.setChatStyle(titleStyle);

        final var first = Arrays.stream(
            new ChatComponentTranslation[] { description, usageTitle,
                new ChatComponentTranslation(String.format("tfixins:command.%s.usage", settings.name)), argumentsTitle

            });
        final var descLangKeys = argumentProcessor.descriptionLangKeys.stream()
            .map(key -> new ChatComponentTranslation(String.format("tfixins:command.%s.args.%s", settings.name, key)));
        Stream.concat(first, descLangKeys)
            .forEachOrdered(sender::addChatMessage);
    }

    protected abstract void process(ICommandSender sender, String[] args);
}
