package dev.rndmorris.salisarcana.lib;

import java.util.Comparator;
import java.util.Objects;

public class ClassComparator implements Comparator<Class<?>> {

    @Override
    public int compare(Class<?> o1, Class<?> o2) {
        Objects.requireNonNull(o1);
        Objects.requireNonNull(o2);

        final var name1 = o1.getCanonicalName();
        final var name2 = o2.getCanonicalName();

        if (name1 == null && name2 == null) {
            return 0;
        }
        if (name1 == null) {
            return 1;
        }
        if (name2 == null) {
            return -1;
        }

        return name1.compareTo(name2);
    }
}
