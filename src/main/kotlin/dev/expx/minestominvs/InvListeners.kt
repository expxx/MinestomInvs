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

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.inventory.InventoryCloseEvent
import net.minestom.server.event.inventory.InventoryOpenEvent
import net.minestom.server.event.inventory.InventoryPreClickEvent

/**
 * Handles global events for inventory interactions, such as opening, closing, and clicking within inventories.
 * This class listens to the inventory events and triggers the appropriate actions for each event.
 */
class InvListeners {

    init {
        val eventHandler: GlobalEventHandler = MinecraftServer.getGlobalEventHandler()

        // Listen for InventoryPreClickEvent to handle item clicks.
        eventHandler.addListener(InventoryPreClickEvent::class.java) { event ->
            onClick(event)
        }

        // Listen for InventoryCloseEvent to handle inventory close events.
        eventHandler.addListener(InventoryCloseEvent::class.java) { event ->
            onClose(event)
        }

        // Listen for InventoryOpenEvent to handle inventory open events.
        eventHandler.addListener(InventoryOpenEvent::class.java) { event ->
            onOpen(event)
        }
    }

    // ------------------------- Event Handlers -------------------------

    /**
     * Handles the event when a player clicks an item in the inventory.
     * It checks if the player has the necessary permission to interact with the item
     * and performs the click action associated with the item.
     *
     * @param event The inventory click event containing the player and the clicked item.
     */
    @Suppress("ReturnCount")
    private fun onClick(event: InventoryPreClickEvent) {
        val gui: Gui = getPlayersCurrentGui(event.player) ?: return
        val index = event.slot

        // Retrieve the icon (item) that was clicked in the GUI.
        val icon: GuiIcon = gui.getItems()[index] ?: return

        // Check if the player has the required permission to click the item.
        val permission: String? = icon.permission
        if (permission != null && !permissionHandler(event.player, permission)) {
            event.isCancelled = true // Cancel the click if the player doesn't have permission.
            icon.noPermAction.accept(event) // Trigger the no permission action.
            return
        }

        // Execute the custom click action associated with the icon.
        icon.clickAction.accept(event)

        // If the item is not stealable, cancel the event to prevent further action.
        if (!icon.isStealable) {
            event.isCancelled = true
        }
    }

    /**
     * Handles the event when a player closes an inventory.
     * It triggers the `onClose` callback for the GUI and marks it as closed.
     *
     * @param event The inventory close event containing the player and the inventory.
     */
    private fun onClose(event: InventoryCloseEvent) {
        val gui: Gui = getPlayersCurrentGui(event.player) ?: return
        gui.onClose(event) // Trigger custom close actions for the GUI.
        gui.setClosed(true) // Mark the GUI as closed.
        removeGui(event.player.uuid) // Remove the player's GUI from the active list.
    }

    /**
     * Handles the event when a player opens an inventory.
     * It triggers the `onOpen` callback for the GUI to perform any custom actions.
     *
     * @param event The inventory open event containing the player and the inventory.
     */
    private fun onOpen(event: InventoryOpenEvent) {
        val player: Player = event.player
        val gui: Gui = getPlayersCurrentGui(player) ?: return
        gui.onOpen(event) // Trigger custom open actions for the GUI.
    }
}
