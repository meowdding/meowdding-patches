package me.owdding.patches.targets.conditions

import me.owdding.ktcodecs.GenerateCodec
import me.owdding.patches.targets.Target
import me.owdding.patches.targets.TargetCondition
import me.owdding.patches.targets.TargetConditions
import me.owdding.patches.utils.VersionInterval
import me.owdding.patches.utils.VersionIntervalParser
import net.fabricmc.loader.api.FabricLoader
import kotlin.jvm.optionals.getOrNull

@GenerateCodec
data class VersionCondition(val predicate: String) : TargetCondition {
    val interval: VersionInterval = VersionIntervalParser.parse(predicate)

    override val type = TargetConditions.VERSION
    override fun test(target: Target): Boolean {
        val modContainer = FabricLoader.getInstance().getModContainer(target.modId).getOrNull() ?: return false
        val version = modContainer.metadata.version
        return interval.test(version)
    }

    fun asString() = predicate
}