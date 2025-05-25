package me.owdding.patches.targets.conditions

import me.owdding.ktcodecs.GenerateCodec
import me.owdding.patches.targets.Target
import me.owdding.patches.targets.TargetCondition
import me.owdding.patches.targets.TargetConditions
import net.fabricmc.loader.api.ModContainer

@GenerateCodec
data class AllOf(val conditions: List<TargetCondition>) : TargetCondition {
    override val type = TargetConditions.ALL_OF

    override fun test(target: Target, modContainer: ModContainer) = conditions.all { it.test(target, modContainer) }
}