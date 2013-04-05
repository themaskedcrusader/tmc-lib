/*
 * Copyright 2013 Topher Donovan (themaskedcrusader.com)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.themaskedcrusader.bukkit.util;

import com.themaskedcrusader.bukkit.config.ConfigAccessor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;

public class WorldUtils {
    public static final String WORLD_NAME = "world.name";
    public static final String SINGLE_WORLD = "world.single";

    // Suppress instantiability of Utility Class
    private WorldUtils() {}

    public static boolean isAllowed(World world, ConfigAccessor settings) {
        if (settings.getConfig().getBoolean(SINGLE_WORLD)) {
            String worldName = settings.getConfig().getString(WORLD_NAME);
            return world.getName().equalsIgnoreCase(worldName);
        }
        return true;
    }

    // You can only use this method if your plugin runs on exactly one world.
    // TODO: Modify this for multiple worlds
    public static World getAuthorizedWorld(JavaPlugin plugin, ConfigAccessor settings) {
        String worldName = settings.getConfig().getString(WORLD_NAME);
        return plugin.getServer().getWorld(worldName);
    }

    public static Collection<Entity> getNearbyPlayers(Location location, int radius) {
        ArrayList<Entity> toReturn = new ArrayList<Entity>();
        final Collection<org.bukkit.entity.Entity> worldEntities = location.getWorld().getEntities();

        for (Entity entity : worldEntities) {
            if (entity instanceof Player) {
                if (entity.getLocation().distance(location) < radius) {
                    toReturn.add(entity);
                }
            }
        }
        return toReturn;
    }

    public static void sendBulkMessage(Collection<Entity> nearbyPlayers, String message) {
        for (Entity player : nearbyPlayers) {
            if (player instanceof Player) {
                ((Player) player).sendMessage(message);
            }
        }
    }

    public static boolean isPlayerMoving(Player player, Location from) {
        Location to = player.getLocation();
        return (from.getX() != to.getX() && from.getZ() != to.getZ());
    }

    public static boolean isPlayerFalling(Player player, Location from) {
        Location to = player.getLocation();
        return (from.getY() != to.getY());
    }

    public static boolean isSingleWorld(ConfigAccessor settings) {
        return settings.getConfig().getBoolean(SINGLE_WORLD);
    }
}
