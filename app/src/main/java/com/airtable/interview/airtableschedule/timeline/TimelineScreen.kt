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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airtable.interview.airtableschedule.models.Event
import com.airtable.interview.airtableschedule.models.assignLanes
import java.text.SimpleDateFormat
import java.util.Locale

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
    // date formatter (e.g. "Feb 1")
    val dayFormat = remember { SimpleDateFormat("MMM d", Locale.getDefault()) }
    val headerHeight = 32.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState) // allow horizontal scroll
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .widthIn(min = totalWidthDp), // make sure width matches total days
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(scale.totalDays) { index ->
            val date = dateAtOffset(scale.minDate, index)
            // check if this is the first day of a month
            val isMonthStart = date.date == 1 // legacy Date API, for test

            // draw vertical line BEFORE the label if it's the start of a month
            if (isMonthStart) {
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(headerHeight)
                        .padding(vertical = 4.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )
            }
            Box(
                modifier = Modifier
                    .width(scale.pxPerDay.dp)
                    .height(headerHeight),
                contentAlignment = Alignment.BottomCenter
            ) {
                // show label every 7 days and at first day of month
                if (isMonthStart || index % 7 == 0) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = SimpleDateFormat("MMM", Locale.getDefault()).format(date),
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            text = SimpleDateFormat("d", Locale.getDefault()).format(date),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
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
            .height(40.dp)
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
    event: Event, widthDp: Dp
) {
    Surface(
        tonalElevation = 1.dp,
        shadowElevation = 0.dp,
        color = MaterialTheme.colorScheme.primaryContainer, // // higher contrast
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer, // // readable text
        modifier = Modifier
            .width(widthDp)
            .height(36.dp)
            .clip(MaterialTheme.shapes.small)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.CenterStart
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
