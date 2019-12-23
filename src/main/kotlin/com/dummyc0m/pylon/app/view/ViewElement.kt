package com.dummyc0m.pylon.app.view

import com.dummyc0m.pylon.app.MenuView

/**
 * Created by Dummy on 7/12/16.
 */
interface ViewElement {
    val x: Int
    val y: Int

    fun render(menuView: MenuView, offsetX: Int, offsetY: Int)
}