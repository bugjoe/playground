package playground.vaadin;

import com.google.gson.annotations.Expose;

import java.io.File;

public class User {
	public static final File USER_FILE_1 = new File("src/main/etc/user1.json");
	public static final File USER_FILE_2 = new File("src/main/etc/user2.json");
	public static final File USER_FILE_3 = new File("src/main/etc/user3.json");
	public static final File USER_FILE_4 = new File("src/main/etc/user4.json");

	@Expose
	private String login;

	@Expose
	private String forename;

	@Expose
	private String surname;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getForename() {
		return forename;
	}

	public void setForename(String forename) {
		this.forename = forename;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
}
