package me.owdding.patches

import com.google.gson.JsonElement
import com.mojang.serialization.Codec
import me.owdding.ktcodecs.GenerateCodec
import me.owdding.ktcodecs.NamedCodec
import me.owdding.patches.actions.PatchAction
import me.owdding.patches.generated.MoewddingPatchesCodecs
import me.owdding.patches.targets.Target
import me.owdding.patches.targets.TargetCondition
import me.owdding.patches.targets.test
import net.fabricmc.loader.api.ModContainer

@GenerateCodec
data class RepoPatch(
    @NamedCodec("target_condition") val targets: Map<Target, TargetCondition>,
    val action: PatchAction,
    val file: String,
    val ordinal: Int = 0, // sorted ascending -> should only be used if order is important
) {

    fun shouldApply(modContainer: ModContainer, file: String): Boolean = targets
        .filter { it.key.modId == modContainer.metadata.id }
        .test(modContainer) && file == this.file

    fun apply(jsonElement: JsonElement) = action.apply(jsonElement)

    companion object {
        val CODEC: Codec<RepoPatch> = MoewddingPatchesCodecs.getCodec<RepoPatch>()
    }
}