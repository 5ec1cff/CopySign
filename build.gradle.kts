plugins {
    id("com.android.library") version "8.1.2" apply false
}

subprojects {
    group = "io.github.a13e300.tools"
    version = "1.0"

    plugins.withId("java") {
        extensions.configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
    }

    plugins.withId("maven-publish") {
        extensions.configure<PublishingExtension> {
            repositories {
                mavenLocal()
            }

            publications {
                withType<MavenPublication> {
                    pom {
                        name.set("CopySign")
                        description.set("copy sign")
                        url.set("https://github.com/5ec1cff/CopySign")
                        licenses {
                            license {
                                name.set("MIT License")
                            }
                        }
                        developers {
                            developer {
                                name.set("5ec1cff")
                            }
                        }
                        scm {
                            connection.set("scm:git:https://github.com/5ec1cff/CopySign.git")
                            url.set("https://github.com/5ec1cff/CopySign.git")
                        }
                    }

                    groupId = project.group.toString()
                    version = project.version.toString()
                }
            }
        }
    }
}

task("clean", type = Delete::class) {
    delete(rootProject.buildDir)
}
