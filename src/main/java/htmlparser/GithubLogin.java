package htmlparser;

import com.google.common.io.Files;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;

public class GithubLogin {
	public static void main(String... args) throws Exception {
		final Connection connection = Jsoup.connect("https://github.com/login");
		final Document document = connection.get();
		Files.write(document.toString(), new File("github-LoginContext.html"), Charset.defaultCharset());

		System.out.println("Response Headers ======================================================");
		printMap(connection.response().headers());

		System.out.println("Response Cookies ======================================================");
		printMap(connection.response().cookies());
	}

	private static void printMap(Map<?, ?> map) {
		for (Object key : map.keySet()) {
			System.out.printf("%s\n\t%s\n\n", key, map.get(key));
		}
	}
}
