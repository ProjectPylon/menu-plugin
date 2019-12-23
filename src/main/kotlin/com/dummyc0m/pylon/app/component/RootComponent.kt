package com.dummyc0m.pylon.app.component

import com.dummyc0m.pylon.app.view.RootElement

/**
 * Created by Dummy on 12/23/19.
 */

abstract class RootComponent(protected val app: AppRoot): Refreshable {
    abstract fun render(): RootElement

    fun <T> prop(initialValue: T): MutableProperty<RootComponent, T> {
        return MutableProperty(initialValue)
    }

    override fun refresh() {
        app.refresh()
    }
}
