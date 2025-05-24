package me.owdding.patches.actions

import com.google.gson.JsonElement
import me.owdding.ktcodecs.Compact
import me.owdding.ktcodecs.GenerateCodec

@GenerateCodec
data class Remove(
    val path: String,
    @Compact val target: List<String>,
    override val required: Boolean,
) : PatchAction {
    override fun apply(element: JsonElement) {

    }
}