package me.owdding.patches

import me.owdding.ktcodecs.GenerateCodec
import me.owdding.ktcodecs.NamedCodec
import me.owdding.patches.actions.PatchAction
import me.owdding.patches.targets.Target
import me.owdding.patches.targets.TargetCondition
import me.owdding.patches.targets.test

@GenerateCodec
data class RepoPatch(
    @NamedCodec("target_condition") val targets: Map<Target, TargetCondition>,
    val action: PatchAction,
    val file: String,
) {

    fun shouldApply(): Boolean = targets.test()

}