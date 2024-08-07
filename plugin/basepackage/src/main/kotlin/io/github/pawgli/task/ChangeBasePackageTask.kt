package io.github.pawgli.task

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

private const val NEW_PACKAGE_KEY = "newPackage"

internal abstract class ChangeBasePackageTask : DefaultTask() {

  @get:Input
  abstract val basePackage: Property<String>

  @get:Input
  @get:Optional
  abstract val newPackage: Property<String>

  @get:Input
  abstract val exclusionPatterns: SetProperty<String>

  private val replaceBasePackage by lazy {
    ReplaceBasePackage(
      projectDir = project.projectDir,
      newBasePackage = getNewPackageProperty(),
      oldBasePackage = basePackage.get(),
      logger = logger,
      exclusionPatterns = exclusionPatterns.get()
    )
  }

  init {
    group = "Refactoring"
    description =
      "Changes the base package of the project, set -P$NEW_PACKAGE_KEY=com.yourdomain.yourpackage"
  }

  @TaskAction
  fun action() {
    replaceBasePackage()
  }

  private fun getNewPackageProperty(): String =
    project.findProperty(NEW_PACKAGE_KEY) as? String
      ?: if (newPackage.isPresent) {
        newPackage.get()
      } else {
        error("The new base package was not provided.")
      }

  companion object {
    const val TASK_NAME = "changeBasePackage"
  }
}
