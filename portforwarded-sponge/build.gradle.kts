import org.spongepowered.gradle.plugin.config.PluginLoaders
import org.spongepowered.plugin.metadata.model.PluginDependency

plugins {
    java
    id("org.spongepowered.gradle.plugin") version "2.2.0"
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
    maven("https://repo.spongepowered.org/maven/") {
        name = "spongepowered-repo"
    }
}

sponge {
    apiVersion("16.0.0-SNAPSHOT")
    license("AGPL-3.0")
    loader {
        name(PluginLoaders.JAVA_PLAIN)
        version("1.0")
    }
    plugin("portforwarded") {
        displayName("PortForwarded")
        entrypoint("dev.relism.portforwarded.sponge.PortforwardedSponge")
        description("PortForwarded Sponge bootstrap.")
        links {
            // homepage("https://spongepowered.org")
            // source("https://spongepowered.org/source")
            // issues("https://spongepowered.org/issues")
        }
        dependency("spongeapi") {
            loadOrder(PluginDependency.LoadOrder.AFTER)
            optional(false)
        }
    }
}

dependencies {
    implementation(project(":portforwarded-common"))
    compileOnly("org.spongepowered:spongeapi:16.0.0-SNAPSHOT")
}

val javaTarget = 21 // Sponge targets a minimum of Java 21
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(javaTarget))
}

tasks.withType<JavaCompile>().configureEach {
    options.apply {
        encoding = "utf-8" // Consistent source file encoding
        if (JavaVersion.current().isJava10Compatible) {
            release.set(javaTarget)
        }
    }
}

// Make sure all tasks which produce archives (jar, sources jar, javadoc jar, etc) produce more consistent output
tasks.withType<AbstractArchiveTask>().configureEach {
    isReproducibleFileOrder = true
    isPreserveFileTimestamps = false
}
