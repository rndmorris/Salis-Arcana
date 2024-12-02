package dev.rndmorris.tfixins.common.commands;

import static dev.rndmorris.tfixins.ThaumicFixins.LOG;
import static dev.rndmorris.tfixins.config.FixinsConfig.commandsModule;
import static dev.rndmorris.tfixins.lib.ArrayHelper.toList;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import dev.rndmorris.tfixins.common.commands.arguments.ArgumentProcessor;
import dev.rndmorris.tfixins.common.commands.arguments.annotations.FlagArg;
import dev.rndmorris.tfixins.common.commands.arguments.annotations.PositionalArg;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.IntHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.ResearchHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.flag.FlagHandler;
import dev.rndmorris.tfixins.lib.EnumHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.Thaumcraft;

public class PrerequisitesCommand extends FixinsCommandBase<PrerequisitesCommand.Arguments> {

    public PrerequisitesCommand() {
        super(commandsModule.prerequisites);
    }

    @Nonnull
    @Override
    protected ArgumentProcessor<Arguments> initializeProcessor() {
        final var depthHandler = new IntHandler(-1, Integer.MAX_VALUE, 3);
        return new ArgumentProcessor<>(
            Arguments.class,
            Arguments::new,
            new IArgumentHandler[] { ResearchHandler.INSTANCE, FlagHandler.INSTANCE, depthHandler });
    }

    @Override
    protected void process(ICommandSender sender, String[] args) {
        final var argsObj = argumentProcessor.process(sender, args);

        final var research = argsObj.research;
        if (research == null) {
            LOG.error("Research not found.");
            CommandErrors.invalidSyntax();
        }

        final var playerKnowledge = getPlayerKnowledge(sender);
        final var isKnown = playerKnowledge.contains(research.key);

        if (!argsObj.allResearch && isKnown) {
            sendAlreadyKnown(sender, research);
            return;
        }

        sender.addChatMessage(
            new ChatComponentText("")
                .appendSibling(
                    new ChatComponentTranslation("tfixins:command.prereqs.header")
                        .setChatStyle(color(EnumChatFormatting.DARK_PURPLE)))
                .appendText(" ")
                .appendSibling(formatResearch(research, EnumChatFormatting.DARK_PURPLE)));

        final var scanPrereqs = buildScanPrereqs(research);
        if (scanPrereqs != null) {
            sender.addChatMessage(scanPrereqs);
        }
        sender.addChatMessage(buildResearchMessage(research, argsObj.allResearch, playerKnowledge));
    }

    private Set<String> getPlayerKnowledge(ICommandSender sender) {
        final var knowledgeList = Thaumcraft.proxy.getPlayerKnowledge().researchCompleted
            .get(sender.getCommandSenderName());
        if (knowledgeList == null) {
            return Collections.emptySet();
        }
        return new TreeSet<>(knowledgeList);
    }

    private void sendAlreadyKnown(ICommandSender sender, ResearchItem research) {
        final var message = new ChatComponentTranslation(
            "tfixins:command.prereqs.already_known",
            formatResearch(research, true));
        message.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_PURPLE));
        sender.addChatMessage(message);
    }

    private IChatComponent buildScanPrereqs(ResearchItem research) {
        final var aspectTriggers = toList(research.getAspectTriggers());
        final var entityTriggers = toList(research.getEntityTriggers());
        final var itemTriggers = toList(research.getItemTriggers());

        final var aspectsMessage = buildAspectsMessage(aspectTriggers);
        final var entitiesMessage = buildEntitiesMessage(entityTriggers);
        final var itemsMessage = buildItemsMessage(itemTriggers);

        if (aspectsMessage == null && entitiesMessage == null && itemsMessage == null) {
            return null;
        }
        final var message = new ChatComponentText("")
            .appendSibling(new ChatComponentTranslation("tfixins:command.prereqs.triggers").setChatStyle(blue()))
            .appendText(" ");

        IChatComponent lastMessage = null;
        if (aspectsMessage != null) {
            message.appendSibling(aspectsMessage);
            lastMessage = aspectsMessage;
        }
        if (entitiesMessage != null) {
            if (lastMessage != null) {
                message.appendText(" ")
                    .appendSibling(
                        new ChatComponentTranslation("tfixins:command.prereqs.triggers_or").setChatStyle(italicBlue()))
                    .appendText(" ");
            }
            message.appendSibling(entitiesMessage);
            lastMessage = entitiesMessage;
        }
        if (itemsMessage != null) {
            if (lastMessage != null) {
                message.appendText(" ")
                    .appendSibling(
                        new ChatComponentTranslation("tfixins:command.prereqs.triggers_or").setChatStyle(italicBlue()))
                    .appendText(" ");
            }
            message.appendSibling(itemsMessage);
        }
        return message;
    }

    @Nullable
    private IChatComponent buildAspectsMessage(List<Aspect> aspectTriggers) {
        final var aspectComponents = aspectTriggers.stream()
            .filter(Objects::nonNull)
            .map(aspect -> {
                final var aspectChat = new ChatComponentText(String.format("[%s]", aspect.getName()));
                final var aspectColor = aspect.getChatcolor();
                EnumChatFormatting color;
                if (aspectColor != null && !aspectColor.isEmpty()
                    && ((color = EnumHelper.findByFormattingCode(aspectColor.charAt(0))) != null)) {
                    aspectChat.setChatStyle(new ChatStyle().setColor(color));
                }
                return aspectChat;
            })
            .collect(Collectors.toList());

        if (aspectComponents.isEmpty()) {
            return null;
        }

        final var message = new ChatComponentTranslation("tfixins:command.prereqs.triggers_aspects").appendText(" ")
            .appendSibling(aspectComponents.get(0));

        aspectComponents.subList(1, aspectTriggers.size())
            .forEach(
                msg -> message.appendText(", ")
                    .appendSibling(msg));

        return message;
    }

    @Nullable
    private IChatComponent buildEntitiesMessage(List<String> entityTriggers) {
        final var entityComponents = entityTriggers.stream()
            .filter(Objects::nonNull)
            .map(
                str -> new ChatComponentTranslation(String.format("entity.%s.name", str)).setChatStyle(
                    new ChatStyle()
                        .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(str)))))
            .map(
                chat -> new ChatComponentText("[").appendSibling(chat)
                    .appendText("]"))
            .collect(Collectors.toList());

        if (entityComponents.isEmpty()) {
            return null;
        }

        final var message = new ChatComponentTranslation("tfixins:command.prereqs.triggers_entities").appendText(" ")
            .appendSibling(entityComponents.get(0));

        entityComponents.subList(1, entityComponents.size())
            .forEach(
                msg -> message.appendText(", ")
                    .appendSibling(msg));

        return message;
    }

    @Nullable
    private IChatComponent buildItemsMessage(List<ItemStack> itemTriggers) {
        final var itemComponents = itemTriggers.stream()
            .filter(item -> item != null && item.getItem() != null)
            .map(ItemStack::func_151000_E)
            .collect(Collectors.toList());

        if (itemComponents.isEmpty()) {
            return null;
        }

        final var message = new ChatComponentTranslation("tfixins:command.prereqs.triggers_items").appendText(" ")
            .appendSibling(itemComponents.get(0));

        itemComponents.subList(1, itemComponents.size())
            .forEach(
                msg -> message.appendText(", ")
                    .appendSibling(msg));

        return message;
    }

    @Nonnull
    private IChatComponent buildResearchMessage(ResearchItem research, boolean allResearch,
        Set<String> playerKnowledge) {
        final var visited = new TreeSet<String>();
        final var todo = new ArrayDeque<String>();

        visited.add(research.key);
        queueParents(research, visited, todo);

        IChatComponent message = null;

        while (!todo.isEmpty()) {
            final var currentKey = todo.pop();
            visited.add(currentKey);

            if (!allResearch && playerKnowledge.contains(currentKey)) {
                continue;
            }

            final var currentResearch = ResearchCategories.getResearch(currentKey);
            final var researchText = formatResearchClickCommand(currentResearch, playerKnowledge.contains(currentKey));

            if (message != null) {
                message.appendText(" ")
                    .appendSibling(researchText);
            } else {
                message = researchText;
            }

            queueParents(currentResearch, visited, todo);
        }

        final var header = new ChatComponentText("")
            .appendSibling(new ChatComponentTranslation("tfixins:command.prereqs.parents").setChatStyle(blue()))
            .appendText(" ");

        if (message == null) {
            return header.appendSibling(new ChatComponentTranslation("tfixins:command.prereqs.parents_none"));
        }

        return header.appendSibling(message);
    }

    private void queueParents(ResearchItem research, Set<String> visited, Deque<String> todo) {
        if (research.parents != null) {
            Arrays.stream(research.parents)
                .filter(p -> !visited.contains(p))
                .forEach(p -> {
                    visited.add(p);
                    todo.add(p);
                });
        }
        if (research.parentsHidden != null) {
            Arrays.stream(research.parentsHidden)
                .filter(p -> !visited.contains(p))
                .forEach(p -> {
                    visited.add(p);
                    todo.add(p);
                });
        }
    }

    private IChatComponent formatResearch(ResearchItem research, boolean known) {
        return formatResearch(research, known ? EnumChatFormatting.DARK_PURPLE : EnumChatFormatting.DARK_RED);
    }

    private IChatComponent formatResearch(ResearchItem research, EnumChatFormatting formatting) {
        final var style = new ChatStyle().setColor(formatting)
            .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(research.key)));
        return new ChatComponentText("[").setChatStyle(style)
            .appendSibling(new ChatComponentTranslation(String.format("tc.research_name.%s", research.key)))
            .appendText("]");
    }

    private IChatComponent formatResearchClickCommand(ResearchItem research, boolean known) {
        var result = formatResearch(research, known);
        result.getChatStyle()
            .setChatClickEvent(suggestResearchCommand(research));
        return result;
    }

    private ClickEvent suggestResearchCommand(ResearchItem research) {
        return new ClickEvent(
            ClickEvent.Action.SUGGEST_COMMAND,
            String.format("/%s %s", getCommandName(), research.key));
    }

    private ChatStyle color(EnumChatFormatting color) {
        return new ChatStyle().setColor(color);
    }

    private ChatStyle blue() {
        return color(EnumChatFormatting.BLUE);
    }

    private ChatStyle italicBlue() {
        return blue().setItalic(true);
    }

    public static class Arguments {

        @PositionalArg(index = 0, handler = ResearchHandler.class, descLangKey = "research")
        public ResearchItem research;

        @FlagArg(name = "--completed", descLangKey = "all")
        public boolean allResearch;

    }
}
