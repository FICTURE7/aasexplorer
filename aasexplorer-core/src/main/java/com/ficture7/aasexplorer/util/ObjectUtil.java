package com.ficture7.aasexplorer.util;

import java.lang.reflect.Modifier;

/**
 * Provides utility methods to valid objects.
 *
 * @author FICTURE7
 */
public final class ObjectUtil {

    /**
     * Constructor to restrict instantiation.
     */
    ObjectUtil() {
        // Space
    }

    /**
     * Checks if the specified value is null; if yes it throws an {@link NullPointerException}; otherwise
     * it returns the same specified value instance.
     *
     * @param value     Value to check.
     * @param valueName Name of the value.
     * @param <T>       Type of the value.
     * @return Same as value.
     * @throws NullPointerException {@code value} is null.
     */
    public static <T> T checkNotNull(T value, String valueName) {
        if (value == null) {
            throw clean(new NullPointerException(String.format("Argument '%s' cannot be null.", valueName)));
        }
        return value;
    }

    /**
     * Checks if the specified {@link Class} is an abstract class;
     * if yes it throws an {@link IllegalArgumentException}; otherwise it returns the same specified value instance.
     * <p>
     * <p>Note: the user takes responsibility to check if {@code value} is null.</p>
     *
     * @param value     Value to check.
     * @param valueName Name of the value.
     * @param <T>       Type of the value.
     * @return Same as value.
     * @throws IllegalArgumentException {@code value} is an abstract class.
     */
    public static <T> Class<T> checkNotAbstract(Class<T> value, String valueName) {
        if (Modifier.isAbstract(value.getModifiers())) {
            throw clean(new IllegalArgumentException(String.format("Argument '%s' cannot be an abstract class.", valueName)));
        }
        return value;
    }

    /**
     * Checks if the specified {@link Class} is an interface;
     * if yes it throws an {@link IllegalArgumentException}; otherwise it returns the same specified value instance.
     * <p>
     * <p>Note: the user takes responsibility to check if {@code value} is null.</p>
     *
     * @param value     Value to check.
     * @param valueName Name of the value.
     * @param <T>       Type of the value.
     * @return Same as value.
     * @throws IllegalArgumentException {@code value} is an interface.
     */
    public static <T> Class<T> checkNotInterface(Class<T> value, String valueName) {
        if (Modifier.isInterface(value.getModifiers())) {
            throw clean(new IllegalArgumentException(String.format("Argument '%s' cannot be an interface.", valueName)));
        }
        return value;
    }

    /**
     * Trims the last StackTraceElement from the specified {@link Exception} instance.
     */
    private static <T extends Exception> T clean(T instance) {
        StackTraceElement[] frames = instance.getStackTrace();
        StackTraceElement[] cleanedFrames = new StackTraceElement[frames.length - 1];
        System.arraycopy(frames, 1, cleanedFrames, 0, cleanedFrames.length);

        instance.setStackTrace(cleanedFrames);
        return instance;
    }
}

