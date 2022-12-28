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

package me.schalk.spigot.lib.util

import me.schalk.spigot.lib.config.SaveFile
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

const val WORLD_NAME = "world.name"
const val SINGLE_WORLD = "world.single"

fun isAllowed(world: World, settings: SaveFile): Boolean {
    if (settings.getConfig().getBoolean(SINGLE_WORLD)) {
        val worldName: String? = settings.getConfig().getString(WORLD_NAME)
        return world.name.equals(worldName, ignoreCase = true)
    }
    return true
}

// You can only use this method if your plugin runs on exactly one world.
// TODO: Modify this for multiple worlds
fun getAuthorizedWorld(plugin: JavaPlugin, settings: SaveFile): World? {
    val worldName: String? = settings.getConfig().getString(WORLD_NAME)
    return plugin.server.getWorld(worldName!!)
}

fun getNearbyPlayers(location: Location, radius: Int): Collection<Entity> {
    val toReturn = ArrayList<Entity>()
    val worldEntities: Collection<Entity> = location.world!!.entities
    for (entity in worldEntities) {
        if (entity is Player) {
            if (entity.getLocation().distance(location) < radius) {
                toReturn.add(entity)
            }
        }
    }
    return toReturn
}

fun sendBulkMessage(nearbyPlayers: Collection<Entity?>, message: String?) {
    for (player in nearbyPlayers) {
        if (player is Player) {
            player.sendMessage(message)
        }
    }
}

fun isPlayerMoving(player: Player, from: Location): Boolean {
    val to = player.location
    return from.x != to.x && from.z != to.z
}

fun isPlayerFalling(player: Player, from: Location): Boolean {
    val to = player.location
    return from.y != to.y
}

fun isSingleWorld(settings: SaveFile): Boolean {
    return settings.getConfig().getBoolean(SINGLE_WORLD)
}