package playground.htmlparser;

import com.google.common.io.Files;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;

public class GithubLogin {
	public static void main(String... args) throws Exception {
		useProxy();
		Connection connection = Jsoup.connect("https://github.com/login");
		final Document document = connection.get();
		Files.write(document.toString(), new File("github-LoginContext.html"), Charset.defaultCharset());

		System.out.println("========================={ Response Headers }=========================");
		printMap(connection.response().headers());

		System.out.println("========================={ Response Cookies }=========================");
		printMap(connection.response().cookies());

		final Element body = document.body();
		final String authenticity_token = body.select("input[name=authenticity_token]").attr("value");
		final String cookieSessionId = connection.response().cookie("_gh_sess");

		connection = Jsoup.connect("https://github.com/session");
		connection.request().cookies().put("_gh_sess", cookieSessionId);
		connection.request().data().add(HttpConnection.KeyVal.create("utf8", "%E2%9C%93"));
		connection.request().data().add(HttpConnection.KeyVal.create("authenticity_token", authenticity_token));
		connection.request().data().add(HttpConnection.KeyVal.create("login", "bauerjoe@gmx.de"));
		connection.request().data().add(HttpConnection.KeyVal.create("password", "XXXXXXXXXXXXXX"));

		connection.post();
	}

	private static void printMap(Map<String, String> map) {
		for (String key : map.keySet()) {
			System.out.printf("%s\n\t%s\n\n", key, map.get(key));
		}
	}

	private static void useProxy() {
		System.setProperty("http.proxyHost", "localhost");
		System.setProperty("http.proxyPort", "8080");
		System.setProperty("https.proxyHost", "localhost");
		System.setProperty("https.proxyPort", "8080");
	}
}
