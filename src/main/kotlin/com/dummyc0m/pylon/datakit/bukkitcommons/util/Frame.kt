package com.dummyc0m.pylon.datakit.bukkitcommons.util

/**
 * Created by Dummy on 7/18/16.
 */
class Frame(private var value: Int = 0) : Number() {

    fun inc() {
        value++
    }

    fun dec() {
        value--
    }

    operator fun plusAssign(other: Int) {
        value += other
    }

    operator fun minusAssign(other: Int) {
        value -= other
    }

    operator fun timesAssign(other: Int) {
        value *= other
    }

    operator fun divAssign(other: Int) {
        value /= other
    }

    operator fun modAssign(other: Int) {
        value %= other
    }

    fun get(): Int {
        return value
    }

    override fun toByte(): Byte {
        return value.toByte()
    }

    override fun toChar(): Char {
        return value.toChar()
    }

    override fun toDouble(): Double {
        return value.toDouble()
    }

    override fun toFloat(): Float {
        return value.toFloat()
    }

    override fun toInt(): Int {
        return value
    }

    override fun toLong(): Long {
        return value.toLong()
    }

    override fun toShort(): Short {
        return value.toShort()
    }
}