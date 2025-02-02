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

import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.item.ItemStack
import java.util.function.Consumer

/**
 * Represents a GUI icon with a click action and an item.
 */
interface GuiIcon {
    /**
     * The action to be performed when the icon is clicked.
     */
    val clickAction: Consumer<InventoryPreClickEvent>

    /**
     * The item associated with the icon.
     */
    val item: ItemStack

    /**
     * Whether the item can be taken from the inventory
     */
    val isStealable: Boolean

    /**
     * An optional permission to have for the icon to be used
     */
    val permission: String?
}
