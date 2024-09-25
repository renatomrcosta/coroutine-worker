plugins {
    kotlin("jvm") version "1.4.32"
    `java-library`
    `maven-publish`
}

group = "com.xunfos"
version = "0.0.7"

repositories {
    mavenCentral()
}

dependencies {
//    val kotestVersion = "4.2.5"

    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
//    testImplementation("io.kotest", "kotest-runner-junit5", kotestVersion) // for kotest framework
//    testImplementation("io.kotest", "kotest-assertions-core", kotestVersion) // for kotest core jvm assertions
//    testImplementation("io.kotest", "kotest-property", kotestVersion) // for kotest property test
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "13"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "13"
    }
}

publishing {
    repositories {
        maven {
            name = "coworker"
            url = uri("https://maven.pkg.github.com/renatomrcosta/co-worker")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("coworker") {
            groupId = "com.xunfos"
            artifactId = "coworker"
            version = "0.0.7"

            from(components["java"])
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
tasks.jar {
    manifest {
        attributes(mapOf("Implementation-Title" to project.name,
            "Implementation-Version" to project.version))
    }
}
java {
    withSourcesJar()
}

