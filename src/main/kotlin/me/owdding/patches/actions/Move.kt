package me.owdding.patches.actions

import com.google.gson.JsonElement
import me.owdding.ktcodecs.GenerateCodec
import me.owdding.patches.utils.getPath

@GenerateCodec
data class Move(
    val from: String,
    val to: String,
    override val required: Boolean = false,
) : PatchAction {
    override fun apply(element: JsonElement) {
        val path = element.getPath(from)
        val parent = to.substringBeforeLast(".", "")
        val name = to.substringAfterLast(".")
        element.getPath(parent, true)?.asJsonObject?.add(name, path)
    }
}