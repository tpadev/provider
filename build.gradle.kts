import com.lagradost.cloudstream3.gradle.CloudstreamExtension
import com.android.build.gradle.BaseExtension

buildscript {
    repositories {
        google()
        mavenCentral()
        // Shitpack repo which contains our tools and dependencies
        maven("https://jitpack.io")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.8.2")
        // Cloudstream gradle plugin which makes everything work and builds plugins
        classpath("com.github.recloudstream:gradle:-SNAPSHOT")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.0")
    }
}

allprojects {
//    repositories {
//        google()
//        mavenCentral()
//        maven("https://jitpack.io")
//    }
}

fun Project.cloudstream(configuration: CloudstreamExtension.() -> Unit) =
    extensions.getByName<CloudstreamExtension>("cloudstream").configuration()

fun Project.android(configuration: BaseExtension.() -> Unit) =
    extensions.getByName<BaseExtension>("android").configuration()

subprojects {
    apply(plugin = "com.android.library")
    apply(plugin = "kotlin-android")
    apply(plugin = "com.lagradost.cloudstream3.gradle")

    cloudstream {
        // when running through github workflow, GITHUB_REPOSITORY should contain current repository name
        // you can modify it to use other git hosting services, like gitlab
        setRepo(System.getenv("GITHUB_REPOSITORY") ?: "https://github.com/tpadev/csidext")

        authors = listOf("tpadev")
    }

    android {
        compileSdkVersion(34)

        defaultConfig {
            minSdk = 21
            targetSdk = 33
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_18
            targetCompatibility = JavaVersion.VERSION_18
        }

        tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_18.toString() // Required
                // Disables some unnecessary features
                freeCompilerArgs = freeCompilerArgs +
                        "-Xno-call-assertions" +
                        "-Xno-param-assertions" +
                        "-Xno-receiver-assertions"
            }
        }
    }

    dependencies {
        val apkTasks = listOf("deployWithAdb", "build")
        val useApk = gradle.startParameter.taskNames.any { taskName ->
            apkTasks.any { apkTask ->
                taskName.contains(apkTask, ignoreCase = true)
            }
        }

        val implementation by configurations
        val apk by configurations
        apk("com.lagradost:cloudstream3:pre-release")
        // If the task is specifically to compile the app then use the stubs, otherwise us the library.
        if (useApk) {
            // Stubs for all Cloudstream classes
        } else {
//            implementation("com.github.Blatzar:CloudstreamApi:0.1.7")
        }

        implementation(kotlin("stdlib")) // adds standard kotlin features, like listOf, mapOf etc
        implementation("com.github.Blatzar:NiceHttp:0.4.11") // http library
        implementation("org.jsoup:jsoup:1.17.2") // html parser
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
        implementation("org.mozilla:rhino:1.7.14")
    }
}