// R is licensed under the CC0 1.0 Universal License. View the original source at https://github.com/MrNavaStar/R

package dev.rndmorris.salisarcana.lib;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

public class R {

    private static boolean oldJavaCompat = false;
    static {
        try {
            Field.class.getDeclaredField("modifiers");
            oldJavaCompat = true;
        } catch (NoSuchFieldException ignore) {}
    }

    private final Object instance;
    private final Class<?> clazz;

    public R(Object instance) {
        this.instance = instance;
        clazz = instance.getClass();
    }

    public R(Class<?> clazz) {
        instance = null;
        this.clazz = clazz;
    }

    /**
     * Create an instance of {@link R}. Can be used for static or non-static actions
     */
    public static R of(Object instance) {
        return new R(instance);
    }

    /**
     * Create an instance of {@link R} that can only be used for static actions
     */
    public static R of(Class<?> clazz) {
        return new R(clazz);
    }

    /**
     * Create an instance of {@link R} from a field in another {@link R} instance
     */
    public R of(String name) {
        try {
            return R.of(findField(name, clazz).get(instance));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    // Search super classes for field
    private Field findField(String name, Class<?> clazz) throws NoSuchFieldException {
        if (clazz == null) throw new NoSuchFieldException();

        Field field;
        try {
            field = clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            field = findField(name, clazz.getSuperclass());
        }
        field.setAccessible(true);
        return field;
    }

    // Search super classes for methods
    private Method findMethod(String name, Class<?> clazz, Class<?>[] argTypes) throws NoSuchMethodException {
        if (clazz == null) throw new NoSuchMethodException();

        Method method;
        try {
            method = clazz.getDeclaredMethod(name, argTypes);
        } catch (NoSuchMethodException e) {
            method = findMethod(name, clazz.getSuperclass(), argTypes);
        }
        method.setAccessible(true);
        return method;
    }

    /**
     * Get the value of a field. Can be private or static
     */
    public <T> T get(String name, Class<T> type) {
        try {
            return type.cast(findField(name, clazz).get(instance));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Set the value of a field. Can be private, final, or static
     */
    public R set(String name, Object value) {
        try {
            Field toSet = findField(name, clazz);
            if (oldJavaCompat) {
                Field modifiersField = findField("modifiers", toSet.getClass());
                modifiersField.setInt(toSet, toSet.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
            }
            toSet.set(instance, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * Invoke a function with a return type
     */
    public <T> T call(String name, Class<T> returnType, Object... args) {
        try {
            Class<?>[] classes = Arrays.stream(args)
                .map(Object::getClass)
                .toArray(Class[]::new);
            Object returnVal = findMethod(name, clazz, classes).invoke(instance, args);
            if (returnVal == null || returnType == null) return null;
            return returnType.cast(returnVal);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Invoke a function with no return type
     */
    public R call(String name, Object... args) {
        call(name, null, args);
        return this;
    }

    /**
     * Get a list of the generic type params of a class
     */
    public Class<?>[] generics() {
        if (clazz.isEnum()) return new Class[] {}; // Enums cant have generics

        Type generic = clazz.getGenericSuperclass();
        if (generic instanceof ParameterizedType) {
            return Arrays.stream(((ParameterizedType) generic).getActualTypeArguments())
                .map(t -> {
                    try {
                        return Class.forName(t.getTypeName());
                    } catch (ClassNotFoundException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toArray(Class[]::new);
        }
        return new Class[] {};
    }
}
