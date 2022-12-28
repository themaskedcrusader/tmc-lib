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

// TODO: See line 208 for CLEANUP - OTHERWISE DONE!

package me.schalk.spigot.lib.model

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionType
import java.io.Serializable

class MaskedItem : Serializable, Cloneable {
    private val _delimiter = '|'
    private val _enchantment_delimiter = '\\'
    private val _enchantment_separator = ','
    private val _enchantment_namespace_delimiter = ':'

// Masked Items are a Pipe-Delimited string with each column containing a specific value. The ENUM values in this list
//  The values below are the available attributes. This could be represented in your plugin as a spreadsheet where each
//  row represents one item. Basically every single item available in Minecraft can be made from these values.
//
// This class is used (primarily) to convert items from config files into ItemStacks and vice versa. Probably should
//  not use this class to simply modify values in-game. Only when reading from config files and writing to save files

    // REQUIRED VALUES: These values are REQUIRED on ALL masked items
    private var material : String = ""              // The Material ID of the item in String format
    private var quantity : Int = 1                  // The quantity in the stack. Default is 1

    // The following fields are OPTIONAL on all masked items. However, if a value is specified (such as LORE or
    //   Enchantments), then all the previous fields MUST also be specified, even if the value is null or empty

    private var damage : Int = 0                    // For Tools, the current Damage to the tool. Default to 0 for NEW

    // These values are only applicable if the MATERIAL is in {POTION, LINGERING_POTION, SPLASH_POTION}
    private var potionName     : String? = null     // The name of the potion or effect as defined in Minecraft
    private var potionExtended : Boolean = false    // Is the option extended release (8 minutes instead of 3)
    private var potionUpgraded : Boolean = false    // Is the potion upgraded (II instead of regular)

    // These values are available on all items, but are for LORE use only
    private var lore        : String? = null        // Lore String displayed on item in game
    private var displayName : String? = null        // Name of item, as if it were renamed in an anvil
    private var playerName  : String? = null        // Player Name - Used to create Player Skulls

    // Map of enchantments to apply to the item in {Name, Value pairings} // TODO: figure out how to do this.
    private val enchantments : MutableMap<String, Int> = mutableMapOf()

    // ### Constructors ### //

    constructor(itemString: String) {
        val values = itemString.split(_delimiter)
        var index = 0
        for (value: String in values) {
            when (index) {
                0 -> material = value
                1 -> quantity = value.toInt()
                2 -> damage = value.toInt()
                3 -> potionName = value
                4 -> potionExtended = value.toBoolean()
                5 -> potionUpgraded = value.toBoolean()
                6 -> lore = value
                7 -> displayName = value
                8 -> playerName = value
                9 -> getEnchantments(value)
            }
            index += 1
        }
    }

    constructor(item: ItemStack) {
        material = item.type.name
        quantity = item.amount
        val meta = item.itemMeta

        if (meta is Damageable) {
            damage = meta.damage
        }

        if (meta is PotionMeta) {
            potionName = meta.basePotionData.type.name
            potionExtended = meta.basePotionData.isExtended
            potionUpgraded = meta.basePotionData.isUpgraded
        }

        if (meta is SkullMeta) {
            playerName = meta.owningPlayer?.name
        }

        lore = meta?.lore.toString()
        displayName = meta?.displayName.toString()

        getEnchantments(item)
    }

    // ### Private Functions ### //

    private fun getEnchantments(enchantmentString: String) {
        val trimmed = enchantmentString.replace("{","").replace("}","")
        val enchantmentsInString = trimmed.split(_enchantment_delimiter)
        for (enchantment in enchantmentsInString) {
            if ("" != enchantment) {
                enchantments[enchantment.split(_enchantment_separator)[0]] =
                    (enchantment.split(_enchantment_separator)[1]).toInt()
            }
        }
    }

    private fun getEnchantments(item: ItemStack) {
        val itemEnchantments = item.enchantments
        for (enchantment in itemEnchantments) {
            val enchantmentKey = enchantment.key.key
            val power = enchantment.value as Int
            val key: String = enchantmentKey.namespace + _enchantment_namespace_delimiter + enchantmentKey.key
            enchantments[key] = power;
        }
    }

    private fun serializeEnchantments(): String {
        var ret = ""
        for (enchantment in enchantments) {
            ret += enchantment.key + _enchantment_separator + enchantment.value + _enchantment_delimiter
        }
        return "{$ret}"
    }

    private fun unmaskEnchantments(item: ItemStack) {
        for (enchantment in enchantments) {
            // TODO: enchantmentNamespace should be a plugin, so add something to me.schalk.spigot.lib.config.ConfigAccessor to get Plugin By Name
            val enchantmentNamespace = enchantment.key.split(_enchantment_namespace_delimiter)[0]
            val enchantmentUnderlyingName = enchantment.key.split(_enchantment_namespace_delimiter)[1]
            // This is Deprecated because it should be NamespacedKey(Plugin, enchantmentUnderlying) : TODO get working
            val nameSpaceKey = NamespacedKey(enchantmentNamespace, enchantmentUnderlyingName);
            val itemEnchantment = Enchantment.getByKey(nameSpaceKey)
            if (itemEnchantment != null) {
                try {
                    item.addEnchantment(itemEnchantment, enchantment.value.coerceAtMost(itemEnchantment.maxLevel))
                } catch (ex: Exception) {
                    println("Enchantment " + itemEnchantment.key + " Can't be applied to object")
                }
            }
        }
    }

    // ### Public Functions ### //

    fun serialize(): String {
        return (material + _delimiter +
                quantity + _delimiter +
                damage + _delimiter +
                potionName + _delimiter +
                potionExtended + _delimiter +
                potionUpgraded + _delimiter +
                lore + _delimiter +
                displayName + _delimiter +
                playerName + _delimiter +
                serializeEnchantments())
            .replace("null", "")
            .replace("false", "")
    }

    fun unmask(): ItemStack {
        try {
            val incoming: Material = Material.matchMaterial(material.uppercase())!!
            val ret = ItemStack(incoming, quantity)
            val meta = ret.itemMeta
            if (meta != null) {
                // Lore and Display Name
                meta.lore = mutableListOf(lore)
                meta.setDisplayName(displayName)
                ret.itemMeta = meta

                val damageMeta = ret.itemMeta as Damageable
                damageMeta.damage = damage
                ret.itemMeta = damageMeta

                if (incoming in listOf(Material.POTION, Material.LINGERING_POTION, Material.SPLASH_POTION)) {
                    val potionType = potionName?.let { PotionType.valueOf(it.uppercase()) }
                    val potionData = potionType?.let { PotionData(it, potionExtended, potionUpgraded) }
                    val potionMeta = ret.itemMeta as PotionMeta
                    if (potionData != null) {
                        potionMeta.basePotionData = potionData
                        ret.itemMeta = potionMeta
                    }
                }

                if (incoming == Material.PLAYER_HEAD) {
                    val skullMeta = ret.itemMeta as SkullMeta
                    skullMeta.owningPlayer = playerName?.let { Bukkit.getOfflinePlayer(it) }
                    ret.itemMeta = skullMeta
                }
            }
            unmaskEnchantments(ret)
            return ret

        } catch (ex: Exception) {
            println("Error generating item: " + serialize())
            ex.printStackTrace()
            return default()
        }
    }

    // ### Static (default) Object

    companion object {
        private const val _default = "AIR|0|0"
        fun default(): ItemStack {
            return MaskedItem(_default).unmask()
        }
    }
}