package de.alpharogroup.file.search;

import de.alpharogroup.collections.list.ListFactory;
import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.util.List;
import java.util.Properties;

@Data
@Builder
public class DependenciesData
{
	public static final String BUILD_GRADLE_NAME = "build.gradle";
	public static final String GRADLE_PROPERTIES_NAME = "gradle.properties";
	File buildGradle;
	List<String> versionStrings;
	Properties properties;
}
