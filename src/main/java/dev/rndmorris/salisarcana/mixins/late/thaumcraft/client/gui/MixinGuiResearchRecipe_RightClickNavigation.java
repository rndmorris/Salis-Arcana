package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.gui;

import static dev.rndmorris.salisarcana.lib.ThaumonomiconGuiHelper.RightClickClose$ScreenStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Tuple;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import dev.rndmorris.salisarcana.config.SalisConfig;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.client.gui.GuiResearchRecipe;

@Mixin(GuiResearchRecipe.class)
public class MixinGuiResearchRecipe_RightClickNavigation extends GuiScreen {

    @Shadow(remap = false)
    protected double guiMapX;

    @Shadow(remap = false)
    protected double guiMapY;

    @Shadow(remap = false)
    private int page;

    @Shadow(remap = false)
    private ResearchItem research;

    @WrapMethod(method = "initGui")
    private void onInit(Operation<Void> original) {
        RightClickClose$ScreenStack.push(new Tuple(research, page));
        original.call();
    }

    @WrapMethod(method = "mouseClicked")
    private void wrapMouseClicked(int mouseX, int mouseY, int button, Operation<Void> original) {
        if (button == 1) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            if ((RightClickClose$ScreenStack.size() <= 1)) {
                player.worldObj.playSound(player.posX, player.posY, player.posZ, "thaumcraft:page", 0.66F, 1.0F, false);
                // it can be zero in the case of opening directly to a page, where the page was originally the only
                // thing in the stack
                RightClickClose$ScreenStack.clear();
                if (this.page != 0) {
                    this.mc.displayGuiScreen(new GuiResearchRecipe(this.research, 0, this.guiMapX, this.guiMapY));
                    return;
                }
                this.mc.displayGuiScreen(new GuiResearchBrowser());
            } else {
                player.worldObj.playSound(player.posX, player.posY, player.posZ, "thaumcraft:page", 0.66F, 1.0F, false);
                Tuple item = RightClickClose$ScreenStack.pop(); // current screen
                if (this.page != 0) {
                    this.mc.displayGuiScreen(
                        new GuiResearchRecipe((ResearchItem) item.getFirst(), 0, this.guiMapX, this.guiMapY));
                    return;
                }
                item = RightClickClose$ScreenStack.pop(); // next screen
                this.mc.displayGuiScreen(
                    new GuiResearchRecipe(
                        (ResearchItem) item.getFirst(),
                        (int) item.getSecond(),
                        this.guiMapX,
                        this.guiMapY));
            }
            return;
        }

        original.call(mouseX, mouseY, button);
    }

    @WrapOperation(
        method = "keyTyped",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V"))
    private void onKeyTyped(Minecraft instance, GuiScreen i, Operation<Void> original) {
        if (SalisConfig.features.nomiconSavePage.isEnabled()) {
            RightClickClose$ScreenStack.pop(); // current screen at first page
            RightClickClose$ScreenStack.push(new Tuple(research, page)); // push the current page on the stack
            // just close the window entirely
            original.call(instance, null);
            return;
        }
        original.call(instance, i);
    }
}
