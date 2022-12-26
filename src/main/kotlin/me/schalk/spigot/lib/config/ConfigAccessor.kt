///*
// * Copyright 2013 Topher Donovan (themaskedcrusader.com)
// *
// *    Licensed under the Apache License, Version 2.0 (the "License");
// *    you may not use this file except in compliance with the License.
// *    You may obtain a copy of the License at
// *
// *        http://www.apache.org/licenses/LICENSE-2.0
// *
// *    Unless required by applicable law or agreed to in writing, software
// *    distributed under the License is distributed on an "AS IS" BASIS,
// *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *    See the License for the specific language governing permissions and
// *    limitations under the License.
// *
// *    This class is dual licensed under Apache 2.0 and MIT Licenses
// *
// */
//package me.schalk.spigot.config
//
//import org.bukkit.configuration.file.FileConfiguration
//import org.bukkit.configuration.file.YamlConfiguration
//import org.bukkit.plugin.java.JavaPlugin
//import java.io.File
//import java.io.IOException
//import java.util.logging.Level
//
//class ConfigAccessor(plugin: JavaPlugin?, fileName: String) {
//    private val fileName: String
//    private val plugin: JavaPlugin
//    private var configFile: File? = null
//    private var fileConfiguration: FileConfiguration? = null
//
//    private fun getConfigFile() {
//        if (configFile == null) {
//            val dataFolder = plugin.dataFolder ?: throw IllegalStateException()
//            configFile = File(dataFolder, fileName)
//        }
//    }
//
//    init {
//        requireNotNull(plugin) { "plugin cannot be null" }
//        require(plugin.) { "plugin must be initialized" }
//        this.plugin = plugin
//        this.fileName = fileName
//    }
//
//    fun reloadConfig() {
//        getConfigFile()
//        fileConfiguration = YamlConfiguration.loadConfiguration(configFile!!)
//
//        // Look for defaults in the jar
//        val defConfigStream = plugin.getResource(fileName)
//        if (defConfigStream != null) {
//            val defConfig: YamlConfiguration = YamlConfiguration.loadConfiguration(defConfigStream)
//            fileConfiguration.setDefaults(defConfig)
//        }
//    }
//
//    val config: FileConfiguration?
//        get() {
//            if (fileConfiguration == null) {
//                reloadConfig()
//            }
//            return fileConfiguration
//        }
//
//    fun saveConfig() {
//        if (fileConfiguration != null && configFile != null) {
//            try {
//                config!!.save(configFile!!)
//            } catch (ex: IOException) {
//                plugin.logger.log(Level.SEVERE, "Could not save config to $configFile", ex)
//            }
//        }
//    }
//
//    fun saveDefaultConfig() {
//        if (!configFile!!.exists()) {
//            plugin.saveResource(fileName, false)
//        }
//    }
//
//    fun delete() {
//        getConfigFile()
//        configFile!!.delete()
//    }
//}