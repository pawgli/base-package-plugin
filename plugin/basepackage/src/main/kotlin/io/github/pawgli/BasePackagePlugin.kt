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
        registerChangeBasePackageTask(extension)
      }
    }
  }

  internal companion object {
    const val EXTENSION_NAME = "basePackagePlugin"
  }
}

private fun Project.createBasePackageExtension(): BasePackagePluginExtension =
  extensions.create(BasePackagePlugin.EXTENSION_NAME, BasePackagePluginExtension::class.java)

private fun Project.registerChangeBasePackageTask(
  extension: BasePackagePluginExtension,
) {
  tasks.register(ChangeBasePackageTask.TASK_NAME, ChangeBasePackageTask::class.java) {
    basePackage.set(extension.basePackage)
    newPackage.convention(extension.newPackage.orNull)
    exclusionPatterns.convention(extension.exclusionPatterns)
  }
}
