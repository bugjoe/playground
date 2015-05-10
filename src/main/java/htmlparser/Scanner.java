package htmlparser;

import com.google.common.io.Files;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class Scanner implements Runnable
{
	private static final String LEADING_ZEROES = "00000000";
	private final long startProfile;
	private final long endProfile;

	public Scanner(long startProfile, long endProfile)
	{
		this.startProfile = startProfile;
		this.endProfile = endProfile;
	}

	public void run()
	{

		System.out.printf("Start scanning from %s to %s %n",
				Long.toHexString(startProfile).toUpperCase(),
				Long.toHexString(endProfile).toUpperCase());

		int validProfiles = 0;
		int invalidProfiles = 0;

		final Map<String, Document> profiles = new HashMap<String, Document>();

		final long startTime = System.currentTimeMillis() / 1000;
		for (long i = startProfile; i <= endProfile; i++)
		{
			final String profileId = getProfileHexString(i);
			final String url = "https://www.gulp.de/freiberufler/" + profileId + ".html";

			try
			{
				final Document document = Jsoup.connect(url).get();
				profiles.put(profileId, document);
				validProfiles++;
			}
			catch (IOException e)
			{
				invalidProfiles++;
			}
		}
		final long endTime = System.currentTimeMillis() / 1000;

		writeToDisc(profiles);

		System.out.printf("Scanned %d profiles in %d seconds%n", endProfile - startProfile + 1, endTime - startTime);
		System.out.printf("Found %d valid profile(s) and %d invalid profile(s)%n", validProfiles, invalidProfiles);
	}

	private void writeToDisc(Map<String, Document> profiles)
	{
		for (String profileId : profiles.keySet()) {
			final String fileName = "profiles/" + profileId + ".txt";

			final File storeDir = new File("profiles");

			if (!storeDir.mkdir()) {
				System.out.printf("Directory (%s) was not created (maybe it already exists)%n", storeDir.toString());
			}

			final Document document = profiles.get(profileId);

			try
			{
				Files.write(document.toString(), new File(fileName), Charset.defaultCharset());
			}
			catch (IOException e)
			{
				System.out.printf("ERROR (%s) while writing profile (%s) to disc%n", e.getMessage(), profileId);
			}
		}
	}

	private static String getProfileHexString(long number) {
		final String hexString =  Long.toHexString(number).toUpperCase();

		final int hexStringLength = hexString.length();

		final String hexStringWithLeadingZeroes = LEADING_ZEROES + hexString;

		return hexStringWithLeadingZeroes.substring(hexStringLength, hexStringWithLeadingZeroes.length());
	}
}
