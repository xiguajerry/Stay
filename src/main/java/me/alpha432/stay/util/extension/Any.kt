@file:Suppress("unused", "unused", "unused", "unused", "unused", "unused", "unused")

package me.alpha432.stay.util.extension

inline fun <reified T : Any> Any?.ifType(block: (T) -> Unit) {
    if (this is T) block(this)
}


