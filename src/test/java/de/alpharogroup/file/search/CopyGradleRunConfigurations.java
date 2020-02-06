package de.alpharogroup.file.search;

import java.io.File;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CopyGradleRunConfigurations
{
	public static final String IDEA_DIR_NAME = ".idea";
	public static final String RUN_CONFIGURATIONS_DIR_NAME = "runConfigurations";
	File ideaSourceDir;
	File ideaTargetDir;
	String sourceFilenamePrefix;
	File sourceProjectDir;
	String sourceProjectName;
	File sourceRunConfigDir;
	String targetFilenamePrefix;
	File targetProjectDir;
	String targetProjectName;
	File targetRunConfigDir;

}
