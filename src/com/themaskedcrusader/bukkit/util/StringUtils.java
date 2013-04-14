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

public class StringUtils {

    // Suppress instantiability of Utility Class
    private StringUtils() {}

    public static String removeNull(Object in) {
        if (in == null) {
            return "";
        } else {
            return "" + in;
        }
    }

    public static String capitalize(String in) {
        if (!"".equals(removeNull(in))) {
            String first = in.substring(0,1).toUpperCase();
            return first + in.substring(1).toLowerCase();
        }
        return null;
    }

}
