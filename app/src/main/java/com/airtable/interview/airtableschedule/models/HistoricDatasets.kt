package com.airtable.interview.airtableschedule.models

import java.util.Date

object HistoricDatasets {
    private val y1939 = 1939 - 1900
    private val y1969 = 1969 - 1900

    val ww2: List<Event> = listOf(
        Event(101, Date(y1939, 8, 1), Date(y1939, 8, 30), "Invasion of Poland"),
        Event(102, Date(y1939, 10, 1), Date(y1939, 5, 10), "Phoney War")
    )

    val moon: List<Event> = listOf(
        Event(201, Date(y1969, 6, 16), Date(y1969, 6, 24), "Apollo 11 Mission"),
        Event(202, Date(y1969, 6, 20), Date(y1969, 6, 20), "Moon Landing")
    )

    val sample = SampleTimelineItems.timelineItems
}
