package htmlparser;

import com.google.common.io.Files;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class App {
	private static final String LEADING_ZEROES = "00000000";

	public static void main(String... args) throws Exception {
		final Map<String, Document> profiles = parse(1423777100, 1423777250);

		for (String profileId : profiles.keySet()) {
			final String fileName = "profiles/" + profileId + ".txt";

			final File storeDir = new File("profiles");

			storeDir.mkdir();

			final Document document = profiles.get(profileId);

			Files.write(document.toString(), new File(fileName), Charset.defaultCharset());
		}
	}

	private static Map<String, Document> parse(int start, int end) {
		int validPorfiles = 0,
			invalidProfiles = 0;

		final Map<String, Document> profiles = new HashMap<String, Document>();

		for (int i = start; i <= end; i++) {
			final String profileId = getHexString(i);
			final String url = "https://www.gulp.de/freiberufler/" + profileId + ".html";
//			System.out.printf("Parsing URL: %s%n", url);

			try {
				final Document document = Jsoup.connect(url).get();
				profiles.put(profileId, document);
				System.out.printf("Profile ID valid:   %s%n", profileId);
				validPorfiles++;
			} catch (IOException e) {
				System.out.printf("Profile ID invalid: %s%n", profileId);
				invalidProfiles++;
			}
		}

		System.out.printf("Found %d valid profile(s) and %d invalid profile(s)", validPorfiles, invalidProfiles);

		return profiles;
	}

	private static String getHexString(int number) {
		final String hexString =  Integer.toHexString(number).toUpperCase();

		final int hexStringLength = hexString.length();

		final String hexStringWithLeadingZeroes = LEADING_ZEROES + hexString;

		return hexStringWithLeadingZeroes.substring(hexStringLength, hexStringWithLeadingZeroes.length());
	}
}
