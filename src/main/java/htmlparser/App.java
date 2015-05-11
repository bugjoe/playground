package htmlparser;

import java.io.IOException;

public class App {
	public static void main(String... args) throws Exception {
		if (args.length == 0) {
			printHelpAndExit();
		}

		if (args[0].equalsIgnoreCase("scan")) {
			if (args.length == 4) {
				scan(args);
			} else {
				printHelpAndExit();
			}
		} else if (args[0].equalsIgnoreCase("parse")) {
			if (args.length == 2) {
				parse(args);
			} else {
				printHelpAndExit();
			}
		} else {
			printHelpAndExit();
		}

	}

	private static void scan(String... args) {
		final int numberOfJobs = Integer.parseInt(args[1]);
		final long firstProfile = Long.decode("0x" + args[2]);
		final long lastProfile = Long.decode("0x" + args[3]);
		final long numberOfProfilesPerJob = (lastProfile - firstProfile) / numberOfJobs;

		for (long nextStartProfile = firstProfile; nextStartProfile < lastProfile; nextStartProfile += numberOfProfilesPerJob + 1) {
			final Scanner scanner = new Scanner(nextStartProfile, nextStartProfile + numberOfProfilesPerJob);
			new Thread(scanner).start();
		}
	}

	private static void parse(String... args) throws IOException {
		new Parser().parse(args[1]);
	}

	private static void printHelpAndExit() {
		System.out.println("Please use one of the following commands:");
		System.out.println("scan [number_of_parallel_jobs] [first_profile_number] [last_profile_number]");
		System.out.println("parse [profile_file]");
		System.exit(0);
	}
}
