import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

public class UserDao {
	private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

	public User read(File userFile) {
		try {
			final FileReader fileReader = new FileReader(userFile);
			return GSON.fromJson(fileReader, User.class);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public void write(User user, File userFile) {
		try {
			final String jsonString = GSON.toJson(user);
			Files.write(jsonString, userFile, Charset.defaultCharset());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
