package de.alpharogroup.file.search;

import java.io.File;

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

	CopyGradleRunConfigurations(File ideaSourceDir, File ideaTargetDir, String sourceFilenamePrefix,
		File sourceProjectDir, String sourceProjectName, File sourceRunConfigDir,
		String targetFilenamePrefix, File targetProjectDir, String targetProjectName,
		File targetRunConfigDir)
	{
		this.ideaSourceDir = ideaSourceDir;
		this.ideaTargetDir = ideaTargetDir;
		this.sourceFilenamePrefix = sourceFilenamePrefix;
		this.sourceProjectDir = sourceProjectDir;
		this.sourceProjectName = sourceProjectName;
		this.sourceRunConfigDir = sourceRunConfigDir;
		this.targetFilenamePrefix = targetFilenamePrefix;
		this.targetProjectDir = targetProjectDir;
		this.targetProjectName = targetProjectName;
		this.targetRunConfigDir = targetRunConfigDir;
	}

	public static CopyGradleRunConfigurationsBuilder builder()
	{
		return new CopyGradleRunConfigurationsBuilder();
	}

	public File getIdeaSourceDir()
	{
		return this.ideaSourceDir;
	}

	public File getIdeaTargetDir()
	{
		return this.ideaTargetDir;
	}

	public String getSourceFilenamePrefix()
	{
		return this.sourceFilenamePrefix;
	}

	public File getSourceProjectDir()
	{
		return this.sourceProjectDir;
	}

	public String getSourceProjectName()
	{
		return this.sourceProjectName;
	}

	public File getSourceRunConfigDir()
	{
		return this.sourceRunConfigDir;
	}

	public String getTargetFilenamePrefix()
	{
		return this.targetFilenamePrefix;
	}

	public File getTargetProjectDir()
	{
		return this.targetProjectDir;
	}

	public String getTargetProjectName()
	{
		return this.targetProjectName;
	}

	public File getTargetRunConfigDir()
	{
		return this.targetRunConfigDir;
	}

	public void setIdeaSourceDir(File ideaSourceDir)
	{
		this.ideaSourceDir = ideaSourceDir;
	}

	public void setIdeaTargetDir(File ideaTargetDir)
	{
		this.ideaTargetDir = ideaTargetDir;
	}

	public void setSourceFilenamePrefix(String sourceFilenamePrefix)
	{
		this.sourceFilenamePrefix = sourceFilenamePrefix;
	}

	public void setSourceProjectDir(File sourceProjectDir)
	{
		this.sourceProjectDir = sourceProjectDir;
	}

	public void setSourceProjectName(String sourceProjectName)
	{
		this.sourceProjectName = sourceProjectName;
	}

	public void setSourceRunConfigDir(File sourceRunConfigDir)
	{
		this.sourceRunConfigDir = sourceRunConfigDir;
	}

	public void setTargetFilenamePrefix(String targetFilenamePrefix)
	{
		this.targetFilenamePrefix = targetFilenamePrefix;
	}

	public void setTargetProjectDir(File targetProjectDir)
	{
		this.targetProjectDir = targetProjectDir;
	}

	public void setTargetProjectName(String targetProjectName)
	{
		this.targetProjectName = targetProjectName;
	}

	public void setTargetRunConfigDir(File targetRunConfigDir)
	{
		this.targetRunConfigDir = targetRunConfigDir;
	}

	public boolean equals(final Object o)
	{
		if (o == this)
			return true;
		if (!(o instanceof CopyGradleRunConfigurations))
			return false;
		final CopyGradleRunConfigurations other = (CopyGradleRunConfigurations)o;
		if (!other.canEqual((Object)this))
			return false;
		final Object this$ideaSourceDir = this.getIdeaSourceDir();
		final Object other$ideaSourceDir = other.getIdeaSourceDir();
		if (this$ideaSourceDir == null ?
			other$ideaSourceDir != null :
			!this$ideaSourceDir.equals(other$ideaSourceDir))
			return false;
		final Object this$ideaTargetDir = this.getIdeaTargetDir();
		final Object other$ideaTargetDir = other.getIdeaTargetDir();
		if (this$ideaTargetDir == null ?
			other$ideaTargetDir != null :
			!this$ideaTargetDir.equals(other$ideaTargetDir))
			return false;
		final Object this$sourceFilenamePrefix = this.getSourceFilenamePrefix();
		final Object other$sourceFilenamePrefix = other.getSourceFilenamePrefix();
		if (this$sourceFilenamePrefix == null ?
			other$sourceFilenamePrefix != null :
			!this$sourceFilenamePrefix.equals(other$sourceFilenamePrefix))
			return false;
		final Object this$sourceProjectDir = this.getSourceProjectDir();
		final Object other$sourceProjectDir = other.getSourceProjectDir();
		if (this$sourceProjectDir == null ?
			other$sourceProjectDir != null :
			!this$sourceProjectDir.equals(other$sourceProjectDir))
			return false;
		final Object this$sourceProjectName = this.getSourceProjectName();
		final Object other$sourceProjectName = other.getSourceProjectName();
		if (this$sourceProjectName == null ?
			other$sourceProjectName != null :
			!this$sourceProjectName.equals(other$sourceProjectName))
			return false;
		final Object this$sourceRunConfigDir = this.getSourceRunConfigDir();
		final Object other$sourceRunConfigDir = other.getSourceRunConfigDir();
		if (this$sourceRunConfigDir == null ?
			other$sourceRunConfigDir != null :
			!this$sourceRunConfigDir.equals(other$sourceRunConfigDir))
			return false;
		final Object this$targetFilenamePrefix = this.getTargetFilenamePrefix();
		final Object other$targetFilenamePrefix = other.getTargetFilenamePrefix();
		if (this$targetFilenamePrefix == null ?
			other$targetFilenamePrefix != null :
			!this$targetFilenamePrefix.equals(other$targetFilenamePrefix))
			return false;
		final Object this$targetProjectDir = this.getTargetProjectDir();
		final Object other$targetProjectDir = other.getTargetProjectDir();
		if (this$targetProjectDir == null ?
			other$targetProjectDir != null :
			!this$targetProjectDir.equals(other$targetProjectDir))
			return false;
		final Object this$targetProjectName = this.getTargetProjectName();
		final Object other$targetProjectName = other.getTargetProjectName();
		if (this$targetProjectName == null ?
			other$targetProjectName != null :
			!this$targetProjectName.equals(other$targetProjectName))
			return false;
		final Object this$targetRunConfigDir = this.getTargetRunConfigDir();
		final Object other$targetRunConfigDir = other.getTargetRunConfigDir();
		if (this$targetRunConfigDir == null ?
			other$targetRunConfigDir != null :
			!this$targetRunConfigDir.equals(other$targetRunConfigDir))
			return false;
		return true;
	}

	protected boolean canEqual(final Object other)
	{
		return other instanceof CopyGradleRunConfigurations;
	}

	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $ideaSourceDir = this.getIdeaSourceDir();
		result = result * PRIME + ($ideaSourceDir == null ? 43 : $ideaSourceDir.hashCode());
		final Object $ideaTargetDir = this.getIdeaTargetDir();
		result = result * PRIME + ($ideaTargetDir == null ? 43 : $ideaTargetDir.hashCode());
		final Object $sourceFilenamePrefix = this.getSourceFilenamePrefix();
		result = result * PRIME + ($sourceFilenamePrefix == null ?
			43 :
			$sourceFilenamePrefix.hashCode());
		final Object $sourceProjectDir = this.getSourceProjectDir();
		result = result * PRIME + ($sourceProjectDir == null ? 43 : $sourceProjectDir.hashCode());
		final Object $sourceProjectName = this.getSourceProjectName();
		result = result * PRIME + ($sourceProjectName == null ? 43 : $sourceProjectName.hashCode());
		final Object $sourceRunConfigDir = this.getSourceRunConfigDir();
		result =
			result * PRIME + ($sourceRunConfigDir == null ? 43 : $sourceRunConfigDir.hashCode());
		final Object $targetFilenamePrefix = this.getTargetFilenamePrefix();
		result = result * PRIME + ($targetFilenamePrefix == null ?
			43 :
			$targetFilenamePrefix.hashCode());
		final Object $targetProjectDir = this.getTargetProjectDir();
		result = result * PRIME + ($targetProjectDir == null ? 43 : $targetProjectDir.hashCode());
		final Object $targetProjectName = this.getTargetProjectName();
		result = result * PRIME + ($targetProjectName == null ? 43 : $targetProjectName.hashCode());
		final Object $targetRunConfigDir = this.getTargetRunConfigDir();
		result =
			result * PRIME + ($targetRunConfigDir == null ? 43 : $targetRunConfigDir.hashCode());
		return result;
	}

	public String toString()
	{
		return "CopyGradleRunConfigurations(ideaSourceDir=" + this.getIdeaSourceDir()
			+ ", ideaTargetDir=" + this.getIdeaTargetDir() + ", sourceFilenamePrefix=" + this
			.getSourceFilenamePrefix() + ", sourceProjectDir=" + this.getSourceProjectDir()
			+ ", sourceProjectName=" + this.getSourceProjectName() + ", sourceRunConfigDir=" + this
			.getSourceRunConfigDir() + ", targetFilenamePrefix=" + this.getTargetFilenamePrefix()
			+ ", targetProjectDir=" + this.getTargetProjectDir() + ", targetProjectName=" + this
			.getTargetProjectName() + ", targetRunConfigDir=" + this.getTargetRunConfigDir() + ")";
	}

	public static class CopyGradleRunConfigurationsBuilder
	{
		private File ideaSourceDir;
		private File ideaTargetDir;
		private String sourceFilenamePrefix;
		private File sourceProjectDir;
		private String sourceProjectName;
		private File sourceRunConfigDir;
		private String targetFilenamePrefix;
		private File targetProjectDir;
		private String targetProjectName;
		private File targetRunConfigDir;

		CopyGradleRunConfigurationsBuilder()
		{
		}

		public CopyGradleRunConfigurations.CopyGradleRunConfigurationsBuilder ideaSourceDir(
			File ideaSourceDir)
		{
			this.ideaSourceDir = ideaSourceDir;
			return this;
		}

		public CopyGradleRunConfigurations.CopyGradleRunConfigurationsBuilder ideaTargetDir(
			File ideaTargetDir)
		{
			this.ideaTargetDir = ideaTargetDir;
			return this;
		}

		public CopyGradleRunConfigurations.CopyGradleRunConfigurationsBuilder sourceFilenamePrefix(
			String sourceFilenamePrefix)
		{
			this.sourceFilenamePrefix = sourceFilenamePrefix;
			return this;
		}

		public CopyGradleRunConfigurations.CopyGradleRunConfigurationsBuilder sourceProjectDir(
			File sourceProjectDir)
		{
			this.sourceProjectDir = sourceProjectDir;
			return this;
		}

		public CopyGradleRunConfigurations.CopyGradleRunConfigurationsBuilder sourceProjectName(
			String sourceProjectName)
		{
			this.sourceProjectName = sourceProjectName;
			return this;
		}

		public CopyGradleRunConfigurations.CopyGradleRunConfigurationsBuilder sourceRunConfigDir(
			File sourceRunConfigDir)
		{
			this.sourceRunConfigDir = sourceRunConfigDir;
			return this;
		}

		public CopyGradleRunConfigurations.CopyGradleRunConfigurationsBuilder targetFilenamePrefix(
			String targetFilenamePrefix)
		{
			this.targetFilenamePrefix = targetFilenamePrefix;
			return this;
		}

		public CopyGradleRunConfigurations.CopyGradleRunConfigurationsBuilder targetProjectDir(
			File targetProjectDir)
		{
			this.targetProjectDir = targetProjectDir;
			return this;
		}

		public CopyGradleRunConfigurations.CopyGradleRunConfigurationsBuilder targetProjectName(
			String targetProjectName)
		{
			this.targetProjectName = targetProjectName;
			return this;
		}

		public CopyGradleRunConfigurations.CopyGradleRunConfigurationsBuilder targetRunConfigDir(
			File targetRunConfigDir)
		{
			this.targetRunConfigDir = targetRunConfigDir;
			return this;
		}

		public CopyGradleRunConfigurations build()
		{
			return new CopyGradleRunConfigurations(ideaSourceDir, ideaTargetDir,
				sourceFilenamePrefix, sourceProjectDir, sourceProjectName, sourceRunConfigDir,
				targetFilenamePrefix, targetProjectDir, targetProjectName, targetRunConfigDir);
		}

		public String toString()
		{
			return "CopyGradleRunConfigurations.CopyGradleRunConfigurationsBuilder(ideaSourceDir="
				+ this.ideaSourceDir + ", ideaTargetDir=" + this.ideaTargetDir
				+ ", sourceFilenamePrefix=" + this.sourceFilenamePrefix + ", sourceProjectDir="
				+ this.sourceProjectDir + ", sourceProjectName=" + this.sourceProjectName
				+ ", sourceRunConfigDir=" + this.sourceRunConfigDir + ", targetFilenamePrefix="
				+ this.targetFilenamePrefix + ", targetProjectDir=" + this.targetProjectDir
				+ ", targetProjectName=" + this.targetProjectName + ", targetRunConfigDir="
				+ this.targetRunConfigDir + ")";
		}
	}
}
