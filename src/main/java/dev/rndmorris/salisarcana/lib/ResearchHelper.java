package dev.rndmorris.salisarcana.lib;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;
import static dev.rndmorris.salisarcana.config.ConfigModuleRoot.commands;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.FakePlayer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.mojang.authlib.GameProfile;

import dev.rndmorris.salisarcana.SalisArcana;
import dev.rndmorris.salisarcana.api.IResearchItemExtended;
import dev.rndmorris.salisarcana.common.commands.PrerequisitesCommand;
import dev.rndmorris.salisarcana.lib.customresearch.ResearchEntry;
import dev.rndmorris.salisarcana.lib.customresearch.pages.ArcaneResearchPageEntry;
import dev.rndmorris.salisarcana.lib.customresearch.pages.AspectResearchPageEntry;
import dev.rndmorris.salisarcana.lib.customresearch.pages.CraftingResearchPageEntry;
import dev.rndmorris.salisarcana.lib.customresearch.pages.CrucibleResearchPageEntry;
import dev.rndmorris.salisarcana.lib.customresearch.pages.InfusionResearchPageEntry;
import dev.rndmorris.salisarcana.lib.customresearch.pages.PictureResearchPageEntry;
import dev.rndmorris.salisarcana.lib.customresearch.pages.ResearchPageEntry;
import dev.rndmorris.salisarcana.lib.customresearch.pages.SmeltingResearchPageEntry;
import dev.rndmorris.salisarcana.lib.customresearch.pages.TextResearchPageEntry;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategoryList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketPlayerCompleteToServer;
import thaumcraft.common.lib.research.ResearchManager;

public class ResearchHelper {

    private static Gson _researchGson;

    public static synchronized Gson researchGson() {
        if (_researchGson == null) {
            RuntimeTypeAdapterFactory<ResearchPageEntry> typeFactory = RuntimeTypeAdapterFactory
                .of(ResearchPageEntry.class, "pageType")
                .registerSubtype(ArcaneResearchPageEntry.class, "arcane")
                .registerSubtype(AspectResearchPageEntry.class, "aspect")
                .registerSubtype(CraftingResearchPageEntry.class, "crafting")
                .registerSubtype(CrucibleResearchPageEntry.class, "crucible")
                .registerSubtype(InfusionResearchPageEntry.class, "infusion")
                .registerSubtype(PictureResearchPageEntry.class, "picture")
                .registerSubtype(SmeltingResearchPageEntry.class, "smelting")
                .registerSubtype(TextResearchPageEntry.class, "text");
            _researchGson = new GsonBuilder().registerTypeAdapterFactory(typeFactory)
                .setPrettyPrinting()
                .create();
        }
        return _researchGson;
    }

    private static FakePlayer knowItAll;

    /**
     * A fake player whose entire purpose is to know all Thaumcraft research (created to support
     * {@link dev.rndmorris.salisarcana.common.commands.UpgradeFocusCommand}.
     *
     * @return The fake player.
     */
    public static FakePlayer knowItAll() {
        if (knowItAll == null) {
            knowItAll = new FakePlayer(
                MinecraftServer.getServer()
                    .worldServerForDimension(0),
                new GameProfile(null, SalisArcana.MODID + ":KnowItAll"));
            for (var category : ResearchCategories.researchCategories.values()) {
                for (var researchItem : category.research.values()) {
                    if (ResearchManager.isResearchComplete(knowItAll.getCommandSenderName(), researchItem.key)) {
                        continue;
                    }
                    Thaumcraft.proxy.getResearchManager()
                        .completeResearch(knowItAll, researchItem.key);
                }
            }
        }
        return knowItAll;
    }

    /**
     * Reset the fake player, to be rebuilt later.
     */
    public static void resetKnowItAll() {
        knowItAll = null;
    }

    public static boolean matchesTerm(ResearchItem research, String searchTerm) {
        searchTerm = searchTerm.toLowerCase();
        return research.key.toLowerCase()
            .contains(searchTerm)
            || research.getName()
                .toLowerCase()
                .contains(searchTerm);
    }

    public static Collection<IChatComponent> printResearchToChat(Predicate<ResearchItem> filter) {
        final var results = new ArrayList<IChatComponent>();

        for (var entry : ResearchCategories.researchCategories.entrySet()) {
            final var category = entry.getValue();

            IChatComponent researchMessage = new ChatComponentText("");

            final var research$ = category.research.values()
                .iterator();
            var anyInCategory = false;

            while (research$.hasNext()) {
                final var research = research$.next();
                if (!filter.test(research)) {
                    continue;
                }
                anyInCategory = true;
                final var item = formatResearch(research);

                researchMessage.appendSibling(item);
                if (research$.hasNext()) {
                    researchMessage.appendText(", ");
                }
            }

            if (anyInCategory) {
                results.add(categoryMessage(entry.getKey()));
                results.add(researchMessage);
            }
        }

        return results;
    }

    private static IChatComponent categoryMessage(String categoryKey) {
        final var style = new ChatStyle();
        style.setBold(true);
        style.setColor(EnumChatFormatting.BLUE);
        style.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(categoryKey)));

        final var headerMessage = new ChatComponentTranslation(
            "salisarcana:command.category_header",
            ResearchCategories.getCategoryName(categoryKey));
        headerMessage.setChatStyle(style);

        return headerMessage;
    }

    public static IChatComponent formatResearch(ResearchItem research) {
        return formatResearch(research, EnumChatFormatting.DARK_PURPLE);
    }

    public static IChatComponent formatResearch(ResearchItem research, EnumChatFormatting formatting) {
        final var researchNameKey = research instanceof IResearchItemExtended extended
            ? extended.getNameTranslationKey()
            : String.format("tc.research_name.%s", research.key);

        final var style = new ChatStyle().setColor(formatting)
            .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(research.key)));
        return new ChatComponentText("[").setChatStyle(style)
            .appendSibling(new ChatComponentTranslation(researchNameKey))
            .appendText("]");
    }

    public static IChatComponent formatResearchClickCommand(ResearchItem research) {
        var result = formatResearch(research);
        result.getChatStyle()
            .setChatClickEvent(suggestResearchCommandOnClick(research));
        return result;
    }

    public static IChatComponent formatResearchClickCommand(ResearchItem research, EnumChatFormatting formatting) {
        var result = formatResearch(research, formatting);
        result.getChatStyle()
            .setChatClickEvent(suggestResearchCommandOnClick(research));
        return result;
    }

    public static ClickEvent suggestResearchCommandOnClick(ResearchItem research) {
        if (commands.prerequisites.isEnabled()
            && commands.prerequisites.getCommand() instanceof PrerequisitesCommand command) {
            return new ClickEvent(
                ClickEvent.Action.SUGGEST_COMMAND,
                String.format("/%s --research %s", command.getCommandName(), research.key));
        }
        return null;
    }

    public static void completeResearchClient(EntityPlayer player, String research) {
        if (!player.worldObj.isRemote) {
            return;
        }
        PacketHandler.INSTANCE.sendToServer(
            new PacketPlayerCompleteToServer(
                research,
                player.getCommandSenderName(),
                player.worldObj.provider.dimensionId,
                (byte) 0));
    }

    public static void exportResearchToJson(@Nonnull ResearchEntry research, File dumpTo) throws IOException {
        try (FileWriter writer = new FileWriter(dumpTo)) {
            researchGson().toJson(research, writer);
        }
    }

    public static ResearchEntry importResearchFromJson(String json) throws JsonSyntaxException {
        return researchGson().fromJson(json, ResearchEntry.class);
    }

    public static ResearchEntry importResearchFromJson(@Nonnull File file) throws IOException {
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        try (FileReader reader = new FileReader(file)) {
            return researchGson().fromJson(reader, ResearchEntry.class);
        }
    }

    public static boolean registerCustomResearch(ResearchEntry research) {
        if (research.getKey() == null || research.getKey()
            .isEmpty()) {
            LOG.error("Research entry missing key.");
            return false;
        } else if (research.getCategory() == null || research.getCategory()
            .isEmpty() || ResearchCategories.getResearchList(research.getCategory()) == null) {
                LOG.error("Research entry {} category missing or invalid.", research.getKey());
                return false;
            }
        switch (research.getType()) {
            case "create" -> {
                LOG.info("Registering custom research: {}", research.getKey());
                ResearchItem newResearch = new ResearchItem(research.getKey(), research.getCategory());
                research.updateResearchItem(newResearch);
                ResearchCategoryList categoryList = ResearchCategories.getResearchList(research.getCategory());
                categoryList.research.put(research.getKey(), newResearch);
                newResearch.registerResearchItem();
                return true;
            }
            case "replace" -> {
                LOG.info("Replacing research: {}", research.getKey());
                ResearchItem original = ResearchCategories.getResearch(research.getKey());
                String originalCategory = original.category;
                if (!originalCategory.equals(research.getCategory())) {
                    ResearchCategoryList originalList = ResearchCategories.getResearchList(originalCategory);
                    originalList.research.remove(research.getKey());
                    ResearchCategoryList newList = ResearchCategories.getResearchList(research.getCategory());
                    newList.research.put(research.getKey(), original);
                }
                research.updateResearchItem(original);
                return true;
            }
            case "update" -> {
                LOG.info("Updating research: {}", research.getKey());
                ResearchItem original = ResearchCategories.getResearch(research.getKey());
                research.updateResearchItem(original);
                return true;
            }
            default -> {
                LOG.error("Research entry {} has invalid type: {}", research.getKey(), research.getType());
                return false;
            }
        }
    }

    public static boolean registerCustomResearch(String json) {
        return registerCustomResearch(importResearchFromJson(json));
    }
}
