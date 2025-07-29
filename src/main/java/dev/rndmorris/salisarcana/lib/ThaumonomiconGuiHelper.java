package dev.rndmorris.salisarcana.lib;

import java.util.Stack;

import net.glease.tc4tweak.api.BrowserPagingAPI;
import net.glease.tc4tweak.api.TC4TweaksAPI;
import net.minecraft.util.Tuple;

import dev.rndmorris.salisarcana.mixins.TargetedMod;
import thaumcraft.client.gui.GuiResearchBrowser;

public class ThaumonomiconGuiHelper {

    private static final BrowserPagingAPI browserPaging = getBrowserPaging();

    // we can't just static init it otherwise we get classnotfound without tc4tweak installed
    private static BrowserPagingAPI getBrowserPaging() {
        if (TargetedMod.TC4_TWEAKS.isLoaded()) {
            return TC4TweaksAPI.getBrowserPagingAPI();
        }
        return null;
    }

    public static java.util.Map<String, thaumcraft.api.research.ResearchCategoryList> BrowserPaging$GetTabsOnCurrentPage(
        GuiResearchBrowser browser, String player) {
        return browserPaging.getTabsOnCurrentPage(browser, player);
    }

    public static int BrowserPaging$GetTabsPerPage(GuiResearchBrowser browser) {
        return browserPaging.getTabsPerPage(browser);
    }

    public static void BrowserPaging$SetPage(GuiResearchBrowser browser, int page) {
        browserPaging.setPage(browser, page);
    }

    public static void BrowserPaging$NextPage(GuiResearchBrowser browser) {
        browserPaging.moveNextPage(browser);
    }

    public static int BrowserPaging$CurrentPageIndex(GuiResearchBrowser browser) {
        return browserPaging.getCurrentPage(browser);
    }

    public static int BrowserPaging$MaxPageIndex(GuiResearchBrowser browser) {
        return browserPaging.getTotalPages(browser);
    }

    // used client side only in MixinGuiResearchRecipe, MixinGuiResearchBrowser_RightClickClose
    public static final Stack<Tuple> RightClickClose$ScreenStack = new Stack<>();

}
