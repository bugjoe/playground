package htmlparser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

public class Parser {
	public void parse(String fileName) throws IOException {
		final File profileFile = new File(fileName);

		if(!profileFile.exists())
		{
			System.out.printf("Profile file %s does not exist%n", fileName);
			System.exit(0);
		}

		System.out.println("=======================================================");
		System.out.printf("Information about profile %s%n", profileFile.getName().replace(".html", ""));
		System.out.println("=======================================================\n");

		final Element body = Jsoup.parse(profileFile, "UTF-8").body();

//		System.out.println(body.select("section.gp-section:eq(1)").select("span"));

		System.out.printf("Verfuegbar ab: %s\n", body.select("section.gp-section:eq(1)").select("span").get(2).text());

		System.out.printf("Verfuegbar zu: %s\n", body.select("section.gp-section:eq(1)").select("span").get(5).text());

		System.out.println("\nPosition (Rolle):");
		printElements(body.select("section:eq(2)").select("div:eq(1)").select("span.form-input.text-left"));

		System.out.println("\nPosition (Kommentar):");
		printElements(body.select("section:eq(2)").select("div:eq(1)").select("span.form-textarea.gp-tinymce"));

		System.out.println("\nBranche:");
		printElements(body.select("section:eq(3)").select("li"));

		System.out.println("\nSkills:");
		printElements(body.select("section:eq(5)").select("span"));
	}

	private void printElements(Elements elements) {
		for (Element element : elements) {
			System.out.printf("\t%s\n", element.text());
		}
	}
}
