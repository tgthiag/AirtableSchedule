package com.airtable.interview.airtableschedule

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import com.airtable.interview.airtableschedule.timeline.TimelineScreen
import com.airtable.interview.airtableschedule.theme.AirtableScheduleTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier


class MainActivity : AppCompatActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AirtableScheduleTheme {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text("Timeline") }
                        )
                    }
                ) { padding ->
                    TimelineScreen(modifier = Modifier.padding(padding))
                }
            }
        }
    }
}
