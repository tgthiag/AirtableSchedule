// app/src/main/java/com/airtable/interview/airtableschedule/timeline/TimelineScreen.kt
package com.airtable.interview.airtableschedule.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airtable.interview.airtableschedule.models.Event
import com.airtable.interview.airtableschedule.models.assignLanes

// Screen that shows all events in a timeline
@Composable
fun TimelineScreen(
    viewModel: TimelineViewModel = viewModel(), modifier: Modifier = Modifier
) {
    // collect state from the view model
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    // show the timeline view with the events
    TimelineView(uiState.events, modifier)
}

// Timeline with swimlanes and a header with days
@Composable
private fun TimelineView(
    events: List<Event>, modifier: Modifier = Modifier
) {
    // if no events, show a simple text
    if (events.isEmpty()) {
        Box(
            modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("No events")
        }
        return
    }

    val lanes = assignLanes(events) //organize events into lanes (no overlap in the same lane)
    val scale = buildTimeScale(events) //create a time scale (days -> pixels) default pxPerDay = 24
    val scroll = rememberScrollState()  // scroll state to move horizontally

    Column(modifier = modifier.fillMaxSize()) {
        TimelineHeader(scale = scale, scrollState = scroll) // top header with days
        HorizontalDivider(thickness = 1.dp)
        Spacer(Modifier.height(8.dp))

        // Render each lane
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()
        ) {
            lanes.forEach { lane ->
                LaneRow(
                    events = lane, scale = scale, scrollState = scroll
                )
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

// Simple header with ticks for each day
@Composable
private fun TimelineHeader(
    scale: TimeScale, scrollState: androidx.compose.foundation.ScrollState
) {
    // total width in dp = totalDays * pxPerDay
    val totalWidthDp = (scale.totalDays * scale.pxPerDay).dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .widthIn(min = totalWidthDp),
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(scale.totalDays) { dayIndex ->
            Box(
                modifier = Modifier
                    .width(scale.pxPerDay.dp)
                    .height(40.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                // show label only every 5 days
                if (dayIndex % 5 == 0) {
                    Text(
                        text = dayIndex.toString(), style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

// One lane: events are placed with spacing based on date
@Composable
private fun LaneRow(
    events: List<Event>, scale: TimeScale, scrollState: androidx.compose.foundation.ScrollState
) {
    // Total width for all days
    val totalWidthDp = (scale.totalDays * scale.pxPerDay).dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 12.dp)
            .height(IntrinsicSize.Min)
            .widthIn(min = totalWidthDp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        var lastRightEdgeDays = 0

        events.forEach { event ->
            val offsetDays = offsetDaysFrom(scale.minDate, event)
            val duration = durationDays(event)

            val gapDays = (offsetDays - lastRightEdgeDays).coerceAtLeast(0)
            if (gapDays > 0) {
                Spacer(modifier = Modifier.width((gapDays * scale.pxPerDay).dp))
            }

            EventChip(
                event = event, widthDp = (duration * scale.pxPerDay).dp
            )

            lastRightEdgeDays = offsetDays + duration
            // little spacer when in the same line
            Spacer(Modifier.width(4.dp))
        }
    }
}

// Minimal visual for an event
@Composable
private fun EventChip(
    event: Event, widthDp: androidx.compose.ui.unit.Dp
) {
    Surface(
        tonalElevation = 2.dp,
        shadowElevation = 1.dp,
        modifier = Modifier
            .width(widthDp)
            .height(36.dp)
            .clip(MaterialTheme.shapes.small)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 8.dp), contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = event.name,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
