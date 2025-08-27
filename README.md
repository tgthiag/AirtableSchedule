# Airtable Timeline Assignment

## Project Structure
- **MainActivity.kt** → Entry point; sets up the `Scaffold` with logo, title, dataset selector, and timeline screen.
- **models/** → Core data classes and datasets (`Event`, `HistoricDatasets`, `SampleTimelineItems`).
- **repositories/** → Repository abstraction returning timeline events (`EventDataRepository`).
- **theme/** → Compose Material 3 theme setup (colors, typography, shapes).
- **timeline/** → All timeline logic:
  - `TimelineScreen` → Main composable, connects ViewModel to UI.
  - `TimelineViewModel` / `TimelineUiState` → State management.
  - `TimeScale` → Date math, pixel-per-day scaling utilities.
  - UI pieces → Header, lane rows, event chips, zoom slider.
- **res/** → Android resources: drawables (icons, logo), values (colors, strings, dimens, themes).
- **Gradle setup** → Kotlin `2.0.21`, Compose UI `1.8.3`, Material3 `1.3.2`, AGP `8.9.1`.

---

## What Was Delivered
- **Jetpack Compose timeline** with:
  - Dataset picker (**Sample / WW2 / Moon**)
  - **Swimlane layout** (packs non-overlapping events per lane)
  - **Horizontal + vertical scrolling**
  - **Zoom control** (pixels-per-day slider)
  - **Adaptive header ticks** (day / 3-day / week / month, based on zoom)
  - **Auto-scaling** for long ranges (keeps total width manageable)
  - **Minimum chip width** so 1-day events remain visible

---

## Implementation Notes
- Designed a **simple model**: `Event(id, startDate, endDate, name)`.
- Used **greedy lane assignment** for compact, predictable packing.
- Introduced a single **time scale (px/day)** → spacing, widths, and header ticks all derive from one source of truth.
- Header density dynamically adapts to zoom level for **clarity vs. clutter**.
- Preferred **Compose scroll + straightforward layout** over virtualization for code clarity.

---

## Time Spent
~4–5 hours (implementation + polish).

---

## How to Run
1. Open in **Android Studio** (latest stable).
2. Let Gradle sync and **Run** the `app` module on an emulator or device.  
   *(Kotlin 2.0.21, Compose UI 1.8.3, Material3 1.3.2, AGP 8.9.1.)*

---

## Usage
- Pick a dataset at the top.
- Use the **Scale** slider to zoom (header labels adapt automatically).
- Scroll **horizontally** through the timeline and **vertically** across lanes.

---

## Possible Improvements
- **New datasets** (e.g. modern conflicts, space race, tech history).
- **Optimal lane packing** (e.g. interval graph coloring).
- **Pinch-to-zoom**, mini-map, richer event details/interactions.
- Switch to `java.time` (`LocalDate`) for cleaner date handling.
- Improve **theming, accessibility**, and add tests for:
  - Lane assignment math
  - Scale calculations
  - Header labeling
