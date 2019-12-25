package com.dummyc0m.pylon.app.view

import java.util.*

/**
 * internal metadata to collect callbacks
 * Created by Dummy on 7/12/16.
 */
internal class ViewMeta {
    internal val animationHandler: List<Pair<Int, ItemElement>>
        get() = tickList
    private val tickList: MutableList<Pair<Int, ItemElement>> = ArrayList()

    internal val clickHandler: Map<Int, ItemElement>
        get() = clickMap
    private val clickMap: MutableMap<Int, ItemElement> = HashMap()

    internal fun click(x: Int, y: Int, itemElement: ItemElement) {
        clickMap.put(x + y * 9, itemElement)
    }

    internal fun animate(x: Int, y: Int, animatedItemElement: ItemElement) {
        tickList.add(Pair(x + y * 9, animatedItemElement))
    }

    internal fun clear(x: Int, y: Int) {
        tickList.removeIf { (idx, _) -> idx == (x + y * 9) }
        clickMap.remove(x + y * 9)
    }
}