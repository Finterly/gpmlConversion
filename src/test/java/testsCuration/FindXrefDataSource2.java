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
package org.pathvisio.libgpml.libgpml.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bridgedb.DataSource;
import org.bridgedb.bio.DataSourceTxt;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.pathvisio.libgpml.libgpml.io.ConverterException;

import junit.framework.TestCase;

/**
 * Test for collecting Xref values.
 * 
 * @author finterly
 */
public class FindXrefDataSource2 extends TestCase {

	/**
	 * For collecting Xref data sources for BridgeDb from directory of GPML2013a
	 * files. Prints out name of data sources and their frequency of use.
	 * 
	 * @throws IOException
	 * @throws ConverterException
	 */
	public static void testCollectXrefs() throws IOException, ConverterException {

		DataSourceTxt.init();
		// Gets all organism directories
		File dirAllOrganisms = new File("C:/Users/p70073399/Documents/wikipathways-20210527-all-species/cache");
		String[] dirOrganisms = dirAllOrganisms.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
		System.out.println(Arrays.toString(dirOrganisms));
		// Gets all gpml for each organism directory
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
					assertTrue(file.exists());
					try {
						SAXBuilder builder = new SAXBuilder();
						Document readDoc = builder.build(file);
						Element root = readDoc.getRootElement();
						List<Element> elements = root.getChildren();
						for (Element e : elements) {
							Element xref = e.getChild("Xref", e.getNamespace());
							if (xref != null) {
								String id = xref.getAttributeValue("ID");
								String dataSource = xref.getAttributeValue("Database");

								if (id == "" && dataSource != "") {
									System.out.println("File " + i + " : " + file.getName());
									System.out.println(dataSource);
								}
							}
						}
					} catch (JDOMException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}
}