package com.airtable.interview.airtableschedule.repositories

import com.airtable.interview.airtableschedule.models.Event
import com.airtable.interview.airtableschedule.models.HistoricDatasets
import com.airtable.interview.airtableschedule.models.SampleTimelineItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * A store for data related to events. Currently, this just returns sample data.
 */
// EventDataRepository.kt
interface EventDataRepository {
    fun getTimelineItems(dataset: String): Flow<List<Event>>
}

class EventDataRepositoryImpl : EventDataRepository {
    override fun getTimelineItems(dataset: String): Flow<List<Event>> {
        val data = when (dataset) {
            "WW2" -> HistoricDatasets.ww2
            "Moon" -> HistoricDatasets.moon
            else -> HistoricDatasets.sample
        }
        return flowOf(data)
    }
}
