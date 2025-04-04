package dev.rndmorris.salisarcana.lib.ifaces;

/**
 * Implemented in nodes when the RechargeTime mixin is applied.
 */
public interface IGameTimeNode {

    /**
     * Update the node's lastTickActive field to the current world total time.
     */
    void sa$updateLastTickActive();
}
