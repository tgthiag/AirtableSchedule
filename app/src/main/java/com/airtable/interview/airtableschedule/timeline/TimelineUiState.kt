package com.airtable.interview.airtableschedule.timeline

import com.airtable.interview.airtableschedule.models.Event

/**
 * UI state for the timeline screen.
 */
data class TimelineUiState(
    val events: List<Event> = emptyList(),
)
