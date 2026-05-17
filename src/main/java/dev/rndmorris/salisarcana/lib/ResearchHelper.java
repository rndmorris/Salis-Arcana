package dev.rndmorris.salisarcana.lib;

import static dev.rndmorris.salisarcana.config.SalisConfig.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.FakePlayer;

import dev.rndmorris.salisarcana.api.IResearchItemExtended;
import dev.rndmorris.salisarcana.common.commands.PrerequisitesCommand;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketPlayerCompleteToServer;
import thaumcraft.common.lib.research.ScanManager;

public class ResearchHelper {

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

            boolean anyInCategory = false;
            for (final var research : category.research.values()) {
                if (!filter.test(research)) {
                    continue;
                }

                if (anyInCategory) {
                    researchMessage.appendText(", ");
                }

                anyInCategory = true;
                researchMessage.appendSibling(formatResearch(research));
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

    public static void sendResearchError(EntityPlayer player, String researchKey, String translationKey) {
        if (player instanceof EntityPlayerMP playerMP && !(player instanceof FakePlayer)) {
            final var research = ResearchCategories.getResearch(researchKey);

            IChatComponent researchName;
            if (research instanceof IResearchItemExtended extended) {
                researchName = new ChatComponentTranslation(extended.getNameTranslationKey());
            } else {
                researchName = new ChatComponentText(research.getName());
            }

            final var message = new ChatComponentTranslation(
                translationKey,
                researchName,
                new ChatComponentTranslation("tc.research_category." + research.category));
            message.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED));
            playerMP.addChatMessage(message);
        }
    }

    public static IChatComponent formatResearch(ResearchItem research) {
        return formatResearch(research, EnumChatFormatting.DARK_PURPLE);
    }

    public static IChatComponent formatResearch(ResearchItem research, EnumChatFormatting formatting) {
        final var researchKeyName = research instanceof IResearchItemExtended extended
            ? extended.getNameTranslationKey()
            : String.format("tc.research_name.%s", research.key);

        final var style = new ChatStyle().setColor(formatting)
            .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(research.key)));
        return new ChatComponentText("[").setChatStyle(style)
            .appendSibling(new ChatComponentTranslation(researchKeyName))
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

    public static boolean isItemScanned(EntityPlayer player, ItemStack stack, String prefix) {
        Item item = stack.getItem();
        int meta = stack.getItemDamage();
        Map<String, ArrayList<String>> scannedObjects = Thaumcraft.proxy.getScannedObjects();
        ArrayList<String> scanned = scannedObjects.get(player.getCommandSenderName());
        if (scanned == null) {
            return false;
        }
        String hash = prefix + ScanManager.generateItemHash(item, meta);
        return scanned.contains(hash);
    }

    public static boolean hasResearchAspects(String username, AspectList aspects) {
        final var playerAspects = Thaumcraft.proxy.playerKnowledge.getAspectsDiscovered(username);
        for (final var aspect : aspects.aspects.entrySet()) {
            if (playerAspects.getAmount(aspect.getKey()) < aspect.getValue()) {
                return false;
            }
        }
        return true;
    }
}
