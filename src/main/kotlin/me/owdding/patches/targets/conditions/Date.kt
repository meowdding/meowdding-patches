package me.owdding.patches.targets.conditions

import com.ibm.icu.text.SimpleDateFormat
import com.ibm.icu.util.Calendar
import com.ibm.icu.util.TimeZone
import com.ibm.icu.util.ULocale
import kotlinx.datetime.Clock
import me.owdding.ktcodecs.Compact
import me.owdding.ktcodecs.FieldName
import me.owdding.ktcodecs.GenerateCodec
import me.owdding.patches.targets.Target
import me.owdding.patches.targets.TargetCondition
import me.owdding.patches.targets.TargetConditions

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

    override fun test(target: Target) = date in validWhen
}
