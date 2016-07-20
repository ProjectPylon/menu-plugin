package com.dummyc0m.pylon.datakit.bukkitcommons.app

import com.google.common.collect.ImmutableBiMap
import org.bukkit.Bukkit
import org.bukkit.entity.HumanEntity
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

/**
 * not thread safe
 * use the safely method for buttom enabled menus
 * Created by Dummy on 7/11/16.
 */
class Router(val plugin: JavaPlugin) {
    private val defaultRoute = "/"
    private val routeMap: MutableMap<String, Menu> = HashMap()

    fun default(menu: Menu) {
        routeMap.put(defaultRoute, menu)
    }

    fun getMenu(route: String): Menu {
        val menu = routeMap.get(route)
        if (menu != null) {
            return menu
        }
        throw AppException("Unreachable getMenu $route")
    }

    fun routeTo(humanEntity: HumanEntity, route: String = "/", argumentMap: Map<String, Any> = emptyArgumentMap) {
        getMenu(route).render(plugin, humanEntity, argumentMap)
    }

    /**
     * for bottom enabled menus
     */
    fun routeSafelyTo(humanEntity: HumanEntity, route: String = "/", argumentMap: Map<String, Any> = emptyArgumentMap) {
        hide(humanEntity)
        Bukkit.getScheduler().runTask(plugin) {
            getMenu(route).render(plugin, humanEntity, argumentMap)
        }
    }

    fun hide(humanEntity: HumanEntity) {
        Bukkit.getScheduler().runTask(plugin) {
            humanEntity.closeInventory()
        }
    }

    fun addRoute(route: String, menu: Menu): Router {
        if (routeMap.containsKey(route)) {
            throw AppException("Route $route already exists")
        }
        routeMap.put(route, menu)
        return this
    }

    fun removeRoute(route: String) {
        routeMap.remove(route)
    }

    companion object {
        private val emptyArgumentMap: Map<String, Any> = ImmutableBiMap.of()
    }
}