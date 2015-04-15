package test.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class App {
    public static void main(String ... args) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Record record = objectMapper.readValue(new File("/home/bauerjos/projects/dns-test/target/classes/record.json"), Record.class);
        System.out.println("Record POJO: " + record);
    }
}
