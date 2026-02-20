# 🏥 Al-Raqi University Hospital QMS
> **A Modern, Scalable, and High-Performance Queue Management System**

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Compose](https://img.shields.io/badge/UI-Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![License](https://img.shields.io/badge/License-Proprietary-red?style=for-the-badge)

Al-Raqi University Hospital QMS is a sophisticated digital orchestration layer designed to streamline patient flow, automate financial tracking, and provide real-time visibility across hospital departments. From the reception desk to the specialized clinic TV displays, this system ensures a seamless experience for both staff and patients.

---

## 🌟 Key Features

### 🛠️ Dynamic Section Orchestration
Manage the entire hospital structure from a single pane of glass. 
*   **Full CRUD Operations**: Create, Read, Update, and Delete sections (Clinics, Laboratories, X-Ray, etc.) on the fly.
*   **Doctor Assignment**: Link specific medical professionals to departments.
*   **Real-time Pricing**: Configure service costs dynamically in **SDG**.

### 📊 Data Intelligence & Excel Integration
Leverage the power of **Apache POI** for enterprise-grade data handling.
*   **Bulk Ingestion**: Upload hundreds of sections and doctor profiles instantly via Excel.
*   **Automated Templates**: Download pre-formatted `.xlsx` templates for error-free data entry.
*   **Analytical Reporting**: Export detailed customer traffic and financial data for specific timeframes:
    *   🕒 **24 Hours** (Daily Audit)
    *   📅 **7 Days** (Weekly Trends)
    *   🗓️ **1 Month** (Monthly Performance)
    *   📈 **3 Months** (Quarterly Strategy)

### 📺 Digital Signage (Smart TV Modes)
Transform standard displays into intelligent queue monitors.
*   **Reception Overview**: A birds-eye view of all hospital departments, showing active token numbers and queue lengths.
*   **Clinic Focus**: A high-contrast, large-font display optimized for clinic waiting areas, showing "Now Serving" tokens and assigned doctors.

### 🖨️ Thermal Printing Integration
Native support for Sunmi and ESC/POS thermal printers.
*   Automated ticket generation upon payment.
*   Professional headers, service details, and wait instructions.

---

## 🚀 Technical Architecture

| Component | Technology |
| :--- | :--- |
| **Framework** | Jetpack Compose (Modern Declarative UI) |
| **Language** | Kotlin (Coroutines & Flow for Reactive State) |
| **Storage** | Encrypted SharedPreferences & JSON Serialization |
| **Excel Engine** | Apache POI 5.2.3 (API 26+) |
| **Navigation** | Jetpack Navigation Component |
| **Architecture** | MVVM (Model-View-ViewModel) |

---

## 🛠️ Installation & Setup

### Prerequisites
*   **Android SDK**: Minimum API 26 (Android 8.0 Oreo) is required for Excel processing.
*   **Hardware**: Android Tablet (Recommended for Admin/Reception) or Smart TV (For Display Modes).

### Build Instructions
1. Clone the repository.
2. Ensure `gradle.properties` is configured for your environment.
3. Sync project with Gradle files.
4. Build the APK via `./gradlew assembleDebug`.

### App Icon Configuration
The application uses a custom high-resolution brand asset located at:
`app/src/main/res/drawable/ic_launcher.png`

---

## 📸 Interface Preview

*   **Main Screen**: Intuitive selection of services and instant token generation.
*   **Admin Dashboard**: Secured portal for system configuration and reporting.
*   **TV Displays**: Dark-mode optimized layouts for high-visibility in hospital halls.

---

## 🔒 Security & Privacy
The system is designed with data integrity in mind. All local storage is handled via serialized JSON objects, ensuring that even in the event of a power failure, queue states and financial history remain intact.

---
**Developed for Al-Raqi University Hospital**  
*Optimizing Patient Care through Digital Innovation.*
