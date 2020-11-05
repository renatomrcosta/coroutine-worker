plugins {
    kotlin("jvm") version "1.3.72"
    `maven-publish`
}

group = "com.xunfos"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    val kotestVersion = "4.2.5"

    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
    testImplementation("io.kotest", "kotest-runner-junit5", kotestVersion) // for kotest framework
    testImplementation("io.kotest", "kotest-assertions-core", kotestVersion) // for kotest core jvm assertions
    testImplementation("io.kotest", "kotest-property", kotestVersion) // for kotest property test
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
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/renatomrcosta/co-worker")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("default") {
            from(components["java"])
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
