package io.github.astrapi69.file.copy;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Objects;

/**
 * Interface for copying files.
 */
public interface CopyFile {
    /**
     * Copies the given source file to the destination file.
     *
     * @param source      The source file
     * @param destination The destination file
     * @return true if the file was successfully copied, otherwise false
     * @throws IOException if an I/O error occurs during the copy
     */
    default boolean copyFile(File source, File destination) throws IOException {
        return CopyFileExtensions.copyFile(source, destination);
    }

    /**
     * Copies multiple files to the destination directory.
     *
     * @param sources       List of source files to copy
     * @param destination   The destination directory
     * @throws IOException  if an I/O error occurs during the copy
     */
    default void copyFiles(List<File> sources, File destination,
                           final Charset sourceEncoding, final Charset destinationEncoding, final boolean lastModified) throws IOException {
        CopyFileExtensions.copyFiles(sources, destination, sourceEncoding, destinationEncoding, lastModified);
    }

    /**
     * Creates a backup file with ".bak" extension in the same directory as the original file.
     *
     * @param file The file to back up
     * @return the path to the backup file
     * @throws IOException if an I/O error occurs during the copy
     */
    default File newBackupOf(File file, final Charset sourceEncoding,
                             final Charset destinationEncoding) throws IOException {
        return CopyFileExtensions.newBackupOf(file, sourceEncoding, destinationEncoding);
    }

    /**
     * Copies the given source file to the destination path.
     *
     * @param source      The source path
     * @param destination The destination path
     * @return true if the file was successfully copied, otherwise false
     * @throws IOException if an I/O error occurs during the copy
     */
    default boolean copyFile(Path source, Path destination) throws IOException {
        return CopyFileExtensions.copyFile(source, destination);
    }

    /**
     * Copies multiple files to the destination directory.
     *
     * @param sources       List of source paths to copy
     * @param destination   The destination path
     * @throws IOException  if an I/O error occurs during the copy
     */
    default void copyFiles(List<Path> sources, Path destination) throws IOException {
        CopyFileExtensions.copyFiles(sources, destination);
    }

    /**
     * Creates a backup file with ".bak" extension in the same directory as the original path.
     *
     * @param path The path to back up
     * @return the path to the backup file
     * @throws IOException if an I/O error occurs during the copy
     */
    default Path newBackupOf(Path path) throws IOException {
        return CopyFileExtensions.newBackupOf(path);
    }
}
