package de.alpharogroup.file.copy;

import java.io.IOException;

import org.testng.annotations.Test;

import de.alpharogroup.file.exceptions.FileDoesNotExistException;
import de.alpharogroup.file.exceptions.FileIsADirectoryException;


public class GradleRunConfigurationsCopierTest
{


	/**
	 * Test method for copy run configurations file from a source project to a target project and
	 * modifies its content
	 */
	@Test(enabled = true)
	public void testCopyIdeaRunConfigurations()
		throws FileDoesNotExistException, IOException, FileIsADirectoryException
	{
		String sourceProjectDirNamePrefix;
		String targetProjectDirNamePrefix;
		CopyGradleRunConfigurations copyGradleRunConfigurationsData;
		String sourceProjectName;
		String targetProjectName;
		sourceProjectName = "file-worker";
		targetProjectName = "net-extensions";
		sourceProjectDirNamePrefix = "/home/astrapi69/dev/github/lightblueseas/";
		targetProjectDirNamePrefix = "/home/astrapi69/dev/github/lightblueseas/";
		copyGradleRunConfigurationsData = GradleRunConfigurationsCopier
			.newCopyGradleRunConfigurations(sourceProjectName, targetProjectName,
					sourceProjectDirNamePrefix, targetProjectDirNamePrefix);

		GradleRunConfigurationsCopier.of(copyGradleRunConfigurationsData).copy();
	}

}
