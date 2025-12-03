pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://artifactory-external.vkpartner.ru/artifactory/maven")
        }
        maven("https://jitpack.io")
    }
}

rootProject.name = "ShoppingListPlus"
include(":app")
include(":common")
include(":core-data")
include(":core-domain")
include(":feature-shoppinglist")
include(":feature-productlist")
include(":feature-settings")
include(":feature-archive")
include(":feature-premium")
include(":shared_shoppinglist")
include(":feature-fruit-album")
include(":feature-add-product")
