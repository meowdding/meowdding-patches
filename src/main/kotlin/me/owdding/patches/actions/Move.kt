package me.owdding.patches.actions

import com.google.gson.JsonElement
import me.owdding.ktcodecs.GenerateCodec

@GenerateCodec
data class Move(
    val from: String,
    val to: String,
    override val required: Boolean = false,
) : PatchAction {
    override fun apply(element: JsonElement) {

    }
}