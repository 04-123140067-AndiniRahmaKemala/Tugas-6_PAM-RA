# News Reader App - Tugas 6 PAM RA

A Premium News Reader application built with **Compose Multiplatform** following modern Android development standards.

## 🚀 Features
- **Fetch News API**: Real-time news fetching using Ktor Client.
- **Premium UI/UX**: Modern dark theme with Material 3, Glassmorphism, and Fluid Animations.
- **Breaking News Hero**: Highlighted first article with immersive design.
- **Category Filter**: News categorization (Technology, Sports, Business, etc.).
- **Live Search**: Instant news search functionality.
- **Bookmark System**: Save articles to read later.
- **Reading Time Estimation**: Automatic calculation of article read time.
- **Pull to Refresh**: Seamless data update.
- **Proper State Management**: Pulse Skeleton Loading, Success, and Interactive Error states.
- **Repository Pattern**: Clean architecture for API calls.

## 🛠️ API & Security
This application uses the **NewsAPI** to fetch global headlines.

- **Provider**: [NewsAPI.org](https://newsapi.org/)
- **Endpoints Used**: `https://newsapi.org/v2/top-headlines`
- **API Key**: `e318b8beda7e4a4ab8c6c3bf96424540`

## 📸 App States & Screenshots
Below are the visual states implemented in the application:

### 1. Loading State (Pulse Skeleton)
When data is being fetched, the app displays a modern pulse skeleton animation (shimmer-like) to provide a smooth visual transition.

### 2. Success State (Discover Screen)
Displays the **Hero Slider** for breaking news, **Horizontal Category Chips**, and the **Latest Stories** list with rich thumbnails and metadata.

### 3. Detail Screen (Immersive View)
A deep dive into the article with:
- **Parallax Header Image**.
- **Reading Progress Bar** at the top.
- **Floating Action Button** to open the full story in a browser.
- **Quick Bookmark** toggle.

### 4. Search & Filter State
Interactive search bar and active category filtering to help users find specific content.

### 5. Error & Empty State
Professional warning UI with a "Try Again" action button when the connection fails or no results are found.

## 💻 Tech Stack
- **Compose Multiplatform**: Shared UI for Android & Desktop.
- **Ktor Client**: Asynchronous networking.
- **Kotlinx Serialization**: Type-safe JSON parsing.
- **Coil 3**: Image loading and caching.
- **Navigation Compose**: Type-safe screen routing.
- **MVVM Architecture**: State-driven UI with ViewModel.

## 🏃 How to Run
1. Open the project in **Android Studio (Ladybug or newer)**.
2. Sync the project with Gradle files.
3. Ensure you have an internet connection to fetch the news.
4. Run the `:composeApp` module on an Android emulator or physical device.

---
**Author**: Tugas 6 PAM RA
**Status**: Completed & Fixed (Optimized Gradle 8.11.1)
