// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.3" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.android.library") version "8.1.3" apply false
}

subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs += "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi"
            freeCompilerArgs += "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi"
            freeCompilerArgs += "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
        }
    }
}
