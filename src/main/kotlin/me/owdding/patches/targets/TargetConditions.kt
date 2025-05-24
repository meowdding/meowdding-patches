package me.owdding.patches.targets

import me.owdding.ktcodecs.GenerateDispatchCodec
import me.owdding.patches.generated.DispatchHelper
import me.owdding.patches.targets.conditions.*
import kotlin.reflect.KClass

interface TargetCondition {
    val type: TargetConditions

    fun test(target: Target): Boolean
}

@GenerateDispatchCodec(TargetCondition::class)
enum class TargetConditions(override val type: KClass<out TargetCondition>) : DispatchHelper<TargetCondition> {
    VERSION(VersionCondition::class),
    ANY_OF(AnyOf::class),
    ALL_OF(AllOf::class),
    N_OF(NOf::class),
    NEGATE(Negate::class),
    DATE(Date::class),
    ;

    companion object {
        fun getType(id: String) = entries.first { it.id.equals(id, true) }
    }
}

typealias Targets = Map<Target, TargetCondition>

fun Targets.test(): Boolean = this.entries.any { (target, condition) -> condition.test(target) }