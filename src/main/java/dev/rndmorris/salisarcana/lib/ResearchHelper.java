package dev.rndmorris.salisarcana.lib;

import static dev.rndmorris.salisarcana.config.SalisConfig.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.FakePlayer;

import dev.rndmorris.salisarcana.api.IResearchItemExtended;
import dev.rndmorris.salisarcana.common.commands.PrerequisitesCommand;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketPlayerCompleteToServer;

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
}
