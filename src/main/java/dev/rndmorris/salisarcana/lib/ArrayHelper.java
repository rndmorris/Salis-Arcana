package dev.rndmorris.salisarcana.lib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.bsideup.jabel.Desugar;

public class ArrayHelper {

    public static String[] appendToArray(String[] array, String newValue) {
        final var list = new ArrayList<String>();
        if (array != null) {
            Collections.addAll(list, array);
        }
        list.add(newValue);
        return list.toArray(new String[0]);
    }

    public static int indexOf(int[] array, int key) {
        for (var index = 0; index < array.length; ++index) {
            if (array[index] == key) {
                return index;
            }
        }
        return -1;
    }

    public static boolean tryAssign(boolean[] arr, int index, boolean value) {
        if (0 <= index && index < arr.length) {
            arr[index] = value;
            return true;
        }
        return false;
    }

    public static <E> TryGetResult<E> tryGet(E[] arr, int index) {
        if (0 <= index && index < arr.length) {
            return TryGetResult.success(arr[index]);
        }
        return TryGetResult.failure();
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

    @Desugar
    public record TryGetResult<E> (boolean success, E data) {

        public static <E> TryGetResult<E> failure() {
            return new TryGetResult<>(false, null);
        }

        public static <E> TryGetResult<E> success(E data) {
            return new TryGetResult<>(true, data);
        }
    }

}
