package de.alpharogroup.file.modify;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.util.function.BiFunction;

import de.alpharogroup.file.modify.api.FileChangable;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ModifyFileExtensions
{

	/**
	 * Modifies the input file line by line and writes the modification in the new output file.
	 *
	 * @param inFilePath
	 *            the in file path
	 * @param outFilePath
	 *            the out file path
	 * @param modifier
	 *            the modifier {@linkplain BiFunction}
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void modifyFile(Path inFilePath, Path outFilePath, FileChangable modifier)
		throws IOException
	{
		try (
			BufferedReader bufferedReader = new BufferedReader(new FileReader(inFilePath.toFile()));
			Writer writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(outFilePath.toFile()), "utf-8")))
		{
			String readLine;
			int counter = 0;
			while ((readLine = bufferedReader.readLine()) != null)
			{
				writer.write(modifier.apply(counter, readLine));
				counter++;
			}
		}
	}
}
