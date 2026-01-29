import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

plugins {
  `java-library`
  id("io.papermc.paperweight.userdev") version "2.0.0-beta.19"
  id("xyz.jpenilla.run-paper") version "3.0.2"
  id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.3.0"
}

group = "org.jufyer.plugin"
version = properties["projectVersion"] ?: "0.0.1"
description = "JustMakesSense Minecraft Plugin by Jufyer"

java {
  toolchain.languageVersion = JavaLanguageVersion.of(21)
}

repositories {
  mavenCentral()
  maven("https://repo.papermc.io/repository/maven-public/")
  maven("https://jitpack.io")

  maven("https://oss.sonatype.org/content/groups/public/")
  maven("https://maven.devs.beer/")
  maven("https://repo.oraxen.com/releases")
  maven("https://repo.citizensnpcs.co/")

}

dependencies {
  paperweight.paperDevBundle("1.21.11-R0.1-SNAPSHOT")

  compileOnly("io.th0rgal:oraxen:1.181.0")
  compileOnly("dev.lone:api-itemsadder:4.0.10")

  compileOnly(files("libs/citizens-api-2.0.33.jar"))
  compileOnly(files("libs/citizens-main-2.0.33.jar"))
}

tasks {
  compileJava {
    options.release = 21
  }
  javadoc {
    options.encoding = Charsets.UTF_8.name()
  }
}

bukkitPluginYaml {
  main = "org.jufyer.plugin.justMakesSense.Main"
  load = BukkitPluginYaml.PluginLoadOrder.STARTUP
  authors.add("Jufyer")
  apiVersion = "1.21"
  softDepend.add("Citizens")
  commands {

  }
}
