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

import java.io.File;
import java.util.List;
import java.util.Properties;

public class DependenciesData
{
	public static class DependenciesDataBuilder
	{
		private File buildGradle;
		private Properties properties;
		private List<String> versionStrings;

		DependenciesDataBuilder()
		{
		}

		public DependenciesData build()
		{
			return new DependenciesData(buildGradle, versionStrings, properties);
		}

		public DependenciesData.DependenciesDataBuilder buildGradle(File buildGradle)
		{
			this.buildGradle = buildGradle;
			return this;
		}

		public DependenciesData.DependenciesDataBuilder properties(Properties properties)
		{
			this.properties = properties;
			return this;
		}

		@Override
		public String toString()
		{
			return "DependenciesData.DependenciesDataBuilder(buildGradle=" + this.buildGradle
				+ ", versionStrings=" + this.versionStrings + ", properties=" + this.properties
				+ ")";
		}

		public DependenciesData.DependenciesDataBuilder versionStrings(List<String> versionStrings)
		{
			this.versionStrings = versionStrings;
			return this;
		}
	}

	public static final String BUILD_GRADLE_NAME = "build.gradle";
	public static final String GRADLE_PROPERTIES_NAME = "gradle.properties";

	public static DependenciesDataBuilder builder()
	{
		return new DependenciesDataBuilder();
	}

	File buildGradle;

	Properties properties;

	List<String> versionStrings;

	DependenciesData(File buildGradle, List<String> versionStrings, Properties properties)
	{
		this.buildGradle = buildGradle;
		this.versionStrings = versionStrings;
		this.properties = properties;
	}

	protected boolean canEqual(final Object other)
	{
		return other instanceof DependenciesData;
	}

	@Override
	public boolean equals(final Object o)
	{
		if (o == this)
			return true;
		if (!(o instanceof DependenciesData))
			return false;
		final DependenciesData other = (DependenciesData)o;
		if (!other.canEqual(this))
			return false;
		final Object this$buildGradle = this.getBuildGradle();
		final Object other$buildGradle = other.getBuildGradle();
		if (this$buildGradle == null
			? other$buildGradle != null
			: !this$buildGradle.equals(other$buildGradle))
			return false;
		final Object this$versionStrings = this.getVersionStrings();
		final Object other$versionStrings = other.getVersionStrings();
		if (this$versionStrings == null
			? other$versionStrings != null
			: !this$versionStrings.equals(other$versionStrings))
			return false;
		final Object this$properties = this.getProperties();
		final Object other$properties = other.getProperties();
		if (this$properties == null
			? other$properties != null
			: !this$properties.equals(other$properties))
			return false;
		return true;
	}

	public File getBuildGradle()
	{
		return this.buildGradle;
	}

	public Properties getProperties()
	{
		return this.properties;
	}

	public List<String> getVersionStrings()
	{
		return this.versionStrings;
	}

	@Override
	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $buildGradle = this.getBuildGradle();
		result = result * PRIME + ($buildGradle == null ? 43 : $buildGradle.hashCode());
		final Object $versionStrings = this.getVersionStrings();
		result = result * PRIME + ($versionStrings == null ? 43 : $versionStrings.hashCode());
		final Object $properties = this.getProperties();
		result = result * PRIME + ($properties == null ? 43 : $properties.hashCode());
		return result;
	}

	public void setBuildGradle(File buildGradle)
	{
		this.buildGradle = buildGradle;
	}

	public void setProperties(Properties properties)
	{
		this.properties = properties;
	}

	public void setVersionStrings(List<String> versionStrings)
	{
		this.versionStrings = versionStrings;
	}

	@Override
	public String toString()
	{
		return "DependenciesData(buildGradle=" + this.getBuildGradle() + ", versionStrings="
			+ this.getVersionStrings() + ", properties=" + this.getProperties() + ")";
	}
}
