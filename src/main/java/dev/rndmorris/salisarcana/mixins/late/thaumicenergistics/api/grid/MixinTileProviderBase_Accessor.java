package dev.rndmorris.salisarcana.mixins.late.thaumicenergistics.api.grid;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import dev.rndmorris.salisarcana.lib.ifaces.IAccessorTileProviderBase;
import thaumicenergistics.api.grid.IMEEssentiaMonitor;
import thaumicenergistics.common.tiles.abstraction.TileProviderBase;

@Mixin(value = TileProviderBase.class, remap = false)
abstract class MixinTileProviderBase_Accessor implements IAccessorTileProviderBase {

    @Shadow
    protected IMEEssentiaMonitor monitor;

    @Shadow
    protected abstract boolean getEssentiaMonitor();

    @Override
    public IMEEssentiaMonitor salisarcana$loadAndGetMonitor() {
        getEssentiaMonitor();
        return monitor;
    }
}
