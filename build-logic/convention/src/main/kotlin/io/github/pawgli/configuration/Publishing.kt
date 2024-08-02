package io.github.pawgli.configuration

import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SonatypeHost
import io.github.pawgli.publishing.PublishingDefaults
import io.github.pawgli.publishing.PublishingPropertyKey
import io.github.pawgli.utils.requireProperty
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureMavenPublish(version: String) {
  configure<MavenPublishBaseExtension> {
    coordinates(
      groupId = PublishingDefaults.GROUP_ID,
      artifactId = requireProperty(PublishingPropertyKey.ARTIFACT_ID),
      version = version
    )
    setPom(target = this@configureMavenPublish)
    publishToMavenCentral(
      host = SonatypeHost.CENTRAL_PORTAL,
      automaticRelease = true
    )
    signAllPublications()
  }
}

private fun MavenPublishBaseExtension.setPom(target: Project) = with(target) {
  pom {
    name.set(requireProperty<String>(PublishingPropertyKey.NAME))
    description.set(requireProperty(PublishingPropertyKey.DESCRIPTION, default = ""))
    inceptionYear.set(PublishingDefaults.INCEPTION_YEAR)
    url.set(requireProperty<String>(PublishingPropertyKey.REPO_URL))
    licenses {
      license {
        name.set(requireProperty<String>(PublishingPropertyKey.LICENSE))
        url.set(requireProperty<String>(PublishingPropertyKey.LICENSE_URL))
      }
    }
    scm {
      url.set(requireProperty<String>(PublishingPropertyKey.REPO_URL))
      connection.set(requireProperty<String>(PublishingPropertyKey.SCM_CONNECTION))
      developerConnection.set(requireProperty<String>(PublishingPropertyKey.SCM_CONNECTION))
    }
    developers {
      developer {
        id.set(PublishingDefaults.DEVELOPER_SCM_ID)
      }
    }
  }
}
