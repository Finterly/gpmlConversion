package testsCuration;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.pathvisio.libgpml.io.ConverterException;

import junit.framework.TestCase;

/**
 * Test which searches for state comments of GPML2013a pathways. 
 * 
 * @author finterly
 */
public class FindStateComment extends TestCase {

	/**
	 * Searches for State comments for phosphosites in all GPMLs.
	 */
	public static void testStateComment() throws IOException, ConverterException {
		Map<String, Set<String>> foundFiles = new TreeMap<String, Set<String>>();
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
					Set<String> commentTexts = new HashSet<String>();
					try {
						SAXBuilder builder = new SAXBuilder();
						Document readDoc = builder.build(file);
						Element root = readDoc.getRootElement();
						List<Element> dns = root.getChildren("State", root.getNamespace());
						for (Element dn : dns) {
							List<Element> cmmts = dn.getChildren("Comment", dn.getNamespace());
							for (Element cmmt : cmmts) {
								String commentText = cmmt.getText();
								if (commentText.contains(";") || commentText.contains("="))
									commentTexts.add(commentText);
							}
						}
						if (!commentTexts.isEmpty()) {
							foundFiles.put(file.getName(), commentTexts);
						}
					} catch (JDOMException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		Set<String> keys = foundFiles.keySet();
		for (String key : keys) {
			String keyPrint = key.substring(0, key.lastIndexOf('.'));
//			if (foundFiles.get(key).toString().contains("="))
			System.out.format("%s	%s " + "\n", keyPrint, foundFiles.get(key).toString());
		}
//		Set<String> keys2 = foundFiles.keySet();
//		for (String key : keys2) {
//			String keyPrint = key.substring(0, key.lastIndexOf('.'));
//			if (!foundFiles.get(key).toString().contains("="))
//				System.out.format("%s	%s " + "\n", keyPrint, foundFiles.get(key).toString());
//		}
	}
}
