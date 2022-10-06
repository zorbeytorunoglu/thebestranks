package com.zorbeytorunoglu.thebestranks.utils

import com.zorbeytorunoglu.thebestranks.TBR
import net.md_5.bungee.api.ChatColor
import java.util.regex.Pattern

class StringUtils(private val plugin: TBR) {

    companion object {
        fun getNumberFromString(string: String): Int {
            return string.filter { it.isDigit() }.toInt()
        }

        fun getDoubleFromString(string: String): Double {
            return string.filter { it.isDigit() }.toDouble()
        }

        fun hex(message: String): String {
            var message = message
            val pattern = Pattern.compile("#[a-fA-F0-9]{6}")
            var matcher = pattern.matcher(message)
            while (matcher.find()) {
                val hexCode = message.substring(matcher.start(), matcher.end())
                val replaceSharp = hexCode.replace('#', 'x')
                val ch = replaceSharp.toCharArray()
                val builder = StringBuilder("")
                for (c in ch) {
                    builder.append("&$c")
                }
                message = message.replace(hexCode, builder.toString())
                matcher = pattern.matcher(message)
            }
            return ChatColor.translateAlternateColorCodes('&', message)
        }
    }

}