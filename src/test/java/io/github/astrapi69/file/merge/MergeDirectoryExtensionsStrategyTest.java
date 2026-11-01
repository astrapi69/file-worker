/**
 * The MIT License
 *
 * Copyright (C) 2015 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.astrapi69.file.merge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapi69.file.merge.strategy.MergeStrategy;

/**
 * Tests {@link MergeDirectoryExtensions} on directories built in a temporary directory: the merge
 * with a {@link MergeStrategy} for each case of a file in source and target, and the merge without
 * a strategy, where the newest file wins.
 */
public class MergeDirectoryExtensionsStrategyTest
{

	private static final long OLDER = 1_577_836_800_000L;

	private static final long NEWER = 1_609_459_200_000L;

	@TempDir
	Path tempDir;

	private Path source;

	private Path target;

	/**
	 * Gives a source to merge that is no directory, created in the given directory if it exists
	 */
	@FunctionalInterface
	interface SourceThatIsNoDirectory
	{
		File createIn(Path directory) throws IOException;
	}

	static Stream<Arguments> sourcesThatAreNoDirectory()
	{
		return Stream
			.of(Arguments.of("null", (SourceThatIsNoDirectory)directory -> null),
				Arguments
					.of("a missing directory",
						(SourceThatIsNoDirectory)directory -> directory.resolve("missing")
							.toFile()),
				Arguments.of("a regular file",
					(SourceThatIsNoDirectory)directory -> write(directory.resolve("regular.txt"),
						"regular", OLDER).toFile()));
	}

	/**
	 * One of the merges of {@link MergeDirectoryExtensions}: without a strategy or with one
	 */
	@FunctionalInterface
	interface MergeCall
	{
		void merge(File targetDirectory, File... directoriesToMerge) throws IOException;
	}

	/**
	 * A merge call with its name and whether it deletes a merged file from the source
	 */
	record MergeVariant(String name, MergeCall call, boolean deletesFromTheSource) {
		@Override
		public String toString()
		{
			return name;
		}
	}

	static Stream<MergeVariant> mergeVariants()
	{
		return Stream
			.of(new MergeVariant("without a strategy", MergeDirectoryExtensions::merge, false),
				new MergeVariant("SOURCE_TO_TARGET",
					(targetDirectory, directoriesToMerge) -> MergeDirectoryExtensions.mergeAndGet(
						targetDirectory, MergeStrategy.SOURCE_TO_TARGET, directoriesToMerge),
					false),
				new MergeVariant("TARGET_AS_MASTER",
					(targetDirectory, directoriesToMerge) -> MergeDirectoryExtensions.mergeAndGet(
						targetDirectory, MergeStrategy.TARGET_AS_MASTER, directoriesToMerge),
					true));
	}

	static Stream<Arguments> directoryOrders()
	{
		return Stream.of(Arguments.of("the newer directory first", List.of("newer", "older")),
			Arguments.of("the older directory first", List.of("older", "newer")));
	}

	private static Path write(Path file, String content, long lastModified) throws IOException
	{
		Files.createDirectories(file.getParent());
		Files.writeString(file, content);
		assertTrue(file.toFile().setLastModified(lastModified), "the time is set on " + file);
		return file;
	}

	private static Set<String> relativeFilesOf(Path directory) throws IOException
	{
		try (Stream<Path> paths = Files.walk(directory))
		{
			return paths.filter(Files::isRegularFile)
				.map(path -> directory.relativize(path).toString().replace(File.separatorChar, '/'))
				.collect(Collectors.toSet());
		}
	}

	@BeforeEach
	void createDirectories() throws IOException
	{
		source = Files.createDirectory(tempDir.resolve("source"));
		target = Files.createDirectory(tempDir.resolve("target"));
	}

	@Test
	void mergeWithStrategy_createsTheTargetDirectory_whenItDoesNotExist() throws IOException
	{
		write(source.resolve("a.txt"), "a", OLDER);
		File missingTarget = tempDir.resolve("not").resolve("yet").toFile();

		File merged = MergeDirectoryExtensions.mergeAndGet(missingTarget,
			MergeStrategy.SOURCE_TO_TARGET, source.toFile());

		assertSame(missingTarget, merged);
		assertEquals("a", Files.readString(missingTarget.toPath().resolve("a.txt")));
	}

	@Test
	void mergeWithStrategy_skipsSourcesThatAreNullMissingOrNoDirectory() throws IOException
	{
		write(source.resolve("a.txt"), "a", OLDER);
		Path regularFile = write(tempDir.resolve("regular.txt"), "regular", OLDER);
		File missing = tempDir.resolve("missing").toFile();

		MergeDirectoryExtensions.mergeAndGet(target.toFile(), MergeStrategy.TARGET_AS_MASTER, null,
			missing, regularFile.toFile(), source.toFile());

		assertEquals(Set.of("a.txt"), relativeFilesOf(target));
		assertEquals("a", Files.readString(target.resolve("a.txt")));
		assertEquals("regular", Files.readString(regularFile));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("sourcesThatAreNoDirectory")
	void mergeWithoutStrategy_skipsASourceThatIsNoDirectory(String displayName,
		SourceThatIsNoDirectory sourceThatIsNoDirectory) throws IOException
	{
		write(source.resolve("a.txt"), "a", OLDER);
		File noDirectory = sourceThatIsNoDirectory.createIn(tempDir);

		MergeDirectoryExtensions.merge(target.toFile(), noDirectory, source.toFile());

		assertEquals(Set.of("a.txt"), relativeFilesOf(target));
		assertEquals("a", Files.readString(target.resolve("a.txt")));
	}

	@Test
	void mergeWithTargetAsMaster_namesEverySourceFileItCannotDelete_afterMergingTheRest()
		throws IOException
	{
		Path locked = source.resolve("locked");
		Path first = write(locked.resolve("x.txt"), "x", OLDER);
		Path second = write(locked.resolve("y.txt"), "y", OLDER);
		Path other = Files.createDirectory(tempDir.resolve("other"));
		Path free = write(other.resolve("free.txt"), "free", OLDER);
		IOException exception;
		try
		{
			assumeTrue(locked.toFile().setWritable(false), "the file system takes the permission");
			assumeFalse(Files.isWritable(locked), "the test runs without override rights");

			exception = assertThrows(IOException.class,
				() -> MergeDirectoryExtensions.mergeAndGet(target.toFile(),
					MergeStrategy.TARGET_AS_MASTER, source.toFile(), other.toFile()));
		}
		finally
		{
			locked.toFile().setWritable(true);
		}

		assertTrue(exception.getMessage().contains(first.toString()), exception.getMessage());
		assertTrue(exception.getMessage().contains(second.toString()), exception.getMessage());
		assertEquals(2, exception.getSuppressed().length, "one suppressed exception per file");
		for (Throwable single : exception.getSuppressed())
		{
			assertInstanceOf(IOException.class, single);
		}
		assertEquals("x", Files.readString(first), "the file is left in the source");
		assertEquals("y", Files.readString(second), "the file is left in the source");
		assertEquals(Set.of("locked/x.txt", "locked/y.txt", "free.txt"), relativeFilesOf(target));
		assertFalse(Files.exists(free), "the next source is merged and emptied after a failure");
	}

	@Test
	void mergeWithTargetAsMaster_namesAnEmptiedSourceDirectoryItCannotDelete() throws IOException
	{
		Path outer = source.resolve("outer");
		Path inner = outer.resolve("inner");
		Path moved = write(inner.resolve("z.txt"), "z", OLDER);
		IOException exception;
		try
		{
			assumeTrue(outer.toFile().setWritable(false), "the file system takes the permission");
			assumeFalse(Files.isWritable(outer), "the test runs without override rights");

			exception = assertThrows(IOException.class, () -> MergeDirectoryExtensions
				.mergeAndGet(target.toFile(), MergeStrategy.TARGET_AS_MASTER, source.toFile()));
		}
		finally
		{
			outer.toFile().setWritable(true);
		}

		assertTrue(exception.getMessage().contains(inner.toString()), exception.getMessage());
		assertFalse(exception.getMessage().contains(moved.toString()), exception.getMessage());
		assertEquals(1, exception.getSuppressed().length, "one suppressed exception per file");
		assertEquals("z",
			Files.readString(target.resolve("outer").resolve("inner").resolve("z.txt")));
		assertFalse(Files.exists(moved), "the merged file is deleted from the source");
		assertTrue(Files.isDirectory(inner), "the emptied directory is left in the source");
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("mergeVariants")
	void merge_namesASubdirectoryItCannotRead_afterMergingTheRest(MergeVariant variant)
		throws IOException
	{
		Path readable = write(source.resolve("readable.txt"), "readable", OLDER);
		Path locked = source.resolve("locked");
		Path hidden = write(locked.resolve("hidden.txt"), "hidden", OLDER);
		Path other = Files.createDirectory(tempDir.resolve("other"));
		write(other.resolve("free.txt"), "free", OLDER);
		IOException exception;
		try
		{
			assumeTrue(locked.toFile().setReadable(false), "the file system takes the permission");
			assumeFalse(Files.isReadable(locked), "the test runs without override rights");

			exception = assertThrows(IOException.class,
				() -> variant.call().merge(target.toFile(), source.toFile(), other.toFile()));
		}
		finally
		{
			locked.toFile().setReadable(true);
		}

		assertTrue(exception.getMessage().contains(locked.toString()), exception.getMessage());
		assertEquals(1, exception.getSuppressed().length, "one suppressed exception per directory");
		assertInstanceOf(IOException.class, exception.getSuppressed()[0]);
		assertEquals(Set.of("readable.txt", "free.txt"), relativeFilesOf(target));
		assertEquals("readable", Files.readString(target.resolve("readable.txt")));
		assertEquals("hidden", Files.readString(hidden),
			"the unread directory stays in the source");
		assertEquals(variant.deletesFromTheSource(), Files.notExists(readable),
			"the merged file is deleted from the source only under TARGET_AS_MASTER");
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("mergeVariants")
	void merge_namesASourceDirectoryItCannotRead_afterMergingTheOthers(MergeVariant variant)
		throws IOException
	{
		Path hidden = write(source.resolve("hidden.txt"), "hidden", OLDER);
		Path other = Files.createDirectory(tempDir.resolve("other"));
		write(other.resolve("free.txt"), "free", OLDER);
		IOException exception;
		try
		{
			assumeTrue(source.toFile().setReadable(false), "the file system takes the permission");
			assumeFalse(Files.isReadable(source), "the test runs without override rights");

			exception = assertThrows(IOException.class,
				() -> variant.call().merge(target.toFile(), source.toFile(), other.toFile()));
		}
		finally
		{
			source.toFile().setReadable(true);
		}

		assertTrue(exception.getMessage().contains(source.toString()), exception.getMessage());
		assertEquals(1, exception.getSuppressed().length, "one suppressed exception per directory");
		assertEquals(Set.of("free.txt"), relativeFilesOf(target));
		assertEquals("hidden", Files.readString(hidden), "the unread directory stays as it is");
	}

	@Test
	void mergeWithTargetAsMaster_namesTheDirectoriesItCannotRead_nextToTheFilesItCannotDelete()
		throws IOException
	{
		Path unread = source.resolve("unread");
		write(unread.resolve("hidden.txt"), "hidden", OLDER);
		Path stuck = source.resolve("stuck");
		Path left = write(stuck.resolve("left.txt"), "left", OLDER);
		IOException exception;
		try
		{
			assumeTrue(unread.toFile().setReadable(false), "the file system takes the permission");
			assumeTrue(stuck.toFile().setWritable(false), "the file system takes the permission");
			assumeFalse(Files.isReadable(unread), "the test runs without override rights");
			assumeFalse(Files.isWritable(stuck), "the test runs without override rights");

			exception = assertThrows(IOException.class, () -> MergeDirectoryExtensions
				.mergeAndGet(target.toFile(), MergeStrategy.TARGET_AS_MASTER, source.toFile()));
		}
		finally
		{
			unread.toFile().setReadable(true);
			stuck.toFile().setWritable(true);
		}

		assertTrue(exception.getMessage().contains(unread.toString()), exception.getMessage());
		assertTrue(exception.getMessage().contains(left.toString()), exception.getMessage());
		assertEquals(2, exception.getSuppressed().length, "one suppressed exception per failure");
		assertEquals(Set.of("stuck/left.txt"), relativeFilesOf(target));
	}

	@Test
	void mergeWithTargetAsMaster_attachesTheFailuresCollectedSoFar_toTheCopyFailureThatStopsIt()
		throws IOException
	{
		Path stuck = source.resolve("stuck");
		Path left = write(stuck.resolve("left.txt"), "left", OLDER);
		Path unread = source.resolve("unread");
		write(unread.resolve("hidden.txt"), "hidden", OLDER);
		Path other = Files.createDirectory(tempDir.resolve("other"));
		write(other.resolve("closed").resolve("new.txt"), "new", OLDER);
		Path closed = Files.createDirectory(target.resolve("closed"));
		IOException exception;
		try
		{
			assumeTrue(stuck.toFile().setWritable(false), "the file system takes the permission");
			assumeTrue(unread.toFile().setReadable(false), "the file system takes the permission");
			assumeTrue(closed.toFile().setWritable(false), "the file system takes the permission");
			assumeFalse(Files.isWritable(stuck), "the test runs without override rights");
			assumeFalse(Files.isReadable(unread), "the test runs without override rights");

			exception = assertThrows(IOException.class,
				() -> MergeDirectoryExtensions.mergeAndGet(target.toFile(),
					MergeStrategy.TARGET_AS_MASTER, source.toFile(), other.toFile()));
		}
		finally
		{
			stuck.toFile().setWritable(true);
			unread.toFile().setReadable(true);
			closed.toFile().setWritable(true);
		}

		assertTrue(exception.getMessage().contains(closed.resolve("new.txt").toString()),
			"the copy failure is the exception: " + exception.getMessage());
		Set<String> suppressed = Stream.of(exception.getSuppressed()).map(Throwable::getMessage)
			.collect(Collectors.toSet());
		assertEquals(Set.of(left.toString(), unread.toString()), suppressed,
			"the failures collected before the copy failed");
	}

	@ParameterizedTest
	@CsvSource({ "SOURCE_TO_TARGET, true", "TARGET_AS_MASTER, false" })
	void mergeWithStrategy_copiesTheFilesTheTargetLacks_inEveryLevel(MergeStrategy strategy,
		boolean sourceIsKept) throws IOException
	{
		write(source.resolve("top.txt"), "top", OLDER);
		write(source.resolve("fresh").resolve("deeper").resolve("new.txt"), "new", OLDER);

		MergeDirectoryExtensions.mergeAndGet(target.toFile(), strategy, source.toFile());

		assertEquals("top", Files.readString(target.resolve("top.txt")));
		assertEquals("new",
			Files.readString(target.resolve("fresh").resolve("deeper").resolve("new.txt")));
		assertEquals(sourceIsKept, Files.exists(source.resolve("top.txt")));
		assertEquals(sourceIsKept, Files.exists(source.resolve("fresh")),
			"the emptied source directory is kept only when the source is kept");
	}

	@ParameterizedTest
	@CsvSource({ "SOURCE_TO_TARGET, true", "TARGET_AS_MASTER, false" })
	void mergeWithStrategy_leavesATargetFileWithEqualContentUntouched(MergeStrategy strategy,
		boolean sourceIsKept) throws IOException
	{
		Path sourceFile = write(source.resolve("shared").resolve("same.txt"), "same", NEWER);
		Path targetFile = write(target.resolve("shared").resolve("same.txt"), "same", OLDER);

		MergeDirectoryExtensions.mergeAndGet(target.toFile(), strategy, source.toFile());

		assertEquals("same", Files.readString(targetFile));
		assertEquals(OLDER, targetFile.toFile().lastModified(), "the target file is not copied");
		assertEquals(sourceIsKept, Files.exists(sourceFile));
	}

	@ParameterizedTest
	@CsvSource({ "SOURCE_TO_TARGET, true", "TARGET_AS_MASTER, false" })
	void mergeWithStrategy_overwritesATargetFileWithOtherContent(MergeStrategy strategy,
		boolean sourceIsKept) throws IOException
	{
		Path sourceFile = write(source.resolve("shared").resolve("config.txt"), "new", OLDER);
		Path targetFile = write(target.resolve("shared").resolve("config.txt"), "old", NEWER);

		MergeDirectoryExtensions.mergeAndGet(target.toFile(), strategy, source.toFile());

		assertEquals("new", Files.readString(targetFile));
		assertEquals(sourceIsKept, Files.exists(sourceFile));
	}

	@ParameterizedTest
	@CsvSource({ "SOURCE_TO_TARGET", "TARGET_AS_MASTER" })
	void mergeWithStrategy_keepsTheFilesOnlyTheTargetHas(MergeStrategy strategy) throws IOException
	{
		write(source.resolve("shared").resolve("from-source.txt"), "source", OLDER);
		Path onlyInTarget = write(target.resolve("shared").resolve("only.txt"), "only", OLDER);

		MergeDirectoryExtensions.mergeAndGet(target.toFile(), strategy, source.toFile());

		assertEquals("only", Files.readString(onlyInTarget));
		assertEquals(Set.of("shared/from-source.txt", "shared/only.txt"), relativeFilesOf(target));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("directoryOrders")
	void mergeWithoutStrategy_takesTheNewestFile_whateverTheOrderOfTheDirectories(
		String displayName, List<String> order) throws IOException
	{
		write(tempDir.resolve("older").resolve("config.txt"), "old", OLDER);
		write(tempDir.resolve("newer").resolve("config.txt"), "new", NEWER);
		write(tempDir.resolve("older").resolve("sub").resolve("data.txt"), "old data", NEWER);
		write(tempDir.resolve("newer").resolve("sub").resolve("data.txt"), "new data", OLDER);
		File[] directories = order.stream().map(tempDir::resolve).map(Path::toFile)
			.toArray(File[]::new);
		File targetDirectory = target.toFile();

		File merged = MergeDirectoryExtensions.mergeAndGet(targetDirectory, directories);

		assertSame(targetDirectory, merged);
		assertEquals("new", Files.readString(target.resolve("config.txt")));
		assertEquals("old data", Files.readString(target.resolve("sub").resolve("data.txt")),
			"the newest file wins, not the newest directory");
	}
}
