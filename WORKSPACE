# Bazel workspace for water-sort-solver
workspace(name = "water_sort_solver")

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

# Rules for Java
RULES_JAVA_TAG = "7.11.1"
RULES_JAVA_SHA = "6f3ce0e9fba979a844faba2d60467843fbf5191d8ca61fa3d2ea17655b56bb8c"

http_archive(
    name = "rules_java",
    sha256 = RULES_JAVA_SHA,
    url = "https://github.com/bazelbuild/rules_java/releases/download/%s/rules_java-%s.tar.gz" % (RULES_JAVA_TAG, RULES_JAVA_TAG),
)

load("@rules_java//java:repositories.bzl", "rules_java_dependencies", "rules_java_toolchains")

rules_java_dependencies()
rules_java_toolchains()

# Rules for JVM external dependencies (Maven)
RULES_JVM_EXTERNAL_TAG = "6.0"
RULES_JVM_EXTERNAL_SHA = "85fd6bad58ac76cc3a27c8e051e4255ff9ccd8c92ba879670d195622e7c0a9b7"

http_archive(
    name = "rules_jvm_external",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    sha256 = RULES_JVM_EXTERNAL_SHA,
    url = "https://github.com/bazelbuild/rules_jvm_external/releases/download/%s/rules_jvm_external-%s.tar.gz" % (RULES_JVM_EXTERNAL_TAG, RULES_JVM_EXTERNAL_TAG)
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

# Maven dependencies
maven_install(
    artifacts = [
        "org.junit.jupiter:junit-jupiter-api:5.10.1",
        "org.junit.jupiter:junit-jupiter-engine:5.10.1",
        "org.junit.platform:junit-platform-console:1.10.1",
        "org.hamcrest:hamcrest:2.2",
    ],
    repositories = [
        "https://repo1.maven.org/maven2",
    ],
)