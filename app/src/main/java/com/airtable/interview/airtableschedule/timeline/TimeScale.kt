package com.airtable.interview.airtableschedule.timeline

import com.airtable.interview.airtableschedule.models.Event
import java.util.Date
import kotlin.math.max

/**
 * Simple time scale for the timeline.
 * - Pixel-per-day value that the UI can read.
 * - Compute global bounds (min/max date) from the dataset.
 * - We expose helpers to get durations and horizontal offsets in days.
 */

data class TimeScale(
    val minDate: Date, val maxDate: Date, val pxPerDay: Float = DEFAULT_PX_PER_DAY
) {
    companion object {
        const val DEFAULT_PX_PER_DAY = 24f
    }

    // Total days spanned by the entire dataset.
    val totalDays: Int = inclusiveDaysBetween(minDate, maxDate)

    // Converts a number of days into pixels using the current scale.
    fun daysToPx(days: Int): Float = days * pxPerDay
}

// Compute the global bounds (min, max) for a list of events.
fun computeBounds(events: List<Event>): Pair <Date, Date> {
    require(events.isNotEmpty()) { "computeBounds requires at least one event" }
    var minDate = events.first().startDate
    var maxDate = events.first().endDate

    for (e in events) {
        minDate = if (e.startDate.before(minDate)) e.startDate else minDate
        maxDate = if (e.endDate.after(maxDate)) e.endDate else maxDate
    }
    return minDate to maxDate
}
// Returns the number of days between two dates, inclusive of both ends.
fun inclusiveDaysBetween(start: Date, end: Date): Int {
    // drop sub-day precision by flooring to day-sized buckets
    val msPerDay = 24L * 60 * 60 * 1000
    val startDays = floorDiv(start.time, msPerDay)
    val endDays = floorDiv(end.time, msPerDay)
    return (endDays - startDays + 1).toInt().coerceAtLeast(1)
}

// Duration of an event in days (inclusive).
fun durationDays(event: Event): Int = inclusiveDaysBetween(event.startDate, event.endDate)


//Offset (in days) from the global minDate to the event's startDate.

fun offsetDaysFrom(minDate: Date, event: Event): Int {
    val msPerDay = 24L * 60 * 60 * 1000
    val startDays = floorDiv(minDate.time, msPerDay)
    val eventDays = floorDiv(event.startDate.time, msPerDay)
    return max(0, (eventDays - startDays).toInt())
}


// Build a TimeScale for a given dataset.

fun buildTimeScale(
    events: List<Event>, pxPerDay: Float = TimeScale.DEFAULT_PX_PER_DAY
): TimeScale {
    val (minD, maxD) = computeBounds(events)
    return TimeScale(minD, maxD, pxPerDay)
}

//Safe integer floor division for positive/negative ms values.

private fun floorDiv(x: Long, y: Long): Long {
    val q = x / y
    val r = x % y
    return if (r != 0L && ((x xor y) < 0)) q - 1 else q
}
