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

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class InventoryUtils {

    // Suppress instantiability of Utility Class
    private InventoryUtils() {}

    public static ItemStack[] copyInventory(Inventory inv) {
        ArrayList<ItemStack> toReturn = new ArrayList<ItemStack>();

        for(ItemStack item : inv.getContents()) {
            if (item != null) {
                ItemStack newItem = item.clone();
                toReturn.add(newItem);
            }
        }

        return toReturn.toArray(new ItemStack[0]);
    }

    public static void addArmor(Player player, Inventory newInv) {
        try {
            newInv.addItem(player.getInventory().getArmorContents().clone());
        } catch (Exception ignored) {
            // not enough space in inventory for armor items... ignore ArrayIndexOutOfBounds errors.
        }
    }
}
