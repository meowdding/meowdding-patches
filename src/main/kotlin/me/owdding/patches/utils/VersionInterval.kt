package me.owdding.patches.utils

import net.fabricmc.loader.api.Version
import net.fabricmc.loader.api.metadata.version.VersionPredicate

class VersionInterval(val predicates: List<VersionPredicate>) {
    constructor(vararg predicates: VersionPredicate) : this(listOf(*predicates))

    fun test(version: Version): Boolean {
        if (predicates.isEmpty()) return true
        return predicates.all { it.test(version) }
    }
}