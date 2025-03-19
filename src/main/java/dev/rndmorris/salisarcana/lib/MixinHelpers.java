package dev.rndmorris.salisarcana.lib;

import net.glease.tc4tweak.api.BrowserPagingAPI;
import net.glease.tc4tweak.api.TC4TweaksAPI;

import thaumcraft.client.gui.GuiResearchBrowser;

public class MixinHelpers {

    private static final BrowserPagingAPI browserPaging = TC4TweaksAPI.getBrowserPagingAPI();

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
}
