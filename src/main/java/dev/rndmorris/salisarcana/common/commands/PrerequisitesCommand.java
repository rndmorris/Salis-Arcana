package dev.rndmorris.salisarcana.common.commands;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;
import static dev.rndmorris.salisarcana.config.SalisConfig.commands;
import static dev.rndmorris.salisarcana.lib.ArrayHelper.toList;
import static dev.rndmorris.salisarcana.lib.ResearchHelper.formatResearch;
import static dev.rndmorris.salisarcana.lib.ResearchHelper.formatResearchClickCommand;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.command.ICommandSender;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import cpw.mods.fml.common.registry.GameRegistry;
import dev.rndmorris.salisarcana.common.commands.arguments.ArgumentProcessor;
import dev.rndmorris.salisarcana.common.commands.arguments.annotations.FlagArg;
import dev.rndmorris.salisarcana.common.commands.arguments.annotations.NamedArg;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IntHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.ItemHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.ResearchHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.flag.FlagHandler;
import dev.rndmorris.salisarcana.lib.EnumHelper;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.Thaumcraft;

public class PrerequisitesCommand extends ArcanaCommandBase<PrerequisitesCommand.Arguments> {

    private Map<String, List<CacheEntry>> recipeOutputCache;

    private static class CacheEntry {

        public final ItemStack itemStack;
        public final Set<String> researchKeys = new TreeSet<>();

        public CacheEntry(@Nonnull ItemStack item, String firstKey) {
            itemStack = item;
            researchKeys.add(firstKey);
        }
    }

    public PrerequisitesCommand() {
        super(commands.prerequisites);
    }

    @Nonnull
    @Override
    protected ArgumentProcessor<Arguments> initializeProcessor() {
        buildOutputCache();
        final var depthHandler = new IntHandler(-1, Integer.MAX_VALUE, 3);
        final var itemhandler = new ItemHandler(recipeOutputCache.keySet());
        return new ArgumentProcessor<>(
            Arguments.class,
            Arguments::new,
            new IArgumentHandler[] { ResearchHandler.INSTANCE, FlagHandler.INSTANCE, depthHandler, itemhandler });
    }

    private void buildOutputCache() {
        recipeOutputCache = new TreeMap<>();
        for (var recipe : ThaumcraftApi.getCraftingRecipes()) {
            if (recipe instanceof IArcaneRecipe arcane) {
                final var recipeOutput = arcane.getRecipeOutput();
                if (recipeOutput != null && recipeOutput.getItem() != null) {
                    addToCache(arcane.getResearch(), recipeOutput);
                }
                continue;
            }
            if (recipe instanceof CrucibleRecipe crucible) {
                final var recipeOutput = crucible.getRecipeOutput();
                if (recipeOutput != null && recipeOutput.getItem() != null) {
                    addToCache(crucible.key, recipeOutput);
                }
                continue;
            }
            if (recipe instanceof InfusionRecipe infusion) {
                final var output = infusion.getRecipeOutput();
                if (output instanceof ItemStack recipeOutput && recipeOutput.getItem() != null) {
                    addToCache(infusion.getResearch(), recipeOutput);
                }
            }
        }
    }

    private void addToCache(String researchKey, ItemStack recipeOutput) {
        final var id = GameRegistry.findUniqueIdentifierFor(recipeOutput.getItem());
        if (id == null) {
            LOG.error("Could not find unique id for item {}", recipeOutput.toString());
            return;
        }

        final var itemId = formatItemId(id);
        List<CacheEntry> cacheEntries;
        if ((cacheEntries = recipeOutputCache.get(itemId)) == null) {
            cacheEntries = new ArrayList<>();
            recipeOutputCache.put(itemId, cacheEntries);
        }
        var needNewCacheEntry = true;
        for (var entry : cacheEntries) {
            if (entry.itemStack.isItemEqual(recipeOutput)) {
                entry.researchKeys.add(researchKey);
                needNewCacheEntry = false;
                break;
            }
        }
        if (needNewCacheEntry) {
            cacheEntries.add(new CacheEntry(recipeOutput, researchKey));
        }
    }

    private String formatItemId(GameRegistry.UniqueIdentifier id) {
        return String.format("%s:%s", id.modId, id.name);
    }

    @Override
    protected void process(ICommandSender sender, Arguments arguments, String[] args) {
        if (arguments.research != null) {
            sendPrereqsForResearch(sender, arguments.research, arguments.allResearch);
            return;
        }

        if (arguments.itemStack != null) {
            sendPrereqsForItem(sender, arguments.itemStack);
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
                    new ChatComponentTranslation("salisarcana:command.prereqs.header")
                        .setChatStyle(color(EnumChatFormatting.LIGHT_PURPLE)))
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
                new ChatComponentTranslation("salisarcana:command.prereqs.item_not_found", item.func_151000_E())
                    .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
            return;
        }

        final var message = new ChatComponentText("").appendSibling(
            new ChatComponentTranslation("salisarcana:command.prereqs.header_item", item.func_151000_E())
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

        final var itemUid = GameRegistry.findUniqueIdentifierFor(item.getItem());
        if (itemUid == null) {
            LOG.error("Could not find unique id for item {}", item.toString());
            return foundResearch;
        }
        final var id = formatItemId(itemUid);
        final var caches = recipeOutputCache.get(id);
        if (caches == null) {
            return foundResearch;
        }
        for (var cache : caches) {
            if (cache.itemStack.isItemEqual(item)) {
                foundResearch.addAll(cache.researchKeys);
            }
        }

        return foundResearch;
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
            "salisarcana:command.prereqs.already_known",
            formatResearch(research));
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
            .appendSibling(new ChatComponentTranslation("salisarcana:command.prereqs.triggers").setChatStyle(blue()))
            .appendText(" ");

        IChatComponent lastMessage = null;
        if (aspectsMessage != null) {
            message.appendSibling(aspectsMessage);
            lastMessage = aspectsMessage;
        }
        if (entitiesMessage != null) {
            if (lastMessage != null) {
                message.appendSibling(orText());
            }
            message.appendSibling(entitiesMessage);
            lastMessage = entitiesMessage;
        }
        if (itemsMessage != null) {
            if (lastMessage != null) {
                message.appendSibling(orText());
            }
            message.appendSibling(itemsMessage);
        }
        return message;
    }

    private IChatComponent orText() {
        return new ChatComponentText(" ")
            .appendSibling(
                new ChatComponentTranslation("salisarcana:command.prereqs.triggers_or").setChatStyle(italicBlue()))
            .appendText(" ");
    }

    @Nullable
    private IChatComponent buildAspectsMessage(List<Aspect> aspectTriggers) {

        final var message = new ChatComponentText("")
            .appendSibling(new ChatComponentTranslation("salisarcana:command.prereqs.triggers_aspects"))
            .appendText(" ");

        var anyAspects = false;
        final var aspects$ = aspectTriggers.iterator();
        while (aspects$.hasNext()) {
            final var aspect = aspects$.next();
            if (aspect == null) {
                continue;
            }
            anyAspects = true;
            final var aspectChat = new ChatComponentText(String.format("[%s]", aspect.getName()));
            final var aspectColor = aspect.getChatcolor();
            EnumChatFormatting color;
            if (aspectColor != null && !aspectColor.isEmpty()
                && ((color = EnumHelper.findByFormattingCode(aspectColor.charAt(0))) != null)) {
                aspectChat.setChatStyle(new ChatStyle().setColor(color));
            }
            message.appendSibling(aspectChat);
            if (aspects$.hasNext()) {
                message.appendText(", ");
            }
        }

        return anyAspects ? message : null;
    }

    @Nullable
    private IChatComponent buildEntitiesMessage(List<String> entityTriggers) {

        final var message = new ChatComponentText("")
            .appendSibling(new ChatComponentTranslation("salisarcana:command.prereqs.triggers_entities"))
            .appendText(" ");

        var noEntities = true;

        final var entities$ = entityTriggers.iterator();
        while (entities$.hasNext()) {
            final var entity = entities$.next();
            if (entity == null) {
                continue;
            }
            noEntities = false;
            final var component = new ChatComponentText("[")
                .setChatStyle(
                    new ChatStyle()
                        .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(entity))))
                .appendSibling(new ChatComponentTranslation(String.format("entity.%s.name", entity)))
                .appendText("]");
            message.appendSibling(component);
            if (entities$.hasNext()) {
                message.appendText(", ");
            }
        }

        if (noEntities) {
            return null;
        }

        return message;
    }

    @Nullable
    private IChatComponent buildItemsMessage(List<ItemStack> itemTriggers) {

        final var message = new ChatComponentText("")
            .appendSibling(new ChatComponentTranslation("salisarcana:command.prereqs.triggers_items"))
            .appendText(" ");

        var noItems = true;
        final var items$ = itemTriggers.iterator();
        while (items$.hasNext()) {
            final var item = items$.next();
            if (item == null || item.getItem() == null) {
                continue;
            }
            noItems = false;
            message.appendSibling(item.func_151000_E());
            if (items$.hasNext()) {
                message.appendText(", ");
            }
        }

        if (noItems) {
            return null;
        }

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
            final var researchText = formatResearchClickCommand(
                currentResearch,
                researchColor(playerKnowledge.contains(currentKey)));

            if (message != null) {
                message.appendText(" ")
                    .appendSibling(researchText);
            } else {
                message = researchText;
            }

            queueParents(currentResearch, visited, todo);
        }

        final var header = new ChatComponentText("")
            .appendSibling(new ChatComponentTranslation("salisarcana:command.prereqs.parents").setChatStyle(blue()))
            .appendText(" ");

        if (message == null) {
            return header.appendSibling(new ChatComponentTranslation("salisarcana:command.prereqs.parents_none"));
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

    private EnumChatFormatting researchColor(boolean isKnown) {
        return isKnown ? EnumChatFormatting.DARK_PURPLE : EnumChatFormatting.DARK_RED;
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

    @Override
    protected int minimumRequiredArgs() {
        return 1;
    }

    public static class Arguments {

        @NamedArg(name = "--research", excludes = "--item", handler = ResearchHandler.class, descLangKey = "research")
        public ResearchItem research;

        @FlagArg(name = "--completed", excludes = "--item", descLangKey = "all")
        public boolean allResearch;

        @NamedArg(
            name = "--item",
            handler = ItemHandler.class,
            excludes = { "--research", "--completed" },
            descLangKey = "item")
        public ItemStack itemStack;

    }
}
