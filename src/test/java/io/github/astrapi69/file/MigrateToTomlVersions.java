package io.github.astrapi69.file;

import static io.github.astrapi69.gradle.migration.data.DependenciesExtensions.getLibsVersionTomlMapAsString;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import io.github.astrapi69.file.read.ReadFileExtensions;
import io.github.astrapi69.file.search.PathFinder;
import io.github.astrapi69.file.write.StoreFileExtensions;
import io.github.astrapi69.gradle.migration.data.DependenciesExtensions;
import io.github.astrapi69.gradle.migration.data.DependencyInfo;

public class MigrateToTomlVersions
{


	@Test(enabled = false)
	public void testWriteToTomlFileWithJackson() throws IOException
	{

		File projectDirectory = PathFinder.getProjectDirectory();
		File gradleDirectory = PathFinder.getRelativePath(projectDirectory, "gradle");
		File dependenciesGradle = PathFinder.getRelativePath(gradleDirectory,
			"dependencies.gradle");

		File gradlePropertiesFile = PathFinder.getRelativePath(projectDirectory,
			"gradle.properties");
		assertTrue(gradlePropertiesFile.exists());
		// 1. Load all version from gradle.properties
		Map<String, String> versionMap = DependenciesExtensions.getVersionMap(gradlePropertiesFile,
			"Version");
		String dependenciesContent = DependenciesExtensions
			.getDependenciesContent(dependenciesGradle);
		List<String> dependenciesAsStringList = DependenciesExtensions
			.getDependenciesAsStringList(dependenciesContent);


		List<DependencyInfo> dependencyInfos = DependenciesExtensions
			.getDependencyInfos(dependenciesAsStringList, versionMap);
		assertNotNull(dependencyInfos);
		assertTrue(dependencyInfos.size() == 21);

		String newDependenciesStructure = DependenciesExtensions
			.getNewDependenciesStructure(dependencyInfos);

		System.out.println(newDependenciesStructure);

		String libsVersionTomlMapAsString = getLibsVersionTomlMapAsString(dependencyInfos);
		System.out.println("========");
		System.out.println(libsVersionTomlMapAsString);
		// 2. Load all version from libs.versions.toml
		File libsVersionsToml = PathFinder.getRelativePath(gradleDirectory, "libs.versions.toml");
		if (!libsVersionsToml.exists())
		{
			StoreFileExtensions.toFile(libsVersionsToml, libsVersionTomlMapAsString);
		}
		String libsVersionsTomlFileContent = ReadFileExtensions.fromFile(libsVersionsToml);
		assertEquals(libsVersionsTomlFileContent, libsVersionTomlMapAsString);
	}
}
