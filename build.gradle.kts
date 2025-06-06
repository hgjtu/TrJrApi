plugins {
	java
	id("org.springframework.boot") version "3.4.3"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.course"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")
	implementation("io.jsonwebtoken:jjwt-api:0.12.6")
	implementation("io.jsonwebtoken:jjwt-impl:0.12.6")
	implementation("io.jsonwebtoken:jjwt-jackson:0.12.6")
	implementation("io.minio:minio:8.5.7")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-validation:3.4.4")
	implementation("me.paulschwarz:spring-dotenv:4.0.0")
	implementation("org.springframework.boot:spring-boot-maven-plugin:3.4.5")
	implementation("org.springframework.boot:spring-boot-starter-actuator:3.4.5")
	compileOnly("org.projectlombok:lombok")
	compileOnly("org.apache.tomcat:tomcat-servlet-api:9.0.37")
	compileOnly("io.swagger.core.v3:swagger-annotations:2.2.25")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks {
	bootJar {
		archiveFileName.set("app.jar")
	}

	jar {
		enabled = false
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
