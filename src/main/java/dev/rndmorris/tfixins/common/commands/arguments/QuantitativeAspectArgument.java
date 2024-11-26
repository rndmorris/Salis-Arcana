package dev.rndmorris.tfixins.common.commands.arguments;

import thaumcraft.api.aspects.Aspect;

public class QuantitativeAspectArgument {

    public Aspect aspect;
    public int amount;

    public QuantitativeAspectArgument(Aspect aspect, int amount) {
        this.aspect = aspect;
        this.amount = amount;
    }
}
