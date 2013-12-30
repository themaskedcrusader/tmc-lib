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

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaskedItem implements Serializable, Cloneable {

    private static final long serialVersionUID = 247478674377242550L;

    private String itemType;
    private int quantity;
    private short durability;
    private byte data;
    private List<String> lore;
    private String displayName;

    private String playerName;

    private static final String PIPE = "|";

    private HashMap<String, Integer> enchantments;

    public MaskedItem(String tmcItemFormatString) {
        String[] itemData = tmcItemFormatString.split("\\|");
        switch (itemData.length) {
            case 4:
                durability = Short.parseShort(itemData[3]);
            case 3:
                data = Byte.parseByte(itemData[2]);
            case 2:
                quantity = Integer.parseInt(itemData[1]);
            case 1:
                itemType = itemData[0];
        }
    }

    public MaskedItem(ItemStack item) {
        mask(item);
    }

    public void mask(ItemStack item) {
        this.itemType = item.getType().name();
        this.quantity = item.getAmount();
        this.durability = item.getDurability();
        if (durability == 0) {
            // There isn't a way to do this the new way. Don't deprecate until there is another way to do it!
            this.data = item.getData().getData();
        } else {
            this.data = 0;
        }
        this.displayName = item.getItemMeta().getDisplayName();
        this.lore = item.getItemMeta().getLore();
        maskEnchantments(item);

        if (item.getType() == Material.SKULL_ITEM) {
            playerName = ((SkullMeta) item.getItemMeta()).getOwner();
        }
    }

    private void maskEnchantments(ItemStack item) {
        this.enchantments = new HashMap<String, Integer>();
        Map<Enchantment, Integer> toWrap = item.getEnchantments();
        for(Enchantment enchantment : toWrap.keySet()) {
            enchantments.put(enchantment.getName(), toWrap.get(enchantment));
        }
    }

    public ItemStack unmask() {
        ItemStack item = new ItemStack(Material.getMaterial(itemType), quantity, durability != 0 ? durability : (short) data);

        if (enchantments != null) {
            HashMap<Enchantment, Integer> map = new HashMap<Enchantment, Integer>();
            for(Map.Entry<String, Integer> enchantment : enchantments.entrySet()) {
                map.put(Enchantment.getByName(enchantment.getKey()), enchantment.getValue());
            }
            item.addUnsafeEnchantments(map);
        }

        if (item.getType() == Material.SKULL_ITEM) {
            if (playerName != null && !"".equals(playerName)) {
                SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
                skullMeta.setOwner(playerName);
                skullMeta.setDisplayName(playerName + "'s head");
                item.setItemMeta(skullMeta);
            }

        } else {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(displayName);
            meta.setLore(lore);
            item.setItemMeta(meta);

        }
        return item;
    }

    public String toString() {
        String toReturn = itemType + PIPE  + quantity + PIPE;

        if (data > 0) {
            toReturn += data + PIPE;
        }

        if (durability > 0) {
            if (data <= 0) {
                toReturn += "-1" + PIPE;
            }
            toReturn += durability;
        }

        return toReturn;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public short getDurability() {
        return durability;
    }

    public void setDurability(short durability) {
        this.durability = durability;
    }

    public byte getData() {
        return data;
    }

    public void setData(byte data) {
        this.data = data;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
        this.data = (byte) 3;
    }
}
