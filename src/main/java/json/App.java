package json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;

public class App {
	public static void main(String... args) throws Exception {
		final ObjectMapper objectMapper = new ObjectMapper();
		final Record record = objectMapper.readValue(new File("record.json"), Record.class);
		System.out.println("Record POJO: " + record);
		final StringWriter stringWriter = new StringWriter();
		objectMapper.writeValue(stringWriter, record);
		System.out.println("JSON String: " + stringWriter.toString());
		stringWriter.close();
		Collections.synchronizedList(new ArrayList<String>());
	}
}
