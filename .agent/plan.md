# Project Plan

App that creates links for scriptures (standard works of The Church of Jesus Christ of Latter-day Saints). User can select multiple verses in a chapter to get a link to those specific verses. Main pain point is official app currently only navigates to one verse if user searches for a scripture. Multiple verses are possible but only with in app links or a manually created link. Basic material 3 app that works on as many devices as possible.

## Project Brief

# Versal Project Brief

Versal is a focused utility app designed to solve a specific
 pain point for members of The Church of Jesus Christ of Latter-day Saints: the difficulty of creating and sharing links to multiple specific
 scripture verses. While the official Gospel Library app is excellent for reading, generating deep links for verse ranges often requires manual effort.
 Versal simplifies this process with a Material 3 interface that adapts to any device.

## Features

*   **Scripture
 Selection & Browsing**: A streamlined interface to navigate through the Standard Works (Old Testament, New Testament, Book of Mormon
, Doctrine and Covenants, and Pearl of Great Price) to locate specific chapters.
*   **Multi-Verse Selector**: Allows users to select multiple verses or a specific range within a chapter through a simple checklist or drag-to-select
 interaction.
*   **Instant Link Generation**: Automatically generates a formatted deep link (compatible with the Gospel Library app and web browsers
) for the exact verses selected.
*   **One-Tap Share & Copy**: A primary action button to quickly
 copy the generated link to the clipboard or share it directly to messaging and social media apps.

## High-Level Technical
 Stack

*   **Kotlin**: The core programming language for the application logic.
*   **Jetpack Compose (
Material 3)**: For building a modern, vibrant UI that adheres to the latest Material Design 3 guidelines.

*   **Jetpack Navigation 3**: A state-driven navigation architecture to manage app flow and deep linking.
*
   **Compose Material Adaptive Library**: Used to implement a responsive layout (e.g., List-Detail or Supporting Pane
) that works seamlessly on phones, foldables, and tablets.
*   **Kotlinx Coroutines**: For handling
 asynchronous tasks and maintaining a smooth UI thread.
*   **Kotlinx Serialization**: For efficient data handling and state preservation.

## Implementation Steps

### Task_1_Setup_Data_and_Nav: Define the scripture data models (Volumes, Books, Chapters) and set up the Navigation 3 and Adaptive Layout framework.
- **Status:** DONE
- **Acceptance Criteria:**
  - Data models for Standard Works are defined
  - Navigation 3 is implemented with a basic adaptive layout skeleton
  - The project builds successfully
- **StartTime:** 2026-06-19 22:52:41 MDT

### Task_2_Scripture_Browsing_UI: Implement the UI for browsing the Standard Works, allowing users to select a volume, book, and chapter.
- **Status:** DONE
- **Acceptance Criteria:**
  - User can browse through Volumes and Books to select a Chapter
  - UI follows Material 3 guidelines
  - Navigation between levels is functional

### Task_3_Verse_Selector_and_Link_Gen: Develop the verse selection screen and the logic to generate and share Gospel Library deep links.
- **Status:** DONE
- **Acceptance Criteria:**
  - Verse selection UI supports multiple selections or ranges
  - Gospel Library deep links are correctly generated based on selection
  - Copy to clipboard and Share actions are functional

### Task_4_Theming_and_Verification: Apply Material 3 theming (vibrant colors, dark/light mode), implement edge-to-edge, create an adaptive icon, and perform final verification.
- **Status:** DONE
- **Acceptance Criteria:**
  - Material 3 theme with vibrant color scheme is applied
  - Adaptive app icon is created
  - App supports edge-to-edge display
  - Final verification: app is stable, no crashes, and meets all requirements
