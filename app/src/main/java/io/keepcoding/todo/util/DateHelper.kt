package io.keepcoding.todo.util

import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.PeriodType
import org.joda.time.format.ISOPeriodFormat
import org.joda.time.format.PeriodFormat
import java.util.*

object DateHelper {

    // TODO("Improve this method")
    fun calculateTimeAgo(date: Date): String {
        val past = DateTime(date.time)
        val now = DateTime.now()

        val period = Period(
            past,
            now,
            PeriodType
                .dayTime()
                .withSecondsRemoved()
                .withMillisRemoved())

        val formatter =
            PeriodFormat
                .getDefault()
                .withLocale(Locale.ENGLISH)

        val timeAgo = formatter.print(period)
        return timeAgo
    }


}