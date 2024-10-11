plugins {
    id("java-library")
    id("com.gradleup.shadow") version "8.3.3"
}

group = "io.github.uoyteamsix"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    api("com.badlogicgames.gdx:gdx:1.12.1")
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:1.12.1")
    implementation("com.badlogicgames.gdx:gdx-platform:1.12.1:natives-desktop")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.compileJava {
    options.release = 17;
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "io.github.uoyteamsix.UniSimGame"
        )
    }
}

tasks.shadowJar {
    minimize()
}

tasks.test {
    useJUnitPlatform()
}
