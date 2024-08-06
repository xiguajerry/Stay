/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.loader;

import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Author B_312
 */
public class ClassHelper {

    private static Map<String, byte[]> resourceCache;
    private static final Logger logger = LogManager.getLogger("Class Helper");

    public static boolean inject(InputStream inputStream) {
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        return use(zipInputStream);
    }

    @SuppressWarnings("unchecked")
    private static boolean use(ZipInputStream zipInputStream) {
        //Get resource cache
        if (!runSafe("Can't get the resource cache", () -> {
            Field field = LaunchClassLoader.class.getDeclaredField("resourceCache");
            field.setAccessible(true);
            resourceCache = (Map<String, byte[]>) field.get(Launch.classLoader);
            return true;
        })) return false;

        //Inject InputStream
        return runSafe("Can't inject to resource cache", () -> {
            ZipEntry zipEntry;
            while (true) {
                zipEntry = zipInputStream.getNextEntry();
                if (zipEntry == null) break;
                String name = zipEntry.getName();
                if (name.endsWith(".class")) {
                    name = name.substring(0, name.length() - 6);
                    name = name.replace('/', '.');
                    resourceCache.put(name, readBytes(zipInputStream));
                }
            }
            return true;
        });
    }

    private static boolean runSafe(String message, SafeTask task) {
        try {
            return task.invoke();
        } catch (Exception exception) {
            exception.printStackTrace();
            logger.error(message);
        }
        return false;
    }

    private static byte[] readBytes(InputStream input) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(Math.max(8 * 1024, input.available()));
        copyTo(input, buffer);
        return buffer.toByteArray();
    }

    private static void copyTo(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[8 * 1024];
        int bytes = in.read(buffer);
        while (bytes >= 0) {
            out.write(buffer, 0, bytes);
            bytes = in.read(buffer);
        }
    }

    private interface SafeTask {
        boolean invoke() throws Exception;
    }

}