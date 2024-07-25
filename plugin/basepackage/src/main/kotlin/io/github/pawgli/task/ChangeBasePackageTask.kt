package io.github.pawgli.task

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

private const val NEW_BASE_PACKAGE_KEY = "newBasePackage"

internal abstract class ChangeBasePackageTask : DefaultTask() {

  @get:Input
  abstract val basePackage: Property<String>

  @get:Input
  abstract val exclusionPatterns: SetProperty<String>

  private val replaceBasePackage by lazy {
    ReplaceBasePackage(
      projectDir = project.projectDir,
      newBasePackage = project.findProperty(NEW_BASE_PACKAGE_KEY) as String,
      oldBasePackage = basePackage.get(),
      logger = logger,
      exclusionPatterns = exclusionPatterns.get()
    )
  }

  init {
    group = "Build Setup"
    description =
      "Changes the base package of the project, set -P$NEW_BASE_PACKAGE_KEY=com.yourdomain.yourpackage"
  }

  @TaskAction
  fun action() {
    replaceBasePackage()
  }

  companion object {
    const val TASK_NAME = "changeBasePackage"
  }
}
