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
package dev.expx.minestominvs.extension

import dev.expx.minestominvs.Gui
import dev.expx.minestominvs.Icon
import net.minestom.server.item.Material
import java.util.LinkedList

@Suppress("TooManyFunctions")
class PaginationManager {

    private val gui: Gui
    private val slots: LinkedList<Int> = LinkedList()
    private val items: LinkedList<Icon> = LinkedList()
    private var page: Int = 1

    constructor(gui: Gui) {
        this.gui = gui
    }

    fun getSlots(): LinkedList<Int> {
        return slots
    }

    fun registerPageSlots(slots: List<Int>) {
        this.slots.addAll(slots)
    }

    fun registerPageSlots(slots: Iterable<Int>) {
        this.slots.addAll(slots)
    }

    fun registerPageSlotsBetween(start: Int, end: Int) {
        for (i in start..end) {
            slots.add(i)
        }
    }

    fun unregisterPageSlots() {
        this.slots.clear()
    }

    fun getCurrentPage(): Int {
        return page
    }

    fun setPage(page: Int): PaginationManager {
        this.page = page
        return this
    }

    fun goNextPage(): PaginationManager {
        if (this.page >= getLastPage()) return this
        this.page++
        return this
    }

    fun goPreviousPage(): PaginationManager {
        if (this.page <= 0) return this
        this.page--
        return this
    }

    fun goFirstPage(): PaginationManager {
        this.page = 1
        return this
    }

    fun goLastPage(): PaginationManager {
        this.page = this.items.size
        return this
    }

    fun isLastPage(): Boolean {
        return this.page == getLastPage()
    }

    fun isFirstPage(): Boolean {
        return this.page == 1
    }

    fun getLastPage(): Int {
        if (this.slots.isEmpty() || this.items.isEmpty()) {
            return 0
        }
        val dev = Math.floor(this.items.size.toDouble() / this.slots.size.toDouble())
        return if (this.items.size % this.slots.size == 0) dev.toInt() - 1 else dev.toInt()
    }

    fun addItem(icon: Iterable<Icon>) {
        this.items.addAll(icon)
    }

    fun getIcons(): LinkedList<Icon> {
        return this.items
    }

    fun update() {
        if (this.page < 0) return

        for (i in 0 until this.slots.size) {
            val itemNumber = i + (this.page * this.slots.size)
            if (this.items.size > itemNumber) {
                this.gui.addItem(this.slots[i], this.items[itemNumber])
            } else {
                this.gui.addItem(this.slots[i], Icon(Material.AIR))
            }
        }
    }
}
