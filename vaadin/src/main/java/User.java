import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class User {
	public static final File USER_FILE_1 = new File("src/main/etc/user1");
	public static final File USER_FILE_2 = new File("src/main/etc/user2");
	public static final File USER_FILE_3 = new File("src/main/etc/user3");
	public static final File USER_FILE_4 = new File("src/main/etc/user4");

	private String login;
	private String forename;
	private String surname;
	private File userFile;

	public User(File userFile) {
		this.userFile = userFile;
	}

	public String getLogin() {
		readAll();
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
		writeAll();
	}

	public String getForename() {
		readAll();
		return forename;
	}

	public void setForename(String forename) {
		this.forename = forename;
		writeAll();
	}

	public String getSurname() {
		readAll();
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
		writeAll();
	}

	private void readAll() {
		try {
			final List<String> lines = Files.readLines(userFile, Charset.defaultCharset());
			for (String line : lines) {
				final String[] keyValue = line.split("=");

				if (keyValue.length < 2) {
					continue;
				}

				final String key = keyValue[0].trim();
				final String value = keyValue[1].trim();

				if (key.equalsIgnoreCase("login")) {
					login = value;
				} else if (key.equalsIgnoreCase("forename")) {
					forename = value;
				} else if (key.equalsIgnoreCase("surname")) {
					surname = value;
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void writeAll() {
		final String content = "login=" + login + "\nforename=" + forename + "\nsurname=" + surname + "\n";
		try {
			Files.write(content, userFile, Charset.defaultCharset());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
