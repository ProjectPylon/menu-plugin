package com.dummyc0m.pylon.datakit.bukkitcommons.app

import org.bukkit.entity.HumanEntity
import org.bukkit.plugin.java.JavaPlugin

/**
 * Created by Dummy on 7/13/16.
 */
open class App(plugin: JavaPlugin) {
    val router = Router(plugin)

    fun render(humanEntity: HumanEntity, route: String = "/") {
        router.routeTo(humanEntity, route)
    }
}