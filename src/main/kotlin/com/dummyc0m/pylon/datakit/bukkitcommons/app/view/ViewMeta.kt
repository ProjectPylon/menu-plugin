package com.dummyc0m.pylon.datakit.bukkitcommons.app.view

import java.util.*

/**
 * Created by Dummy on 7/12/16.
 */
class ViewMeta(val argumentMap: Map<String, Any>) {
    internal val animationHandler: List<AnimatedItemElement>
        get() = tickList
    private val tickList: MutableList<AnimatedItemElement> = ArrayList()

    internal val blankHandler: Map<Int, BlankElement>
        get() = blankMap
    private val blankMap: MutableMap<Int, BlankElement> = HashMap()

    internal val clickHandler: Map<Int, ItemElement>
        get() = clickMap
    private val clickMap: MutableMap<Int, ItemElement> = HashMap()

    internal fun blank(blankElement: BlankElement) {
        blankMap.put(blankElement.x + blankElement.y * 9, blankElement)
    }

    internal fun click(itemElement: ItemElement) {
        clickMap.put(itemElement.x + itemElement.y * 9, itemElement)
    }

    internal fun animate(animatedItemElement: AnimatedItemElement) {
        tickList.add(animatedItemElement)
    }
}