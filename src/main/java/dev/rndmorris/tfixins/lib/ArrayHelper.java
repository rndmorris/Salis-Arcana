package dev.rndmorris.tfixins.lib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ArrayHelper {

    @Nonnull
    public static <T> List<T> toList(@Nullable T[] arr) {

        return toList(arr, ArrayList::new);
    }

    @Nonnull
    public static <T> List<T> toList(@Nullable T[] arr, Supplier<List<T>> listType) {
        final var result = listType.get();
        if (arr != null) {
            Collections.addAll(result, arr);
        }
        return result;
    }

}
