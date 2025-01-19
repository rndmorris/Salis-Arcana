package dev.rndmorris.salisarcana.common.commands;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import dev.rndmorris.salisarcana.SalisArcana;
import dev.rndmorris.salisarcana.common.commands.arguments.ArgumentProcessor;
import dev.rndmorris.salisarcana.config.settings.CommandSettings;

public abstract class ArcanaCommandBase<T> extends CommandBase {

    protected final CommandSettings settings;
    protected final ArgumentProcessor<T> argumentProcessor;

    public ArcanaCommandBase(@Nonnull CommandSettings settings) {
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
        return String.format("%s:command.%s.usage", SalisArcana.MODID, settings.name);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return settings.getPermissionLevel();
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return getRequiredPermissionLevel() == 0 || super.canCommandSenderUseCommand(sender);
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < minimumRequiredArgs()) {
            printUsage(sender);
            return;
        }
        try {
            process(sender, argumentProcessor.process(sender, args), args);
        } catch (Exception ex) {
            if (ex instanceof CommandException ce) {
                throw ce;
            }
            LOG.error(
                String.format(
                    "An error occurred for player %s while executing /%s.",
                    sender.getCommandSenderName(),
                    String.join(" ", args)),
                ex);
            sender.addChatMessage(
                new ChatComponentTranslation("salisarcana:command.error.execute")
                    .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        try {
            return CommandBase.getListOfStringsFromIterableMatchingLastWord(
                args,
                argumentProcessor.getAutocompletionSuggestions(sender, args));
        } catch (Exception ex) {
            LOG.error(
                String.format(
                    "An error occurred for player %s while providing tab completion for /%s.",
                    sender.getCommandSenderName(),
                    String.join(" ", args)),
                ex);
            sender.addChatMessage(
                new ChatComponentTranslation("salisarcana:command.error.tabcomplete")
                    .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
        }
        return null;
    }

    protected ChatStyle titleStyle() {
        final var titleStyle = new ChatStyle();
        titleStyle.setBold(true);
        titleStyle.setColor(EnumChatFormatting.BLUE);
        return titleStyle;
    }

    public void printHelp(ICommandSender sender) {
        final var titleStyle = titleStyle();

        final var description = new ChatComponentTranslation("salisarcana:command.desc");
        description.setChatStyle(titleStyle);
        description.appendText(" ");
        final var descriptionText = new ChatComponentTranslation(
            String.format("salisarcana:command.%s.desc", settings.name));
        descriptionText.getChatStyle()
            .setColor(EnumChatFormatting.RESET)
            .setBold(false);
        description.appendSibling(descriptionText);

        final var usageTitle = new ChatComponentTranslation("salisarcana:command.usage");
        usageTitle.setChatStyle(titleStyle);

        final var argumentsTitle = new ChatComponentTranslation("salisarcana:command.args");
        argumentsTitle.setChatStyle(titleStyle);

        final var first = Arrays.stream(
            new ChatComponentTranslation[] { description, usageTitle,
                new ChatComponentTranslation(String.format("salisarcana:command.%s.usage", settings.name)),
                argumentsTitle

            });
        final var descLangKeys = argumentProcessor.descriptionLangKeys.stream()
            .map(
                key -> new ChatComponentTranslation(
                    String.format("salisarcana:command.%s.args.%s", settings.name, key)));
        Stream.concat(first, descLangKeys)
            .forEachOrdered(sender::addChatMessage);
    }

    public void printUsage(ICommandSender sender) {
        sender.addChatMessage(new ChatComponentTranslation("salisarcana:command.usage").setChatStyle(titleStyle()));
        sender.addChatMessage(new ChatComponentTranslation(getCommandUsage(sender)));
    }

    protected abstract int minimumRequiredArgs();

    protected abstract void process(ICommandSender sender, T arguments, String[] args);
}
