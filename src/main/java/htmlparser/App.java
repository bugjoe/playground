package htmlparser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;

public class App
{
	public static void main(String... args) throws Exception
	{
		if (args.length == 0)
		{
			printHelpAndExit();
		}

		if (args[0].equalsIgnoreCase("scan"))
		{
			if (args.length == 4)
			{
				scan(args);
			} else
			{
				printHelpAndExit();
			}
		}
		else if (args[0].equalsIgnoreCase("parse"))
		{
			if (args.length == 2)
			{
				parse(args);
			} else
			{
				printHelpAndExit();
			}
		}
		else
		{
			printHelpAndExit();
		}

	}

	private static void scan(String... args)
	{
		final int numberOfJobs = Integer.parseInt(args[1]);
		final long firstProfile = Long.decode("0x" + args[2]);
		final long lastProfile = Long.decode("0x" + args[3]);
		final long numberOfProfilesPerJob = (lastProfile - firstProfile) / numberOfJobs;

		for (long nextStartProfile = firstProfile; nextStartProfile < lastProfile; nextStartProfile += numberOfProfilesPerJob + 1)
		{
			final Scanner scanner = new Scanner(nextStartProfile, nextStartProfile + numberOfProfilesPerJob);
			new Thread(scanner).start();
		}
	}

	private static void parse(String... args) throws IOException
	{
		final File profileFile = new File(args[1]);

		if(!profileFile.exists())
		{
			System.out.printf("Profile file %s does not exist%n", args[1]);
			System.exit(0);
		}

		final Element body = Jsoup.parse(profileFile, "UTF-8").body();

		System.out.println(body.select("section:eq(2)").select("div:eq(1)").select("span.form-input.text-left"));

//		for (Element element : document.body().select("h2.title"))
//		{
//			System.out.println("===========================================================");
//			System.out.println(element.toString());
//		}
//
		System.out.printf("Availability:\n\t%s\n", body.select("div#id7").select("span.form-input").select("span").last().text());

		System.out.println("\nPositions (role):");
		for (Element element : body.select("section:eq(2)").select("div:eq(1)").select("span.form-input.text-left"))
		{
			System.out.printf("\t%s\n", element.text());
		}

		System.out.println("\nPosition (comment):");
		for (Element element : body.select("section:eq(2)").select("div:eq(1)").select("span.form-textarea.gp-tinymce"))
		{
			System.out.printf("\t%s\n", element.text());
		}
	}

	private static void printHelpAndExit()
	{
		System.out.println("Please use one of the following commands:");
		System.out.println("scan [number_of_parallel_jobs] [first_profile_number] [last_profile_number]");
		System.out.println("parse [profile_file]");
		System.exit(0);
	}
}
