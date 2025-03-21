package dev.rndmorris.salisarcana.lib;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

public class MultiContainer extends Container {

    private final ArrayList<Container> multiContainer = new ArrayList<>(4);

    private MultiContainer(Container a, Container b) {
        multiContainer.add(a);
        multiContainer.add(b);
    }

    public static Container mergeContainers(final Container initial, final Container addition) {
        if (initial == null) {
            return addition;
        } else if (initial instanceof MultiContainer multi) {
            multi.multiContainer.add(addition);
            return multi;
        } else {
            return new MultiContainer(initial, addition);
        }
    }

    public static Container removeContainer(final Container initial, final Container removal) {
        if (initial == removal) {
            return null;
        } else if (initial instanceof MultiContainer multi) {
            multi.multiContainer.remove(removal);
            return multi.multiContainer.size() == 1 ? multi.multiContainer.get(0) : multi;
        } else {
            return initial;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return false;
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventory) {
        for (final Container container : multiContainer) {
            container.onCraftMatrixChanged(inventory);
        }
    }
}
