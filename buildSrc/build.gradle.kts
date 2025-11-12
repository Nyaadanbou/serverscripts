plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()

    // 仓库提供: nyaadanbou version catalog, nyaadanbou conventions
    maven {
        url = uri("https://repo.mewcraft.cc/private")
        name = "nyaadanbouPrivate"
        credentials(PasswordCredentials::class)
    }
}

dependencies {
    implementation(local.plugin.kotlin.jvm)
    implementation(local.plugin.kotlin.kapt)
    implementation(local.plugin.shadow)
    implementation(local.plugin.nyaadanbou.conventions)
}

dependencies {
    implementation(files(local.javaClass.superclass.protectionDomain.codeSource.location))
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}