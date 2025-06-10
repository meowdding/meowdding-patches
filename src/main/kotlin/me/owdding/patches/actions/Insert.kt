package me.owdding.patches.actions

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import me.owdding.ktcodecs.GenerateCodec
import me.owdding.patches.utils.getPath
import net.minecraft.util.GsonHelper

@GenerateCodec
data class Insert(
    val target: String,
    val addition: JsonElement,
    val spread: Boolean = true,
    override val required: Boolean,
) : PatchAction {
    override val type = PatchActions.INSERT

    override fun apply(element: JsonElement) {
        val targetElement = element.getPath(target, true)
        when (targetElement) {
            is JsonObject -> {
                val addition = GsonHelper.convertToJsonObject(addition, "addition")
                addition.entrySet().forEach { (key, value) ->
                    targetElement.add(key, value)
                }
            }

            is JsonArray -> {
                val addition = GsonHelper.convertToJsonArray(addition, "addition")
                if (spread) {
                    for (jsonElement in addition) {
                        targetElement.add(jsonElement)
                    }
                } else {
                    targetElement.add(addition)
                }
            }

            is JsonElement -> throw UnsupportedOperationException("Can't modify json element $target")
        }
    }
}