package dev.rndmorris.salisarcana.lib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import scala.Tuple2;

public class ArrayHelper {

    public static boolean tryAssign(boolean[] arr, int index, boolean value) {
        if (0 <= index && index < arr.length) {
            arr[index] = value;
            return true;
        }
        return false;
    }

    public static <E> Tuple2<Boolean, E> tryGet(E[] arr, int index) {
        if (0 <= index && index < arr.length) {
            return Tuple2.apply(true, arr[index]);
        }
        return Tuple2.apply(false, null);
    }

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
