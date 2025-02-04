/*
 * Copyright (C) 2025 Cam M.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 */
package dev.expx.minestominvs

import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.event.inventory.InventoryCloseEvent
import net.minestom.server.event.inventory.InventoryOpenEvent
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import org.jetbrains.annotations.NotNull
import java.util.*

/**
 * GUI representation for creating custom inventories.
 */
@Suppress("TooManyFunctions", "MagicNumber")
open class Gui {

    private val numberOfSlotsInARow = 9
    private var icons = HashMap<Int, GuiIcon>()
    private var id: String
    private var type: InventoryType
    private var inv: Inventory
    private var title: Component
    private var size: Int
    private var isClosed: Boolean = false

    /**
     * Creates a new Gui with a specified id, title, and row count.
     *
     * @param id The unique identifier for the GUI.
     * @param title The title to be displayed on the GUI.
     * @param rows The number of rows in the inventory (1-6).
     */
    constructor(@NotNull id: String, title: Component, @NotNull rows: Int) {
        this.icons = HashMap(rows * numberOfSlotsInARow)
        this.title = title
        this.id = id
        this.type = determineInventoryType(rows)
        this.size = type.size
        this.inv = createInventory(type, title)
    }

    /**
     * Creates a new Gui with a specified id, title, and custom inventory type.
     *
     * @param id The unique identifier for the GUI.
     * @param title The title to be displayed on the GUI.
     * @param type The InventoryType to be used for the GUI.
     */
    constructor(@NotNull id: String, title: Component, type: InventoryType) {
        this.icons = HashMap(type.size)
        this.size = type.size
        this.title = title
        this.id = id
        this.type = type
        this.inv = createInventory(type, title)
    }

    // ------------------------- Event Handlers -------------------------

    /**
     * Called when the inventory is opened.
     * This function is designed to be overridden by the end user.
     *
     * @param event The InventoryOpenEvent triggered when the inventory is opened.
     */
    open fun onOpen(event: InventoryOpenEvent) {
        // Event designed to be overridden by the end user.
    }

    /**
     * Called when the inventory is closed.
     * This function is designed to be overridden by the end user.
     *
     * @param event The InventoryCloseEvent triggered when the inventory is closed.
     */
    open fun onClose(event: InventoryCloseEvent) {
        // Event designed to be overridden by the end user.
    }

    // ------------------------- Opening GUI -------------------------

    /**
     * Opens the GUI for a list of players.
     *
     * @param players The players to open the GUI for.
     */
    fun open(players: Iterable<Player>) {
        players.forEach { player -> open(player) }
    }

    /**
     * Opens the GUI for a single player.
     *
     * @param player The player to open the GUI for.
     */
    fun open(player: Player) {
        addGui(player.uuid, this)
        player.openInventory(inv)
    }

    // ------------------------- Inventory Management -------------------------

    /**
     * Returns the first available empty slot in the inventory.
     *
     * @return The index of the first empty slot.
     * @throws IndexOutOfBoundsException If there are no empty slots.
     */
    private fun firstEmpty(): Int {
        for (i in 0 until size) {
            if (icons[i] == null) {
                return i
            }
        }
        throw IndexOutOfBoundsException("No empty slots available.")
    }

    /**
     * Adds an item to a specified slot in the inventory.
     *
     * @param slot The index of the slot.
     * @param icon The icon to be added.
     * @throws IndexOutOfBoundsException If the slot is outside the inventory size.
     */
    fun addItem(slot: Int, icon: GuiIcon) {
        validateSlot(slot)
        icons[slot] = icon
        inv.setItemStack(slot, icon.item)
    }

    fun removeItem(slot: Int) {
        validateSlot(slot)
        icons.remove(slot)
        inv.setItemStack(slot, ItemStack.of(Material.AIR))
    }

    /**
     * Fills the entire GUI with a specified icon.
     *
     * @param icon The icon to fill the GUI with.
     */
    fun fillGui(icon: GuiIcon) {
        for (slot in 0 until size) {
            addItem(slot, icon)
        }
    }

    /**
     * Fills the entire GUI except for certain blacklisted slots with a specified icon.
     *
     * @param icon The icon to fill the GUI with.
     * @param blacklisted A list of slots to exclude from filling.
     */
    fun fillGui(icon: GuiIcon, blacklisted: Iterable<Int>) {
        for (slot in 0 until size) {
            if (slot !in blacklisted) {
                addItem(slot, icon)
            }
        }
    }

    /**
     * Fills a specific row with an icon.
     *
     * @param icon The icon to fill the row with.
     * @param row The row index (0-based).
     */
    fun fillRow(icon: GuiIcon, row: Int) {
        for (i in 0 until numberOfSlotsInARow) {
            addItem((row * numberOfSlotsInARow + i), icon)
        }
    }

    /**
     * Fills a specific column with an icon.
     *
     * @param icon The icon to fill the column with.
     * @param column The column index (0-based).
     */
    fun fillColumn(icon: GuiIcon, column: Int) {
        for (i in 0 until size / numberOfSlotsInARow) {
            addItem((i * numberOfSlotsInARow + column), icon)
        }
    }

    /**
     * Adds an item to multiple slots.
     *
     * @param icon The icon to add.
     * @param slots A list of slot indices where the icon should be added.
     */
    fun addItem(icon: GuiIcon, slots: Iterable<Int>) {
        for (slot in slots) {
            addItem(slot, icon)
        }
    }

    /**
     * Adds an item to the first available empty slot in the inventory.
     *
     * @param icon The icon to add.
     * @throws IndexOutOfBoundsException If there are no empty slots available.
     */
    fun addItem(icon: GuiIcon) {
        addItem(firstEmpty(), icon)
    }

    /**
     * Retrieves all registered icons in the inventory.
     *
     * @return A map of all registered icons with their respective slot indices.
     */
    fun getItems(): HashMap<Int, GuiIcon> {
        return icons
    }

    /**
     * Retrieves the inventory object associated with this GUI.
     *
     * @return The inventory.
     */
    fun getInventory(): Inventory {
        return inv
    }

    /**
     * Sets the closed state of the GUI.
     *
     * @param value The state to set (true for closed, false for open).
     */
    fun setClosed(value: Boolean) {
        isClosed = value
    }

    // ------------------------- Helper Methods -------------------------

    /**
     * Determines the appropriate InventoryType based on the number of rows.
     *
     * @param rows The number of rows in the inventory.
     * @return The corresponding InventoryType.
     */
    private fun determineInventoryType(rows: Int): InventoryType {
        return when (rows) {
            1 -> InventoryType.CHEST_1_ROW
            2 -> InventoryType.CHEST_2_ROW
            3 -> InventoryType.CHEST_3_ROW
            4 -> InventoryType.CHEST_4_ROW
            5 -> InventoryType.CHEST_5_ROW
            6 -> InventoryType.CHEST_6_ROW
            else -> InventoryType.CHEST_6_ROW
        }
    }

    /**
     * Validates that the provided slot index is within the bounds of the inventory size.
     *
     * @param slot The slot index to validate.
     * @throws IndexOutOfBoundsException If the slot is out of bounds.
     */
    private fun validateSlot(slot: Int) {
        if (slot >= size) {
            throw IndexOutOfBoundsException("Slot cannot be bigger than inventory size!")
        }
    }

    /**
     * Creates a new inventory with a specified type and title.
     *
     * @param type The inventory type.
     * @param title The title of the inventory.
     * @return The newly created inventory.
     */
    private fun createInventory(type: InventoryType, title: Component): Inventory {
        return Inventory(type, title)
    }
}
