package me.owdding.patches.targets.conditions

import me.owdding.ktcodecs.GenerateCodec
import me.owdding.patches.targets.Target
import me.owdding.patches.targets.TargetCondition
import me.owdding.patches.targets.TargetConditions
import net.fabricmc.loader.api.ModContainer

@GenerateCodec
data class Negate(val condition: TargetCondition) : TargetCondition {
    override val type = TargetConditions.NEGATE

    override fun test(target: Target, modContainer: ModContainer): Boolean {
        return !condition.test(target, modContainer)
    }
}