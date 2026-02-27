# CI Pipeline Documentation

## Overview

This repository uses GitHub Actions for Continuous Integration. The CI pipeline ensures code quality by building and testing all changes before they can be merged.

## Workflows

### 1. `ci.yml` - Main CI Pipeline
**Triggers:**
- Push to `main` or `master` branches
- Pull requests targeting `main` or `master`

**Actions:**
- Sets up Java 11
- Installs Bazel
- Builds all targets (`//src:all`)
- Runs unit tests (`//src:base_test`)
- Verifies main binary builds

### 2. `pr-checks.yml` - PR Validation
**Triggers:**
- Pull request opened, synchronized, or reopened

**Actions:**
- Complete build verification
- Detailed test execution with summary
- Reports results in PR checks

## What Gets Tested

### ✅ Included in CI
- **Build**: All production code (`//src:all`)
  - `//src:base` - Core solver classes
  - `//src:bfs` - BFS solver implementation
  - `//src:solver` - Main binary

- **Tests**: Main test suite
  - `//src:base_test` - Unit tests for core functionality (22 tests)

## Running Tests Locally

### Run CI checks locally
```bash
# Full CI simulation
bazel build //src:all
bazel test //src:base_test --test_output=errors

# Build verification
bazel build //src:solver
bazel run //src:solver

# Run tests with verbose output
bazel test //src:base_test --test_output=all
```

## CI Requirements for PRs

Before a PR can be merged, it must:

1. ✅ **Build successfully**: All code must compile without errors
2. ✅ **Pass tests**: `base_test` must pass all test cases
3. ✅ **Status checks pass**: Both CI workflows must complete successfully

## Viewing CI Results

### On GitHub
1. Navigate to your PR
2. Scroll to the bottom to see **Checks** section
3. Click **Details** on any check to view logs
4. Green checkmark ✅ = passed, red X ❌ = failed

### In Actions Tab
1. Go to repository → **Actions** tab
2. View all workflow runs
3. Click on any run to see detailed logs
4. Download logs for offline analysis

## Cache Optimization

The CI uses caching to speed up builds:
- **Bazel cache**: `~/.cache/bazel` (keyed by WORKSPACE/MODULE.bazel)
- **Bazelisk cache**: `~/.cache/bazelisk`

Cache is automatically invalidated when build files change.

## Troubleshooting

### CI is slow
- First run after cache invalidation can take 2-3 minutes
- Subsequent runs typically complete in 30-60 seconds

### Tests fail locally but pass in CI (or vice versa)
```bash
# Clean local build
bazel clean --expunge
bazel test //src:base_test
```

### Need to see more test output
```bash
# Locally
bazel test //src:base_test --test_output=all

# In CI: modify workflow to use --test_output=all
```

## Future Improvements

Potential enhancements:
- [ ] Add code coverage reporting
- [ ] Add linting/formatting checks
- [ ] Fix and include `old_test` in CI
- [ ] Add performance benchmarks
- [ ] Matrix testing (multiple Java versions)
