
//structur project
SimpleExplorer\
│
├── src/ (Semua kode Java Anda)
│   └── main/
│       ├── java/
│       │   └── com 
│       │       └── indolibs 
│       │           └── libs
│       │               └── SimpleExplorer.java    <-- File kelas untuk logika & utilitas
│       │
│       └── resources/ 
├── gradle/ (Folder wrapper Gradle)
│   └── wrapper/
├── libs/ (Jika Anda meletakkan file .jar eksternal di sini yang dibutuhkan oleh library Anda)
├── build.gradle (File konfigurasi untuk library)
├── settings.gradle
├── gradlew (Gradle Wrapper Linux)
├── gradlew.bat (Gradle Wrapper windows)
└── .gitignore <-- supaya tidak disertakan saat push Folder build/ dan .gradle/