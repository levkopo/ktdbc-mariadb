plugins {
    kotlin("multiplatform") version "1.7.10"
}

group = "com.github.levkoposc"
version = "1.0.0-alpha1"

repositories {
    mavenCentral()
}

val mingwPath = File(System.getenv("MINGW64_DIR") ?: "C:/msys64/mingw64")

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    js(BOTH) {
        nodejs {

        }
    }

    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        compilations["main"].cinterops {
            val mariadb by creating {
                when (preset) {
                    presets["macosX64"] -> includeDirs.headerFilterOnly("/opt/local/include", "/usr/local/include")
                    presets["linuxX64"] -> includeDirs.headerFilterOnly("/usr/include", "/usr/include/x86_64-linux-gnu")
                    presets["mingwX64"] -> includeDirs.headerFilterOnly(mingwPath.resolve("include"))
                }
            }
        }
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.github.levkoposc:ktdbc-core:$version")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("com.github.levkoposc:ktdbc-core-jvm:$version")
            }
        }

        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                implementation(npm("mysql2", "2.3.3"))
                implementation("com.github.levkoposc:ktdbc-core-js:$version")
            }
        }

        val jsTest by getting
        val nativeMain by getting {
            dependencies {
                implementation("com.github.levkoposc:ktdbc-core-native:$version")
            }
        }

        val nativeTest by getting
    }
}
