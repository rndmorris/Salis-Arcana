package dev.rndmorris.salisarcana.lib;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class AspectHelper {

    public static AspectList primalList(int amount) {
        return primalList(amount, amount, amount, amount, amount, amount);
    }

    public static AspectList primalList(int air, int earth, int fire, int water, int order, int entropy) {
        return new AspectList().add(Aspect.AIR, air)
            .add(Aspect.EARTH, earth)
            .add(Aspect.FIRE, fire)
            .add(Aspect.WATER, water)
            .add(Aspect.ORDER, order)
            .add(Aspect.ENTROPY, entropy);
    }

}
