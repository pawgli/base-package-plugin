package io.github.pawgli.configuration

import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SonatypeHost
import io.github.pawgli.publishing.PublishingDefaults
import io.github.pawgli.publishing.artifactId
import io.github.pawgli.publishing.license
import io.github.pawgli.publishing.licenseUrl
import io.github.pawgli.publishing.publicationDescription
import io.github.pawgli.publishing.publicationName
import io.github.pawgli.publishing.repoUrl
import io.github.pawgli.publishing.scmConnection
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureLibraryPublication(version: String) {
  configure<MavenPublishBaseExtension> {
    coordinates(
      groupId = PublishingDefaults.GROUP_ID,
      artifactId = artifactId,
      version = version
    )
    setPom(target = this@configureLibraryPublication)
    publishToMavenCentral(
      host = SonatypeHost.CENTRAL_PORTAL,
      automaticRelease = true
    )
    signAllPublications()
  }
}

private fun MavenPublishBaseExtension.setPom(target: Project) = with(target) {
  pom {
    name.set(publicationName)
    description.set(publicationDescription)
    inceptionYear.set(PublishingDefaults.INCEPTION_YEAR)
    url.set(repoUrl)
    licenses {
      license {
        name.set(license)
        url.set(licenseUrl)
      }
    }
    scm {
      url.set(repoUrl)
      connection.set(scmConnection)
      developerConnection.set(scmConnection)
    }
    developers {
      developer {
        id.set(PublishingDefaults.DEVELOPER_SCM_ID)
      }
    }
  }
}
