package io.github.pawgli.publishing

import io.github.pawgli.publishing.PublishingProperty.DESCRIPTION
import io.github.pawgli.utils.requireProperty
import org.gradle.api.Project

private object PublishingProperty {
  const val VERSION_SYSTEM_ENV = "PUBLISHING_LIBRARY_VERSION_SYSTEM_ENV"

  const val NAME = "PUBLISHING_LIBRARY_NAME"
  const val DESCRIPTION = "PUBLISHING_LIBRARY_DESCRIPTION"
  const val REPO_URL = "PUBLISHING_REPO_URL"
  const val LICENSE = "PUBLISHING_LICENSE"
  const val LICENSE_URL = "PUBLISHING_LICENSE_URL"
  const val SCM_CONNECTION = "PUBLISHING_SCM_CONNECTION"
  const val ARTIFACT_ID = "PUBLISHING_ARTIFACT_ID"

  // Plugin publishing
  const val PLUGIN_IMPLEMENTATION_CLASS = "PUBLISHING_PLUGIN_IMPLEMENTATION_CLASS"
  const val PLUGIN_TAGS = "PUBLISHING_PLUGIN_TAGS"
}

internal fun Project.getPublicationVersionOrNull(): String? {
  val versionEnv = findProperty(PublishingProperty.VERSION_SYSTEM_ENV) as? String
  return versionEnv?.let(System::getenv)
}

internal val groupId: String
  get() = PublishingDefaults.GROUP_ID

internal val Project.artifactId: String
  get() = requireProperty(PublishingProperty.ARTIFACT_ID)

internal val Project.publicationName: String
  get() = requireProperty(PublishingProperty.NAME)

internal val Project.publicationDescription: String
  get() = requireProperty(DESCRIPTION, default = "")

internal val Project.repoUrl: String
  get() = requireProperty<String>(PublishingProperty.REPO_URL)

internal val Project.scmConnection: String
  get() = requireProperty<String>(PublishingProperty.SCM_CONNECTION)

internal val Project.license: String
  get() = requireProperty<String>(PublishingProperty.LICENSE)

internal val Project.licenseUrl: String
  get() = requireProperty<String>(PublishingProperty.LICENSE_URL)

// Plugin publishing
internal val Project.isPlugin: Boolean
  get() = hasProperty(PublishingProperty.PLUGIN_IMPLEMENTATION_CLASS)

internal val Project.pluginImplementationClass: String
  get() = requireProperty<String>(PublishingProperty.PLUGIN_IMPLEMENTATION_CLASS)

internal val Project.pluginTags: List<String>
  get() = requireProperty<String>(PublishingProperty.PLUGIN_TAGS).split(",")
