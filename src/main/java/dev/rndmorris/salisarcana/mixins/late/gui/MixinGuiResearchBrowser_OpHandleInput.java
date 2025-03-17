package dev.rndmorris.salisarcana.mixins.late.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import dev.rndmorris.salisarcana.lib.ResearchHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.common.lib.research.PlayerKnowledge;
import thaumcraft.common.lib.research.ResearchManager;

// We have to set a higher priority here because in MixinGuiResearchBrowser_Scroll we Inject into handleInput,
// which only exists after we @Override it here.
@Mixin(value = GuiResearchBrowser.class, priority = 1001)
public abstract class MixinGuiResearchBrowser_OpHandleInput extends GuiScreen {

    @Shadow(remap = false)
    private ResearchItem currentHighlight = null;

    @Shadow(remap = false)
    public boolean hasScribestuff;

    @WrapOperation(
        method = { "mouseClicked", "genResearchBackground" },
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/lib/research/PlayerKnowledge;getAspectPoolFor(Ljava/lang/String;Lthaumcraft/api/aspects/Aspect;)S"),
        remap = false)
    private short creativeAspectPurchaseCheck(PlayerKnowledge instance, String username, Aspect aspect,
        Operation<Short> original) {
        if (this.mc.thePlayer.capabilities.isCreativeMode) {
            return Short.MAX_VALUE;
        }

        return original.call(instance, username, aspect);
    }

    @WrapOperation(
        method = "genResearchBackground",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/lib/research/PlayerKnowledge;hasDiscoveredAspect(Ljava/lang/String;Lthaumcraft/api/aspects/Aspect;)Z"),
        remap = false)
    private boolean creativeAspectDiscoveredCheck(PlayerKnowledge instance, String username, Aspect aspect,
        Operation<Boolean> original) {
        if (this.mc.thePlayer.capabilities.isCreativeMode) {
            return true;
        }

        return original.call(instance, username, aspect);
    }

    @Inject(
        method = "updateResearch",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/api/research/ResearchCategories;getResearchList(Ljava/lang/String;)Lthaumcraft/api/research/ResearchCategoryList;",
            ordinal = 0),
        remap = false)
    private void creativePaperCheck(CallbackInfo ci) {
        this.hasScribestuff = this.mc.thePlayer.capabilities.isCreativeMode;
    }

    @Override
    public void handleInput() {
        super.handleInput();
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
}
