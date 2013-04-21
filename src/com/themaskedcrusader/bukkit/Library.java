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

package com.themaskedcrusader.bukkit;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Library {
    public static final double version = 2.0;

    public static void checkForNewVersion(ConsoleCommandSender notify) {
        String currentVersion = readCurrentVersion();
        try {
            Double cv = Double.parseDouble(currentVersion);
            if (cv > version) {
                notify.sendMessage(ChatColor.YELLOW + "A new version of TMC-LIB is available. See " +
                        "http://dev.bukkit.org/server-mods/tmc-lib/ for details");
                return;
            }
        } catch (Exception e) {
            notify.sendMessage(ChatColor.RED + "Could not locate new version number. " +
                    "Please check http://dev.bukkit.org/server-mods/tmc-lib/ for a new version");
            return;
        }
        notify.sendMessage(ChatColor.GREEN + "TMC-LIB is up to date");
    }

    private static String readCurrentVersion() {
        try {
            String url = "https://dl.dropboxusercontent.com/u/3386824/bukkit/tmc-lib-version.txt";
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            return reader.readLine();
        } catch (Exception e) {
            return null;
        }
    }
}
