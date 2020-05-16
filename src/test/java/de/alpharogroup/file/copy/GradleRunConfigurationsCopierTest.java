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
	@Test(enabled = false)
	public void testCopyIdeaRunConfigurations()
		throws FileDoesNotExistException, IOException, FileIsADirectoryException
	{
		CopyGradleRunConfigurations copyGradleRunConfigurationsData;
		copyGradleRunConfigurationsData = GradleRunConfigurationsCopier
			.newCopyGradleRunConfigurations("file-worker", "lottery-app",
				"/home/astrapi69/dev/github/lightblueseas/", "/home/astrapi69/dev/bitbucket/");

		GradleRunConfigurationsCopier.of(copyGradleRunConfigurationsData).copy();
	}

}
