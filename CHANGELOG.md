## Change log
----------------------

Version 20.1-SNAPSHOT
-------------

ADDED:

- `MergeWithTemplate.run(String[], Path, Path)`: the third argument is the source and destination base that neither
  the arguments nor the defaults name, which `run(String[], Path)` takes from the current working directory, as
  before (#99)

CHANGED:

- the tests write into temporary directories, no longer into `src/test/resources` or the project directory, and
  change copies of the fixtures instead of the fixtures: `FileTestCase` keeps its directories in a JUnit `@TempDir`
  with a copy of `src/test/resources`, and the classes that wrote there themselves do the same. A PIT run left
  result files behind and shortened committed fixtures whenever a mutant made a test fail before its own cleanup
  (#99)
- `MergeWithTemplateTest` and `NewMergeWithTemplateTest` call `run` instead of `main`, which ended the test JVM
  with `System.exit` on a usage error, so a mutant could kill the test process (#99)
- `MergeWithTemplate.main` ends the JVM with what the package-private `commandLine` returns: 0, or 2 after it has
  printed the usage error. The tests check both through `commandLine`, and the success path of `main` and
  `run(String[])` in `MergeWithTemplateMainTest` (#99)
- the tests PIT found missing: PIT detects 1117 of the 1204 mutants, 93%, up from 985 of 1202, and the CI build
  fails below 91%. The tests check, among others, the default files, the report and the modification times of
  `MergeWithTemplate`; `equals`, `hashCode` and `toString` of the compare, search and model beans; every getter
  of `SystemPropertiesExtensions` and `SystemFileExtensions` against its system property; a directory copy that
  meets a directory it cannot read; the charsets of the write and read overloads; a rename without the copy
  fallback; the merge strategies; and that an empty search result is a list the caller can add to. Of the 87
  mutants left, `build.gradle` says what decides each group; most are booleans that cannot be `false` (#117) and
  `hashCode` arithmetic. Line coverage 98.8%, branch coverage 93.5% (#99)
- `SearchableTest`, `FileContentInfoTest` and `FileInfoTest` write into a `@TempDir`, no longer into
  `java.io.tmpdir` or `/tmp/foo/bar` (#99, #119)
- the coverage floor rises to what the tests reach: the build fails below 98% of the lines or 93% of the
  branches, up from 90% and 85% (#99)
- the tests no longer write into the system's temporary directory: `MergeFileExtensionsTest`,
  `FileTimeExtensionsTest`, `LineAppenderTest`, `FileFactoryTest` and `DirectoryFactoryTest` work in a JUnit
  `@TempDir`, and `java.io.tmpdir` of the test JVM and of PIT's minions is a directory below `build/`, for the
  methods that use the temporary directory by design. A test run left 12 entries in the system's temporary
  directory and a PIT run 156; both leave none now (#119)
- `FileSearchExtensionsTest.testGetAllFilesFromDir` and `testGetAllFilesFromDirRecursive` check what the methods
  return; they compared their expectation with their own list (#119)
- the Javadoc of `ModifyFileExtensions.concatenateAll(List)`, `concatenateFilesWithExtension(File, String)` and
  `concatenateFilesWithExtensionRecursive(File, String)` says that the result file is created in the system's
  temporary directory and that the caller deletes it (#119)
- dependencies: silly-io 5.0 (#98), jsoup 1.24.1, Spotless 8.11.0
- mutation testing with PIT 1.30.1 (`./gradlew pitest`, `gradle/mutation-testing.gradle`): the tests detect 985 of
  the 1202 mutants, 82%, and the CI build fails below 80% (`mutationThreshold` in `build.gradle`; PIT rounds the
  score half up to a whole percent before it compares). The README's mutation score badge shows the score of
  `develop`, which CI publishes on the `gh-pages` branch from each push to it (#99)
- the test runtime class path no longer holds this project's own jar: zip4j-extension depends on an older
  file-worker, which Gradle resolved to this project, so `test` needed `mustRunAfter(jar)`, and so would `pitest`.
  The workaround is gone with its cause (#99)
- the PIT plugin no longer adds `junit-platform-launcher` itself (`addJUnitPlatformLauncher = false`; it is
  declared in `testRuntimeOnly`). To find its version the plugin resolved a copy of `testImplementation` without
  the constraints and the exclusion of `gradle/dependencies.gradle`, and the dependency submission reported what
  that copy held: jackson 2.16.0-rc1 and an old file-worker with commons-io 2.11.0, commons-lang3 3.12.0 and
  commons-beanutils 1.9.4, 18 Dependabot alerts for versions no class path of the build uses (#103)

Version 20.0
-------------

BREAKING:

- Java 25 is the minimum, up from 17: the family builds, tests and releases on JDK 25
  (astrapi69/mystic-crypt#234), so this release is 20.0 instead of 19.2 (#41)
- `FileCreationState` carries no file: `getFile()` and `setFile(File)` are gone, since a constant is shared by the
  whole JVM and `newDirectory` wrote every caller's directory into it; `DirectoryFactory.newTempDir` (both
  overloads) is gone with them, `newTempDirectory` returns the created directory (#68)
- every comparison of `CompareFileExtensions` and `SimpleCompareFileExtensions` rejects a directory, as the source
  and as the file to compare, with an `IllegalArgumentException` that names it: a directory as the file to compare
  threw a NullPointerException in the name check, one as the source was compared, `compareFiles(File, File, boolean)`
  reported two empty directories as equal, and `compareFilesByChecksum` compared two directories through a
  checksum that can report two different trees as equal. The finders never pass a directory, so their results stay
  (#78)

FIXED:

- `changeFilenameSuffix` keeps a file in its directory below a directory whose name holds a dot (`~/.cache`,
  `~/.config`): with silly-io 3.6 the target of a file without an extension was built outside it, so a rename could
  move the file away and replace another one; silly-io 4.0 looks for the extension in the file name only (#37,
  astrapi69/silly-io#9)
- a copy without charsets keeps every byte: `CopyFileExtensions.copyFile` copied through a Reader and a Writer
  and corrupted binary files, backups made with `newBackupOf` and the File-based directory copies included (#45)
- a copy onto the file itself is refused with an `IllegalArgumentException` instead of emptying the file (#73)
- `copyDirectoryWithFileFilter` leaves out the listed files at every level, not only the top one (#53)
- the File-based directory copies answer for the whole tree, not for the last entry they listed (#54)
- the File-based copies into a directory create its missing parents (#55)
- `copyFile` names the source, not the destination, when the source is a directory (#56); the Javadoc of
  `Copyable#copyFile` says it copies to a destination file (#63)
- `FileSearchExtensions.match` returns false for a name shorter than the suffix instead of throwing, so
  `findFiles(String, String[])`, `deleteAllFilesWithSuffix` and `changeAllFilenameSuffix` no longer stop half way (#50)
- `findFiles(File, String)` returns the files of the searched directory, not of the working directory (#49)
- `getFileLengthInKilobytes` and `getFileLengthInMegabytes` return the length of the file, not the size of its
  partition (#57)
- `findFilesWithPrefixAndExtension` and its recursive variant read the prefix and the extension as text, not as a
  regular expression (#58)
- `findFilesWithPrefixAndExtension` and its recursive variant delegate to `Searchable`, which gains the top-level
  variant, so both answer alike from one definition of a match: the name starts with the prefix and ends with a dot
  and the extension, which may overlap. The prefix `report.` and the extension `txt` now find `report.txt` (#88)
- `compareFileContentByBytes` and `compareFileContentByLines` compare the content; they reported equal content
  for any two files (#46)
- `findEqualFilesWithSameContent` compares the content it names (#47)
- the content equality of `compareFileContentByBytes` and `compareFileContentByLines` says whether the content is
  the same, whatever the name, the time or the extension: it was true for any two files whose other properties
  differed, without a comparison. `compareFileContentByBytes` takes files of unequal length as unequal without
  reading them; `compareFileContentByLines` compares the lines with the line ends ignored and takes no shortcut over
  the length, so the same lines with `\r\n` and with `\n` line ends are equal content (#77)
- `CompareFileExtensions` compares a file without an extension instead of throwing a NullPointerException, so
  the finders work on a directory with such a file (#48)
- `compareFiles(File, File, true)` and `compareFileContentByLines` count a line the second file has beyond the
  first (#52)
- `appendSystemtimeToFilename` (and `renameFileWithSystemtime`) takes a name without a dot (#64)
- `new FileInfo(File)` and `FileInfo.toFileInfo(File)` take the parent of the absolute file, so a relative file
  without a parent works (#66)
- `FileFactory.newFiles` reports the overall state: FAILED if any file failed, else CREATED if any was created, else
  ALREADY_EXISTS (#67)
- `changeAllFilenameSuffix` returns the files it could not rename, an empty list and never null when every rename
  works; with `delete`, a file whose copy fails goes into that list too, instead of an exception in the middle of
  the batch. `Renameable#changeAllFilenameSuffix` delegates to it, and the Javadoc of both says what `delete` and
  the list mean (#69)
- `write2FileWithBuffer` creates a destination that does not exist (#70)
- the messages of `RenameFileExtensions` for a missing file read as `Renameable`'s (#71); the Javadoc of `moveFile`
  says it moves to a target file (#65)
- `MergeFileExtensions.mergeByKey` splits at the delimiter as it is, not as a regular expression, and keeps every
  line (#51)
- `DeleteFileExtensions.delete` reports true for a directory it deleted (#59)
- a merge under `TARGET_AS_MASTER` reports every source file it cannot delete, in one `IOException` at the end,
  after merging the rest (#60)
- a merge without a strategy skips a missing source, as the strategy variant does, and empty files merge to an
  empty file (#61)
- `deleteAllFiles` writes nothing to stderr: not for a regular file (#62), not for a restricted directory (#79)
- a merge over a directory it cannot read merges the rest and names that directory in its one `IOException`, next to
  the source files it could not delete, under every strategy and without one; it threw a bare
  NullPointerException without a strategy and under `TARGET_AS_MASTER`, and left the files out silently under
  `SOURCE_TO_TARGET` (#76)
- a copy failure that stops a merge with a strategy carries the delete failures and the unread directories collected
  until then as suppressed exceptions, instead of dropping them (#80)
- a copy failure that stops a merge without a strategy is thrown as the `IOException` the method declares, with the
  unread directories collected until then as suppressed exceptions; it was wrapped in a `RuntimeException`, and
  the unread directories were dropped (#90)

CHANGED:

- built with Gradle 9.8.2 and the nmcp settings plugin 1.6.2, without a deprecation warning (#83)

- the tests resolve jackson 2.22.4 through a constraint to jobj-copy 4.3, no longer the release candidate
  2.16.0-rc1 that jobj-contract-verifier 5.2 brought (#87); the 15 Dependabot alerts on that test-only jackson
  are closed as false alarms for users (#86)

- the tests cover 95.7% of the lines and 89.6% of the branches, up from 70.6% and 62.4%, and the build
  fails below 90% of the lines or 85% of the branches (`jacocoTestCoverageVerification` in `check`). The defects
  the new tests found are #45 to #71 (#38)

- dependencies raised with versionCatalogUpdate, stable versions only: checksum-up 3.2, commons-lang3 3.21.0,
  crypt-api 10.3, silly-collection 28.2, and for the tests meanbean-factories 3.1, mockito 5.24.0 and test-object 9.1.
  crypt-api 10 names its module `io.github.astrapi69.crypt.api`, which `module-info.java` now requires (#43)

- the grgit and hierynomus license plugins are gone: a release is tagged through git
  (`gradle/tagging.gradle`), and Spotless writes the license header from `src/main/resources/license-header.txt` (#41)

- a release is published from its `RELEASE-*` tag by the new `publish.yml`, as in the rest of the family,
  and a snapshot by a manual run of it; `gradle.yml` only builds and no longer uploads on every push to
  `develop`. The build refuses an upload to the Central Portal outside CI, and a release from anything but
  a `RELEASE-*` tag (#24)

Version 19.1
-------------

### 📖 Documentation
- **Created a comprehensive Wiki for the File-Worker library**
  - Added a structured [Home Wiki Page](https://github.com/astrapi69/file-worker/wiki) with navigation.
  - Implemented a **custom sidebar** for easy access to different documentation sections.
  - Linked the **wiki homepage** in the `README.md` for better accessibility.
  - Added **"Getting Started"** documentation in the wiki and updated references in README.

### 🔄 Refactor
- **Gradle Configuration Cleanup**
  - Moved exclusion lists to `gradle/list/` for better organization.
  - Introduced `java-compile.gradle` for Java compilation settings.
  - Updated `apply-gradle-files.gradle` to handle exclusions properly.
  - Improved property handling in `repositories.gradle`, `packaging.gradle`, and `publishing.gradle`.

### 🗑️ Cleanup
- **Removed obsolete IntelliJ run configurations**
  - Deleted outdated Gradle run configurations for `dependencyUpdates`, `spotlessJavaApply`, and `spotlessMiscApply`.

### ⬆️ Dependency Updates
- **JUnit Jupiter**
  - Upgraded `junit-jupiter` and `junit-jupiter-params` to `5.12.0-RC1`.
- **Library Updates**
  - Updated `commons-io` to version 2.18.0
  - Updated `gradle-plugin-spotless` to version 7.0.2
  - Updated `gradle-plugin-versions` to version 0.52.0
  - Updated `jsoup` to version 1.18.3
  - Updated `mockito-core` to version 5.15.2
  - Updated `silly-io` to version 3.6
  - Upgraded `Gradle Wrapper` to version 8.12.1
  - Updated `jobj-compare` to version 12 (#12): 11.1 declared commons-beanutils 1.9.4, which reached every consumer of file-worker; 12 declares 1.11.0

### ✨ Added
- Added `newDataScienceDirStructure` method to `DirectoryStructureTestData` for structured test data generation.

### 🛠 Fixed
- Disabled `nl.littlerobots.vcu.resolver` in `gradle.properties`.
- Removed unnecessary blank line in `MergeDirectoryExtensionsTest`.

### 📜 Package Documentation
- **Added `package-info.java` files** for better package-level documentation:
  - `io.github.astrapi69.file.copy.api`
  - `io.github.astrapi69.file.create.api`
  - `io.github.astrapi69.file.create.model`
  - `io.github.astrapi69.file.delete.api`
  - `io.github.astrapi69.file.rename.api`
  - `io.github.astrapi69.file.read.api`
  - `io.github.astrapi69.file.search.api`
  - `io.github.astrapi69.file.write.api`
  - `io.github.astrapi69.file.merge.strategy`

Version 19.0
-------------

ADDED:

- `setSystemProperties` method to set system properties from a provided `Properties` object, allowing convenient bulk assignment of system properties
- added new property for the gradle-plugin version-catalog-update for resolve dependency-versions with ease

CHANGED:

- update to jdk version to version 21
- update gradle to new version 8.11-rc-3

Version 18.1
-------------

ADDED:

- new interface Storable for store files
- new interface Searchable for search files
- new interface Renameable for rename files or directories
- new interface Readable for read files
- new interface Erasable for delete files or directories
- new interface Createable for create files or directories
- new interface Copyable for copy files or directories
- new class CopyDirectoryExtensions with all methods for copy directories
- new methods in class CopyFileExtensions with classes from the nio package

CHANGED:

- update gradle to new version 8.11-rc-1
- update of gradle-plugin with id 'com.diffplug.spotless' to new beta version 7.0.0.BETA4
- update of gradle-plugin with id 'nl.littlerobots.version-catalog-update' to new patch version 0.8.5
- removed all deprecated classes from package 'io.github.astrapi69.file.csv'
- move all methods related to copy directories from CopyFileExtensions to the new class CopyDirectoryExtensions
- optimized class DirectoryFactory with classes from the nio package
- optimized class DeleteFileExtensions with classes from the nio package
- update of test dependency mockito-core to the new patch version 5.14.2
- update of test dependency junit-jupiter to the new minor version 5.11.3

Version 18.0
-------------

ADDED:

- new class LineAppender for appending lines of text to a file
- new method in PathFinder for get the current directory
- new method in ModifyFileExtensions that concatenate the given list of text files into a single result text file

CHANGED:

- rename of module name 'file.worker' to new name 'io.github.astrapisixtynine.file.worker'
- update gradle to new version 8.10.2
- update of gradle-plugin dependency 'org.ajoberstar.grgit:grgit-gradle' in version 5.3.0
- update of gradle-plugin with id 'com.diffplug.spotless' to new beta version 7.0.0.BETA2
- update of dependency commons-lang3 to the new minor version 3.17.0
- update of dependency silly-collection to the new minor version 28.1
- update of dependency comparator-extensions to new major version 2
- update of dependency commons-io to the new minor version 2.17.0
- update of dependency silly-bean to the new major version 3.0
- update of dependency silly-io to the new minor version 3.2
- update of dependency silly-strings to new minor version 9.2
- update of test dependency test-object to new major version 9
- update of test dependency mockito-core to the new patch version 5.14.1
- update of test dependency junit-jupiter to the new minor version 5.11.2
- tagged all methods from class WriteFileExtensions as deprecated
- tagged all classes from package 'io.github.astrapi69.file.csv' as deprecated. Moved to its own module 'cvs-worker'
- dependencies are managed now over bundles

Version 17.4
-------------

ADDED:

- new search method that find all files in the specified directory that match the given prefix and file extension
- new factory method in FileInfo toFile that creates a new File object from this FileInfo object
- new test dependency org.junit.jupiter:junit-jupiter in version 5.11.0-M2

CHANGED:

- update gradle to new version 8.10-rc-1
- remove of test dependency testng
- tagged all files in zip package as deprecated because there moved to another module zip.worker
- update of dependency commons-lang3 to the new minor version 3.16.0
- update of test dependency jsoup to new patch version 1.18.1
- remove of zip package
- update of dependency checksum-up to new minor version 3.1
- update of dependency crypt-api to the new minor version to 9.3

Version 17.3
-------------

ADDED:

- new class DeleteLinesByIndexInFile that deletes lines by index in a given file
- new method for find the index of a given search string that start with in a given file

CHANGED:

- update of dependency silly-io to new minor version 3.1
- update of dependency silly-strings to new minor version 9.1
- update of dependency throwable to new major version 3
- update of test dependency jobj-contract-verifier to new minor version 5.1
- fix of all javadoc issues


Version 17.2
-------------

ADDED:

- new test dependency jsoup
- new test dependency mockito-core
- new properties file for spotless for the order of imports
- new package-info.java files to packages
- new factory methods in DirectoryFactory for creation of temporary directory
- new libs.versions.toml file for new automatic catalog versions update

CHANGED:

- update gradle to new version 8.8
- update of com.github.ben-manes.versions.gradle.plugin to new version 0.51.0
- update of gradle-plugin dependency 'org.ajoberstar.grgit:grgit-gradle' in version 5.2.2
- update of gradle-plugin dependency 'com.diffplug.spotless:spotless-plugin-gradle' to new minor version 7.0.0.BETA1
- update of commons-io dependency to the new minor version 2.15.1
- update of commons-lang3 dependency to new version 3.14.0
- update of dependency silly-io to new major version 3
- update of dependency silly-collection to new version 27.1
- update of dependency checksum-up to new major version 3
- removed deprecated methods in extension class WriteFileExtensions
- removed all obsolete package.html files
- update of test dependency test-object to new minor version 8.2
- update of test dependency testng to new version 7.10.1
- update of test dependency zip4j to new version 2.11.5

Version 17.1
-------------

ADDED:

- new extension class for store string objects and byte arrays to files

CHANGED:

- update gradle to new version 8.4
- update of com.github.ben-manes.versions.gradle.plugin to new version 0.49.0
- update of gradle-plugin dependency 'com.diffplug.spotless:spotless-plugin-gradle' to new minor version 6.22.0
- update of commons-io dependency to the new minor version 2.14.0
- update of dependency crypt-api to the new minor version to 8.7
- update of dependency silly-strings to new major version 9

Version 17
-------------

CHANGED:

- update to jdk version 17
- update gradle to new version 8.3
- update of com.github.ben-manes.versions.gradle.plugin to new version 0.47.0
- update of gradle-plugin dependency 'org.ajoberstar.grgit:grgit-gradle' in version 5.2.0
- update of gradle-plugin dependency 'com.diffplug.spotless:spotless-plugin-gradle' to new minor
  version 6.20.0
- removed deprecated methods in class ReadFileExtensions
- update of commons-io dependency to new version 2.13.0
- update of commons-lang3 dependency to new version 3.13.0
- update of dependency crypt-api to new version to 8.6
- update of dependency silly-collections to new version 21
- update of dependency silly-io to new version 2.2
- update of dependency comparator-extensions to new version 1.5
- update of test dependency testng to new version 7.8.0
- update of test dependency 'com.github.meanbeanlib:meanbean' to new version 3.0.0-M9

Version 11.6
-------------


CHANGED:

- update gradle to new version 7.6-rc-3
- update of com.github.ben-manes.versions.gradle.plugin to new version 0.44.0
- improvement of method delete in class DeleteFileExtensions
- update of dependency crypt-api to new version to 8.4
- update of dependency checksum-up to new version 2.2
- update of test dependency test-object to new version 7.2
- tagged obsolete methods with input stream and readers as deprecated

Version 11.5.1
-------------

CHANGED:

- close reader after read file

Version 11.5
-------------

ADDED:

- new extension class for merge directories in to a target directory
- new factory class DirectoryFactory for create directories
- new factory class DirectoryStructureFactory for create directory structures
- new flag directory in FileInfo class
- new methods for get the temporary file of the user

CHANGED:

- moved all factory methods from FileFactory related to directories to new factory class DirectoryFactory
- update of com.github.ben-manes.versions.gradle.plugin to new version 0.43.0

Version 11.4
-------------

ADDED:

- new module-info.java file

CHANGED:

- removed dependency zip4j and all related classes
- removed all deprecated methods in class FileExtensions
- update of dependency silly-collections to new version 20.1
- update of dependency silly-strings to new version 8.2
- update of dependency comparator-extensions to new version 1.4

Version 11.3
-------------

ADDED:

- new bean class FileContentInfo that can be used for file creation with content and the corresponding checksum
- new method in FileExtensions that gets the content type from the file

CHANGED:

- update of gradle-plugin dependency 'com.diffplug.spotless:spotless-plugin-gradle' to new minor
  version 6.11.0
- update of dependency zip4j to new version 2.11.2

Version 11.2
-------------

ADDED:

- new factory methods in FileFactory class for create File objects quietly
- new factory method in FileInfo class for create the new File object from a FileInfo object
- new factory method in FileInfo class for create the new FileInfo object from a File object

CHANGED:

- update of gradle-plugin dependency 'com.diffplug.spotless:spotless-plugin-gradle' to new minor
  version 6.10.0
- update of dependency crypt-api to new version to 8.3
- update of dependency checksum-up to new version 2.1

Version 11.1
-------------

ADDED:

- new factory method in class Zip4jExtensions for create a new ZipParameters object from the given
  parameters
- new bean class FileInfo that can be used for file creation
- new factory method in class FileFactory with new bean class FileInfo

CHANGED:

- update of silly-collection dependency to new version 20
- update of silly-bean dependency to new version 2
- update of test dependency jobj-contract-verifier to new version 4
- update of test dependency test-object to new version 7.1

Version 11
-------------

ADDED:

- new factory method in class FileFactory for create a new directory with the absolute path as
  string object
- new factory method in class FileFactory for create a new file with the parent directory and the
  file name as string objects
- new factory method in class FileFactory for create a new directory with the parent directory and
  the file name as string objects
- new test dependency comparator-extensions in new version 1.2

CHANGED:

- update to jdk version 11
- update gradle to new version 7.5.1
- update of gradle-plugin dependency 'com.diffplug.spotless:spotless-plugin-gradle' to new minor
  version 6.9.1
- update of gradle-plugin dependency 'org.ajoberstar.grgit:grgit-gradle' in version 5.0.0
- update of dependency silly-io to new version 2.1
- update of test dependency test-objects to new version 6.1
- removed deprecated class FileConst
- update of test dependency testng to new version 7.6.1
- update of dependency zip4j to new version 2.11.1
- update of dependency crypt-api to new version to 8.2
- update of throw-able dependency to new version 2.3
- update of jobj-compare dependency to new version 11.1
- update of checksum-up dependency to new version 2
- update of silly-collections dependency to new version 19

Version 8.2
-------------

ADDED:

- new factory method in class FileFactory for create a new directory with the parent directory and
  the directory name
- new gradle plugin spotless for formatting source code
- new gradle plugin grgit-gradle for create git tags for releases

CHANGED:

- update gradle to new version 7.4
- update of gradle plugin dependency com.github.ben-manes.versions.gradle.plugin to new version
  0.42.0
- update of test dependency testng to new version 7.5
- update of dependency zip4j to new version 2.9.1
- update of dependency silly-io to new version 1.7
- update of dependency crypt-api to new version to 7.7
- update of test dependency test-objects to new version 5.7
- update of test dependency jobj-contract-verifier to new version 3.5

Version 8.1
-------------

ADDED:

- new method for get the root directory of the current system
- new method for get the root directory from a given file
- new method for get the drive directory from the given drive letter on windows operating system
- improve gradle build performance by adding new gradle parameters for caching, parallel, configure
  on demand and file watch

CHANGED:

- update gradle to new version 7.3
- update of silly-strings dependency to new version 8.1
- update of jobj-compare dependency to new version 2.9
- update of silly-collections dependency to new version 18

Version 8
-------------

ADDED:

- new method for count lines from file in the class ReadFileExtensions

CHANGED:

- removed deprecated classes and methods

Version 5.10
-------------

ADDED:

- new factory method for create a new File object from a string with the absolut path and a boolean
  flag if true to create an empty file

CHANGED:

- update gradle to new version 7.2
- update gradle-plugin dependency of gradle.plugin.com.hierynomus.gradle.plugins:
  license-gradle-plugin to new version 0.16.1
- update of commons-io dependency to new version 2.11.0
- update of silly-io dependency to new version 1.6
- update of throw-able dependency to new version 1.7
- update of crypt-api dependency to new version to 7.6.1
- update of test dependency test-objects to new version 5.5
- moved all packages to the 'file' package

Version 5.9
-------------

ADDED:

- new method that can delete files recursively with a given prefix in a source directory
- new unit test classes for the class SystemFileExtensions and SystemPropertiesExtensions created
- new factory method for create new File with parent directory and filename in FileFactory class
- added java area in build.gradle for activate support of module path inference

CHANGED:

- update of silly-io dependency to new version 1.4
- update of throw-able dependency to new version 1.6
- update of commons-lang3 dependency to new version 3.12.0
- remove of unused silly-collections dependency
- update of zip4j dependency to new version 2.9.0
- update of com.github.ben-manes.versions.gradle.plugin to new version 0.39.0
- changed dependencies to the new group id io.github.astrapi69
- update gradle to new version 6.9
- changed to new package io.github.astrapi69
- update of jobj-compare dependency to new version 3.8
- update of silly-collections dependency to new version 8.7
- update of silly-io dependency to new version 1.3
- update of commons-io dependency to new version 2.10.0
- update of commons-lang3 dependency to new version 3.12.0
- update of zip4j dependency to new version 2.6.4
- update of test dependency testng to new version 7.4.0
- update of test dependency meanbean-factories to new version 1.3

Version 5.7
-------------

ADDED:

- new methods in class SystemFileExtensions for get the download directory as File object

CHANGED:

- update gradle to new version 6.5
- update of jobj-compare dependency to new version 3.6.1
- update of silly-collections dependency to new version 8.2
- update of checksum-up dependency to new version 1.1
- update of commons-io dependency to new version 2.7
- update of zip4j dependency to new version 2.6.1
- update of gradle plugin sonarqube to new version 3.0
- extracted project properties to gradle.properties

Version 5.6
-------------

ADDED:

- new class that provides most user environment variables like users home
- new class in test sources that can copy gradle config files after a migration from maven

CHANGED:

- update of jobj-compare dependency to new version 3.4
- update of silly-collections dependency to new version 8.1
- update of commons-lang3 dependency to new version 3.10
- update of test dependency testng to new version 7.2.0
- update of test dependency meanbean-factories to new version 1.2

Version 5.5
-------------

ADDED:

- new idea run configurations for gradle builds created
- created file gradle.properties
- new dependency jobj-compare in version 3.2 added
- new dependency throw-able in version 1.2 added
- new dependency checksum-up in version 1 added
- new test method for copy run configurations files from one source project to another target
  project and modifies its content

CHANGED:

- removed lombok dependency
- removed all lombok dependent imports
- removed idea run configurations for maven
- moved dependency versions to file gradle.properties
- update of silly-strings dependency version to 5.5
- update of silly-io dependency version to 1.2
- update of silly-collections dependency version to 8
- update of crypt-api dependency version to 7.3
- removed of mystic-crypt dependency
- removed of junit dependency
- removed of mockito-core dependency

Version 5.4
-------------

CHANGED:

- removed maven related files

Version 5.3
-------------

ADDED:

- new method created for modify a single file
- gradle as build system

CHANGED:

- changed project nature from maven to gradle nature
- update of parent version to 5
- update of silly-collections dependency version to 5.4
- update of test-objects dependency version to 5.2
- update of silly-strings dependency version to 5.3.1
- update of jobj-contract-verifier dependency version to 3.2
- moved ChecksumExtensions class to mystic-crypt project and tagged as deprecated

Version 5.2
-------------

ADDED:

- new methods created for create directory from a Path obejct
- new dependency commons-lang3 in version 3.9 added
- new dependency jobj-contract-verifier in version 3.1 added
- new dependency commons-io in version 2.6 added
- new dependency silly-io in version 1.1 added
- new dependency silly-strings in version 5.3 added

CHANGED:

- update of parent version to 4.8
- removed jcommons-lang dependency
- removed jobject-evaluate dependency
- update of silly-collections dependency version to 5.2
- update of test-objects dependency version to 5.1

Version 5.1
-------------

ADDED:

- new extension class for modify file content
- new BiFunction interface created for generic modify file content
- new unit tests for the new extension class for modify file content

CHANGED:

- moved interfaces for comparison from files or directories to appropriate api package

Version 5.0.2
-------------

CHANGED:

- update of jcommons-lang dependency version to 5.1.1

Version 5.0.1
-------------

CHANGED:

- update of parent version to 4.5
- update of silly-collections dependency version to 5
- update of jobject-extensions version to 2.5
- update of jcommons-lang dependency version to 5.1
- update of test-objects dependency version to 5
- update of jobject-evaluate dependency version to 2.5
- excluded logging dependencies

Version 5
-------------

ADDED:

- new method for get the checksum from a given byte arrays with a given algorithm

CHANGED:

- update of parent version to 4.2
- removed poi dependencies and corresponding classes
- update of silly-collections dependency version to 4.34.1
- update of mystic-crypt dependency version to 5.6

Version 4.23
-------------

CHANGED:

- update of parent version to 4.1
- update of jcommons-lang dependency version to 4.35
- update of silly-collections dependency version to 4.33
- update of mystic-crypt dependency version to 5.4
- update of jobject-extensions version to 1.12

Version 4.22
-------------

ADDED:

- new classes created and moved to them all methods that catches exceptions and handle them in a
  quietly manner

CHANGED:

- update of parent version to 3.12
- unit tests extended for improve code coverage
- removed deprecated methods, enums and classes
- update of silly-collections dependency version to 4.31

Version 4.21
-------------

ADDED:

- new dependency silly-collections in version 4.30.1

CHANGED:

- update of jcommons-lang dependency version from 4.33 to 4.34

Version 4.20
-------------

CHANGED:

- update of parent version to 3.11
- removed unneeded .0 at the end of version
- update of test-objects dependency version from 4.22.0 to 4.26
- update of jcommons-lang dependency version from 4.29.0 to 4.33
- javadoc improved
- tagged URLExtensions and Protocol classes as deprecated. Moved to net-extensions project

Version 4.19.0
-------------

ADDED:

- provide package.html for the javadoc of new packages
- Donation buttons extended for paypal and bitcoin
- added new meanbean dependency for better unit testing of beans

CHANGED:

- update of parent version and of dependencies versions
- javadoc extended and improved

Notable links:
[keep a changelog](http://keepachangelog.com/en/1.0.0/) Don’t let your friends dump git logs into
changelogs
