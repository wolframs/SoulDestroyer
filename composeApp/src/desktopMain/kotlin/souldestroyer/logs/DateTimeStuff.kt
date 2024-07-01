package souldestroyer.logs

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

val wfDateTimeFormat = LocalTime.Format {
    hour()
    char(':')
    minute()
    char(':')
    second()
    char('.')
    secondFraction(fixedLength = 2)
}

fun getLocalDateTimeNow(doApply: ((Instant) -> Instant)? = null): LocalDateTime {
    var now = Clock.System.now()

    if (doApply != null) {
        now = doApply(now)
    }

    return now.toLocalDateTime(
        TimeZone.currentSystemDefault()
    )
}