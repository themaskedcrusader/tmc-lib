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

package com.themaskedcrusader.bukkit.chest;

import com.themaskedcrusader.bukkit.serializer.Serializer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class SmartChest implements Serializable {
    private boolean isDoubleChest;
    private Map<String, Object> leftChest;
    private Map<String, Object> rightChest;
    private byte chestFacing;
    private ArrayList<MaskedItem> inventory;
    private String pluginName;

    public SmartChest(Block chest, JavaPlugin plugin) {
        this.pluginName = plugin.getName();
        this.isDoubleChest = ((Chest) chest.getState()).getInventory().getSize() > 27;
        this.leftChest = Serializer.serializeLocation(getChestLocation(chest.getLocation(), true));
        if (isDoubleChest) {
            this.rightChest = Serializer.serializeLocation(getChestLocation(chest.getLocation(), false));
        }
        this.chestFacing = chest.getState().getRawData();
        setInventory(((Chest) chest.getState()).getInventory().getContents());
    }

    public boolean isDoubleChest() {
        return isDoubleChest;
    }

    private boolean isBlockAChest(Block block) {
        return block.getType() == Material.CHEST;
    }

    private Location getChestLocation(Location loc, boolean left) {
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        int newX, newZ;
        if (left) {
            newX = x - 1; newZ = z - 1;
        } else {
            newX = x + 1; newZ = z + 1;
        }

        if (isBlockAChest(loc.getWorld().getBlockAt(newX, y, z))) {
            return new Location(loc.getWorld(), newX, y, z);
        } else if (isBlockAChest(loc.getWorld().getBlockAt(x, y, newZ))) {
            return new Location(loc.getWorld(), x, y, newZ);
        }
        return new Location(loc.getWorld(), x, y, z);
    }

    public Map<String, Object> getLocation() {
        return leftChest;
    }

    public String getChestKey() {
        return Serializer.toChestKey(leftChest);
    }

    public Map<String, Object> getOtherSideLocation() {
        return rightChest;
    }

    public Byte getChestFacing() {
        return chestFacing;
    }

    public void setInventory(ItemStack[] inv) {
        inventory = new ArrayList<MaskedItem>();

        for (ItemStack i : inv) {
            if (i != null) {
                MaskedItem packaged = new MaskedItem(i);
                inventory.add(packaged);
            }
        }
    }

    public ArrayList<MaskedItem> getInventory() {
        return inventory;
    }

    public String getPlugin() {
        return pluginName;
    }
}
