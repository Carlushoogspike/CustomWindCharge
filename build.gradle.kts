plugins {
    id("java")
}

group = "com.carlos.plugins.tst"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    testCompileOnly("org.projectlombok:lombok:1.18.34")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.34")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {

    withType(JavaCompile::class).configureEach {
        options.encoding = "UTF-8"
    }

}

