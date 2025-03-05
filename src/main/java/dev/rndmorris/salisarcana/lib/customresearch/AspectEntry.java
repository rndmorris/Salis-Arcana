package dev.rndmorris.salisarcana.lib.customresearch;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class AspectEntry {

    public String aspect;
    public int amount;

    public String getAspect() {
        return aspect;
    }

    public int getAmount() {
        return amount;
    }

    public static AspectList getAspects(AspectEntry[] aspects) {
        AspectList aspectList = new AspectList();
        for (AspectEntry entry : aspects) {
            aspectList.add(Aspect.getAspect(entry.getAspect()), entry.getAmount());
        }
        return aspectList;
    }

    public static AspectEntry[] fromAspectList(AspectList aspects) {

        return aspects.aspects.entrySet()
            .stream()
            .map(entry -> {
                var aspectEntry = new AspectEntry();
                aspectEntry.aspect = entry.getKey()
                    .getTag();
                aspectEntry.amount = entry.getValue();
                return aspectEntry;
            })
            .toArray(AspectEntry[]::new);
    }

}
