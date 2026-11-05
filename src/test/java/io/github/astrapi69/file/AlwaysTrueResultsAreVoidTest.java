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
package io.github.astrapi69.file;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Collection;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapi69.file.copy.CopyDirectoryExtensions;
import io.github.astrapi69.file.copy.CopyFileExtensions;
import io.github.astrapi69.file.copy.api.Copyable;
import io.github.astrapi69.file.create.DirectoryFactory;
import io.github.astrapi69.file.create.api.Createable;
import io.github.astrapi69.file.rename.RenameFileExtensions;
import io.github.astrapi69.file.rename.api.Renameable;
import io.github.astrapi69.file.write.StoreFileExtensions;
import io.github.astrapi69.file.write.api.Storable;

/**
 * The methods whose result could only be {@code true} return nothing: each one either does what it
 * names or throws (#124). The methods that can answer {@code false}, such as
 * {@link RenameFileExtensions#renameFile(File, File, boolean)} without delete or
 * {@link io.github.astrapi69.file.delete.DeleteFileExtensions#delete(Path)} of a path that does not
 * exist, keep their {@code boolean}.
 */
public class AlwaysTrueResultsAreVoidTest
{

	static Stream<Arguments> methodsThatOnlyEverReturnedTrue()
	{
		return Stream.of(
			Arguments.of(CopyFileExtensions.class, "copyFile",
				new Class<?>[] { Path.class, Path.class }),
			Arguments.of(CopyFileExtensions.class, "copyFile",
				new Class<?>[] { File.class, File.class }),
			Arguments.of(CopyFileExtensions.class, "copyFile",
				new Class<?>[] { File.class, File.class, boolean.class }),
			Arguments.of(CopyFileExtensions.class, "copyFile",
				new Class<?>[] { File.class, File.class, Charset.class, Charset.class,
						boolean.class }),
			Arguments.of(CopyFileExtensions.class, "copyFileToDirectory",
				new Class<?>[] { File.class, File.class }),
			Arguments.of(CopyFileExtensions.class, "copyFileToDirectory",
				new Class<?>[] { File.class, File.class, boolean.class }),
			Arguments.of(CopyDirectoryExtensions.class, "copyDirectory",
				new Class<?>[] { Path.class, Path.class }),
			Arguments.of(Copyable.class, "copyDirectory",
				new Class<?>[] { Path.class, Path.class }),
			Arguments.of(Copyable.class, "copyFile", new Class<?>[] { File.class, File.class }),
			Arguments.of(Copyable.class, "copyFile", new Class<?>[] { Path.class, Path.class }),
			Arguments.of(StoreFileExtensions.class, "toFile",
				new Class<?>[] { File.class, String.class }),
			Arguments.of(StoreFileExtensions.class, "toFile",
				new Class<?>[] { File.class, Collection.class }),
			Arguments.of(StoreFileExtensions.class, "toFile",
				new Class<?>[] { File.class, Collection.class, String.class }),
			Arguments.of(StoreFileExtensions.class, "toFile",
				new Class<?>[] { File.class, Collection.class, Charset.class }),
			Arguments.of(StoreFileExtensions.class, "toFile",
				new Class<?>[] { File.class, String.class, String.class }),
			Arguments.of(StoreFileExtensions.class, "toFile",
				new Class<?>[] { File.class, String.class, Charset.class }),
			Arguments.of(Storable.class, "toFile", new Class<?>[] { File.class, String.class }),
			Arguments.of(Storable.class, "toFile", new Class<?>[] { File.class, Collection.class }),
			Arguments.of(Storable.class, "toFile",
				new Class<?>[] { File.class, Collection.class, Charset.class }),
			Arguments.of(Storable.class, "toFile",
				new Class<?>[] { File.class, String.class, Charset.class }),
			Arguments.of(RenameFileExtensions.class, "forceToMoveFile",
				new Class<?>[] { File.class, File.class }),
			Arguments.of(RenameFileExtensions.class, "moveFile",
				new Class<?>[] { File.class, File.class }),
			Arguments.of(Renameable.class, "forceToMoveFile",
				new Class<?>[] { File.class, File.class }),
			Arguments.of(Renameable.class, "moveFile", new Class<?>[] { File.class, File.class }),
			Arguments.of(DirectoryFactory.class, "newDirectory",
				new Class<?>[] { Path.class, FileAttribute[].class }),
			Arguments.of(DirectoryFactory.class, "newDirectoryQuietly",
				new Class<?>[] { Path.class, FileAttribute[].class }),
			Arguments.of(Createable.class, "newDirectory",
				new Class<?>[] { Path.class, FileAttribute[].class }));
	}

	@ParameterizedTest(name = "{0}.{1}")
	@MethodSource("methodsThatOnlyEverReturnedTrue")
	public void returnsNothing(final Class<?> type, final String name, final Class<?>[] parameters)
		throws NoSuchMethodException
	{
		final Method method = type.getMethod(name, parameters);
		assertEquals(void.class, method.getReturnType(), method.toGenericString());
	}

	static Stream<Arguments> methodsThatCanAnswerFalse()
	{
		return Stream.of(
			Arguments.of(RenameFileExtensions.class, "renameFile",
				new Class<?>[] { File.class, File.class, boolean.class }),
			Arguments.of(RenameFileExtensions.class, "renameFile",
				new Class<?>[] { File.class, File.class }),
			Arguments.of(RenameFileExtensions.class, "changeFilenameSuffix",
				new Class<?>[] { File.class, String.class, boolean.class }),
			Arguments.of(CopyDirectoryExtensions.class, "copyDirectory",
				new Class<?>[] { File.class, File.class }),
			Arguments.of(io.github.astrapi69.file.delete.DeleteFileExtensions.class, "delete",
				new Class<?>[] { Path.class }),
			Arguments.of(io.github.astrapi69.file.delete.DeleteFileExtensions.class, "delete",
				new Class<?>[] { File.class }),
			Arguments.of(DirectoryFactory.class, "newDirectories",
				new Class<?>[] { Path.class, FileAttribute[].class }),
			Arguments.of(Createable.class, "newDirectories",
				new Class<?>[] { Path.class, FileAttribute[].class }));
	}

	@ParameterizedTest(name = "{0}.{1}")
	@MethodSource("methodsThatCanAnswerFalse")
	public void keepsItsBoolean(final Class<?> type, final String name, final Class<?>[] parameters)
		throws NoSuchMethodException
	{
		final Method method = type.getMethod(name, parameters);
		assertEquals(boolean.class, method.getReturnType(), method.toGenericString());
	}
}
