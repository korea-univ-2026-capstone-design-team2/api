import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	kotlin("jvm") version "2.3.10"
	kotlin("plugin.spring") version "2.3.10"
	id("org.springframework.boot") version "4.0.5" apply false
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("plugin.jpa") version "2.3.10" apply false
	kotlin("kapt") version "2.3.10" apply false
}

allprojects {
	group = "com.examhelper"
	version = "0.0.1-SNAPSHOT"

	repositories {
		mavenCentral()
	}
}

subprojects {
	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "org.jetbrains.kotlin.plugin.spring")
	apply(plugin = "io.spring.dependency-management")
	apply(plugin = "org.jetbrains.kotlin.plugin.jpa")

	dependencyManagement {
		imports {
			mavenBom("org.springframework.boot:spring-boot-dependencies:4.0.5")
			mavenBom("tools.jackson:jackson-bom:3.1.2")
			mavenBom("org.springframework.ai:spring-ai-bom:1.1.5")
		}
	}

	dependencies {
		// Kotlin
		implementation("tools.jackson.core:jackson-databind")
		implementation("tools.jackson.module:jackson-module-kotlin")
		implementation("org.jetbrains.kotlin:kotlin-reflect")
		implementation("io.github.microutils:kotlin-logging:3.0.5")
		implementation(kotlin("stdlib"))
	}

	java {
		toolchain {
			languageVersion = JavaLanguageVersion.of(25)
		}
	}

	kotlin {
		jvmToolchain(25)
		compilerOptions {
			jvmTarget.set(JvmTarget.JVM_25)
			freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
		}
	}

	allOpen {
		annotation("jakarta.persistence.Entity")
		annotation("jakarta.persistence.MappedSuperclass")
		annotation("jakarta.persistence.Embeddable")
	}

	tasks.withType<Test> {
		useJUnitPlatform()
		failOnNoDiscoveredTests = false
	}

	tasks.withType<Jar> {
		val modulePath = project.path
			.removePrefix(":")
			.replace(":", "-")

		archiveBaseName.set(modulePath)
	}

	group = when {
		path.startsWith(":question:") -> "com.examhelper.api.question"
		path.startsWith(":question_generation:") -> "com.examhelper.api.question_generation"
		path.startsWith(":user:") -> "com.examhelper.api.user"
		path.startsWith(":auth:") -> "com.examhelper.api.auth"
		else -> "com.examhelper.api.shared"
	}
}
