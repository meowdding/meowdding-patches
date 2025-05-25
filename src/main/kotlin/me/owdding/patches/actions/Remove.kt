package me.owdding.patches.actions

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import me.owdding.ktcodecs.Compact
import me.owdding.ktcodecs.GenerateCodec
import me.owdding.patches.utils.getPath

@GenerateCodec
data class Remove(
    val path: String,
    @Compact val target: List<String>,
    override val required: Boolean,
) : PatchAction {
    override fun apply(element: JsonElement) {
        val targetElement = element.getPath(path, true)
        when (targetElement) {
            is JsonObject -> {
                target.forEach {
                    targetElement.remove(it)
                }
            }

            is JsonArray -> {
                target.forEach { value ->
                    targetElement.removeAll { it.toString() == value }
                }
            }

            is JsonElement -> throw UnsupportedOperationException("Can't modify json element $target")
        }
    }
}