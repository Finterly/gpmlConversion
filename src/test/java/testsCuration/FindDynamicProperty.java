package testsCuration;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.pathvisio.libgpml.io.ConverterException;

import junit.framework.TestCase;

/**
 * Test which searches for all shape types used in GPML2013a pathways.
 * 
 * note: Anchor shapes are Circle and None.
 * 
 * @author finterly
 */
public class FindDynamicProperty extends TestCase {

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
						List<Element> es = root.getChildren();
						for (Element e : es) {
							List<Element> dps = e.getChildren("Attribute", e.getNamespace());
							for (Element dp : dps) {
								String key = dp.getAttributeValue("Key");
								String value = dp.getAttributeValue("Value");
								if (Objects.equals(key, "org.pathvisio.libgpml.core.StateRotation")) {
									map.put(value, file.getName());
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
			for (String shapeType : map.keySet()) {
				System.out.println(shapeType + "   " + map.get(shapeType));
			}
		}
	}
}