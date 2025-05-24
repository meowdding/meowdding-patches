package me.owdding.patches.targets.conditions

import me.owdding.ktcodecs.GenerateCodec
import me.owdding.patches.targets.Target
import me.owdding.patches.targets.TargetCondition
import me.owdding.patches.targets.TargetConditions
import org.jetbrains.annotations.Range

@GenerateCodec
data class NOf(
    val conditions: List<TargetCondition>,
    val amount: @Range(from = 0, to = Long.MAX_VALUE) Int,
    val exact: Boolean = true,
) : TargetCondition {
    override val type = TargetConditions.N_OF

    override fun test(target: Target): Boolean {
        val matches = conditions.filter { it.test(target) }.size
        if (exact) {
            return matches == amount
        }
        return matches >= amount
    }
}