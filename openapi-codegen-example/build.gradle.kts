
plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.0"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.openapi.generator") version "7.13.0"
}

group = "com.sindeaux"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(17))
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.8")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.8.8")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll(
			listOf(
				"-Xjsr305=strict"
			)
		)
	}
}

openApiGenerate {
	generatorName.set("spring")
	inputSpec.set("$rootDir/src/main/resources/static/api-docs.yaml")
	outputDir.set(layout.buildDirectory.dir("generated/openapi").get().asFile.absolutePath)
	modelNameSuffix.set("ExternalModel")
	apiPackage.set("com.sindeaux.openapi_codegen_example.application.web.apis")
	modelPackage.set("com.sindeaux.openapi_codegen_example.application.web.models")
	configOptions.set(
		mapOf(
			"dateLibrary" to "java8",
			"gradleBuildFile" to "false",
			"interfaceOnly" to "true",
			"openapiNullable" to "true",
			"useTags" to "true",
			"jakarta" to "true",
			"useJakartaEe" to "true",
			"useBeanValidation" to "true",
		)
	)
}
sourceSets {
	getByName("main") {
		java {
			srcDir(layout.buildDirectory.dir("generated/openapi/src/main/java").get().asFile.absolutePath)
		}
	}
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.compileKotlin {
	dependsOn("openApiGenerate")
}
