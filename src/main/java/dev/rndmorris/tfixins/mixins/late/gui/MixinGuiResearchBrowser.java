package dev.rndmorris.tfixins.mixins.late.gui;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import dev.rndmorris.tfixins.config.FixinsConfig;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.common.lib.research.ResearchManager;

@Mixin(GuiResearchBrowser.class)
public class MixinGuiResearchBrowser extends GuiScreen {

    @Shadow(remap = false)
    private static String selectedCategory = null;

    @Shadow(remap = false)
    private String player;

    @Unique
    private int tf$lastDir = 0;

    @Override
    public void handleInput() {
        super.handleInput();
        if (FixinsConfig.researchBrowserModule.scrollwheelEnabled.isEnabled()) {
            // We need to run this every time to avoid buffering a scroll
            int dir = (int) Math.signum(Mouse.getDWheel()); // We want DWheel since last call, not last mouse event, as
                                                            // it's possible no new mouse events will have been sent
            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                if (dir != tf$lastDir) {
                    tf$lastDir = dir;
                    ArrayList<String> categories = new ArrayList<>();
                    for (String category : ResearchCategories.researchCategories.keySet()) {
                        if (category.equals("ELDRITCH")
                            && !ResearchManager.isResearchComplete(this.player, "ELDRITCHMINOR")) {
                            continue;
                        }
                        categories.add(category);
                    }

                    if (FixinsConfig.researchBrowserModule.invertedScrolling.isEnabled()) {
                        dir *= -1;
                    }

                    int new_index = (categories.indexOf(selectedCategory) + dir) % categories.size();
                    if (new_index < 0) {
                        new_index += categories.size();
                    }
                    selectedCategory = categories.get(new_index);
                }
            }
        }
    }
}
