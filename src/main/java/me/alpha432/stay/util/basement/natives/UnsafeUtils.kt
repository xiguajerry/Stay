/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/9 下午8:13
 */
@file:Suppress("deprecation", "unused", "nothing_to_inline")
package me.alpha432.stay.util.basement.natives

import com.google.common.annotations.Beta
import io.netty.util.internal.UnstableApi
import net.minecraft.client.Minecraft
import org.jetbrains.annotations.TestOnly
import sun.misc.Unsafe
import java.io.File
import java.io.FileInputStream

@RequiresOptIn("This feature is unsafe.")
annotation class UnsafeFeature

@Beta
@UnstableApi
@UnsafeFeature
@Deprecated("Unsafe API(s)")
internal abstract class UnsafeUtils @Beta @UnstableApi @TestOnly private constructor() {

    enum class ForceShutDown {
        ERROR, OOM, STACKOVERFLOW
    }

    companion object {
        var showReport = true
            private set

        val nativeUnsafe: Unsafe = Unsafe::class.java.getDeclaredField("theUnsafe").also { it.isAccessible = true }.get(null) as Unsafe

        @TestOnly
        fun forceReleaseMemory(clearMCStuff: Boolean = false) {
            if (clearMCStuff) {
                Minecraft.getMinecraft().freeMemory()
            } else {
                System.gc()
            }
        }

        @TestOnly
        fun forceReleaseMemory(offset: Long) {
            forceReleaseMemory0(offset)
        }

        @TestOnly
        private fun forceReleaseMemory0(long1: Long) = nativeUnsafe.freeMemory(long1)

        @TestOnly
        fun forceShutDown(forceShutDown: ForceShutDown) {
            when (forceShutDown) {
                ForceShutDown.OOM -> {
                    nativeUnsafe.allocateMemory(Long.MAX_VALUE)
                }
                ForceShutDown.STACKOVERFLOW -> {
                    makeStackOF0()
                }
                ForceShutDown.ERROR -> {
                    throw Error()
                }
            }
        }

        @TestOnly
        private fun makeStackOF0() {
            makeStackOF0()
        }

        @TestOnly
        @Suppress("unchecked_cast")
        fun<T: Any> forceCreateInstance(clazz: Class<T>): T {
            return nativeUnsafe.allocateInstance(clazz) as T
        }

        @TestOnly
        fun allocateMemory(long: Long) = nativeUnsafe.allocateMemory(long)

        @TestOnly
        fun reallocateMemory(long1: Long, long2: Long) = nativeUnsafe.reallocateMemory(long1, long2)

        @TestOnly
        fun setMemory(long1: Long, long2: Long, byte: Byte) = nativeUnsafe.setMemory(long1, long2, byte)

        @TestOnly
        fun copyMemory(obj1: Any, long1: Long, obj2: Any, long2: Long, long3: Long) = nativeUnsafe.copyMemory(obj1, long1, obj2, long2, long3)

        @TestOnly
        fun throwExceptionNoStackTrace(e: Throwable) = nativeUnsafe.throwException(e)

        @TestOnly
        fun<T: Any> getClassDynamic(file: File, classLoader: ClassLoader? = null): Class<*>? {
            val input = FileInputStream(file)
            val content = ByteArray(file.length().toInt())
            input.read(content)
            input.close()
            return nativeUnsafe.defineClass(
                null, content, 0, content.size, classLoader, null
            )
        }

        @Throws(Throwable::class)
        fun getAddress(obj: Any): Long {
            val unsafe: Unsafe = nativeUnsafe
            val array = arrayOf(obj)
            val baseOffset = unsafe.arrayBaseOffset(Array<Any>::class.java).toLong()
            val location: Long = when (val addressSize = unsafe.addressSize()) {
                4 -> unsafe.getInt(array, baseOffset).toLong()
                8 -> unsafe.getLong(array, baseOffset)
                else -> throw Error("unsupported address size: $addressSize")
            }
            return location
        }

        fun memoryShutdown() {
            showReport = false
            nativeUnsafe.putAddress(0, 0)
            this.setMemory(0, Long.MAX_VALUE, 0.toByte())
            Runtime.getRuntime().addShutdownHook(Thread { this.nativeUnsafe.setMemory(this@Companion.getAddress(this@Companion), nativeUnsafe.objectFieldOffset(this::class.java.getField("nativeUnsafe")), 0.toByte()) })
//            FMLCommonHandler.instance().exitJava(0, false)
            Runtime.getRuntime().halt(0)
        }
    }
}