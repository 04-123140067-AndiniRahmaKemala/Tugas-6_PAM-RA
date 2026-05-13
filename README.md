# News Reader App - Tugas 6 PAM RA

A Premium News Reader application built with **Compose Multiplatform** following modern Android development standards.

## Dokumentasi Visual

|Read News | Favorite | Home | Search news |
| :---: | :---: | :---: | :---: |
|<img width="335" height="709" alt="Screenshot 2026-05-02 004237" src="https://github.com/user-attachments/assets/7ee29e3d-01f5-4a48-8e77-e37e6f7fc2a6" />|<img width="324" height="714" alt="Screenshot 2026-05-02 004400" src="https://github.com/user-attachments/assets/e643a0e0-6983-4483-8696-508693c597e1" />|<img width="328" height="710" alt="Screenshot 2026-05-02 004432" src="https://github.com/user-attachments/assets/57465464-1de4-43b1-af70-7dfd83a8e9d1" />|<img width="328" height="714" alt="Screenshot 2026-05-02 004257" src="https://github.com/user-attachments/assets/4b56f98c-5c72-4171-8adb-ba5f5cd8a603" />|<img width="330" height="695" alt="Screenshot 2026-05-02 004319" src="https://github.com/user-attachments/assets/9133c5e9-0af0-4e1d-a60c-2546ee63ee67" />|


##  Video Demo
Video demo fitur aplikasi dapat diakses melalui tautan berikut : https://youtube.com/shorts/tY5lW042NF0?si=Pq2CzP6IRISevBVE

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
