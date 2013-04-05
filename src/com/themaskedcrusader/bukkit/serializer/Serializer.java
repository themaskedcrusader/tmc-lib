package com.themaskedcrusader.bukkit.serializer;

import com.themaskedcrusader.bukkit.chest.MaskedItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Serializer {
    public static Map<String, Object> serializeLocation(Location location) {
        HashMap<String, Object> toReturn = new HashMap<String, Object>();
        toReturn.put("world", location.getWorld().getName());
        toReturn.put("x", location.getBlockX());
        toReturn.put("y", location.getBlockY());
        toReturn.put("z", location.getBlockZ());
        return toReturn;
    }

    public static Location deserializeLocation(Map<String, Object> loc) {
        World world = Bukkit.getServer().getWorld("" + loc.get("world"));
        Location toReturn = new Location(world, 0, 0, 0);
        toReturn.setX((Integer) loc.get("x"));
        toReturn.setY((Integer) loc.get("y"));
        toReturn.setZ((Integer) loc.get("z"));
        return toReturn;
    }

    public static Block getBlockAtSerializedLocation(Map<String, Object> loc) {
        Location location = deserializeLocation(loc);
        return location.getWorld().getBlockAt(location);
    }

    public static JavaPlugin deserializePlugin(String pluginName) {
        return (JavaPlugin) Bukkit.getServer().getPluginManager().getPlugin(pluginName);
    }

    public static ItemStack[] unmaskInventory(ArrayList<MaskedItem> inventory) {
        ArrayList<ItemStack> toReturn = new ArrayList<ItemStack>();
        for (MaskedItem item : inventory) {
            toReturn.add(item.unmask());
        }
        return toReturn.toArray(new ItemStack[0]);
    }

    public static String toChestKey(Map<String, Object> location) {
        String toReturn = "" + location.get("world");
        toReturn += "x" + location.get("x");
        toReturn += "y" + location.get("y");
        toReturn += "z" + location.get("z");
        return toReturn;
    }
}
