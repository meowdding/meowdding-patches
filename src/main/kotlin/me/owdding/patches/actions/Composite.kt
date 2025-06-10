package me.owdding.patches.actions

import com.google.gson.JsonElement
import me.owdding.ktcodecs.GenerateCodec

@GenerateCodec
data class Composite(
    val actions: List<PatchAction>,
    override val required: Boolean = false,
) : PatchAction {
    override val type = PatchActions.COMPOSITE

    override fun apply(element: JsonElement) {
        actions.forEach { it.apply(element) }
    }
}