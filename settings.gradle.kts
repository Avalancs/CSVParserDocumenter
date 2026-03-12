rootProject.name = "CSVParserDocumenter"

include("CSVParser", "DocumentationGenerator")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("junit", "6.0.3")

            library("junit-jupiter", "org.junit.jupiter", "junit-jupiter").versionRef("junit")
            library("junit5", "org.junit.jupiter", "junit-jupiter").versionRef("junit")
            library("junit5-params", "org.junit.jupiter", "junit-jupiter-params").versionRef("junit")
            library("junit5-engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")
            library("junit5-platform", "org.junit.platform", "junit-platform-launcher").versionRef("junit")
            library("commons-csv", "org.apache.commons:commons-csv:1.14.1")
            library("lombok", "org.projectlombok:lombok:1.18.44")
            library("logback", "ch.qos.logback:logback-classic:1.5.32")

            //bundle("junit", listOf("junit-bom", "junit-jupiter"))
        }
    }
}