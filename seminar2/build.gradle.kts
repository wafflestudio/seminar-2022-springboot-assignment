import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("org.springframework.boot") version "2.7.2"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21"
    kotlin("plugin.allopen") version "1.3.71"
    kotlin("plugin.noarg") version "1.3.71"
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
    kotlin("kapt") version "1.7.10"
    
    idea
}

apply {
    plugin("org.jlleitschuh.gradle.ktlint")
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
}

noArg {
    annotation("javax.persistence.Entity")
}

group = "com.wafflestudio"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    // Web & DB
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("mysql:mysql-connector-java")


    // Auth
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // Kotlin Features
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    
    
    // QueryDsl
    implementation("com.querydsl:querydsl-jpa:5.0.0")
    kapt("com.querydsl:querydsl-apt:5.0.0:jpa")
    kapt("org.springframework.boot:spring-boot-configuration-processor")
    
    // ModelMapper
    implementation("org.modelmapper:modelmapper:2.1.1")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")

    // Mockk
    val mockkVersion = "1.12.+"
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("com.ninja-squad:springmockk:3.1.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}


idea {
    module {
        val kaptMain = file("build/generated/source/kapt/main")
        sourceDirs.add(kaptMain)
        generatedSourceDirs.add(kaptMain)
    }
}