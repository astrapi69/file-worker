package de.alpharogroup.file.system;

import de.alpharogroup.file.compare.CompareFileExtensions;
import de.alpharogroup.file.create.FileFactory;

import java.io.File;

/**
 * The class {@link SystemFileExtensions} provide methods for get system or user files.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public final class SystemFileExtensions {
    private SystemFileExtensions(){}

    /**
     * Gets the user home directory from the system as {@link File} object
     *
     * @return the user home directory from the system as {@link File} object
     */
    public static File getUserHomeDir(){
        String userHomePath = System.getProperty("user.home");
        return new File(userHomePath);
    }

    /**
     * Gets the user working directory from the system as {@link File} object
     *
     * @return the user working directory from the system as {@link File} object
     */
    public static File getUserWorkingDir(){
        String userWorkingPath = System.getProperty("user.dir");
        return new File(userWorkingPath);
    }

    /**
     * Gets the installation directory for Java Runtime Environment (JRE) from the system as {@link File} object
     *
     * @return the installation directory for Java Runtime Environment (JRE)  from the system as {@link File} object
     */
    public static File getJavaHomeDir(){
        String userHomePath = System.getProperty("java.home");
        return new File(userHomePath);
    }

    /**
     * Gets the temporary directory from the system as File object.
     *
     * @return the temporary directory from the system.
     */
    public static File getTempDir()
    {
        String tmpdirPath = System.getProperty("java.io.tmpdir");
        return new File(tmpdirPath);
    }

}
