package me.owdding.patches.targets.conditions

import com.ibm.icu.text.SimpleDateFormat
import com.ibm.icu.util.Calendar
import com.ibm.icu.util.TimeZone
import com.ibm.icu.util.ULocale
import kotlin.time.Clock
import me.owdding.ktcodecs.Compact
import me.owdding.ktcodecs.FieldName
import me.owdding.ktcodecs.GenerateCodec
import me.owdding.patches.targets.Target
import me.owdding.patches.targets.TargetCondition
import me.owdding.patches.targets.TargetConditions
import net.fabricmc.loader.api.ModContainer
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@GenerateCodec
data class Date(
    val pattern: String,
    val timeZone: TimeZone = TimeZone.getDefault(),
    @Compact @FieldName("valid_when") val validWhen: List<String>,
) : TargetCondition {
    val date: String

    init {
        val uLocale = ULocale("en_us")
        val calendar = Calendar.getInstance(timeZone, uLocale)
        val simpleDateFormat = SimpleDateFormat(pattern, uLocale)
        simpleDateFormat.setCalendar(calendar)
        date = simpleDateFormat.format(Clock.System.now())
    }

    override val type = TargetConditions.DATE

    override fun test(target: Target, modContainer: ModContainer) = date in validWhen
}
