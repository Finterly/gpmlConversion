/*******************************************************************************
 * PathVisio, a tool for data visualization and analysis using biological pathways
 * Copyright 2006-2021 BiGCaT Bioinformatics, WikiPathways
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package testsConvertReadWrite;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;

import org.pathvisio.libgpml.io.ConverterException;
import org.pathvisio.libgpml.model.GPML2021Writer;
import org.pathvisio.libgpml.model.PathwayModel;

import junit.framework.TestCase;

/**
 * Test for conversion of GPML2021 to GPML2013a.
 * 
 * @author finterly
 */
public class TestAllConvertToGPML2013a extends TestCase {

	/**
	 * For testing conversion GPML2021 to older GPML2013a. Reading a directory of
	 * GPML2021 files and writing newer to GPML2013a format. Assert output
	 * equivalent to input.
	 * 
	 * @throws IOException
	 * @throws ConverterException
	 */
	public static void testConvertToGPML2013a() throws IOException, ConverterException {
		// Gets all organism directories
		File dirAllOrganisms = new File("C:/Users/p70073399/Documents/wikipathways-20210527-all-species/cache");
		String[] dirOrganisms = dirAllOrganisms.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
		System.out.println(Arrays.toString(dirOrganisms));
		for (int i = 0; i < dirOrganisms.length; i++) {
			File dirOrganism = new File(
					"C:/Users/p70073399/Documents/wikipathways-20210527-all-species/cache/" + dirOrganisms[i]);
			File[] listOfFiles = dirOrganism.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(".gpml");
				}
			});
			// For all gpml of an organism:
			for (int j = 0; j < listOfFiles.length; j++) {
				File file = listOfFiles[j];
				if (file.isFile()) {
					System.out.println("File " + j + " : " + file.getName());
					assertTrue(file.exists());
					/* read xml to pathway model */
					PathwayModel pathwayModel = new PathwayModel();
					pathwayModel.readFromXml(file, true);

//					/* write pathway model to xml */
//					File outputFile = new File(outputDir, file.getName());
//					GPML2013aWriter.GPML2013aWRITER.writeToXml(pathwayModel, outputFile, true);
//					System.out.println(outputFile);

					/* write pathway model to xml */
					File tmp = File.createTempFile(file.getName() + "_to2021", ".gpml");
					GPML2021Writer.GPML2021WRITER.writeToXml(pathwayModel, tmp, false);
					System.out.println(tmp);

					/* method to assert file is same? */

				} else if (listOfFiles[i].isDirectory()) {
					System.out.println("Directory " + listOfFiles[i].getName());
				}
			}
		}
	}
}