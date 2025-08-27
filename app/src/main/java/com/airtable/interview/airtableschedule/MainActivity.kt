package com.airtable.interview.airtableschedule

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import com.airtable.interview.airtableschedule.timeline.TimelineScreen
import com.airtable.interview.airtableschedule.theme.AirtableScheduleTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : AppCompatActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AirtableScheduleTheme {
                Scaffold { padding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "App logo",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(start = 8.dp, top = 0.dp)
                                .align(Alignment.Start)
                        )
                        Text(
                            text = "Timeline",
                            fontSize = 22.sp,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                        TimelineScreen(
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}
