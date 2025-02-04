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

import dev.expx.minestominvs.util.mm
import net.kyori.adventure.text.Component
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.item.ItemComponent
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import java.util.function.Consumer

/**
 * Represents an icon for a GUI item with customizable properties and actions.
 */
@Suppress("TooManyFunctions", "MagicNumber")
class Icon : GuiIcon {

    override var item: ItemStack
    override var clickAction: Consumer<InventoryPreClickEvent>
    override var noPermAction: Consumer<InventoryPreClickEvent>
    override var isStealable: Boolean = false
    override var permission: String? = null

    /**
     * Creates an Icon from an existing ItemStack.
     *
     * @param item The item to be represented by the Icon.
     */
    constructor(item: ItemStack) {
        this.item = item
        this.clickAction = Consumer { _ -> } // Default no-op click action
        this.noPermAction = Consumer { s ->
            s.player.sendMessage("<red>You do not have permission.".mm())
        } // Default no-op no permission action
    }

    /**
     * Creates an Icon from a Material, creating a default ItemStack.
     *
     * @param material The material of the item.
     */
    constructor(material: Material) {
        this.item = ItemStack.of(material)
        this.clickAction = Consumer { _ -> } // Default no-op click action
        this.noPermAction = Consumer { s ->
            s.player.sendMessage("<red>You do not have permission.".mm())
        } // Default no-op no permission action
    }

    // ------------------------- Icon Customization -------------------------

    /**
     * Sets whether the icon is stealable or not.
     *
     * @param stealable Whether the icon can be stolen.
     * @return The current Icon instance for chaining.
     */
    fun setStealable(stealable: Boolean): Icon {
        isStealable = stealable
        return this
    }

    /**
     * Sets the permission required to interact with this icon.
     *
     * @param perm The permission string.
     * @return The current Icon instance for chaining.
     */
    fun setPermission(perm: String): Icon {
        permission = perm
        return this
    }

    /**
     * Sets the durability of the item.
     *
     * @param durability The durability of the item.
     * @return The current Icon instance for chaining.
     */
    fun setDurability(durability: Int): Icon {
        item = item.with(ItemComponent.DAMAGE, durability)
        return this
    }

    /**
     * Sets the display name of the item.
     *
     * @param name The name to display on the item.
     * @return The current Icon instance for chaining.
     */
    fun setName(name: Component): Icon {
        item = item.withCustomName(name)
        return this
    }

    /**
     * Sets the lore of the item.
     *
     * @param lore The lore to display with the item.
     * @return The current Icon instance for chaining.
     */
    fun setLore(lore: List<Component>): Icon {
        item = item.withLore(lore)
        return this
    }

    /**
     * Appends additional lore to the existing lore of the item.
     *
     * @param lore The lore to append.
     * @return The current Icon instance for chaining.
     */
    fun appendLore(lore: List<Component>): Icon {
        val existingLore = item.get(ItemComponent.LORE) ?: emptyList()
        item = item.withLore(existingLore + lore)
        return this
    }

    /**
     * Sets the amount of the item stack.
     *
     * @param amount The number of items in the stack.
     * @return The current Icon instance for chaining.
     */
    fun setAmount(amount: Int): Icon {
        item = item.withAmount(amount)
        return this
    }

    // ------------------------- Click Handling -------------------------

    /**
     * Gets the action to perform when the icon is clicked.
     *
     * @return The click event consumer.
     */
    fun getClickEvent(): Consumer<InventoryPreClickEvent> {
        return this.clickAction
    }

    /**
     * Set the action to perform when the icon is clicked.
     *
     * @param event The action to perform on click.
     * @return The current Icon instance for chaining.
     */
    fun onClick(event: Consumer<InventoryPreClickEvent>): Icon {
        this.clickAction = event
        return this
    }

    /**
     * Gets the action to perform when the player does not have permission to click the icon.
     *
     * @return The no permission event consumer.
     */
    fun getNoPermissionEvent(): Consumer<InventoryPreClickEvent> {
        return this.noPermAction
    }

    /**
     * Set the action to perform when the player does not have permission to click the icon.
     *
     * @param event The action to perform when the player does not have permission.
     * @return The current Icon instance for chaining.
     */
    fun onNoPermission(event: Consumer<InventoryPreClickEvent>): Icon {
        this.noPermAction = event
        return this
    }

    // ------------------------- Icon Retrieval -------------------------

    /**
     * Retrieves the raw item that represents the icon.
     *
     * @return The ItemStack of the icon.
     */
    fun getIcon(): ItemStack {
        return this.item
    }

    /**
     * Allows for advanced item manipulation by setting a custom ItemStack.
     *
     * @param item The new ItemStack to represent the icon.
     * @return The current Icon instance for chaining.
     */
    fun setIcon(item: ItemStack): Icon {
        this.item = item
        return this
    }
}
