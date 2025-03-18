plugins {
    java
    application
}

group = "org.avalancs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// start in DocumentationGenerator folder so it can find the template directory
tasks.named<JavaExec>("run").configure() {
    workingDir = project.projectDir;
}

application {
    //mainClass = "org.avalancs.documentationgenerator.Main"
    mainClass.set("org.avalancs.documentationgenerator.Main")
    // Asciidoc needs the opens so images can be loaded with JRuby
    applicationDefaultJvmArgs = listOf("-Dfile.encoding=utf-8", "--add-opens", "java.base/sun.nio.ch=ALL-UNNAMED", "--add-opens", "java.base/java.io=ALL-UNNAMED")
}

dependencies {
    implementation(project(":CSVParser"))

    implementation("io.github.classgraph:classgraph:4.8.168")
    implementation("io.pebbletemplates:pebble:3.2.2")
    implementation("org.asciidoctor:asciidoctorj:2.5.12")
    implementation("org.asciidoctor:asciidoctorj-pdf:2.3.15")

    implementation(libs.logback)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
