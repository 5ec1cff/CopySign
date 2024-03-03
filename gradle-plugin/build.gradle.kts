plugins {
    java
    `java-gradle-plugin`
    `maven-publish`
}

java {
    withSourcesJar()
}

dependencies {
    compileOnly(gradleApi())
    compileOnly(libs.android.gradle)
}

gradlePlugin {
    plugins {
        create("copy_sign") {
            id = "io.github.a13e300.tools.copy_sign"
            implementationClass = "io.github.a13e300.tools.copy_sign.CopySignPlugin"
        }
    }
}
