package dev.rndmorris.salisarcana.client.handlers;

import net.minecraft.client.gui.inventory.GuiContainer;

import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.guihook.IContainerInputHandler;
import cpw.mods.fml.common.Optional;
import dev.rndmorris.salisarcana.client.QuickStashFocus;
import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.mixins.early.accessor.AccessorGuiContainer;

@Optional.Interface(iface = "codechicken.nei.guihook.IContainerInputHandler", modid = "NotEnoughItems")
public final class SalisContainerInputHandler implements IContainerInputHandler {

    public static final SalisContainerInputHandler INSTANCE = new SalisContainerInputHandler();

    @Optional.Method(modid = "NotEnoughItems")
    public static void register() {
        GuiContainerManager.addInputHandler(SalisContainerInputHandler.INSTANCE);
    }

    private SalisContainerInputHandler() {}

    @Override
    public boolean keyTyped(GuiContainer gui, char keyChar, int keyCode) {
        return false;
    }

    @Override
    public void onKeyTyped(GuiContainer gui, char keyChar, int keyID) {}

    @Override
    public boolean lastKeyTyped(GuiContainer gui, char keyChar, int keyID) {
        if (SalisConfig.features.quickStashFocus.isEnabled()) {
            if (keyID == QuickStashFocus.KEYBIND.getKeyCode()) {
                QuickStashFocus.tryStashSlot(((AccessorGuiContainer) gui).getHoveredSlot(), gui.inventorySlots);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean mouseClicked(GuiContainer gui, int mousex, int mousey, int button) {
        return false;
    }

    @Override
    public void onMouseClicked(GuiContainer gui, int mousex, int mousey, int button) {}

    @Override
    public void onMouseUp(GuiContainer gui, int mousex, int mousey, int button) {}

    @Override
    public boolean mouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {
        return false;
    }

    @Override
    public void onMouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {}

    @Override
    public void onMouseDragged(GuiContainer gui, int mousex, int mousey, int button, long heldTime) {}
}
