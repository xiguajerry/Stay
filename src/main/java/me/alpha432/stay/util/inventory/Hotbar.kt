package me.alpha432.stay.util.inventory

import anonymous.team.eventsystem.SafeClientEvent
import me.alpha432.stay.util.extension.firstBlock
import me.alpha432.stay.util.extension.firstItem
import me.alpha432.stay.util.inventory.slot.HotbarSlot
import me.alpha432.stay.util.inventory.slot.hotbarSlots
import me.alpha432.stay.util.inventory.slot.storageSlots
import net.minecraft.block.Block
import net.minecraft.inventory.Slot
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import java.util.function.Predicate

/**
 * Try to swap selected hotbar slot to [B] that matches with [predicate]
 */
inline fun <reified B : Block> SafeClientEvent.swapToBlock(predicate: Predicate<ItemStack>? = null): Boolean {
    return player.hotbarSlots.firstBlock<B, HotbarSlot>(predicate)?.let {
        swapToSlot(it)
        true
    } ?: false
}

/**
 * Try to swap selected hotbar slot to [block] that matches with [predicate]
 */
fun SafeClientEvent.swapToBlock(block: Block, predicate: Predicate<ItemStack>? = null): Boolean {
    return player.hotbarSlots.firstBlock(block, predicate)?.let {
        swapToSlot(it)
        true
    } ?: false
}

/**
 * Try to swap selected hotbar slot to [I] that matches with [predicate]
 */
inline fun <reified I : Item> SafeClientEvent.swapToItem(predicate: Predicate<ItemStack>? = null): Boolean {
    return player.hotbarSlots.firstItem<I, HotbarSlot>(predicate)?.let {
        swapToSlot(it)
        true
    } ?: false
}

/**
 * Try to swap selected hotbar slot to [item] that matches with [predicate]
 */
fun SafeClientEvent.swapToItem(item: Item, predicate: Predicate<ItemStack>? = null): Boolean {
    return player.hotbarSlots.firstItem(item, predicate)?.let {
        swapToSlot(it)
        true
    } ?: false
}

/**
 * Swap the selected hotbar slot to [hotbarSlot]
 */
fun SafeClientEvent.swapToSlot(hotbarSlot: HotbarSlot) {
    swapToSlot(hotbarSlot.hotbarSlot)
}

/**
 * Swap the selected hotbar slot to [slot]
 */
fun SafeClientEvent.swapToSlot(slot: Int) {
    if (slot !in 0..8) return
    player.inventory.currentItem = slot
    playerController::class.java.getMethod("syncCurrentPlayItem").also { it.isAccessible = true }.invoke(playerController)
}