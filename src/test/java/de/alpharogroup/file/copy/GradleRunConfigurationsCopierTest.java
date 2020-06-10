package de.alpharogroup.file.copy;

import org.testng.annotations.Test;

public class GradleRunConfigurationsCopierTest
{

	/**
	 * Test method for copy run configurations file from a source project to a target project and
	 * modifies its content
	 */
	@Test(enabled = true) public void testCopyIdeaRunConfigurations()
	{
		String sourceProjectDirNamePrefix;
		String targetProjectDirNamePrefix;
		CopyGradleRunConfigurations copyGradleRunConfigurationsData;
		String sourceProjectName;
		String targetProjectName;
		sourceProjectName = "file-worker";
		targetProjectName = "checksum-up";
		sourceProjectDirNamePrefix = "/home/astrapi69/dev/github/lightblueseas/";
		targetProjectDirNamePrefix = "/home/astrapi69/dev/github/astrapi69/";
		copyGradleRunConfigurationsData = GradleRunConfigurationsCopier
			.newCopyGradleRunConfigurations(sourceProjectName, targetProjectName,
				sourceProjectDirNamePrefix, targetProjectDirNamePrefix, true);
		GradleRunConfigurationsCopier.of(copyGradleRunConfigurationsData).copy();
	}

}
