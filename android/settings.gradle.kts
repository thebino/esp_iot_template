rootProject.name = "ESP IoT Template"
include(":app")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    resolutionStrategy {
        eachPlugin {
            when (requested.id.namespace) {
                "com.android" -> useModule("com.android.tools.build:gradle:${requested.version}")
            }
        }
    }
}
