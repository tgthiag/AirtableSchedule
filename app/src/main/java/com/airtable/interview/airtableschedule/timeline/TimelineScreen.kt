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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airtable.interview.airtableschedule.models.Event
import com.airtable.interview.airtableschedule.models.assignLanes
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

// Screen that shows all events in a timeline
@Composable
fun TimelineScreen(
    dataset: String = "Sample", modifier: Modifier = Modifier
) {
    val viewModel: TimelineViewModel = viewModel(
        key = dataset,
        factory = TimelineViewModelFactory(dataset) // need a factory
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    // zoom: pixels per day (remember across recompositions & dataset switches)
    var pxPerDay by rememberSaveable(dataset) { mutableFloatStateOf(TimeScale.DEFAULT_PX_PER_DAY) }
    LaunchedEffect(dataset, uiState.events) {
        val events = uiState.events
        if (events.isNotEmpty()) {
            val (minD, maxD) = computeBounds(events)
            val days = inclusiveDaysBetween(minD, maxD)
            // target max total width ≈ 12_000 dp. Clamp zoom to [6..80].
            val targetPxPerDay = (12_000f / days).coerceIn(6f, 20f)
            pxPerDay = targetPxPerDay
        }
    }
    TimelineView(
        events = uiState.events,
        pxPerDay = pxPerDay,
        onChangePxPerDay = { pxPerDay = it },
        modifier = modifier
    )
}

// Timeline with swimlanes and a header with days
@Composable
private fun TimelineView(
    events: List<Event>, pxPerDay: Float,onChangePxPerDay: (Float) -> Unit, modifier: Modifier = Modifier
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
    val scale = buildTimeScale(events, pxPerDay = pxPerDay) //create a time scale (days -> pixels) default pxPerDay = 24
    val hScroll = rememberScrollState()
    val vScroll = rememberScrollState()

    Column(modifier = modifier.fillMaxSize().verticalScroll(vScroll)                            // ← vertical scroll
        .padding(bottom = 12.dp)) {
        ZoomBar(value = pxPerDay, onChange = onChangePxPerDay)  // ← zoom control
        TimelineHeader(scale = scale, scrollState = hScroll) // top header with days
        HorizontalDivider(thickness = 1.dp)
        Spacer(Modifier.height(8.dp))

        // Render each lane
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            lanes.forEach { lane ->
                LaneRow(
                    events = lane, scale = scale, scrollState = hScroll
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
    val totalWidthDp = (scale.totalDays * scale.pxPerDay).dp
    val headerHeight = 32.dp

    // choose step size (how many days each "tick box" spans)
    val stepDays = when {
        scale.pxPerDay >= 48f -> 1   // daily
        scale.pxPerDay >= 24f -> 3   // every 3 days
        scale.pxPerDay >= 16f -> 7   // weekly
        scale.totalDays > 5000 -> 30 // monthly for very long spans
        else -> 14                   // biweekly
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .requiredWidth(totalWidthDp.coerceAtMost(12000.dp)),
                horizontalArrangement = Arrangement.spacedBy(0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        var dayIndex = 0
        while (dayIndex < scale.totalDays) {
            val date = dateAtOffset(scale.minDate, dayIndex)
            val isMonthStart = date.date == 1

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
                    .width((stepDays * scale.pxPerDay).dp)
                    .height(headerHeight),
                contentAlignment = Alignment.BottomCenter
            ) {
                // Label at each step; content is lighter when zoomed out
                val showDay = stepDays <= 7
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = java.text.SimpleDateFormat("MMM", Locale.getDefault()).format(date),
                        style = MaterialTheme.typography.labelSmall
                    )
                    if (showDay) {
                        Text(
                            text = java.text.SimpleDateFormat("d", Locale.getDefault()).format(date),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
            dayIndex += stepDays
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
            .requiredWidth(totalWidthDp.coerceAtMost(12000.dp)),
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
    val chipWidth = if (widthDp < 12.dp) 12.dp else widthDp
    Surface(
        tonalElevation = 1.dp,
        shadowElevation = 0.dp,
        color = MaterialTheme.colorScheme.primaryContainer, // // higher contrast
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer, // // readable text
        modifier = Modifier
            .width(chipWidth)
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
class TimelineViewModelFactory(
    private val dataset: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimelineViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TimelineViewModel(dataset) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
@Composable
private fun ZoomBar(value: Float, onChange: (Float) -> Unit) {
    Column(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Scale")
            Text("${value.toInt()} px/day")
        }
        // choose a sensible range for day width
        Slider(
            value = value,
            onValueChange = onChange,
            valueRange = 6f..80f   // zoom out … zoom in
        )
    }
}
