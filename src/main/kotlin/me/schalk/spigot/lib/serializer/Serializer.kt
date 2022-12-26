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

package me.schalk.spigot.lib.serializer

import me.schalk.spigot.lib.model.MaskedItem
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin


fun serializeLocation(location: Location): Map<String, Any> {
    val toReturn = HashMap<String, Any>()
    toReturn["world"] = location.world!!.name
    toReturn["x"] = location.blockX
    toReturn["y"] = location.blockY
    toReturn["z"] = location.blockZ
    return toReturn
}

fun deserializeLocation(loc: Map<String?, Any>): Location {
    val world = Bukkit.getServer().getWorld("" + loc["world"])
    val toReturn = Location(world, 0.0, 0.0, 0.0)
    toReturn.x = (loc["x"] as Int?)!!.toDouble()
    toReturn.y = (loc["y"] as Int?)!!.toDouble()
    toReturn.z = (loc["z"] as Int?)!!.toDouble()
    return toReturn
}

fun getBlockAtSerializedLocation(loc: Map<String?, Any>): Block {
    val location = deserializeLocation(loc)
    return location.world!!.getBlockAt(location)
}

fun deserializePlugin(pluginName: String?): JavaPlugin? {
    return Bukkit.getServer().pluginManager.getPlugin(pluginName!!) as JavaPlugin?
}

fun unmaskInventory(inventory: ArrayList<MaskedItem?>): Array<ItemStack>? {
    val toReturn = ArrayList<ItemStack>()
    for (item in inventory) {
        if (item != null) {
            toReturn.add(item.unmask())
        }
    }
    return toReturn.toTypedArray()
}

fun toChestKey(location: Map<String?, Any>): String {
    var toReturn = "" + location["world"]
    toReturn += "x" + location["x"]
    toReturn += "y" + location["y"]
    toReturn += "z" + location["z"]
    return toReturn
}