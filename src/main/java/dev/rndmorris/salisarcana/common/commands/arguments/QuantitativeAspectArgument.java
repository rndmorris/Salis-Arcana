package dev.rndmorris.salisarcana.common.commands.arguments;

import java.util.Comparator;

import thaumcraft.api.aspects.Aspect;

public class QuantitativeAspectArgument {

    public static final Comparator<QuantitativeAspectArgument> COMPARATOR = Comparator
        .comparing(q -> q.aspect.getTag());

    public final Aspect aspect;
    public final int amount;

    public QuantitativeAspectArgument(Aspect aspect, int amount) {
        this.aspect = aspect;
        this.amount = amount;
    }
}
