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

package me.schalk.spigot.lib.config

import org.bukkit.plugin.java.JavaPlugin



private var settings : SaveFile? = null

fun initSettings(plugin: JavaPlugin) : SaveFile {
    if (settings == null) {
        settings = SaveFile(plugin, "settings.yml")
        settings!!.get().reloadConfig()
        settings!!.get().saveDefaultConfig()
    }
    return settings!!
}

fun Settings(plugin: JavaPlugin) : SaveFile {
    return settings!!
}

private var messages : SaveFile? = null

fun initMessages(plugin: JavaPlugin) : SaveFile {
    if (messages == null) {
        messages = SaveFile(plugin, "settings.yml")
        messages!!.get().reloadConfig()
        messages!!.get().saveDefaultConfig()
    }
    return messages!!
}

fun Messages(plugin: JavaPlugin) : SaveFile {
    return settings!!
}