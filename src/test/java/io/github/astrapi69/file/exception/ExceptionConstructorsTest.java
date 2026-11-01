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
package io.github.astrapi69.file.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The unit test class for the constructors of every exception class in the package
 * {@code io.github.astrapi69.file.exception}
 */
public class ExceptionConstructorsTest
{

	private static final String MESSAGE = "the reason";

	/**
	 * The four constructors every exception class of the package has
	 */
	private record Constructors(Class<? extends Exception> type,
		Supplier<Exception> withoutArguments, Function<String, Exception> withMessage,
		BiFunction<String, Throwable, Exception> withMessageAndCause,
		Function<Throwable, Exception> withCause) {
	}

	static Stream<Arguments> everyConstructorWithItsMessageAndCause()
	{
		return Stream.of(
			new Constructors(DirectoryAlreadyExistsException.class,
				DirectoryAlreadyExistsException::new, DirectoryAlreadyExistsException::new,
				DirectoryAlreadyExistsException::new, DirectoryAlreadyExistsException::new),
			new Constructors(DirectoryCannotBeCreatedException.class,
				DirectoryCannotBeCreatedException::new, DirectoryCannotBeCreatedException::new,
				DirectoryCannotBeCreatedException::new, DirectoryCannotBeCreatedException::new),
			new Constructors(DirectoryCannotBeWrittenException.class,
				DirectoryCannotBeWrittenException::new, DirectoryCannotBeWrittenException::new,
				DirectoryCannotBeWrittenException::new, DirectoryCannotBeWrittenException::new),
			new Constructors(DirectoryHasNoContentException.class,
				DirectoryHasNoContentException::new, DirectoryHasNoContentException::new,
				DirectoryHasNoContentException::new, DirectoryHasNoContentException::new),
			new Constructors(FileDoesNotExistException.class, FileDoesNotExistException::new,
				FileDoesNotExistException::new, FileDoesNotExistException::new,
				FileDoesNotExistException::new),
			new Constructors(FileIsADirectoryException.class, FileIsADirectoryException::new,
				FileIsADirectoryException::new, FileIsADirectoryException::new,
				FileIsADirectoryException::new),
			new Constructors(FileIsNotADirectoryException.class, FileIsNotADirectoryException::new,
				FileIsNotADirectoryException::new, FileIsNotADirectoryException::new,
				FileIsNotADirectoryException::new),
			new Constructors(FileIsSecurityRestrictedException.class,
				FileIsSecurityRestrictedException::new, FileIsSecurityRestrictedException::new,
				FileIsSecurityRestrictedException::new, FileIsSecurityRestrictedException::new),
			new Constructors(FileNotRenamedException.class, FileNotRenamedException::new,
				FileNotRenamedException::new, FileNotRenamedException::new,
				FileNotRenamedException::new))
			.flatMap(ExceptionConstructorsTest::casesOf);
	}

	private static Stream<Arguments> casesOf(Constructors constructors)
	{
		String name = constructors.type().getSimpleName();
		IOException cause = new IOException("the cause");
		Supplier<Exception> withMessage = () -> constructors.withMessage().apply(MESSAGE);
		Supplier<Exception> withMessageAndCause = () -> constructors.withMessageAndCause()
			.apply(MESSAGE, cause);
		Supplier<Exception> withCause = () -> constructors.withCause().apply(cause);
		return Stream.of(
			argumentSet(name + "()", constructors.type(), constructors.withoutArguments(), null,
				null),
			argumentSet(name + "(String)", constructors.type(), withMessage, MESSAGE, null),
			argumentSet(name + "(String, Throwable)", constructors.type(), withMessageAndCause,
				MESSAGE, cause),
			argumentSet(name + "(Throwable)", constructors.type(), withCause, cause.toString(),
				cause));
	}

	/**
	 * Test method for the constructors of every exception class in the package
	 *
	 * @param type
	 *            the exception class
	 * @param construction
	 *            the call of the constructor under test
	 * @param expectedMessage
	 *            the message the exception must carry
	 * @param expectedCause
	 *            the cause the exception must carry
	 */
	@ParameterizedTest
	@MethodSource("everyConstructorWithItsMessageAndCause")
	public void constructor_keepsTheGivenMessageAndCause(Class<? extends Exception> type,
		Supplier<Exception> construction, String expectedMessage, Throwable expectedCause)
	{
		Exception exception = construction.get();

		assertEquals(type, exception.getClass());
		assertEquals(expectedMessage, exception.getMessage());
		assertSame(expectedCause, exception.getCause());
	}

}
