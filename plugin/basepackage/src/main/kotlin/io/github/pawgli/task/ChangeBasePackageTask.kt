package io.github.pawgli.task

import org.gradle.api.DefaultTask
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.provider.SetProperty
import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.PathMatcher
import javax.inject.Inject

private const val NEW_BASE_PACKAGE_KEY = "newBasePackage"
private const val PACKAGE_SEPARATOR = '.'

internal abstract class ChangeBasePackageTask : DefaultTask() {

  @get:Inject
  abstract val providerFactory: ProviderFactory

  @get:Input
  abstract val basePackage: Property<String>

  @get:Input
  abstract val exclusionPatterns: SetProperty<String>

  private val changeBasePackageExecutor =
    providerFactory.of(ChangeBasePackageExecutor::class.java) {
      with(parameters) {
        projectDir.set(project.projectDir)
        newBasePackage.set(project.findProperty(NEW_BASE_PACKAGE_KEY) as? String)
        oldBasePackage.set(basePackage)
        excludes.set(exclusionPatterns)
      }
    }

  init {
    group = "Build Setup"
    description = "Changes the base package of the project, set -P$NEW_BASE_PACKAGE_KEY=com.yourdomain.yourpackage"
  }

  @TaskAction
  fun action() {
    changeBasePackageExecutor.get()
  }

  companion object {
    const val TASK_NAME = "changeBasePackage"
  }
}

private abstract class ChangeBasePackageExecutor : ValueSource<Unit, ChangeBasePackageParameters> {

  private val projectDir: File = parameters.projectDir.get()
  private val newBasePackage: String = parameters.newBasePackage.get()
  private val oldBasePackage: String = parameters.oldBasePackage.get()
  private val oldPath: String = oldBasePackage.replace(PACKAGE_SEPARATOR, File.separatorChar)
  private val exclusionMatchers: List<PathMatcher> = parameters.excludes.get().map {
    FileSystems.getDefault().getPathMatcher("glob:$it")
  }

  private val logger: Logger = Logging.getLogger(ChangeBasePackageExecutor::class.java)

  override fun obtain() {
    with(projectDir) {
      replaceBasePackageInExistingFiles()
      renameDirectories()
      removeOldDirectories()
    }
  }

  private fun File.replaceBasePackageInExistingFiles() {
    walkTopDown().forEach { file ->
      val filePath = file.toPath()
      if (file.isFile && filePath.isNotExcluded()) {
        var text = file.readText()
        text = text.replace(oldBasePackage, newBasePackage)
        file.writeText(text)
      }
    }
  }

  private fun File.renameDirectories() {
    val newPath = newBasePackage.replace(PACKAGE_SEPARATOR, File.separatorChar)

    walkTopDown().forEach { file ->
      if (file.isDirectory && file.path.contains(oldPath) && file.toPath().isNotExcluded()) {
        val newDirPath = file.path.replace(oldPath, newPath)
        val newDir = File(newDirPath)
        newDir.parentFile.mkdirs()
        val isRenamingSuccessful = withRetry { file.renameTo(newDir) }
        if (isRenamingSuccessful) {
          logger.lifecycle("Renamed directory: ${file.path} to $newDirPath")
        } else {
          logger.error("Failed to rename directory: ${file.path} to $newDirPath")
        }
      }
    }
  }

  private fun File.removeOldDirectories() {
    walkBottomUp().forEach { file ->
      if (file.isDirectory && file.listFiles()?.isEmpty() == true) {
        file.deleteAlongWithEmptyParents()
      }
    }
  }

  private fun File.deleteAlongWithEmptyParents() {
    val parent = this.parentFile
    if (this.delete()) {
      parent?.let {
        if (it.isDirectory && it.listFiles()?.isEmpty() == true) {
          it.deleteAlongWithEmptyParents()
        }
      }
    }
  }

  private fun Path.isNotExcluded(): Boolean = !exclusionMatchers.any { it.matches(this) }
}

private interface ChangeBasePackageParameters : ValueSourceParameters {
  val projectDir: Property<File>
  val oldBasePackage: Property<String>
  val newBasePackage: Property<String>
  val excludes: SetProperty<String>
}

private fun withRetry(tries: Int = 3, action: () -> Boolean): Boolean {
  var triesCount = 0
  do {
    if (action()) return true
    triesCount++
  } while (triesCount < tries)
  return false
}
