package com.dummyc0m.pylon.app.component

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by Dummy on 12/23/19.
 */

class MutableProperty<C: Refreshable, T>(private var value: T): ReadWriteProperty<C, T> {
    override fun getValue(thisRef: C, property: KProperty<*>): T {
        return value
    }

    override fun setValue(thisRef: C, property: KProperty<*>, value: T) {
        this.value = value
        thisRef.refresh()
    }
}