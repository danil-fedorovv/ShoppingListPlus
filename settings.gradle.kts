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
