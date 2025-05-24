pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        gradlePluginPortal()
        maven("https://maven.teamresourceful.com/repository/maven-public/")
    }
}

rootProject.name = "meowdding-patches"