package dev.rndmorris.salisarcana.lib;

import java.util.HashSet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;

public class EntityHelper {

    public static HashSet<Class<? extends Entity>> getEntitiesFromStringArr(String[] value) {
        HashSet<Class<? extends Entity>> entities = new HashSet<>();
        for (String entityName : value) {
            Class<? extends Entity> entity = EntityList.stringToClassMapping.get(entityName);
            if (entity != null) {
                entities.add(entity);
            }
        }
        return entities;
    }
}
