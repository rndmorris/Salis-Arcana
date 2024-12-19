package dev.rndmorris.salisarcana.mixins.late.gui;

import java.util.Stack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Tuple;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import thaumcraft.api.research.ResearchItem;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.client.gui.GuiResearchRecipe;

@Mixin(GuiResearchRecipe.class)
public class MixinGuiResearchRecipe extends GuiScreen {

    @Shadow(remap = false)
    protected double guiMapX;
    @Shadow(remap = false)
    protected double guiMapY;
    @Shadow(remap = false)
    private int page;
    @Shadow(remap = false)
    private ResearchItem research;

    @Unique
    private static final Stack<Tuple> sa$screenStack = new Stack<>();

    @Inject(method = "<init>", at = @At(value = "TAIL"), remap = false)
    private void onInit(ResearchItem research, int page, double x, double y, CallbackInfo ci) {
        sa$screenStack.push(new Tuple(research, page));
    }

    @Inject(method = "mouseClicked", at = @At(value = "HEAD"), cancellable = true)
    private void onMouseClicked(int mouseX, int mouseY, int button, CallbackInfo ci) {
        if (button == 1) {
            if (sa$screenStack.size() == 1) {
                sa$screenStack.clear();
                this.mc.displayGuiScreen(new GuiResearchBrowser());
            } else {
                EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                sa$screenStack.pop(); // current screen
                Tuple item = sa$screenStack.pop();
                player.worldObj.playSound(player.posX, player.posY, player.posZ, "thaumcraft:page", 0.66F, 1.0F, false);
                this.mc.displayGuiScreen(
                    new GuiResearchRecipe(
                        (ResearchItem) item.getFirst(),
                        (int) item.getSecond(),
                        this.guiMapX,
                        this.guiMapY));
            }
            ci.cancel();
        } else {
            if ((int) sa$screenStack.peek()
                .getSecond() != this.page) {
                sa$screenStack.pop();
                sa$screenStack.push(new Tuple(this.research, this.page));
            }
        }
    }
}
