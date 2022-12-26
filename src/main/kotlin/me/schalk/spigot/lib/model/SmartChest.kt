/*
 * Copyright (c) 2013 - 2023 Christopher Schalk
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.schalk.spigot.lib.model

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.Container
import org.bukkit.block.ShulkerBox
import org.bukkit.block.data.Directional
import org.bukkit.block.data.type.Chest
import org.bukkit.block.data.type.EnderChest
import org.bukkit.inventory.Inventory
import java.io.Serializable

class SmartChest(block: Block) : Serializable {
    var type : Chest.Type
    var location: Location                      // ALWAYS THE LEFT LOCATION
    var otherSideLocation: Location             // ALWAYS THE RIGHT LOCATION
    private var facing: SmartChestFacing
    private var enderChest = false              // if true, can't get inventory
    private var inventory: MutableList<MaskedItem>? = null
    private var color = ""                      // Color of the ShulkerBox (if is a shulkerbox)

    init  {
        val data = block.blockData
        type = getType(block)
        facing = SmartChestFacing.valueOf((data as Directional).facing.name)
        when (type) {
            Chest.Type.SINGLE -> {
                location = block.location
                otherSideLocation = block.location
            }
            Chest.Type.LEFT -> {
                location = block.location
                otherSideLocation = getChestLocation(block.location, true)
            }
            Chest.Type.RIGHT -> {
                otherSideLocation = block.location
                location = getChestLocation(block.location, false)
            }
        }

        setInventory(block)

        println("Inventory Size   : " + inventory?.size)
        println("ShulkerBox Color : $color ")
    }

    private fun setInventory(block: Block) = if (!enderChest) {
        val inventory = (block.state as Container).inventory
        this.inventory = wrapInventory(inventory)
    } else {
        println("tis an Ender Chest, no can inventory")
    }

    private fun wrapInventory(inventory: Inventory): MutableList<MaskedItem> {
        val ret = mutableListOf<MaskedItem>()
        inventory.contents.forEach {
            if (it != null) {
                val maskedItem = MaskedItem(it)
                ret.add(maskedItem)
            }
        }
        return ret
    }

    private fun getType(block: Block) : Chest.Type {
        return if (block.blockData is EnderChest) {
            enderChest = true
            Chest.Type.SINGLE
        } else if (block.state is ShulkerBox) {
            color = (block.state as ShulkerBox).color?.name ?: ""
            Chest.Type.SINGLE
        } else {
            (block.blockData as Chest).type
        }
    }

    private fun isBlockAChest(block: Block): Boolean {
        return block.type == Material.CHEST || block.type == Material.TRAPPED_CHEST
    }

    private fun getChestLocation(location: Location, findingRightChest: Boolean) : Location {
        val x = location.blockX
        val y = location.blockY
        val z = location.blockZ
        var newX = x
        var newZ = z

        if ((facing == SmartChestFacing.NORTH && findingRightChest) || (facing == SmartChestFacing.SOUTH && !findingRightChest)) {
            newX = x + 1

        } else if ((facing == SmartChestFacing.NORTH && !findingRightChest) || (facing == SmartChestFacing.SOUTH && findingRightChest)) {
            newX = x - 1

        } else if ((facing == SmartChestFacing.EAST && findingRightChest) || (facing == SmartChestFacing.WEST && !findingRightChest)) {
            newZ = z + 1

        } else if ((facing == SmartChestFacing.EAST && !findingRightChest) || (facing == SmartChestFacing.WEST && findingRightChest)) {
            newZ = z - 1

        }

        if (isBlockAChest(location.world!!.getBlockAt(newX, y, z))) {
            return Location(location.world, newX.toDouble(), y.toDouble(), z.toDouble())
        } else if (isBlockAChest(location.world!!.getBlockAt(x, y, newZ))) {
            return Location(location.world, x.toDouble(), y.toDouble(), newZ.toDouble())
        }
        return Location(location.world, x.toDouble(), y.toDouble(), z.toDouble())
    }

    // TODO: find out how to serialize this in a config file. Might not be possible. Might need to just serialize
    // it to a regular file.
}

enum class SmartChestFacing {
    NORTH, SOUTH, EAST, WEST, UP, DOWN;
}

fun getSmartChest(block: Block): SmartChest? {
    return if (block.blockData is Chest || block.blockData is EnderChest || block.state is ShulkerBox) {
        SmartChest(block)
    } else {
        null
    }
}