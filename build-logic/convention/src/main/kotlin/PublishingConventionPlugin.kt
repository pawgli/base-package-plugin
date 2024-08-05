import io.github.pawgli.configuration.configureGradlePluginPublication
import io.github.pawgli.configuration.configureLibraryPublication
import io.github.pawgli.publishing.getPublicationVersionOrNull
import io.github.pawgli.publishing.isPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskContainer

class PublishingConventionPlugin : Plugin<Project> {

  override fun apply(target: Project) {
    with(target) {
      getPublicationVersionOrNull()?.let { version ->
        applyPublishingPlugin(version)
      } ?: onVersionNotFound()
    }
  }

  private fun Project.applyPublishingPlugin(version: String) {
    with(pluginManager) {
      apply("com.vanniktech.maven.publish")
      apply("com.gradle.plugin-publish")
    }
    tasks.registerPublishingTask()
    configureLibraryPublication(version)
    if (isPlugin) configureGradlePluginPublication(version)
  }

  private fun onVersionNotFound() {
    println("The Publishing Plugin wasn't applied due to unspecified publication version.")
  }
}

private fun TaskContainer.registerPublishingTask() {
  register("publishProjectToMavenCentral") {
    group = "publishing"
    description = "Runs the tests, and publishes the project to Maven Central repository."

    dependsOn("test")
    dependsOn("publishAndReleaseToMavenCentral")
  }
}
