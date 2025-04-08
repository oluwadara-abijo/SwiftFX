# SwiftFX - Real-Time Currency Converter

SwiftFX is a modern Android application that provides real-time currency conversion and historical exchange rate data. It leverages Kotlin, Jetpack Compose, and other cutting-edge Android technologies to deliver a seamless and responsive user experience.

## Features

*   **Currency Conversion:** Easily convert between different currencies by entering an amount in the source currency.
*   **Historical Data:** View historical exchange rates presented in an interactive graph, allowing you to track trends.
*   **User-Friendly Interface:** Enjoy a clean, intuitive interface built with Jetpack Compose.
*   **Error Handling:** Robust error handling for network issues and invalid data.

## Technologies Used

*   **Kotlin:** The primary programming language for building the app.
*   **Jetpack Compose:** Modern UI toolkit for building native Android UI.
*   **ViewModel:** Part of the Android Architecture Components, used for managing UI-related data in a lifecycle-conscious way.
*   **Coroutines:** For asynchronous programming, enabling non-blocking network requests and other background tasks.
*   **Hilt:** For dependency injection, simplifying the management of dependencies.
* **Kotlin Symbol Processor(KSP)**: For annotation processing.
*   **Retrofit:** For making network requests to fetch exchange rates.
*   **ComposeCharts:** For displaying interactive charts of historical data.
*   **Result API**: For managing the result of an operation.
*   **Gradle:** Build automation system.

## Project Structure

The project follows a clean architecture, separating concerns into distinct layers:

*   **UI Layer:** Contains all Jetpack Compose composables and the `ViewModel`.
*   **Data Layer:** Responsible for fetching data (from network or local storage) and managing the repository.
*   **Network Layer:** Handles network calls using Retrofit.

## Setup Instructions

1.  **Prerequisites:**
    *   Android Studio (latest stable version recommended)
    *   Android SDK
    *   Git (for version control)
    * JDK 17.
2.  **Clone the Repository**
3.  **Open in Android Studio:**
    *   Open Android Studio and select "Open an Existing Project."
    *   Navigate to the directory where you cloned the repository and select the root folder.
4.  **Get an api key from [FixerApi](https://fixer.io/) for free**
    *  Add your api key to the `local.properties` file like this: `FixerApiKey=your-api-key`
4.  **Build the Project:**
    *   In Android Studio, go to `Build` -> `Make Project`.
5.  **Run the App:**
    *   Connect an Android emulator or a physical Android device to your computer.
    *   In Android Studio, click the "Run" button (green play icon) or `Run` -> `Run 'app'`.

## Key Concepts and Libraries

*   **Asynchronous Operations:** Coroutines are used to perform network requests and other long-running tasks without blocking the main thread.
*   **State Management:** Jetpack Compose's state hoisting and the `ViewModel` are used to manage the UI state effectively.
*   **Dependency Injection:** Hilt makes it easy to inject dependencies, improving testability and maintainability.
* **Chart**: [ComposeCharts](https://github.com/ehsannarmani/ComposeCharts) library is used to display an interactive chart.
* **Concurrency**: The code is using `async` and `awaitAll` to have a better performace.
