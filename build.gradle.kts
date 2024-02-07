plugins {
    application
    java
    id("jacoco")
    id("io.freefair.lombok") version "8.4"
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
}
group = "hexlet.code"
version = "0.0.1-SNAPSHOT"

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}



dependencies {
    runtimeOnly("com.h2database:h2")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    implementation("org.springframework.boot:spring-boot-starter-validation")
//    implementation("org.springframework.boot:spring-boot-starter-security")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            // Use JUnit Jupiter test framework
            useJUnitJupiter("5.9.2")
        }
    }
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(20))
    }
}

application {
    // Define the main class for the application.
    mainClass.set("hexlet.code.App")
}
