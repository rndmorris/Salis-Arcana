package dev.rndmorris.tfixins.lib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;

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

                final var item = researchTextItem(research);
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
            "tfixins:command.category_header",
            ResearchCategories.getCategoryName(categoryKey));
        headerMessage.setChatStyle(style);

        return headerMessage;
    }

    private static IChatComponent researchTextItem(ResearchItem research) {
        final var itemStyle = new ChatStyle().setColor(EnumChatFormatting.DARK_PURPLE)
            .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(research.key)));

        return new ChatComponentText("[").setChatStyle(itemStyle)
            .appendSibling(new ChatComponentText(research.getName()))
            .appendText("]");
    }

}
