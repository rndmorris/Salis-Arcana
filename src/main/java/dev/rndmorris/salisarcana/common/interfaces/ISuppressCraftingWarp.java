package dev.rndmorris.salisarcana.common.interfaces;

import javax.annotation.Nullable;

import thaumcraft.common.Thaumcraft;

public interface ISuppressCraftingWarp {

    /**
     * Returns this extensions instance, if it is enabled, otherwise {@code null}.
     */
    static @Nullable ISuppressCraftingWarp getInstance() {
        return Thaumcraft.instance.worldEventHandler instanceof ISuppressCraftingWarp extension ? extension : null;
    }

    /**
     * Skip giving warp the next time a crafting recipe that would give warp would give warp.
     * No effect if called anywhere except from the server thread.
     */
    void skipNextCraftingWarp();
}
