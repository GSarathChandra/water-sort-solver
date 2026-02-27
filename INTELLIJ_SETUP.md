# IntelliJ IDEA Setup for Water Sort Solver

This project uses **Bazel** as the build system. Follow these steps to set up the project in IntelliJ IDEA.

## Prerequisites

1. **Java 11 or higher** installed
2. **Bazel** installed (verify with `bazel --version`)
3. **IntelliJ IDEA** (Community or Ultimate Edition)

## Setup Steps

### 1. Install IntelliJ Bazel Plugin

1. Open IntelliJ IDEA
2. Go to **Settings/Preferences** → **Plugins**
3. Search for "**Bazel**" (by Google)
4. Install the plugin
5. Restart IntelliJ

### 2. Open Project

**Option A: Import Bazel Project**
1. **File** → **Import Bazel Project**
2. Select the project directory: `/Users/s.gullapalli/Documents/water-sort-solver`
3. Click **Next**
4. The plugin will detect the `.bazelproject` file automatically
5. Click **Finish**

**Option B: Open Existing Project**
1. **File** → **Open**
2. Select the project directory
3. IntelliJ will detect the Bazel configuration automatically

### 3. Sync Project

1. After opening, click **Bazel** → **Sync** → **Sync Project with BUILD Files**
2. Wait for the sync to complete (this will download dependencies)

## Project Structure

```
src/
  solver/
    base/          # Core solver classes (FlaskGameState, Color, Move, etc.)
    bfs/           # BFS solver implementation
    old/           # Legacy code and tests
    test/          # Unit tests
```

## Build Targets

- `//src:base` - Base library
- `//src:bfs` - BFS solver library
- `//src:old` - Old/legacy code
- `//src:solver` - Main executable binary
- `//src:base_test` - Unit tests for base library
- `//src:old_test` - Tests for legacy code

## Running the Project

### From IntelliJ
1. Open the **Bazel** tool window (View → Tool Windows → Bazel)
2. Navigate to `//src:solver`
3. Right-click → **Run Target**

### From Command Line
```bash
# Build
bazel build //src:solver

# Run
bazel run //src:solver

# Test (note: tests have JUnit5+Bazel config issues, but code compiles)
bazel test //src:base_test
bazel test //src:old_test

# Build everything
bazel build //src:all
```

## Dependencies

Managed via `WORKSPACE` file:
- **JUnit 5 (Jupiter)** 5.10.1 - Testing framework
- **Hamcrest** 2.2 - Matcher library (used in FlaskGameState)

## Troubleshooting

### "No SDK configured"
1. **File** → **Project Structure** → **SDKs**
2. Add Java SDK (Java 11+)
3. Set as project SDK

### "Bazel not found"
1. Install Bazel: `brew install bazel` (macOS)
2. Verify: `bazel --version`
3. Restart IntelliJ

### Code doesn't compile in IDE
1. Click **Bazel** → **Sync** → **Sync Project with BUILD Files**
2. **File** → **Invalidate Caches** → **Invalidate and Restart**

### Bazel plugin not showing up
1. Check plugin is installed and enabled in **Settings** → **Plugins**
2. Make sure `.bazelproject` file exists in project root
3. Restart IntelliJ

## Code Quality Notes

✅ **All package declarations match directory structure**
✅ **Code compiles successfully with Bazel**
✅ **Main binary runs successfully**
⚠️ **Unit tests have JUnit 5 + Bazel runner configuration issues** (known issue, code is valid)

## IntelliJ Features

Once set up, you'll have:
- ✅ Code completion and navigation
- ✅ Syntax highlighting
- ✅ Refactoring support
- ✅ Run/Debug configurations via Bazel
- ✅ Integrated terminal for Bazel commands
