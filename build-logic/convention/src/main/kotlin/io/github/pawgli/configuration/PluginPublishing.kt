package io.github.pawgli.configuration

import io.github.pawgli.publishing.artifactId
import io.github.pawgli.publishing.groupId
import io.github.pawgli.publishing.pluginImplementationClass
import io.github.pawgli.publishing.pluginTags
import io.github.pawgli.publishing.publicationDescription
import io.github.pawgli.publishing.publicationName
import io.github.pawgli.publishing.repoUrl
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension

@Suppress("UnstableApiUsage")
internal fun Project.configureGradlePluginPublication(version: String) {
  group = groupId
  this.version = version

  configure<GradlePluginDevelopmentExtension> {
    website.set(repoUrl)
    vcsUrl.set(repoUrl)

    createPlugin(target = this@configureGradlePluginPublication)
  }
}

@Suppress("UnstableApiUsage")
private fun GradlePluginDevelopmentExtension.createPlugin(target: Project) = with(target) {
  plugins {
    create(artifactId) {
      id = "$groupId.$artifactId"
      implementationClass = pluginImplementationClass
      displayName = publicationName
      description = publicationDescription
      tags.set(pluginTags)
    }
  }
}
