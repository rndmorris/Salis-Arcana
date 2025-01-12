package dev.rndmorris.salisarcana.lib;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class AspectHelper {

    private static Map<String, Aspect> aspectsCI = null;
    public static final Comparator<Aspect> COMPARATOR = Comparator.comparing(Aspect::getTag);

    /**
     * All aspects in a case-insensitive map.
     */
    public static Map<String, Aspect> aspectsCI() {
        if (aspectsCI == null) {
            aspectsCI = new TreeMap<>(String::compareToIgnoreCase);
            aspectsCI.putAll(Aspect.aspects);
        }
        return aspectsCI;
    }

    public static Set<Aspect> aspectsExcept(Collection<Aspect> except) {
        final var result = new TreeSet<>(COMPARATOR);

        result.addAll(Aspect.aspects.values());
        result.removeAll(except);

        return result;
    }

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
