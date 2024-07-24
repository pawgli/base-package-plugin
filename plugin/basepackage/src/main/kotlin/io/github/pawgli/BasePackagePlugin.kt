package io.github.pawgli

import io.github.pawgli.extension.BasePackagePluginExtension
import io.github.pawgli.task.ChangeBasePackageTask
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * A Gradle plugin that allows changing the base package of a project.
 */
class BasePackagePlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      val extension: BasePackagePluginExtension = createBasePackageExtension()

      afterEvaluate {
        extension.apply {
          basePackage.convention(DEFAULT_BASE_PACKAGE)
        }
        registerChangeBasePackageTask(extension)
      }
    }
  }

  internal companion object {
    const val EXTENSION_NAME = "basePackagePlugin"
    private const val DEFAULT_BASE_PACKAGE = "com.example"
  }
}

private fun Project.createBasePackageExtension(): BasePackagePluginExtension =
  extensions.create(BasePackagePlugin.EXTENSION_NAME, BasePackagePluginExtension::class.java)

private fun Project.registerChangeBasePackageTask(
  extension: BasePackagePluginExtension,
) {
  tasks.register(ChangeBasePackageTask.TASK_NAME, ChangeBasePackageTask::class.java) {
    basePackage.set(extension.basePackage)
    exclusionPatterns.set(extension.exclusionPatterns)
  }
}
