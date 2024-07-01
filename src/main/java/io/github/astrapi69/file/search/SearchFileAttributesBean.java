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
package io.github.astrapi69.file.search;

/**
 * The class {@link SearchFileAttributesBean} encapsulates flags to indicate whether specific file
 * attributes should be ignored during file comparison operations.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class SearchFileAttributesBean
{

	/** Flag indicating whether content equality should be ignored. */
	private final boolean ignoreContentEquality;

	/** Flag indicating whether extension equality should be ignored. */
	private final boolean ignoreExtensionEquality;

	/** Flag indicating whether last modified attribute should be ignored. */
	private final boolean ignoreLastModified;

	/** Flag indicating whether length equality should be ignored. */
	private final boolean ignoreLengthEquality;

	/** Flag indicating whether name equality should be ignored. */
	private final boolean ignoreNameEquality;

	/**
	 * Constructs a new {@code SearchFileAttributesBean} with specified flags.
	 *
	 * @param ignoreContentEquality
	 *            flag indicating whether content equality should be ignored
	 * @param ignoreExtensionEquality
	 *            flag indicating whether extension equality should be ignored
	 * @param ignoreLastModified
	 *            flag indicating whether last modified attribute should be ignored
	 * @param ignoreLengthEquality
	 *            flag indicating whether length equality should be ignored
	 * @param ignoreNameEquality
	 *            flag indicating whether name equality should be ignored
	 */
	public SearchFileAttributesBean(boolean ignoreContentEquality, boolean ignoreExtensionEquality,
		boolean ignoreLastModified, boolean ignoreLengthEquality, boolean ignoreNameEquality)
	{
		this.ignoreContentEquality = ignoreContentEquality;
		this.ignoreExtensionEquality = ignoreExtensionEquality;
		this.ignoreLastModified = ignoreLastModified;
		this.ignoreLengthEquality = ignoreLengthEquality;
		this.ignoreNameEquality = ignoreNameEquality;
	}

	/**
	 * Returns a builder for constructing {@code SearchFileAttributesBean} objects.
	 *
	 * @return a new instance of {@code SearchFileAttributesBeanBuilder}
	 */
	public static SearchFileAttributesBeanBuilder builder()
	{
		return new SearchFileAttributesBeanBuilder();
	}

	/**
	 * Internal method for checking equality with another object.
	 *
	 * @param other
	 *            the object to compare
	 * @return true if the objects can be considered equal, false otherwise
	 */
	protected boolean canEqual(final Object other)
	{
		return other instanceof SearchFileAttributesBean;
	}

	/**
	 * Indicates whether some other object is "equal to" this one.
	 *
	 * @param o
	 *            the object to compare
	 * @return true if this object is the same as the o argument; false otherwise
	 */
	@Override
	public boolean equals(final Object o)
	{
		if (o == this)
			return true;
		if (!(o instanceof SearchFileAttributesBean))
			return false;
		final SearchFileAttributesBean other = (SearchFileAttributesBean)o;
		if (!other.canEqual(this))
			return false;
		if (this.isIgnoreContentEquality() != other.isIgnoreContentEquality())
			return false;
		if (this.isIgnoreExtensionEquality() != other.isIgnoreExtensionEquality())
			return false;
		if (this.isIgnoreLastModified() != other.isIgnoreLastModified())
			return false;
		if (this.isIgnoreLengthEquality() != other.isIgnoreLengthEquality())
			return false;
		return this.isIgnoreNameEquality() == other.isIgnoreNameEquality();
	}

	/**
	 * Returns a hash code value for the object.
	 *
	 * @return a hash code value for this object
	 */
	@Override
	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		result = result * PRIME + (this.isIgnoreContentEquality() ? 79 : 97);
		result = result * PRIME + (this.isIgnoreExtensionEquality() ? 79 : 97);
		result = result * PRIME + (this.isIgnoreLastModified() ? 79 : 97);
		result = result * PRIME + (this.isIgnoreLengthEquality() ? 79 : 97);
		result = result * PRIME + (this.isIgnoreNameEquality() ? 79 : 97);
		return result;
	}

	/**
	 * Retrieves the flag indicating whether content equality should be ignored.
	 *
	 * @return true if content equality should be ignored, false otherwise
	 */
	public boolean isIgnoreContentEquality()
	{
		return this.ignoreContentEquality;
	}

	/**
	 * Retrieves the flag indicating whether extension equality should be ignored.
	 *
	 * @return true if extension equality should be ignored, false otherwise
	 */
	public boolean isIgnoreExtensionEquality()
	{
		return this.ignoreExtensionEquality;
	}

	/**
	 * Retrieves the flag indicating whether last modified attribute should be ignored.
	 *
	 * @return true if last modified attribute should be ignored, false otherwise
	 */
	public boolean isIgnoreLastModified()
	{
		return this.ignoreLastModified;
	}

	/**
	 * Retrieves the flag indicating whether length equality should be ignored.
	 *
	 * @return true if length equality should be ignored, false otherwise
	 */
	public boolean isIgnoreLengthEquality()
	{
		return this.ignoreLengthEquality;
	}

	/**
	 * Retrieves the flag indicating whether name equality should be ignored.
	 *
	 * @return true if name equality should be ignored, false otherwise
	 */
	public boolean isIgnoreNameEquality()
	{
		return this.ignoreNameEquality;
	}

	/**
	 * Returns a builder initialized with the values of this {@code SearchFileAttributesBean}
	 * object.
	 *
	 * @return a new {@code SearchFileAttributesBeanBuilder} initialized with this object's values
	 */
	public SearchFileAttributesBeanBuilder toBuilder()
	{
		return new SearchFileAttributesBeanBuilder()
			.ignoreContentEquality(this.ignoreContentEquality)
			.ignoreExtensionEquality(this.ignoreExtensionEquality)
			.ignoreLastModified(this.ignoreLastModified)
			.ignoreLengthEquality(this.ignoreLengthEquality)
			.ignoreNameEquality(this.ignoreNameEquality);
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return a string representation of the object
	 */
	@Override
	public String toString()
	{
		return "SearchFileAttributesBean{" + "ignoreContentEquality="
			+ this.isIgnoreContentEquality() + ", ignoreExtensionEquality="
			+ this.isIgnoreExtensionEquality() + ", ignoreLastModified="
			+ this.isIgnoreLastModified() + ", ignoreLengthEquality="
			+ this.isIgnoreLengthEquality() + ", ignoreNameEquality=" + this.isIgnoreNameEquality()
			+ '}';
	}

	/**
	 * The builder class for constructing {@code SearchFileAttributesBean} objects.
	 */
	public static class SearchFileAttributesBeanBuilder
	{

		/** Flag indicating whether content equality should be ignored. */
		private boolean ignoreContentEquality;

		/** Flag indicating whether extension equality should be ignored. */
		private boolean ignoreExtensionEquality;

		/** Flag indicating whether last modified attribute should be ignored. */
		private boolean ignoreLastModified;

		/** Flag indicating whether length equality should be ignored. */
		private boolean ignoreLengthEquality;

		/** Flag indicating whether name equality should be ignored. */
		private boolean ignoreNameEquality;

		/**
		 * Constructs a new {@code SearchFileAttributesBeanBuilder}.
		 */
		SearchFileAttributesBeanBuilder()
		{
		}

		/**
		 * Builds the {@code SearchFileAttributesBean} object.
		 *
		 * @return the constructed {@code SearchFileAttributesBean} object
		 */
		public SearchFileAttributesBean build()
		{
			return new SearchFileAttributesBean(ignoreContentEquality, ignoreExtensionEquality,
				ignoreLastModified, ignoreLengthEquality, ignoreNameEquality);
		}

		/**
		 * Sets the flag indicating whether content equality should be ignored.
		 *
		 * @param ignoreContentEquality
		 *            true if content equality should be ignored, false otherwise
		 * @return this builder instance
		 */
		public SearchFileAttributesBeanBuilder ignoreContentEquality(boolean ignoreContentEquality)
		{
			this.ignoreContentEquality = ignoreContentEquality;
			return this;
		}

		/**
		 * Sets the flag indicating whether extension equality should be ignored.
		 *
		 * @param ignoreExtensionEquality
		 *            true if extension equality should be ignored, false otherwise
		 * @return this builder instance
		 */
		public SearchFileAttributesBeanBuilder ignoreExtensionEquality(
			boolean ignoreExtensionEquality)
		{
			this.ignoreExtensionEquality = ignoreExtensionEquality;
			return this;
		}

		/**
		 * Sets the flag indicating whether last modified attribute should be ignored.
		 *
		 * @param ignoreLastModified
		 *            true if last modified attribute should be ignored, false otherwise
		 * @return this builder instance
		 */
		public SearchFileAttributesBeanBuilder ignoreLastModified(boolean ignoreLastModified)
		{
			this.ignoreLastModified = ignoreLastModified;
			return this;
		}

		/**
		 * Sets the flag indicating whether length equality should be ignored.
		 *
		 * @param ignoreLengthEquality
		 *            true if length equality should be ignored, false otherwise
		 * @return this builder instance
		 */
		public SearchFileAttributesBeanBuilder ignoreLengthEquality(boolean ignoreLengthEquality)
		{
			this.ignoreLengthEquality = ignoreLengthEquality;
			return this;
		}

		/**
		 * Sets the flag indicating whether name equality should be ignored.
		 *
		 * @param ignoreNameEquality
		 *            true if name equality should be ignored, false otherwise
		 * @return this builder instance
		 */
		public SearchFileAttributesBeanBuilder ignoreNameEquality(boolean ignoreNameEquality)
		{
			this.ignoreNameEquality = ignoreNameEquality;
			return this;
		}

		/**
		 * Returns a string representation of the builder.
		 *
		 * @return a string representation of the builder
		 */
		@Override
		public String toString()
		{
			return "SearchFileAttributesBean.SearchFileAttributesBeanBuilder{"
				+ "ignoreContentEquality=" + ignoreContentEquality + ", ignoreExtensionEquality="
				+ ignoreExtensionEquality + ", ignoreLastModified=" + ignoreLastModified
				+ ", ignoreLengthEquality=" + ignoreLengthEquality + ", ignoreNameEquality="
				+ ignoreNameEquality + '}';
		}
	}
}
