package dev.rndmorris.salisarcana.lib;

import net.glease.tc4tweak.modules.researchBrowser.BrowserPaging;

public class MixinHelpers {

    // all the BrowserPaging reflection is temporary until I get a PR to tc4tweaks
    private static final R browserPaging = new R(BrowserPaging.class);

    public static java.util.LinkedHashMap<String, thaumcraft.api.research.ResearchCategoryList> BrowserPaging$getTabsOnCurrentPage(
        String player) {
        return BrowserPaging.getTabsOnCurrentPage(player);
    }

    public static void BrowserPaging$SetPage(int page) {
        browserPaging.set("currentPageIndex", page);
        browserPaging.set("currentPageTabs", null);
    }

    public static void BrowserPaging$NextPage() {
        browserPaging.call("nextPage");
    }

    public static int BrowserPaging$CurrentPageIndex() {
        return browserPaging.get("currentPageIndex", Integer.class);
    }

    public static int BrowserPaging$MaxPageIndex() {
        return (int) browserPaging.get("maxPageIndex", Integer.class);
    }
}
