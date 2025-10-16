package io.github.astrapi69.file.tools;

import io.github.astrapi69.io.shell.OS;

// ============================================
// Verwendungsbeispiel
// ============================================
class ProjectPathsExample
{

	public static void main(String[] args)
	{
		// EINFACHSTE VERWENDUNG - statische Getter
		// Erkennt automatisch das OS und gibt den richtigen Pfad zurück
		System.out.println("=== Direkte statische Getter (empfohlen) ===");
		String githubPath = ProjectPaths.getHomeGithubBookProjectBaseDir();
		String drivePath = ProjectPaths.getDriveBookProjectBaseDir();
		String pycharmPath = ProjectPaths.getPyCharmBookProjectBaseDir();

		System.out.println("Home GitHub: " + githubPath);
		System.out.println("Drive Backup: " + drivePath);
		System.out.println("PyCharm: " + pycharmPath);

		// Alternative: Über getCurrent() - wenn du das Enum brauchst
		System.out.println("\n=== Über getCurrent() ===");
		ProjectPaths currentPaths = ProjectPaths.getCurrent();
		System.out.println("Aktuelles OS: " + OS.get() + " -> " + currentPaths.name());
		System.out.println("Home GitHub: " + currentPaths.homeGithubBookProjectBaseDir());

		// Spezifische OS-Pfade abfragen
		System.out.println("\n=== Spezifische OS-Pfade ===");
		System.out.println("Manjaro Drive: " + ProjectPaths.MANJARO.driveBookProjectBaseDir());
		System.out.println("Ubuntu Drive: " + ProjectPaths.UBUNTU.driveBookProjectBaseDir());
		System.out.println("Windows Home: " + ProjectPaths.WINDOWS.homeGithubBookProjectBaseDir());

		// Praktisches Beispiel
		System.out.println("\n=== Praktisches Beispiel ===");
		String projectPath = ProjectPaths.getHomeGithubBookProjectBaseDir() + "/my-project";
		System.out.println("Mein Projekt liegt unter: " + projectPath);
	}
}