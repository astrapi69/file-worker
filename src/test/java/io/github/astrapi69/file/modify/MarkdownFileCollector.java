package io.github.astrapi69.file.modify;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MarkdownFileCollector {

    public static void main(String[] args) {
        String baseDir = "/run/media/astrapi69/backups/git/hub/astrapi69/chats-with-ai/prompts";

        try {
            List<File> markdownFiles = MarkdownFileCollector.getMarkdownFiles(baseDir);
            markdownFiles.forEach(System.out::println); // Print or use the list
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static List<File> getMarkdownFiles(String directoryPath) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
            return paths
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".md"))
                .map(Path::toFile)
                .collect(Collectors.toList());
        }
    }
    /**
     * Returns a list of files with the given extension in the specified directory and its subdirectories
     *
     * @param directoryPath the base directory path
     * @param extension the file extension to match (e.g. ".md", ".txt", ".java")
     * @return list of matching files
     * @throws IOException if an I/O error occurs
     */
    public static List<File> getFilesWithExtension(String directoryPath, String extension) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(extension))
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        }
    }
}
