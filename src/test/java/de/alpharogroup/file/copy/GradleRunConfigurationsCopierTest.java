package de.alpharogroup.file.copy;

import org.testng.annotations.Test;

public class GradleRunConfigurationsCopierTest
{

	/**
	 * Test method for copy run configurations file from a source project to a target project and
	 * modifies its content
	 */
	@Test(enabled = false)
	public void testCopyIdeaRunConfigurations()
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
