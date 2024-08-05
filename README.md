# Base Package Plugin

Gradle plugin that provides a task for changing the base package of your project, including updating the package declarations in your project's files.

## 📋 Requirements

- **Java**: 17 or higher

## 🚀 Applying the Plugin

Add the plugin to any Gradle project in which you need to change the base package.

```kotlin
plugins {
  id("io.github.pawgli.basepackage") version "0.1.3"
}
```

## 📝 Usage

### 1. Define the Current Base Package

In your project's `build.gradle.kts` file, assign the current base package to the `basePackage` property within the `basePackagePlugin` block.


```kotlin
basePackagePlugin {
  basePackage = "io.github.pawgli"
}
```

### 2. Specify Exclusions 🚫

Define any exclusions using (glob patterns)[https://docs.oracle.com/en-us/iaas/Content/devops/using/glob-patterns.htm].


```kotlin
basePackagePlugin {
  basePackage = "io.github.pawgli"
  exclude("**/excludeddir/**", "**/excluded-file.txt")
}
```

### 3. Execute the Package Change 🔄

Use the `changeBasePackage` task provided by the plugin to change the package. Pass the new package as a parameter.

```kotlin
./gradlew :yourproject:changeBasePackage -PnewBasePackage=your.new.package
```

## Try It Out! 🕵️‍♂️

Feel free to experiment with the plugin in the included [sample app](https://github.com/pawgli/base-package-plugin/tree/main/sample-app).
