package dev.rndmorris.salisarcana.lib.ifaces;

import javax.annotation.Nullable;

import thaumicenergistics.api.grid.IMEEssentiaMonitor;

public interface IAccessorTileProviderBase {

    @Nullable
    IMEEssentiaMonitor salisarcana$loadAndGetMonitor();
}
