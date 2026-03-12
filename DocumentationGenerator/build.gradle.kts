plugins {
    java
    application
}

group = "org.avalancs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

// start in DocumentationGenerator folder so it can find the template directory
tasks.named<JavaExec>("run").configure {
    workingDir = project.projectDir;
}

application {
    mainClass.set("org.avalancs.documentationgenerator.Main")
    // Asciidoc needs the opens so images can be loaded with JRuby
    applicationDefaultJvmArgs = listOf("-Dfile.encoding=utf-8", "--add-opens", "java.base/sun.nio.ch=ALL-UNNAMED", "--add-opens", "java.base/java.io=ALL-UNNAMED")
}

dependencies {
    implementation(project(":CSVParser"))

    implementation("io.github.classgraph:classgraph:4.8.184")
    implementation("io.pebbletemplates:pebble:4.1.1")
    implementation("org.asciidoctor:asciidoctorj:3.0.1")
    implementation("org.asciidoctor:asciidoctorj-pdf:2.3.23")

    implementation(libs.logback)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)

    testImplementation(libs.junit5)
    testImplementation(libs.junit5.params)
    testRuntimeOnly(libs.junit5.engine)
    testRuntimeOnly(libs.junit5.platform)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
