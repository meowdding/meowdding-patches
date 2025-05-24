package me.owdding.patches.utils

import com.google.gson.JsonElement
import com.ibm.icu.util.TimeZone
import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import me.owdding.ktcodecs.IncludedCodec
import me.owdding.patches.generated.MoewddingPatchesCodecs
import me.owdding.patches.targets.Target
import me.owdding.patches.targets.TargetCondition
import me.owdding.patches.targets.conditions.VersionCondition
import net.minecraft.client.renderer.item.properties.select.LocalTime
import net.minecraft.util.ExtraCodecs
import java.util.function.Function
import java.util.function.Supplier

object Codecs {

    val TARGET_CONDITION_CODEC: Codec<TargetCondition> by lazy {
        Codec.either(
            MoewddingPatchesCodecs.getCodec<TargetCondition>(),
            Codec.STRING.xmap(::VersionCondition, VersionCondition::asString),
        ).xmap({ Either.unwrap(it) }, { Either.left(it) })
    }
    @IncludedCodec(named = "target_condition")
    val TARGETS: Codec<Map<Target, TargetCondition>> = Codec.unboundedMap(MoewddingPatchesCodecs.getCodec<Target>(), TARGET_CONDITION_CODEC)

    @IncludedCodec
    val TIME_ZONE_CODEC: Codec<TimeZone> = LocalTime.VALUE_CODEC.comapFlatMap(Function { string: String? ->
        val timeZone = TimeZone.getTimeZone(string)
        if (timeZone == TimeZone.UNKNOWN_ZONE) DataResult.error(Supplier { "Unknown timezone: $string" }) else DataResult.success(
            timeZone
        )
    }, Function { obj: TimeZone? -> obj!!.id })

    @IncludedCodec
    val JSON_CODEC: Codec<JsonElement> = ExtraCodecs.JSON

}