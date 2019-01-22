package com.alloydflanagan.hexcalcrpn.model

import java.math.BigInteger

fun BigInteger.toSpacedString(radix: Int = 10): String {
    val builder = StringBuilder()
    val str = this.toString(radix)
    for (i in 0 until str.length) {
        // distance from current pos to end of string
        val digitPos = str.length - i
        if (!builder.endsWith('-') && i > 0 && digitPos.rem(4) == 0) {
            builder.append(' ')
        }
        builder.append(str[i])
    }
    return builder.toString()
}
