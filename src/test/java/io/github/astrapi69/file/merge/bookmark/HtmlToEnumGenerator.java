package io.github.astrapi69.file.merge.bookmark;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.github.astrapi69.file.create.FileFactory;
import io.github.astrapi69.file.search.PathFinder;
import io.github.astrapi69.file.system.SystemFileExtensions;
import io.github.astrapi69.file.write.StoreFileExtensions;

public class HtmlToEnumGenerator
{

	public static void main(String[] args) throws IOException
	{
		// Load the HTML file
		File userHomeDir = SystemFileExtensions.getUserHomeDir();
		File devTmpDir = PathFinder.getRelativePath(userHomeDir, "dev", "tmp");
		File htmlInput = PathFinder.getRelativePath(devTmpDir, "standard-names.html");
		System.out.println(htmlInput.exists());
		Document doc = Jsoup.parse(htmlInput, "UTF-8");

		// Dictionary to hold all algorithms
		Map<String, List<String>> algorithms = new HashMap<>();

		// Process each section
		Elements sections = doc.select("h2");
		for (Element section : sections)
		{
			String category = section.text().replaceAll("[<>\\-/\\.]", "").replace(" ", "");
			Element sibling = section.nextElementSibling();

			while (sibling != null && !sibling.tagName().equals("h2"))
			{
				if (sibling.tagName().equals("table"))
				{
					List<String> algos = new ArrayList<>();
					Elements rows = sibling.select("tr");
					for (int i = 1; i < rows.size(); i++)
					{ // Skip the header row
						Element row = rows.get(i);
						Elements columns = row.select("th");
						if (!columns.isEmpty())
						{
							String algorithmName = columns.get(0).text().trim();
							algos.add(algorithmName);
						}
					}
					algorithms.put(category, algos);
				}
				sibling = sibling.nextElementSibling();
			}
		}

		// Generate Java Enum code
		for (Map.Entry<String, List<String>> entry : algorithms.entrySet())
		{
			String enumName = entry.getKey().replaceAll("[<>\\-/\\.]", "");
			File file = FileFactory.newFile(devTmpDir, enumName + ".java");
			StringBuilder sb = new StringBuilder();
			System.out.println("public enum " + enumName + " {");
			sb.append("public enum " + enumName + " {");
			for (String algo : entry.getValue())
			{
				if (algo != null)
				{
					String enumConstant = algo.toUpperCase().replaceAll("[<>\\-\\./]", "_");
					if(enumConstant.contains(" ")) {
						String[] split = enumConstant.split(" ");
						for(int i = 0; i < split.length; i++) {
							sb.append("    ").append(split[i]).append(",\n");
						}
					} else {
						sb.append("    ").append(enumConstant).append(",\n");
					}
				}
			}
			System.out.println("}\n");
			sb.append("}\n");
			StoreFileExtensions.toFile(file, sb.toString());
		}
	}
}
