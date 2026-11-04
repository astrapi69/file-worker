# file-worker

**Java operations on files and directory trees: creating, writing and reading, copying, renaming,
moving and deleting, searching, comparing and merging files, and changing a file line by line.**

<div style="text-align: center">

[![Java CI with Gradle](https://github.com/astrapi69/file-worker/actions/workflows/gradle.yml/badge.svg)](https://github.com/astrapi69/file-worker/actions/workflows/gradle.yml)
[![Coverage Status](https://codecov.io/gh/astrapi69/file-worker/branch/develop/graph/badge.svg)](https://codecov.io/gh/astrapi69/file-worker)
[![Mutation Score](https://img.shields.io/endpoint?url=https%3A%2F%2Fraw.githubusercontent.com%2Fastrapi69%2Ffile-worker%2Fgh-pages%2Fbadges%2Fmutation-score-develop.json)](https://pitest.org/)
[![Open Issues](https://img.shields.io/github/issues/astrapi69/file-worker.svg?style=flat)](https://github.com/astrapi69/file-worker/issues)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.astrapi69/file-worker.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.astrapi69/file-worker)
[![Javadoc](https://www.javadoc.io/badge/io.github.astrapi69/file-worker.svg)](https://www.javadoc.io/doc/io.github.astrapi69/file-worker)
[![MIT license](https://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-25-orange)](https://openjdk.org/projects/jdk/25/)

</div>

file-worker collects the operations on files and directories that otherwise get written again in
every project. It builds on [silly-io](https://github.com/astrapi69/silly-io), which holds the
smaller helpers it uses: streams, file names, and file and filename filters.

## Why file-worker

With the JDK alone each of these is code of your own: copying a directory tree without some of its
files, changing the suffix of every file in a tree, merging text files by a strategy or directory
trees by the time their files were modified, comparing two files property by property, changing a
file line by line. file-worker has a method for each, and its tests cover 99.3% of the lines and
96.4% of the branches of 20.1, and detect 94% of the mutants PIT makes; the build fails below 98%,
93% and 92% ([Quality](#quality)).

It is not small: it needs Java 25 and brings 13 jars at runtime, Apache Commons IO among them. If
`java.nio.file.Files` covers what you need, such as reading and writing a file, copying, moving or
deleting one, walking a tree or comparing two files with `Files.mismatch`, the JDK does it without a
dependency. If you need a JDK older than 25, one jar instead of 13, or what file-worker does not
have, such as watching or tailing files, [Apache Commons IO](https://commons.apache.org/proper/commons-io/)
is the better choice: it runs on Java 8, has no runtime dependency of its own, and its `FileUtils`
copies, moves and deletes directory trees too.

## Table of contents

- [Why file-worker](#why-file-worker)
- [Requirements](#requirements)
- [Installation](#installation)
- [Quick start](#quick-start)
- [Features](#features)
- [Quality](#quality)
- [Verifying the release signatures](#verifying-the-release-signatures)
- [Releasing](#releasing)
- [Contributing](#contributing)
- [How this project is made](#how-this-project-is-made)
- [License](#license)
- [Support this project](#support-this-project)
- [Credits](#credits)

## Requirements

**JDK 25 or newer**, at build and at runtime: the published jar holds Java 25 bytecode and does not
load on an older JVM. On the module path its name is `io.github.astrapisixtynine.file.worker`.

file-worker has eight runtime dependencies, which Gradle and Maven fetch with it: commons-io and
commons-lang3 of Apache Commons, and six libraries of the same maintainer: silly-io, silly-strings,
silly-collection, throwable, checksum-up and crypt-api. With their own dependencies they put 13 jars
on the runtime class path.

## Installation

Latest release on Maven Central: 20.1

[![Maven Central](https://img.shields.io/maven-central/v/io.github.astrapi69/file-worker.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.astrapi69/file-worker)

**Gradle** (version catalog, `gradle/libs.versions.toml`):

```toml
[versions]
file-worker-version = "20.1"

[libraries]
file-worker = { module = "io.github.astrapi69:file-worker", version.ref = "file-worker-version" }
```

```groovy
dependencies {
    implementation libs.file.worker
}
```

**Gradle** (plain):

```groovy
dependencies {
    implementation "io.github.astrapi69:file-worker:20.1"
}
```

**Maven**:

```xml
<dependency>
    <groupId>io.github.astrapi69</groupId>
    <artifactId>file-worker</artifactId>
    <version>20.1</version>
</dependency>
```

20.1 has no breaking change against 20.0. Some of its fixes change what a caller sees where the code
did not do what its Javadoc said, for example an `IOException` from `StoreFileExtensions.toFile`
where a failed write returned `true`; the [CHANGELOG](CHANGELOG.md#version-201) names them under
FIXED, each with "For callers" where it matters. From 19.1, the three breaking changes of 20.0 apply
as well: Java 25 instead of 17, `FileCreationState` without its file, and every comparison refusing
a directory.

## Quick start

The examples are checked: each block runs in
[`ReadmeExamplesTest`](src/test/java/io/github/astrapi69/file/ReadmeExamplesTest.java), in a
temporary directory, `directory`, with the values its comments name as assertions.

**Creating files and directories.** `FileFactory.newFile` creates a missing file, with the
directories above it, and leaves an existing one as it is; the state it returns says which.

```java
File summary = new File(directory, "reports/2026/summary.txt");
FileCreationState created = FileFactory.newFile(summary); // CREATED, with reports/2026
FileCreationState again = FileFactory.newFile(summary);   // ALREADY_EXISTS
File archive = new File(directory, "archive");
FileCreationState made = DirectoryFactory.newDirectory(archive); // CREATED
```

`DirectoryFactory.newDirectory(File)` creates one directory, and answers `FAILED` when its parent is
missing; `DirectoryFactory.newDirectories(Path)` creates the parents too, as
`Files.createDirectories` does. The `...Quietly` variants of both factories throw a
`RuntimeException` instead of an `IOException`.

**Writing and reading.**

```java
File names = new File(directory, "names.txt");
StoreFileExtensions.toFile(names, List.of("Ada", "Grace"), StandardCharsets.UTF_8);
LineAppender.appendLines(names, "Linus");
List<String> lines = ReadFileExtensions.readLinesInList(names); // [Ada, Grace, Linus]
String second = ReadFileExtensions.readLine(names, 1);          // "Grace"
String text = ReadFileExtensions.fromFile(names);               // the whole file
```

Each line is written with the line end of the platform, and `LineAppender` creates a missing file.
The readers in the example read UTF-8; `fromFile` and `readLinesInList` also take a charset.
`StoreFileExtensions` without a charset writes the JVM's default charset, which is UTF-8 unless the
JVM is configured otherwise.

**Copying files and directory trees.**

```java
// project holds README.md, src/Main.java and src/Main.class
File copy = new File(directory, "copy");
boolean copied = CopyDirectoryExtensions.copyDirectoryWithFileFilter(project, copy, null,
    SuffixFileFilter.of(".class", false), true); // true: README.md and src/Main.java
Path backup = CopyFileExtensions.newBackupOf(project.toPath().resolve("README.md"));
```

The arguments after the source and the destination are the filter of the files to copy, `null` for
all of them, the filter of the files to leave out, here silly-io's `SuffixFileFilter`, and whether a
copy takes the last-modified time of its original. The filter of the files to copy also decides
which directories the copy enters: `SuffixFileFilter.of(".java", true)` accepts every directory as
well. A copy without charsets copies the bytes, one with a source and a destination charset
converts the text, and a copy of a `File` onto itself is refused with an `IllegalArgumentException`.
`newBackupOf` copies a file to its name with `.bak` appended, next to it.

**Renaming, moving and deleting.**

```java
// notes holds a.txt and old/b.txt
List<File> failed = RenameFileExtensions.changeAllFilenameSuffix(notes, ".txt", ".md"); // []
// notes holds a.md and old/b.md
File a = new File(notes, "a.md");
boolean moved = RenameFileExtensions.moveFile(a, new File(notes, "old/a.md")); // true
boolean deleted = DeleteFileExtensions.delete(new File(notes, "old"));         // true
```

`changeAllFilenameSuffix` goes through the whole tree and returns the files it could not rename.
`moveFile` takes the new path of the file, not a directory to move it into: a move onto an existing
directory is refused with an `IllegalArgumentException`, and the file stays where it is. `delete`
deletes a file, or a directory with everything in it.

**Searching.**

```java
// directory holds report.txt, report.log and archive/report-2025.txt

// report.txt and archive/report-2025.txt
List<File> texts = FileSearchExtensions.findFilesRecursive(directory, "*.txt");
// report.txt and report.log: findFiles stays in the directory
List<File> reports = FileSearchExtensions.findFiles(directory, "report.*");
// report.log
Set<File> logs = FileSearchExtensions.findFilesRecursive(directory,
    file -> file.getName().endsWith(".log"));
```

In a pattern `*` and `?` are wildcards, and the rest is a regular expression: its dot stands for any
character. The variants with a `Predicate` offer it every entry, directories included: `findFiles`
those of the directory, `findFilesRecursive` those of the whole tree, the directory itself among
them. `findFilesWithPrefixAndExtension` and its recursive variant take the prefix and the extension
as text.

**Comparing.**

```java
// a.txt and b.txt hold the same lines
IFileContentResultBean result = CompareFileExtensions.compareFileContentByBytes(a, b);
boolean sameContent = result.getContentEquality(); // true
boolean sameName = result.getNameEquality();       // false
boolean equal = CompareFileExtensions.compareFiles(a, b, true); // true
```

The result says for each property whether the two files agree: the absolute path, the name, the
extension, the length, the last-modified time and the content. `compareFileContentByLines` takes
`\n` and `\r\n` line ends as equal, `compareFileContentByBytes` does not; `compareFiles(a, b, true)`
compares the length, then the lines. Every comparison refuses a directory with an
`IllegalArgumentException`. `SimpleCompareFileExtensions` compares one property at a time, or the
checksums: CRC32, Adler32, or an `Algorithm` of crypt-api.

**Merging.**

```java
// en.txt holds yes and hello, de.txt yes and hallo
File words = new File(directory, "words.txt");
MergeFileExtensions.merge(List.of(en, de), words, MergeFileStrategy.SORTED_UNIQUE);
// words.txt: hallo, hello, yes

// 2025.csv holds id,name and 1,Ada; 2026.csv holds id,name and 2,Grace
File table = new File(directory, "all.csv");
MergeFileExtensions.merge(List.of(y2025, y2026), table, MergeFileStrategy.CSV_HEADER_MERGE);
// all.csv: id,name, 1,Ada and 2,Grace
```

The strategies are `APPEND`; `UNIQUE_LINES`, each line once, in the order it first appears;
`SORTED` and `SORTED_UNIQUE`; `CSV_HEADER_MERGE`, the header of the first file once;
`MARKDOWN_SECTIONS`, a `---` line with an empty line on each side between the files; and `BY_KEY`,
one line for each value of the first comma-separated column, the last one read, which `mergeByKey`
does for another column or delimiter. The files are read and written as UTF-8 unless a charset is
given. `MergeDirectoryExtensions.merge(target, directories...)` merges directory trees: for each
relative path it copies the file of the given directories that was modified last into `target`,
over the file that is there.

**Changing a file line by line.**

```java
// todo, a Path, holds one, two and three
ModifyFileExtensions.modifyFile(todo, new DeleteLinesByIndexInFile(List.of(1))); // one, three
ModifyFileExtensions.modifyFile(todo,
    (index, line) -> line.toUpperCase() + System.lineSeparator()); // ONE, THREE
```

The `FileChangeable` gets the index and the text of each line and returns what is written for it,
its line end included, or `null` to leave the line out. With a second `Path`, `modifyFile` writes
there and leaves the file it reads as it is. It reads and writes UTF-8 unless it gets a charset.

## Features

| Package `io.github.astrapi69.file...` | What it holds |
|---|---|
| (root) | `FileExtensions`: the content type of a file, its absolute path without the name |
| `create` | `FileFactory` and `DirectoryFactory` (files, directories and temporary directories; `newFile(File)` and `newDirectory(File)` answer with a `FileCreationState`), `DirectoryStructureFactory` and `DirectoryStructureExtensions`. The models `FileInfo`, `FileContentInfo` and `FileCreationState` are in `create.model` |
| `write` | `StoreFileExtensions` (strings, lines and bytes into a file), `LineAppender` (lines appended to a file). Every method of `WriteFileExtensions` is deprecated and names its replacement |
| `read` | `ReadFileExtensions`: a file as a string, as lines or as bytes, one line of it, the number of its lines, properties |
| `copy` | `CopyFileExtensions` (files, byte for byte or from one charset to another, backups), `CopyDirectoryExtensions` (directory trees, with filters for the files to copy and to leave out) |
| `rename` | `RenameFileExtensions`: rename and move files, change the suffix of one file or of every file in a tree, add the time of day to a name |
| `delete` | `DeleteFileExtensions`: files and directory trees, or the files of a directory by suffix, prefix or filter |
| `search` | `FileSearchExtensions` (files by pattern, suffix, prefix and extension or predicate, in a directory or its whole tree; the number of files; the first line that starts with a text), `PathFinder` (the source folders of a project in the Maven layout) |
| `compare` | `CompareFileExtensions` and `SimpleCompareFileExtensions`: two files by path, name, extension, length, last-modified time, content or checksum; the equal files in one or two trees |
| `merge` | `MergeFileExtensions` (text files, with a `MergeFileStrategy`), `MergeDirectoryExtensions` (directory trees: the file modified last wins, or a `MergeStrategy` decides) |
| `modify` | `ModifyFileExtensions` (a file line by line through a `FileChangeable`; files concatenated into one), `DeleteLinesByIndexInFile`, `FileTimeExtensions` (the time of creation, of the last modification and of the last access) |
| `sort` | `SortFileExtensions`: the lines of a file, by a `Comparator` |
| `system` | `SystemFileExtensions` (the user's home, downloads, configuration and temporary directories, the working directory, the root, the Java home), `SystemPropertiesExtensions` (the common system properties) |
| `exception` | the checked exceptions of the operations, such as `FileDoesNotExistException` and `FileIsNotADirectoryException` |
| `tools` | `MergeWithTemplate`, a command line that copies or merges chosen directories of a template into a project. The module does not export this package |

The `api` packages hold interfaces with default methods for most of these operations, for a class
that wants them as its own: `Copyable`, `Createable`, `Erasable`, `Readable`, `Renameable`,
`Searchable` and `Storable`; `compare.api` holds the result types of the comparisons, `modify.api`
the `FileChangeable`. The [Javadoc](https://www.javadoc.io/doc/io.github.astrapi69/file-worker) has
the details.

## Quality

As of 20.1, measured on the tree of `RELEASE-20.1` with JDK 25, by `./gradlew test --rerun`,
`./gradlew build` and `./gradlew pitest --rerun-tasks`:

| | |
|---|---|
| tests | 1407 in 61 test classes, none failing; 36 are skipped: 34 disabled with `@Disabled`, 2 that run on Windows only |
| line and branch coverage (JaCoCo) | 2143 of 2159 lines (99.3%), 938 of 973 branches (96.4%); the build fails below 98% of the lines or 93% of the branches |
| mutation score (PIT 1.30.1) | 1119 of 1185 mutants detected (94%); the build fails below 92%, and `build.gradle` says what decides the 66 left |

The CI build runs the tests, the coverage check and the mutation testing on every push to
`develop` and `master` and on every pull request into them. It uploads the coverage to codecov.io
and publishes the mutation score; the two badges above show `develop`.

## Verifying the release signatures

The releases on Maven Central are signed with the OpenPGP key

```text
5B2F A6B1 1E29 8BC0 9287  F423 D8C4 0351 8C49 CA75
```

Since 19.1 they are signed by its signing subkey

```text
6D67 F844 2A6F CC96 BD7C  1B5E 9FCF 7C97 10E2 BD8D
```

valid until 2028-10-09; up to 19.0 they were signed by the primary key itself. The key, with the
subkey, is on keys.openpgp.org. Fetch it once, then verify the signature next to each file:

```sh
gpg --keyserver hkps://keys.openpgp.org --recv-keys 5B2FA6B11E298BC09287F423D8C403518C49CA75
gpg --verify file-worker-20.1.jar.asc file-worker-20.1.jar
```

gpg answers `Good signature` and prints the `Primary key fingerprint`, and for a release signed by
the subkey the `Subkey fingerprint`; both have to be the ones above. The warning that the key "is not
certified with a trusted signature" only says that you have not marked it as trusted in your own
keyring.

## Releasing

What a release consists of, in the order it is run:

1. **One pull request** with the CHANGELOG entry and `projectVersion` without `-SNAPSHOT`
   (`docs: changelog for X.Y, and the version that carries it`), the full build green on it.
2. **Tag the merge commit** with `./gradlew tagRelease`, which names it `RELEASE-X.Y`, and push the
   tag. `publish.yml` uploads to the Central Portal, where the maintainer releases it. A pushed tag is
   never moved or deleted; a failed run is fixed on the branch and re-run.
3. **Verify on Central**: the jar, pom, `.module`, `-sources.jar` and `-javadoc.jar`, their sha1, and
   `gpg --verify` on the `.asc`.
4. **GitHub release** from the CHANGELOG entry.
5. **`master` follows the release** by a merge of the tag: on `master`,
   `git merge --no-ff "RELEASE-X.Y^{commit}" -m "Merge tag RELEASE-X.Y into master"`. Before
   `master` is pushed, `git diff RELEASE-X.Y master` has to be empty; if it is not, nothing is
   pushed. Never a force push: `master` is always the latest release, nothing else.
6. **Open the next cycle**: `projectVersion` to the next `-SNAPSHOT`.

## Contributing

Pull requests are welcome: fork [astrapi69/file-worker](https://github.com/astrapi69/file-worker/fork)
and [open a pull request](https://github.com/astrapi69/file-worker/pull/new/develop) against
`develop`.

Please add tests for your change; the coverage floor above holds for it too. The Gradle wrapper
runs the usual tasks:

```shell
./gradlew build          # tests, coverage check, formatting check
./gradlew spotlessApply  # format, and write the license headers
```

The versions follow the
[Semantic Versioning](https://github.com/lightblueseas/mvn-parent-projects/wiki/Semantic-Versioning)
guidelines, `<major>.<minor>.<patch>`. Questions, bug reports and feature requests belong on the
[issues page](https://github.com/astrapi69/file-worker/issues).

## How this project is made

file-worker was written by its maintainer over many years; since 2026 he uses AI agents as a tool
to maintain it. What the history shows of that, and what the maintainer decides:
[AI_USAGE.md](AI_USAGE.md).

## License

MIT - see [LICENSE](LICENSE).

## Support this project

If file-worker is useful to you, a star on [GitHub](https://github.com/astrapi69/file-worker) helps
others find it. Sharing it or reporting a bug helps just as much.

If you would like to contribute financially, the channels are on the repository's Sponsor button,
which reads [.github/FUNDING.yml](.github/FUNDING.yml): GitHub Sponsors, Liberapay, Ko-fi and PayPal.

## Credits

| | |
|---|---|
| [Maven Central](https://central.sonatype.com/artifact/io.github.astrapi69/file-worker) | for hosting the artifacts of this open source project |
| [codecov.io](https://codecov.io) | for free code coverage reporting |
| [javadoc.io](https://www.javadoc.io) | for free javadoc hosting |

No animals were harmed in the making of this library.
