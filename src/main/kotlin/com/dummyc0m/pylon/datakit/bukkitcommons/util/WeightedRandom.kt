package com.dummyc0m.pylon.datakit.bukkitcommons.util

import java.util.*

/**
 * Created by Dummyc0m on 8/19/15.
 */
object WeightedRandom {
    private val random = Random()

    fun Collection<IWeightedItem>.getWeight(): Int {
        var sum = 0
        for (IWeightedItem in this) {
            sum += IWeightedItem.weight
        }
        return sum
    }

    fun Collection<IWeightedItem>.getRandomItem(totalWeight: Int = getWeight()): IWeightedItem {
        if (totalWeight <= 0) {
            throw IllegalArgumentException("Total weight smaller than 0")
        }
        val randomNumber = random.nextInt(totalWeight)
        return getItem(this, randomNumber)
    }

    private fun getItem(IWeightedItems: Collection<IWeightedItem>, randomNumber: Int): IWeightedItem {
        var number = randomNumber
        val itemIterator = IWeightedItems.iterator()
        var weightedItem: IWeightedItem
        do {
            weightedItem = itemIterator.next()
            number -= weightedItem.weight
        } while (number > 0)
        return weightedItem
    }

    interface IWeightedItem {
        val weight: Int
    }
}
