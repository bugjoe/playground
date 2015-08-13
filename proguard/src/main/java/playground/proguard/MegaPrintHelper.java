package playground.proguard;

import playground.json.Record;

public class MegaPrintHelper {
	public void printHello() {
		System.out.println("Hello World ... what else");
		final Dictionary dictionary = new Dictionary();
		System.out.println(dictionary.getSentence());
		System.out.println(dictionary.getNumber());
		final Record record = new Record();
		record.setName("John Doe");
		record.throwException();
	}
}
