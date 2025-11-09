package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.gui;

import static dev.rndmorris.salisarcana.lib.ThaumonomiconGuiHelper.BrowserPaging$CurrentPageIndex;
import static dev.rndmorris.salisarcana.lib.ThaumonomiconGuiHelper.BrowserPaging$GetTabsOnCurrentPage;
import static dev.rndmorris.salisarcana.lib.ThaumonomiconGuiHelper.BrowserPaging$GetTabsPerPage;
import static dev.rndmorris.salisarcana.lib.ThaumonomiconGuiHelper.BrowserPaging$MaxPageIndex;
import static dev.rndmorris.salisarcana.lib.ThaumonomiconGuiHelper.BrowserPaging$NextPage;
import static dev.rndmorris.salisarcana.lib.ThaumonomiconGuiHelper.BrowserPaging$SetPage;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.lib.ResearchHelper;
import dev.rndmorris.salisarcana.mixins.TargetedMod;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.common.lib.research.ResearchManager;

@Mixin(value = GuiResearchBrowser.class)
public abstract class MixinGuiResearchBrowser_Creative_Scroll extends GuiScreen {

    @Shadow(remap = false)
    public abstract void updateResearch();

    @Shadow(remap = false)
    private static String selectedCategory = null;

    @Shadow(remap = false)
    private String player;

    @Shadow(remap = false)
    private ResearchItem currentHighlight = null;

    @Shadow(remap = false)
    public boolean hasScribestuff;

    @Unique
    private int sa$lastDir = 0;

    @Unique
    private static final boolean sa$opEnabled = SalisConfig.features.creativeOpThaumonomicon.isEnabled();

    @Unique
    private static final boolean sa$scrollEnabled = SalisConfig.features.nomiconScrollwheelEnabled.isEnabled();

    @Unique
    private static final int sa$invertScrolling = SalisConfig.features.nomiconInvertedScrolling.isEnabled() ? -1 : 1;

    @Unique
    private final GuiResearchBrowser sa$this = (GuiResearchBrowser) (Object) this;

    @Override
    public void handleInput() {
        super.handleInput();
        if (sa$scrollEnabled) {
            sa$CtrlScroll_handleInput();
        }
        if (sa$opEnabled) {
            sa$opThaumonomiconHandleInput();
        }
    }

    @Unique
    private void sa$CtrlScroll_handleInput() {
        // We need to run this every time to avoid buffering a scroll
        int dir = (int) Math.signum(Mouse.getDWheel()); // We want DWheel since last call, not last mouse event, as
        // it's possible no new mouse events will have been sent
        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            if (dir != sa$lastDir) {
                sa$lastDir = dir;
                dir *= sa$invertScrolling;
                if (TargetedMod.TC4_TWEAKS.isLoaded()) {
                    sa$handleTc4TweakScroll(dir);
                    return;
                }
                List<String> categories = sa$allCategories();
                selectedCategory = sa$GetNextCategory(dir, categories);
                this.updateResearch();
            }
        }
    }

    @Unique
    private void sa$handleTc4TweakScroll(int dir) {
        List<String> categories = new ArrayList<>(BrowserPaging$GetTabsOnCurrentPage(sa$this, this.player).keySet());
        String next = sa$GetNextCategory(dir, sa$allCategories());
        if (!SalisConfig.modCompat.tc4tweakScrollPages.isEnabled()) {
            if (categories.isEmpty()) return;

            // reset to first if current category is not found
            if (!categories.contains(next)) {
                if (dir == -1) {
                    selectedCategory = categories.get(BrowserPaging$GetTabsPerPage(sa$this) - 1);
                    this.updateResearch();
                    return;
                }
                selectedCategory = categories.get(0);
                this.updateResearch();
                return;
            }
            // where does a list of size 1 scroll to?
            if (categories.size() == 1) {
                return;
            }
            selectedCategory = next;
            this.updateResearch();
            return;
        }

        while (!categories.contains(next)) {
            int currentPageIndex = BrowserPaging$CurrentPageIndex(sa$this);
            int maxPageIndex = BrowserPaging$MaxPageIndex(sa$this);

            if (currentPageIndex == maxPageIndex) {
                BrowserPaging$SetPage(sa$this, 0);
            } else {
                BrowserPaging$NextPage(sa$this);
            }
            categories.clear();
            categories.addAll(BrowserPaging$GetTabsOnCurrentPage(sa$this, this.player).keySet());
        }
        selectedCategory = next;
        this.updateResearch();
        return;
    }

    @Unique
    private String sa$GetNextCategory(int dir, List<String> categories) {
        if (categories.size() <= 1) {
            return categories.get(0);
        }

        int new_index = (categories.indexOf(selectedCategory) + dir) % categories.size();
        if (new_index < 0) {
            new_index += categories.size();
        }
        return categories.get(new_index);
    }

    @Unique
    private ArrayList<String> sa$allCategories() {
        ArrayList<String> categories = new ArrayList<>();
        for (String category : ResearchCategories.researchCategories.keySet()) {
            if (category.equals("ELDRITCH") && !ResearchManager.isResearchComplete(this.player, "ELDRITCHMINOR")) {
                continue;
            }
            categories.add(category);
        }
        return categories;
    }

    @Unique
    private void sa$opThaumonomiconHandleInput() {
        if (currentHighlight == null) {
            return;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            if (Mouse.isButtonDown(0)) {
                EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                if (player.capabilities.isCreativeMode) {
                    String username = player.getCommandSenderName();
                    if (ResearchManager.doesPlayerHaveRequisites(username, currentHighlight.key)
                        && !ResearchManager.isResearchComplete(username, currentHighlight.key)) {
                        ResearchHelper.completeResearchClient(player, currentHighlight.key);
                    }
                }
            }
        }
    }

    @Inject(method = "updateResearch", at = @At("TAIL"), remap = false)
    private void creativePaperCheck(CallbackInfo ci) {
        if (this.mc.thePlayer.capabilities.isCreativeMode && sa$opEnabled) {
            this.hasScribestuff = !Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
        }
    }

}
