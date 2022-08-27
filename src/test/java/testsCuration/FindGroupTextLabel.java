package testsCuration;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.pathvisio.libgpml.io.ConverterException;

import junit.framework.TestCase;

/**
 * Test which searches for all Groups with textLabel used in GPML2013a pathways.
 * 
 * @author finterly
 */
public class FindGroupTextLabel extends TestCase {

	/**
	 * Searches for shape types used.
	 */
	public static void testShapeTypes() throws IOException, ConverterException {
		Map<String, String> map = new HashMap<String, String>();

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
			for (int j = 1; j < listOfFiles.length; j++) {
				File file = listOfFiles[j];
				if (file.isFile()) {
					assertTrue(file.exists());
					try {
						SAXBuilder builder = new SAXBuilder();
						Document readDoc = builder.build(file);
						Element root = readDoc.getRootElement();
						List<Element> es = root.getChildren("Group", root.getNamespace());
						for (Element e : es) {
							String textLabel = e.getAttributeValue("TextLabel");
							String style = e.getAttributeValue("Style");
							if (style != null) {
								if (style.equals("Pathway")) {
									System.out.println(file.getName());
									if (textLabel != null) {
										map.put(textLabel, file.getName());
									}
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
			for (String textLabel : map.keySet()) {
				System.out.println(textLabel + "   " + map.get(textLabel));
			}
		}
	}
}