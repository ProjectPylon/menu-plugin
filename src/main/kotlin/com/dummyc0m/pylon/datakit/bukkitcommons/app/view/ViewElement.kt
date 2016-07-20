package com.dummyc0m.pylon.datakit.bukkitcommons.app.view

import com.dummyc0m.pylon.datakit.bukkitcommons.app.MenuView

/**
 * Created by Dummy on 7/12/16.
 */
interface ViewElement {
    var x: Int
    var y: Int

    fun render(menuView: MenuView)
}