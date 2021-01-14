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
package io.github.astrapi69.search;

/**
 * The class {@link SearchFileAttributesBean} provides file attribute flags to ignore or not.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class SearchFileAttributesBean
{

	/** The ignore content equality. */
	private final boolean ignoreContentEquality;
	/** The ignore extension equality. */
	private final boolean ignoreExtensionEquality;
	/** The ignore last modified. */
	private final boolean ignoreLastModified;
	/** The ignore length equality. */
	private final boolean ignoreLengthEquality;
	/** The ignore name equality. */
	private final boolean ignoreNameEquality;

	public SearchFileAttributesBean(boolean ignoreContentEquality, boolean ignoreExtensionEquality,
		boolean ignoreLastModified, boolean ignoreLengthEquality, boolean ignoreNameEquality)
	{
		this.ignoreContentEquality = ignoreContentEquality;
		this.ignoreExtensionEquality = ignoreExtensionEquality;
		this.ignoreLastModified = ignoreLastModified;
		this.ignoreLengthEquality = ignoreLengthEquality;
		this.ignoreNameEquality = ignoreNameEquality;
	}

	public static SearchFileAttributesBeanBuilder builder()
	{
		return new SearchFileAttributesBeanBuilder();
	}

	protected boolean canEqual(final Object other)
	{
		return other instanceof SearchFileAttributesBean;
	}

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
		if (this.isIgnoreNameEquality() != other.isIgnoreNameEquality())
			return false;
		return true;
	}

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

	public boolean isIgnoreContentEquality()
	{
		return this.ignoreContentEquality;
	}

	public boolean isIgnoreExtensionEquality()
	{
		return this.ignoreExtensionEquality;
	}

	public boolean isIgnoreLastModified()
	{
		return this.ignoreLastModified;
	}

	public boolean isIgnoreLengthEquality()
	{
		return this.ignoreLengthEquality;
	}

	public boolean isIgnoreNameEquality()
	{
		return this.ignoreNameEquality;
	}

	public SearchFileAttributesBeanBuilder toBuilder()
	{
		return new SearchFileAttributesBeanBuilder()
			.ignoreContentEquality(this.ignoreContentEquality)
			.ignoreExtensionEquality(this.ignoreExtensionEquality)
			.ignoreLastModified(this.ignoreLastModified)
			.ignoreLengthEquality(this.ignoreLengthEquality)
			.ignoreNameEquality(this.ignoreNameEquality);
	}

	@Override
	public String toString()
	{
		return "SearchFileAttributesBean(ignoreContentEquality=" + this.isIgnoreContentEquality()
			+ ", ignoreExtensionEquality=" + this.isIgnoreExtensionEquality()
			+ ", ignoreLastModified=" + this.isIgnoreLastModified() + ", ignoreLengthEquality="
			+ this.isIgnoreLengthEquality() + ", ignoreNameEquality=" + this.isIgnoreNameEquality()
			+ ")";
	}

	public static class SearchFileAttributesBeanBuilder
	{
		private boolean ignoreContentEquality;
		private boolean ignoreExtensionEquality;
		private boolean ignoreLastModified;
		private boolean ignoreLengthEquality;
		private boolean ignoreNameEquality;

		SearchFileAttributesBeanBuilder()
		{
		}

		public SearchFileAttributesBean build()
		{
			return new SearchFileAttributesBean(ignoreContentEquality, ignoreExtensionEquality,
				ignoreLastModified, ignoreLengthEquality, ignoreNameEquality);
		}

		public SearchFileAttributesBean.SearchFileAttributesBeanBuilder ignoreContentEquality(
			boolean ignoreContentEquality)
		{
			this.ignoreContentEquality = ignoreContentEquality;
			return this;
		}

		public SearchFileAttributesBean.SearchFileAttributesBeanBuilder ignoreExtensionEquality(
			boolean ignoreExtensionEquality)
		{
			this.ignoreExtensionEquality = ignoreExtensionEquality;
			return this;
		}

		public SearchFileAttributesBean.SearchFileAttributesBeanBuilder ignoreLastModified(
			boolean ignoreLastModified)
		{
			this.ignoreLastModified = ignoreLastModified;
			return this;
		}

		public SearchFileAttributesBean.SearchFileAttributesBeanBuilder ignoreLengthEquality(
			boolean ignoreLengthEquality)
		{
			this.ignoreLengthEquality = ignoreLengthEquality;
			return this;
		}

		public SearchFileAttributesBean.SearchFileAttributesBeanBuilder ignoreNameEquality(
			boolean ignoreNameEquality)
		{
			this.ignoreNameEquality = ignoreNameEquality;
			return this;
		}

		@Override
		public String toString()
		{
			return "SearchFileAttributesBean.SearchFileAttributesBeanBuilder(ignoreContentEquality="
				+ this.ignoreContentEquality + ", ignoreExtensionEquality="
				+ this.ignoreExtensionEquality + ", ignoreLastModified=" + this.ignoreLastModified
				+ ", ignoreLengthEquality=" + this.ignoreLengthEquality + ", ignoreNameEquality="
				+ this.ignoreNameEquality + ")";
		}
	}
}
