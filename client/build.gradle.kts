/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

plugins {
    with(Plugins) {
        kotlin(multiplatform)
        kotlin(cocoapods)
        id(androidLibrary)
        id(sqldelight)
        id(mokoResources)
    }
}

version = ProjectSettings.getVersionName(project)

kotlin {
    android()

    ios()

    cocoapods {
        summary = "CCC"
        homepage = "https://github.com/CurrencyConverterCalculator/CCC"
        frameworkName = "Client"
        ios.deploymentTarget = "14.0"
    }

    js {
        browser {
            binaries.executable()
            testTask {
                enabled = false
            }
        }
    }

    jvm()

    @Suppress("UNUSED_VARIABLE")
    sourceSets {

        all {
            languageSettings.apply {
                useExperimentalAnnotation("kotlinx.coroutines.FlowPreview")
                useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }

        with(Dependencies.Common) {
            val commonMain by getting {
                dependencies {
                    implementation(dateTime)
                    implementation(coroutines)
                    implementation(koinCore)
                    implementation(kermit)

                    with(Modules) {
                        implementation(project(common))
                        implementation(project(calculator))
                        implementation(project(scopemob))
                    }
                }
            }
            val commonTest by getting {
                dependencies {
                    implementation(kotlin(test))
                    implementation(kotlin(testAnnotations))
                }
            }
        }

        val mobileMain by creating {
            dependencies {
                dependsOn(commonMain.get())
                implementation(Dependencies.Common.mokoResources)
            }
        }

        with(Dependencies.Android) {
            val androidMain by getting {
                dependencies {
                    dependsOn(mobileMain)
                    implementation(androidMaterial)
                    implementation(koinAndroidViewModel)
                    implementation(viewModelExt)
                }
            }
            val androidTest by getting {
                dependencies {
                    implementation(kotlin(Dependencies.JVM.testJUnit))
                }
            }
        }

        val iosMain by getting {
            dependencies {
                dependsOn(mobileMain)
            }
        }
        val iosTest by getting

        with(Dependencies.JS) {
            val jsMain by getting
            val jsTest by getting {
                dependencies {
                    implementation(kotlin(test))
                }
            }
        }

        with(Dependencies.JVM) {
            val jvmMain by getting
            val jvmTest by getting {
                dependencies {
                    implementation(kotlin(testJUnit))
                }
            }
        }
    }
}

android {
    with(ProjectSettings) {
        compileSdkVersion(projectCompileSdkVersion)

        defaultConfig {
            minSdkVersion(projectMinSdkVersion)
            targetSdkVersion(projectTargetSdkVersion)
            versionCode = getVersionCode(project)
            versionName = getVersionName(project)
        }

        // todo remove after https://github.com/touchlab/Kermit/issues/67
        testOptions {
            unitTests.isReturnDefaultValues = true
        }

        // todo https://youtrack.jetbrains.com/issue/KT-43944
        configurations {
            create("testApi") {}
            create("testDebugApi") {}
            create("testReleaseApi") {}
        }

        sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "com.github.mustafaozhan.ccc.client"
    multiplatformResourcesSourceSet = "mobileMain"
}
