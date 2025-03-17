package dev.rndmorris.salisarcana.mixins.late.gui;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.common.lib.research.ResearchManager;

@Mixin(value = GuiResearchBrowser.class)
public abstract class MixinGuiResearchBrowser_Scroll extends GuiScreen {

    @Unique
    private final int sa$invertScroll = ConfigModuleRoot.enhancements.nomiconInvertedScrolling.isEnabled() ? -1 : 1;

    @Shadow(remap = false)
    public abstract void updateResearch();

    @Shadow(remap = false)
    private static String selectedCategory = null;

    @Shadow(remap = false)
    private String player;

    @Unique
    private int sa$lastDir = 0;

    // The method doesn't exist at compile time. In MixinGuiResearchBrowser_OpHandleInput, we @Override handleInput,
    // and it has a higher priority than this mixin, so it will exist here so we ignore the warnings.
    @SuppressWarnings({ "MixinAnnotationTarget", "UnresolvedMixinReference" })

    // We Inject(TAIL) here instead of WrapMethod just so we don't have to put the super call here, which would be
    // unintuitive.
    @Inject(method = "handleInput", at = @At(value = "TAIL"))
    public void handleInput(CallbackInfo ci) {

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
                dir *= sa$invertScroll;

                int new_index = (categories.indexOf(selectedCategory) + dir) % categories.size();
                if (new_index < 0) {
                    new_index += categories.size();
                }
                selectedCategory = categories.get(new_index);
                this.updateResearch();
            }
        }

    }
}
