package com.airtable.interview.airtableschedule.models

import java.util.Calendar
import java.util.Date

object HistoricDatasets {
    private fun d(year: Int, month: Int, day: Int): Date {
        val cal = Calendar.getInstance()
        cal.isLenient = false
        cal.set(year, month, day, 0, 0, 0)
        return cal.time
    }

    //  World War II timeline
    val ww2: List<Event> = listOf(
        Event(101, d(1939, 8, 1), d(1939, 8, 30), "Invasion of Poland"),
        Event(102, d(1939, 8, 3), d(1939, 8, 3), "Britain and France declare war on Germany"),
        Event(103, d(1939, 10, 30), d(1940, 2, 13), "Winter War (Soviet Union vs Finland)"),
        Event(104, d(1940, 3, 9), d(1940, 5, 9), "Invasion of Denmark and Norway"),
        Event(105, d(1940, 4, 10), d(1940, 5, 25), "Battle of France and Fall of Paris"),
        Event(106, d(1940, 6, 10), d(1940, 9, 30), "Battle of Britain"),
        Event(107, d(1940, 8, 27), d(1940, 8, 27), "Tripartite Pact (Germany, Italy, Japan)"),
        Event(108, d(1941, 5, 22), d(1941, 11, 5), "Operation Barbarossa (Invasion of USSR)"),
        Event(109, d(1941, 11, 7), d(1941, 11, 7), "Attack on Pearl Harbor"),
        Event(110, d(1942, 0, 20), d(1942, 0, 20), "Wannsee Conference (Final Solution)"),
        Event(111, d(1942, 5, 4), d(1942, 5, 7), "Battle of Midway"),
        Event(112, d(1942, 7, 23), d(1943, 1, 2), "Battle of Stalingrad"),
        Event(113, d(1942, 10, 8), d(1942, 10, 16), "Operation Torch (North Africa landings)"),
        Event(114, d(1943, 6, 9), d(1943, 7, 17), "Allied invasion of Sicily"),
        Event(115, d(1943, 8, 3), d(1943, 8, 3), "Italy surrenders"),
        Event(116, d(1944, 5, 6), d(1944, 5, 6), "D-Day (Normandy Landings)"),
        Event(117, d(1944, 7, 25), d(1944, 7, 25), "Liberation of Paris"),
        Event(118, d(1944, 11, 16), d(1945, 0, 25), "Battle of the Bulge"),
        Event(119, d(1945, 1, 4), d(1945, 1, 11), "Yalta Conference"),
        Event(120, d(1945, 3, 16), d(1945, 4, 2), "Battle of Berlin"),
        Event(121, d(1945, 3, 30), d(1945, 3, 30), "Hitler commits suicide"),
        Event(122, d(1945, 4, 8), d(1945, 4, 8), "Victory in Europe Day (VE Day)"),
        Event(123, d(1945, 7, 6), d(1945, 7, 9), "Atomic bombings of Hiroshima & Nagasaki"),
        Event(124, d(1945, 8, 2), d(1945, 8, 2), "Japan surrenders (VJ Day) – End of WWII")
    )

    //  Space Race – Apollo 11 example
    val moon: List<Event> = listOf(
        Event(201, d(1969, 6, 16), d(1969, 6, 24), "Apollo 11 Mission"),
        Event(202, d(1969, 6, 20), d(1969, 6, 20), "Moon Landing"),
        Event(203, d(1969, 6, 24), d(1969, 6, 24), "Apollo 11 Splashdown")
    )

    // Ukraine timeline
    val ukraine: List<Event> = listOf(
        Event(301, d(1991, 7, 24), d(1991, 7, 24), "Declaration of Independence"),
        Event(302, d(1991, 11, 1), d(1991, 11, 1), "Referendum confirms independence"),
        Event(303, d(2004, 10, 22), d(2004, 11, 8), "Orange Revolution (protests against election fraud)"),
        Event(304, d(2014, 1, 18), d(2014, 1, 22), "Euromaidan protests escalate"),
        Event(305, d(2014, 1, 22), d(2014, 1, 22), "Yanukovych flees, interim govt established"),
        Event(306, d(2014, 2, 20), d(2014, 2, 20), "Russia annexes Crimea"),
        Event(307, d(2014, 3, 6), d(2014, 3, 6), "War begins in Donbas"),
        Event(308, d(2019, 3, 30), d(2019, 3, 30), "Volodymyr Zelenskyy elected President"),
        Event(309, d(2022, 1, 24), d(2022, 1, 24), "Russia launches full-scale invasion"),
        Event(310, d(2022, 2, 24), d(2022, 2, 24), "Kyiv under attack, martial law declared"),
        Event(311, d(2022, 3, 29), d(2022, 3, 29), "Russia withdraws from Kyiv"),
        Event(312, d(2022, 8, 29), d(2022, 10, 11), "Ukraine counter-offensive in Kherson"),
        Event(313, d(2022, 10, 10), d(2022, 10, 10), "Mass missile strikes across Ukraine"),
        Event(314, d(2023, 5, 1), d(2023, 8, 30), "Summer counteroffensive in Zaporizhzhia"),
        Event(315, d(2024, 4, 15), d(2024, 4, 20), "US & EU announce largest military aid package"),
        Event(316, d(2024, 7, 7), d(2024, 7, 7), "NATO summit pledges long-term support"),
        Event(317, d(2025, 0, 15), d(2025, 0, 15), "Winter front stabilizes, high attrition warfare continues")
    )

    val sample = SampleTimelineItems.timelineItems
}
