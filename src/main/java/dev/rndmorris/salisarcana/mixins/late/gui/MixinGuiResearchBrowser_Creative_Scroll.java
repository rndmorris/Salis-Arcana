package dev.rndmorris.salisarcana.mixins.late.gui;

import java.util.ArrayList;

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

import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import dev.rndmorris.salisarcana.lib.ResearchHelper;
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
    private static final boolean sa$opEnabled = ConfigModuleRoot.enhancements.creativeOpThaumonomicon.isEnabled();

    @Unique
    private static final boolean sa$scrollEnabled = ConfigModuleRoot.enhancements.nomiconScrollwheelEnabled.isEnabled();

    @Unique
    private static final int sa$invertScrolling = ConfigModuleRoot.enhancements.nomiconInvertedScrolling.isEnabled()
        ? -1
        : 1;

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
                ArrayList<String> categories = new ArrayList<>();
                for (String category : ResearchCategories.researchCategories.keySet()) {
                    if (category.equals("ELDRITCH")
                        && !ResearchManager.isResearchComplete(this.player, "ELDRITCHMINOR")) {
                        continue;
                    }
                    categories.add(category);
                }

                dir *= sa$invertScrolling;

                int new_index = (categories.indexOf(selectedCategory) + dir) % categories.size();
                if (new_index < 0) {
                    new_index += categories.size();
                }
                selectedCategory = categories.get(new_index);
                this.updateResearch();
            }
        }

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
                    if (!ResearchManager.isResearchComplete(username, currentHighlight.key)) {
                        ResearchHelper.completeResearchClient(player, currentHighlight.key);
                    }
                }
            }
        }
    }

    @Inject(
        method = "updateResearch",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/api/research/ResearchCategories;getResearchList(Ljava/lang/String;)Lthaumcraft/api/research/ResearchCategoryList;",
            ordinal = 0),
        remap = false)
    private void creativePaperCheck(CallbackInfo ci) {
        this.hasScribestuff = this.mc.thePlayer.capabilities.isCreativeMode && sa$opEnabled;
    }

}
