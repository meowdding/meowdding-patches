package me.owdding.patches.actions

import com.google.gson.JsonElement
import me.owdding.ktcodecs.GenerateCodec

@GenerateCodec
data class Insert(
    val path: String,
    val addition: JsonElement,
    val spread: Boolean = false,
    override val required: Boolean,
) : PatchAction {
    override fun apply(element: JsonElement) {
        //JsonPath.compile("t").eva
    }
}