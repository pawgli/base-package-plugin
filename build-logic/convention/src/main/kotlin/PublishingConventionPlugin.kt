import io.github.pawgli.configuration.configureMavenPublish
import io.github.pawgli.publishing.PublishingPropertyKey
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskContainer

class PublishingConventionPlugin : Plugin<Project> {

  override fun apply(target: Project) {
    with(target) {
      getLibVersion()?.let { version ->
        applyPublishingPlugin(version)
      } ?: onVersionNotFound()
    }
  }

  private fun Project.getLibVersion(): String? {
    val versionEnv = findProperty(PublishingPropertyKey.VERSION_SYSTEM_ENV) as? String
    return versionEnv?.let(System::getenv)
  }

  private fun Project.applyPublishingPlugin(version: String) {
    pluginManager.apply("com.vanniktech.maven.publish")
    tasks.registerPublishingTask()
    configureMavenPublish(version)
  }

  private fun onVersionNotFound() {
    println("Publishing Plugin wasn't applied, because library version is not specified.")
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
