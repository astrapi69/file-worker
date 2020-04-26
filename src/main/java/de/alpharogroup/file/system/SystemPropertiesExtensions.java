/**
 * The MIT License
 *
 * Copyright (C) 2015 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package de.alpharogroup.file.system;

import java.io.File;

public class SystemPropertiesExtensions {

    /**
     * Gets the system file separator character. In other words the character that separates components of a file path. This is "/" on UNIX and "\" on Windows.
     *
     * @return the system file separator character
     */
    public static String getFileSeparator(){
        String fileSeparator = System.getProperty("file.separator");
        return fileSeparator;
    }

    /**
     * Gets the system line separator character. In other words the sequence used by operating system to separate lines in text files
     *
     * @return the system line separator character
     */
    public static String getLineSeparator(){
        String fileSeparator = System.getProperty("line.separator");
        return fileSeparator;
    }

    /**
     * Gets the system path separator character used in java.class.path
     *
     * @return the system path separator character used in java.class.path
     */
    public static String getPathSeparator(){
        String fileSeparator = System.getProperty("path.separator");
        return fileSeparator;
    }

    /**
     * Gets the Path used to find directories and JAR archives containing class files. Elements of the class path are separated by a platform-specific character specified in the path.separator property.
     *
     * @return the java class path
     */
    public static String getJavaClassPath(){
        String fileSeparator = System.getProperty("java.class.path");
        return fileSeparator;
    }

    /**
     * Gets the name of JRE vendor name
     *
     * @return the name of JRE vendor name
     */
    public static String getJavaVendor(){
        String javaVendor = System.getProperty("java.vendor");
        return javaVendor;
    }

    /**
     * Gets the JRE version
     *
     * @return the JRE version
     */
    public static String getJavaVersion(){
        String javaVendor = System.getProperty("java.version");
        return javaVendor;
    }

    /**
     * Gets the name of JRE vendor url
     *
     * @return the name of JRE vendor url
     */
    public static String getJavaVendorUrl(){
        String fileSeparator = System.getProperty("java.vendor.url");
        return fileSeparator;
    }

    /**
     * Gets the operating system architecture
     *
     * @return the operating system architecture
     */
    public static String getOsArchitecture(){
        String fileSeparator = System.getProperty("os.arch");
        return fileSeparator;
    }

    /**
     * Gets the operating system name
     *
     * @return the operating system name
     */
    public static String getOsName(){
        String fileSeparator = System.getProperty("os.name");
        return fileSeparator;
    }

    /**
     * Gets the operating system version
     *
     * @return the operating system version
     */
    public static String getOsVersion(){
        String fileSeparator = System.getProperty("os.version");
        return fileSeparator;
    }

    /**
     * Gets the user home directory
     *
     * @return the user home directory
     */
    public static String getUserHome(){
        String userHome = System.getProperty("user.home");
        return userHome;
    }

    /**
     * Gets the user working directory
     *
     * @return the user working directory
     */
    public static String getUserWorkingDirectory(){
        String userHome = System.getProperty("user.dir");
        return userHome;
    }

    /**
     * Gets the user name
     *
     * @return the user name
     */
    public static String getUserName(){
        String userHome = System.getProperty("user.name");
        return userHome;
    }

}
