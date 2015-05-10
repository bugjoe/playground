package htmlparser;

public class App {
	public static void main(String... args) throws Exception {

		if (args.length != 3 )
		{
			System.out.println("Please specify the following parameters:");
			System.out.println("1.) number of parallel running jobs");
			System.out.println("2.) number of first profile as hex");
			System.out.println("3.) number of last profile as hex");
			System.exit(1);
		}

		final int numberOfJobs = Integer.parseInt(args[0]);
		final String foo = "0x" + args[1];
		final long firstProfile = Long.decode(foo);
		final String bar = "0x" + args[2];
		final long lastProfile = Long.decode(bar);
		final long numberOfProfilesPerJob = (lastProfile - firstProfile) / numberOfJobs;

		for (long nextStartProfile = firstProfile; nextStartProfile < lastProfile; nextStartProfile += numberOfProfilesPerJob + 1)
		{
			final Scanner scanner = new Scanner(nextStartProfile, nextStartProfile + numberOfProfilesPerJob);
			new Thread(scanner).start();
		}
	}
}
