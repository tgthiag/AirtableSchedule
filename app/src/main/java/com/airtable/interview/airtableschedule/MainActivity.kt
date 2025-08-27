package com.airtable.interview.airtableschedule

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
                        // 🔑 hold dataset selection state
                        var selected by remember { mutableStateOf("Sample") }
                        // dataset selector row
                        DatasetSelector(
                            selected = selected,
                            onSelect = { selected = it }
                        )
                        // timeline for chosen dataset
                        TimelineScreen(
                            dataset = selected,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun DatasetSelector(selected: String, onSelect: (String) -> Unit) {
    val options = listOf("Sample", "WW2", "Moon")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        options.forEach { opt ->
            Text(
                text = opt,
                modifier = Modifier
                    .padding(4.dp)
                    .clickable { onSelect(opt) },
                color = if (opt == selected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onBackground
            )
        }
    }
}