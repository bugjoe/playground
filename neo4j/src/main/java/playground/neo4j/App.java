package playground.neo4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.neo4j.driver.v1.AuthToken;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

public class App
{
	public static void main(String... args) throws Exception
	{
		final AuthToken authToken = AuthTokens.basic("neo4j", "foobar");
		final Session session = GraphDatabase.driver("bolt://localhost", authToken).session();
		importCis(session);
		importCosts(session);
		buildTree(session);

		session.close();
	}

	private static void importCis(Session session) throws IOException
	{
		final File csv = new File("/Users/joe/projects/playground/neo4j/src/main/etc/cis.csv");
		final BufferedReader reader = new BufferedReader(new FileReader(csv));

		// skip first line -> header
		reader.readLine();
		for (String line = reader.readLine(); line != null; line = reader.readLine())
		{
			final String[] split = line.split(";");
			final String id = split[0];
			final String name = split[1];
			final String level = split[2];
			final String cypherString = String.format("MERGE (:CI {id:'%s', name:'%s', level:%s})", id, name, level);
			System.out.println(cypherString);
			session.run(cypherString);
		}

		reader.close();
	}

	private static void importCosts(Session session) throws IOException
	{
		final File csv = new File("/Users/joe/projects/playground/neo4j/src/main/etc/charges.csv");
		final BufferedReader reader = new BufferedReader(new FileReader(csv));

		// skip first line -> header
		reader.readLine();
		for (String line = reader.readLine(); line != null; line = reader.readLine())
		{
			final String[] split = line.split(";");
			final String id = split[0];
			final String date = split[2];
			final String charge = split[3];
//			final String cypherString = String.format("MERGE (ci:CI {date:'%s', charge:'%s'}) WHERE ci.id='%s'", date, charge, id);
			final String cypherString = String.format("MATCH (ci:CI {id:'%s'}) SET ci.date='%s', ci.charge=%s", id, date, charge);
			System.out.println(cypherString);
			session.run(cypherString);
		}

		reader.close();
	}

	private static void buildTree(Session session) throws IOException
	{
		final File csv = new File("/Users/joe/projects/playground/neo4j/src/main/etc/tree.csv");
		final BufferedReader reader = new BufferedReader(new FileReader(csv));

		// skip first line -> header
		reader.readLine();
		for(String line = reader.readLine(); line != null; line = reader.readLine())
		{
			final String[] split = line.split(";");
			final String mother = split[1];
			final String child = split[2];
			final String share = split[3];
			final String cypherString = String.format(
				"MATCH (mother:CI {id:'%s'}), (child:CI {id:'%s'}) MERGE (mother)-[:MOTHER_OF {share:%s}]->(child)", mother, child, share);
			System.out.println(cypherString);
			session.run(cypherString);
		}

		reader.close();
	}
}
