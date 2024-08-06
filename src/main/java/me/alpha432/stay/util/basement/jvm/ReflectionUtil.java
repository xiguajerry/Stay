
/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/9 下午8:17
 */

package me.alpha432.stay.util.basement.jvm;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.Objects;

public class ReflectionUtil {
    public static <F, T extends F> void copyOf(F from, T to, boolean ignoreFinal) throws NoSuchFieldException, IllegalAccessException {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        Class<?> clazz = from.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            ReflectionUtil.makePublic(field);
            if (ReflectionUtil.isStatic(field) || ignoreFinal && ReflectionUtil.isFinal(field)) continue;
            ReflectionUtil.makeMutable(field);
            field.set(to, field.get(from));
        }
    }

    public static <F, T extends F> void copyOf(F from, T to) throws NoSuchFieldException, IllegalAccessException {
        ReflectionUtil.copyOf(from, to, false);
    }

    public static boolean isStatic(Member instance) {
        return (instance.getModifiers() & 8) != 0;
    }

    public static boolean isFinal(Member instance) {
        return (instance.getModifiers() & 0x10) != 0;
    }

    public static void makeAccessible(AccessibleObject instance, boolean accessible) {
        Objects.requireNonNull(instance);
        instance.setAccessible(accessible);
    }

    public static void makePublic(AccessibleObject instance) {
        ReflectionUtil.makeAccessible(instance, true);
    }

    public static void makePrivate(AccessibleObject instance) {
        ReflectionUtil.makeAccessible(instance, false);
    }

    public static void makeMutable(Member instance) throws NoSuchFieldException, IllegalAccessException {
        Objects.requireNonNull(instance);
        Field modifiers = Field.class.getDeclaredField("modifiers");
        ReflectionUtil.makePublic(modifiers);
        modifiers.setInt(instance, instance.getModifiers() & 0xFFFFFFEF);
    }

    public static void makeImmutable(Member instance) throws NoSuchFieldException, IllegalAccessException {
        Objects.requireNonNull(instance);
        Field modifiers = Field.class.getDeclaredField("modifiers");
        ReflectionUtil.makePublic(modifiers);
        modifiers.setInt(instance, instance.getModifiers() & 0x10);
    }
}

