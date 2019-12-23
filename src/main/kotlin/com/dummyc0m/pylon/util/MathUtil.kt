package com.dummyc0m.pylon.util

import java.util.*

/**
 * Created by Dummyc0m on 3/3/15.
 * copied from mcp
 */
object MathUtil {
    private val SIN_TABLE = FloatArray(65536)
    private val random = Random()

    init {
        for (var0 in 0..65535) {
            SIN_TABLE[var0] = Math.sin(var0.toDouble() * Math.PI * 2.0 / 65536.0).toFloat()
        }
    }

    fun random(min: Int, max: Int): Int {
        return random.nextInt(max - min) + min
    }

    fun random(min: Double, max: Double): Double {
        return (random.nextDouble() + +java.lang.Double.MIN_VALUE) * (max - min) + min
    }

    fun sin(theta: Float): Float {
        return SIN_TABLE[(theta * 10430.378f).toInt() and 65535]
    }

    fun cos(theta: Float): Float {
        return SIN_TABLE[(theta * 10430.378f + 16384.0f).toInt() and 65535]
    }

    fun abs(value: Float): Float {
        return if (value >= 0.0f) value else -value
    }

    fun abs_int(value: Int): Int {
        return if (value >= 0) value else -value
    }

    fun isInteger(str: String?): Boolean {
        if (str == null) {
            return false
        }
        val length = str.length
        if (length == 0) {
            return false
        }
        var i = 0
        if (str[0] == '-') {
            if (length == 1) {
                return false
            }
            i = 1
        }
        while (i < length) {
            val c = str[i]
            if (c < '0' || c > '9') {
                return false
            }
            i++
        }
        return true
    }
}
