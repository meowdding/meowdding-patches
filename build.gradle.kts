import com.google.devtools.ksp.gradle.KspTask
import org.gradle.api.internal.artifacts.DefaultModuleIdentifier
import org.gradle.api.internal.artifacts.dependencies.DefaultMinimalDependency
import org.gradle.api.internal.artifacts.dependencies.DefaultMutableVersionConstraint
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    idea
    `maven-publish`
    alias(libs.plugins.kotlin)
    alias(libs.plugins.loom)
    alias(libs.plugins.ksp)
}

base {
    archivesName.set(project.name.lowercase())
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
    withSourcesJar()
}

loom {
    runs {
        getByName("client") {
            vmArg("-Ddevauth.enabled=true")
            vmArg("-Dskyblockapi.debug=true")
        }
    }
}

repositories {
    maven(url = "https://maven.teamresourceful.com/repository/maven-public/")
    maven(url = "https://repo.hypixel.net/repository/Hypixel/")
    maven(url = "https://api.modrinth.com/maven")
    maven(url = "https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
    maven(url = "https://maven.nucleoid.xyz")
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly(libs.meowdding.ktcodecs)
    ksp(libs.meowdding.ktcodecs)

    minecraft(libs.minecraft)
    @Suppress("UnstableApiUsage")
    mappings(loom.layered {
        officialMojangMappings()
        parchment(libs.parchmentmc.withMcVersion().toString())
    })

    modImplementation(libs.loader)
    modImplementation(libs.fabric)

    modRuntimeOnly(libs.devauth)
    modRuntimeOnly(libs.modmenu)
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE

    filesMatching(listOf("fabric.mod.json")) {
        expand("version" to project.version)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(21)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true

        excludeDirs.add(file("run"))
    }
}

tasks.withType<KspTask> {
    outputs.upToDateWhen { false }
}

ksp {
    arg("meowdding.codecs.project_name", "MoewddingPatches")
    arg("meowdding.codecs.package", "me.owdding.patches.generated")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "patches"
            from(components["java"])

            pom {
                name.set("MeowddingPatches")
                url.set("https://github.com/meowdding/meowdding-patches")

                scm {
                    connection.set("git:https://github.com/meowdding/meowdding-patches.git")
                    developerConnection.set("git:https://github.com/meowdding/meowdding-patches.git")
                    url.set("https://github.com/meowdding/meowdding-patches")
                }
            }
        }
    }
    repositories {
        maven {
            setUrl("https://maven.teamresourceful.com/repository/thatgravyboat/")
            credentials {
                username = System.getenv("MAVEN_USER") ?: providers.gradleProperty("maven_username").orNull
                password = System.getenv("MAVEN_PASS") ?: providers.gradleProperty("maven_password").orNull
            }
        }
    }
}

//<editor-fold defaultstate="collapsed" desc="Helpers">
fun Provider<out ExternalModuleDependency>.withMcVersion() = this.get().withMcVersion()
fun ExternalModuleDependency.withMcVersion(): ExternalModuleDependency {
    return DefaultMinimalDependency(
        DefaultModuleIdentifier.newId(this.group, this.name.replace("<mc_version>", libs.versions.minecraft.get())),
        DefaultMutableVersionConstraint(this.versionConstraint)
    )
}

@Suppress("unused")
fun DependencyHandlerScope.includeImplementationBundle(bundle: Provider<ExternalModuleDependencyBundle>) = bundle.get().forEach {
    includeImplementation(provider { it })
}

@Suppress("unused")
fun DependencyHandlerScope.includeModImplementationBundle(bundle: Provider<ExternalModuleDependencyBundle>) = bundle.get().forEach {
    includeModImplementation(provider { it })
}

fun <T : ExternalModuleDependency> DependencyHandlerScope.includeImplementation(dependencyNotation: Provider<T>) =
    with(dependencyNotation.get().withMcVersion()) {
        include(this)
        modImplementation(this)
    }

fun <T : ExternalModuleDependency> DependencyHandlerScope.includeModImplementation(dependencyNotation: Provider<T>) =
    with(dependencyNotation.get().withMcVersion()) {
        include(this)
        modImplementation(this)
    }
//</editor-fold>