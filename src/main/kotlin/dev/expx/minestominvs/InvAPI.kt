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

import net.minestom.server.entity.Player
import java.util.UUID

/**
 * Type alias for the permission handler function.
 * This function will be used to check if a player has a specific permission.
 *
 * @param player The player to check permission for.
 * @param permission The permission to check.
 * @return A boolean indicating if the player has the permission.
 */
typealias PermissionHandler = (player: Player, permission: String) -> Boolean

// Global variables for permission handler.
lateinit var permissionHandler: PermissionHandler

/**
 * Initializes the Inventory API with the specified permission handler and helper mode option.
 *
 * @param handler The permission handler to use. If `null`, the default handler (always returns true) will be used.
 */
fun initInventoryAPI(
    handler: PermissionHandler?
) {
    // If no handler is provided, use the default one that always returns true (no permission checks).
    permissionHandler = handler ?: { _, _ -> true }

    // Initialize event listeners for inventory interactions.
    InvListeners()
}

// Internal storage for managing the player's current GUIs.
internal val guis: HashMap<UUID, Gui> = HashMap()

/**
 * Retrieves the current GUI associated with a player.
 *
 * @param p The player whose current GUI to fetch.
 * @return The player's currently open GUI, or null if no GUI is open.
 */
internal fun getPlayersCurrentGui(p: Player): Gui? {
    return guis[p.uuid]
}

/**
 * Adds a GUI to the player's active list of GUIs.
 *
 * @param uuid The UUID of the player.
 * @param gui The GUI to associate with the player.
 */
internal fun addGui(uuid: UUID, gui: Gui) {
    guis[uuid] = gui
}

/**
 * Removes the GUI associated with the player from the active list.
 *
 * @param uuid The UUID of the player.
 */
internal fun removeGui(uuid: UUID) {
    guis.remove(uuid)
}
