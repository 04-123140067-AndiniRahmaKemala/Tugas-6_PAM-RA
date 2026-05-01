# News Reader App - Tugas 6 PAM RA

A simple News Reader application built with Compose Multiplatform.

## Features
- Fetch news from **Spaceflight News API (v4)**.
- List articles with Title, Summary, and Image.
- Detail screen for each article.
- Pull to Refresh functionality.
- Proper state handling: Loading, Success, and Error.
- Repository pattern for API calls.

## Tech Stack
- **Compose Multiplatform**: UI framework.
- **Ktor**: Networking client.
- **Kotlinx Serialization**: JSON parsing.
- **Coil 3**: Image loading.
- **Navigation Compose**: Screen navigation.
- **MVVM Architecture**: State management with ViewModel.

## API Used
- **Spaceflight News API**: `https://api.spaceflightnewsapi.net/v4/articles/`

## Screenshots
*Note: Please run the app to see the states in action.*
1. **Loading State**: Shows a CircularProgressIndicator.
2. **Success State**: Displays a list of news cards.
3. **Error State**: Displays an error message with a Retry button.
4. **Refresh**: Use the Refresh icon in the TopBar or Pull to Refresh.

## How to Run
1. Open the project in Android Studio.
2. Sync Gradle.
3. Run the `composeApp` on an Android emulator or device.

---
**Branch**: `week-6`
