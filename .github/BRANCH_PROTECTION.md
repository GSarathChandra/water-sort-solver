# GitHub Branch Protection Setup

This guide will help you configure branch protection rules to require CI checks before merging PRs.

## Setting Up Branch Protection Rules

### 1. Navigate to Branch Protection Settings

1. Go to your repository on GitHub
2. Click **Settings** (repository settings, not account)
3. Click **Branches** in the left sidebar
4. Click **Add branch protection rule** (or edit existing rule for `main`)

### 2. Configure Protection Rules

**Branch name pattern:** `main`

#### Required Status Checks

✅ **Require status checks to pass before merging**
- Check: "Require branches to be up to date before merging"
- Select the following status checks:
  - `Build and Test` (from ci.yml)
  - `PR Build & Test Validation` (from pr-checks.yml)

#### Additional Recommended Settings

✅ **Require a pull request before merging**
- Require approvals: `1` (optional, based on team size)
- Dismiss stale pull request approvals when new commits are pushed

✅ **Do not allow bypassing the above settings**
- Enforces rules for administrators too (recommended)

### 3. Save Changes

Click **Create** or **Save changes**

## What This Enforces

- ✅ All PRs must pass `bazel build //src:all`
- ✅ All PRs must pass `bazel test //src:all`
- ✅ PRs cannot be merged if CI fails
- ✅ Force pushes to `main` are blocked
- ✅ Direct commits to `main` are blocked (must use PRs)

## Testing Your CI Setup

### From a Feature Branch

```bash
# Create a new branch
git checkout -b test-ci-setup

# Make a small change
echo "# Test CI" >> README.md

# Commit and push
git add README.md
git commit -m "Test CI pipeline"
git push -u origin test-ci-setup
```

Then create a PR on GitHub and watch the CI checks run!

## CI Workflow Files

- `.github/workflows/ci.yml` - Runs on push to main and PRs
- `.github/workflows/pr-checks.yml` - Detailed PR validation checks

## Troubleshooting

### CI checks don't appear
- Ensure workflows are pushed to the repository
- Check the **Actions** tab on GitHub to see if workflows are enabled
- Workflows must exist in the **base branch** (main) to run on PRs

### CI checks fail
- View logs in the **Actions** tab
- Common issues:
  - Build failures: Check build errors in the logs
  - Test failures: Review test output with `--test_output=errors`
  - Cache issues: Clear cache by changing cache key version

### Need to merge despite CI failure
- Only repository admins can do this (if bypass is not disabled)
- Generally should be avoided - fix the underlying issue instead
