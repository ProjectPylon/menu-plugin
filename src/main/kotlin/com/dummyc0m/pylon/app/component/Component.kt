package com.dummyc0m.pylon.app.component

import com.dummyc0m.pylon.app.view.ViewElement

/**
 * Created by Dummy on 12/23/19.
 */

abstract class Component(protected val app: AppRoot): Refreshable {
    abstract fun render(): ViewElement

    fun <T> prop(initialValue: T): MutableProperty<Component, T> {
        return MutableProperty(initialValue)
    }

    override fun refresh() {
        app.refresh()
    }
}