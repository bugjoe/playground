package test.dnsjava;

import org.xbill.DNS.DClass;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

public class App {
    public static void main(String ... args) throws Exception {
        final Name name = Name.fromString("foobar.host.com.");
        final Record record = Record.fromString(name, Type.A, DClass.IN, 3600, "123.123.123.123", null);
        System.out.println("Record: " + record);
        System.out.println("Additional Name: " + record.getAdditionalName());
        System.out.println("Name:            " + record.getName());
        final int indexFirstDot = name.toString().indexOf(".");
        final String zone = name.toString().substring(indexFirstDot + 1);
        System.out.println("Zone:            " + zone);
    }
}
