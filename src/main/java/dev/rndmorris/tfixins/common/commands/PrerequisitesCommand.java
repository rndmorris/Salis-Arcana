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
import java.util.function.Consumer;
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

import cpw.mods.fml.common.registry.GameRegistry;
import dev.rndmorris.tfixins.common.commands.arguments.ArgumentProcessor;
import dev.rndmorris.tfixins.common.commands.arguments.annotations.FlagArg;
import dev.rndmorris.tfixins.common.commands.arguments.annotations.NamedArg;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.IntHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.ItemHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.ResearchHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.flag.FlagHandler;
import dev.rndmorris.tfixins.lib.EnumHelper;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
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
        final var itemhandler = new ItemHandler(collectCraftedItemKeys());
        return new ArgumentProcessor<>(
            Arguments.class,
            Arguments::new,
            new IArgumentHandler[] { ResearchHandler.INSTANCE, FlagHandler.INSTANCE, depthHandler, itemhandler });
    }

    @Override
    protected void process(ICommandSender sender, String[] args) {
        final var argsObj = argumentProcessor.process(sender, args);

        if (argsObj.research != null) {
            sendPrereqsForResearch(sender, argsObj.research, argsObj.allResearch);
            return;
        }

        if (argsObj.itemStack != null) {
            sendPrereqsForItem(sender, argsObj.itemStack);
            return;
        }

        CommandErrors.invalidSyntax();
    }

    private void sendPrereqsForResearch(ICommandSender sender, @Nonnull ResearchItem research, boolean allResearch) {
        final var playerKnowledge = getPlayerKnowledge(sender);
        final var isKnown = playerKnowledge.contains(research.key);

        if (!allResearch && isKnown) {
            sendAlreadyKnown(sender, research);
            return;
        }

        sender.addChatMessage(
            new ChatComponentText("")
                .appendSibling(
                    new ChatComponentTranslation("tfixins:command.prereqs.header")
                        .setChatStyle(color(EnumChatFormatting.DARK_PURPLE)))
                .appendText(" ")
                .appendSibling(formatResearch(research)));

        final var scanPrereqs = buildScanPrereqs(research);
        if (scanPrereqs != null) {
            sender.addChatMessage(scanPrereqs);
        }
        sender.addChatMessage(buildResearchMessage(research, allResearch, playerKnowledge));
    }

    private void sendPrereqsForItem(ICommandSender sender, ItemStack item) {
        final var matchingResearchKeys = findResearch(item);

        if (matchingResearchKeys.isEmpty()) {
            sender.addChatMessage(
                new ChatComponentTranslation("tfixins:command.prereqs.item_not_found", item.func_151000_E())
                    .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
            return;
        }

        final var message = new ChatComponentText("").appendSibling(
            new ChatComponentTranslation("tfixins:command.prereqs.header_item", item.func_151000_E())
                .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
        sender.addChatMessage(message);

        final var listMessage = new ChatComponentText("");

        final var keyIterator = matchingResearchKeys.iterator();
        while (keyIterator.hasNext()) {
            final var research = ResearchCategories.getResearch(keyIterator.next());
            listMessage.appendSibling(formatResearchClickCommand(research));
            if (keyIterator.hasNext()) {
                listMessage.appendText(", ");
            }
        }

        sender.addChatMessage(listMessage);
    }

    private TreeSet<String> findResearch(ItemStack item) {
        final var foundResearch = new TreeSet<String>();

        iterateOverThaumRecipes((arcane) -> {
            ItemStack output;
            if ((output = arcane.getRecipeOutput()) != null && item.isItemEqual(output)) {
                foundResearch.add(arcane.getResearch());
            }
        }, (crucible) -> {
            ItemStack output;
            if ((output = crucible.getRecipeOutput()) != null && item.isItemEqual(output)) {
                foundResearch.add(crucible.key);
            }
        }, (infusion) -> {
            if (infusion.getRecipeOutput() instanceof ItemStack infusionOutput && item.isItemEqual(infusionOutput)) {
                foundResearch.add(infusion.getResearch());
            }
        });

        return foundResearch;
    }

    private void iterateOverThaumRecipes(Consumer<IArcaneRecipe> craftingConsumer,
        Consumer<CrucibleRecipe> crucibleConsumer, Consumer<InfusionRecipe> infusionConsumer) {
        for (var recipe : ThaumcraftApi.getCraftingRecipes()) {
            if (recipe instanceof IArcaneRecipe arcane) {
                if (craftingConsumer != null) {
                    craftingConsumer.accept(arcane);
                }
                continue;
            }
            if (recipe instanceof CrucibleRecipe crucible) {
                if (crucibleConsumer != null) {
                    crucibleConsumer.accept(crucible);
                }
                continue;
            }
            if (recipe instanceof InfusionRecipe infusion) {
                if (infusionConsumer != null) {
                    infusionConsumer.accept(infusion);
                }
            }
        }
    }

    private Set<String> collectCraftedItemKeys() {
        final var result = new TreeSet<String>();

        final Consumer<ItemStack> handleItemStack = (recipeOutput) -> {
            final var id = GameRegistry.findUniqueIdentifierFor(recipeOutput.getItem());
            if (id == null) {
                LOG.error("Could not find unique id for item {}", recipeOutput.toString());
                return;
            }
            result.add(String.format("%s:%s", id.modId, id.name));
        };

        iterateOverThaumRecipes((arcane) -> {
            final var recipeOutput = arcane.getRecipeOutput();
            if (recipeOutput != null && recipeOutput.getItem() != null) {
                handleItemStack.accept(recipeOutput);
            }
        }, (crucible) -> {
            final var recipeOutput = crucible.getRecipeOutput();
            if (recipeOutput != null && recipeOutput.getItem() != null) {
                handleItemStack.accept(recipeOutput);
            }
        }, (infusion) -> {
            final var output = infusion.getRecipeOutput();
            if (output instanceof ItemStack recipeOutput && recipeOutput.getItem() != null) {
                handleItemStack.accept(recipeOutput);
            }
        });

        return result;
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

    private IChatComponent formatResearch(ResearchItem research) {
        return formatResearch(research, EnumChatFormatting.DARK_PURPLE);
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

    private IChatComponent formatResearchClickCommand(ResearchItem research) {
        var result = formatResearch(research);
        result.getChatStyle()
            .setChatClickEvent(suggestResearchCommand(research));
        return result;
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

        @NamedArg(name = "--research", excludes = "--item", handler = ResearchHandler.class, descLangKey = "research")
        public ResearchItem research;

        @FlagArg(name = "--completed", excludes = "--item", descLangKey = "all")
        public boolean allResearch;

        @NamedArg(name = "--item", handler = ItemHandler.class, excludes = { "--research", "--completed" })
        public ItemStack itemStack;

    }
}
