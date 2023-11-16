rootProject.name = "CSVParserDocumenter"

include("CSVParser", "DocumentationGenerator")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("junit", "5.10.0")

            library("junit-bom", "org.junit", "junit-bom").versionRef("junit")
            library("junit-jupiter", "org.junit.jupiter", "junit-jupiter").versionRef("junit")
            library("commons-csv", "org.apache.commons:commons-csv:1.10.0")
            library("lombok", "org.projectlombok:lombok:1.18.30")
            library("logback", "ch.qos.logback:logback-classic:1.3.5")

            //bundle("junit", listOf("junit-bom", "junit-jupiter"))
        }
    }
}