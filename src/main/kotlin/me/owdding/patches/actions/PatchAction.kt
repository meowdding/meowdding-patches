package me.owdding.patches.actions

import com.google.gson.JsonElement
import me.owdding.patches.generated.DispatchHelper
import kotlin.reflect.KClass

interface PatchAction {
    val required: Boolean
    fun apply(element: JsonElement)
}

enum class PatchActions(override val type: KClass<out PatchAction>): DispatchHelper<PatchAction> {
    MOVE(Move::class),
    RENAME(Move::class), // alias
    INSERT(Insert::class),
    DELETE(Remove::class),
    COMPOSITE(Composite::class),
    ;


    companion object {
        fun getType(id: String) = PatchActions.entries.first { it.id.equals(id, true) }
    }
}
