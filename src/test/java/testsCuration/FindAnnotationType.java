package testsCuration;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.pathvisio.libgpml.io.ConverterException;

import junit.framework.TestCase;

/**
 * Test which searches for all Annotation types or OpenControlledVocabulary.
 * 
 * @author finterly
 */
public class FindAnnotationType extends TestCase {

	/**
	 * Searches for GPML2013a files to find Annotation types or
	 * OpenControlledVocabulary. [Disease, Pathway Ontology, Cell Type]
	 */
	public static void testAnnotationType() throws IOException, ConverterException {

		final Namespace BIOPAX_NAMESPACE = Namespace.getNamespace("bp",
				"http://www.biopax.org/release/biopax-level3.owl#");

		Set<String> ocvs = new HashSet<String>();
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
						Element bph = root.getChild("Biopax", root.getNamespace());
						if (bph != null) {
							List<Element> bps = bph.getChildren("openControlledVocabulary", BIOPAX_NAMESPACE);
							for (Element bp : bps) {
								String biopaxOntology = bp.getChild("Ontology", BIOPAX_NAMESPACE).getText();
								ocvs.add(biopaxOntology);
							}
						}
					} catch (JDOMException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			System.out.println(ocvs);
		}
	}
}