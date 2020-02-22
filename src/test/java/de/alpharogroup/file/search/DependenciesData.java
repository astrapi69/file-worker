package de.alpharogroup.file.search;

import java.io.File;
import java.util.List;
import java.util.Properties;

public class DependenciesData
{
	public static final String BUILD_GRADLE_NAME = "build.gradle";
	public static final String GRADLE_PROPERTIES_NAME = "gradle.properties";
	File buildGradle;
	List<String> versionStrings;
	Properties properties;

	DependenciesData(File buildGradle, List<String> versionStrings, Properties properties)
	{
		this.buildGradle = buildGradle;
		this.versionStrings = versionStrings;
		this.properties = properties;
	}

	public static DependenciesDataBuilder builder()
	{
		return new DependenciesDataBuilder();
	}

	public File getBuildGradle()
	{
		return this.buildGradle;
	}

	public List<String> getVersionStrings()
	{
		return this.versionStrings;
	}

	public Properties getProperties()
	{
		return this.properties;
	}

	public void setBuildGradle(File buildGradle)
	{
		this.buildGradle = buildGradle;
	}

	public void setVersionStrings(List<String> versionStrings)
	{
		this.versionStrings = versionStrings;
	}

	public void setProperties(Properties properties)
	{
		this.properties = properties;
	}

	public boolean equals(final Object o)
	{
		if (o == this)
			return true;
		if (!(o instanceof DependenciesData))
			return false;
		final DependenciesData other = (DependenciesData)o;
		if (!other.canEqual((Object)this))
			return false;
		final Object this$buildGradle = this.getBuildGradle();
		final Object other$buildGradle = other.getBuildGradle();
		if (this$buildGradle == null ?
			other$buildGradle != null :
			!this$buildGradle.equals(other$buildGradle))
			return false;
		final Object this$versionStrings = this.getVersionStrings();
		final Object other$versionStrings = other.getVersionStrings();
		if (this$versionStrings == null ?
			other$versionStrings != null :
			!this$versionStrings.equals(other$versionStrings))
			return false;
		final Object this$properties = this.getProperties();
		final Object other$properties = other.getProperties();
		if (this$properties == null ?
			other$properties != null :
			!this$properties.equals(other$properties))
			return false;
		return true;
	}

	protected boolean canEqual(final Object other)
	{
		return other instanceof DependenciesData;
	}

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

	public String toString()
	{
		return "DependenciesData(buildGradle=" + this.getBuildGradle() + ", versionStrings=" + this
			.getVersionStrings() + ", properties=" + this.getProperties() + ")";
	}

	public static class DependenciesDataBuilder
	{
		private File buildGradle;
		private List<String> versionStrings;
		private Properties properties;

		DependenciesDataBuilder()
		{
		}

		public DependenciesData.DependenciesDataBuilder buildGradle(File buildGradle)
		{
			this.buildGradle = buildGradle;
			return this;
		}

		public DependenciesData.DependenciesDataBuilder versionStrings(List<String> versionStrings)
		{
			this.versionStrings = versionStrings;
			return this;
		}

		public DependenciesData.DependenciesDataBuilder properties(Properties properties)
		{
			this.properties = properties;
			return this;
		}

		public DependenciesData build()
		{
			return new DependenciesData(buildGradle, versionStrings, properties);
		}

		public String toString()
		{
			return "DependenciesData.DependenciesDataBuilder(buildGradle=" + this.buildGradle
				+ ", versionStrings=" + this.versionStrings + ", properties=" + this.properties
				+ ")";
		}
	}
}
