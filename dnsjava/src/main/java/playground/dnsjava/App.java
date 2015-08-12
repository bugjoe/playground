package playground.dnsjava;

import org.xbill.DNS.*;

public class App {
    public static void main(String ... args) throws Exception {
        final Name subdomainName = Name.fromString("foobar.test.org.");

        final Name zoneName = Name.fromString("test.org.");

        final Record recordA = Record.fromString(subdomainName, Type.A, DClass.IN, 3600, "123.123.123.123", null);

        final Record recordNs = Record.fromString(zoneName, Type.NS, DClass.IN, 3600, "ns1.test.org.", null);

        final SOARecord soaRecord = new SOARecord(
            zoneName,
            DClass.IN,
            3600,
            zoneName,
            Name.fromConstantString("admin@test.org."),
            1,
            100,
            10,
            100,
            1
        );

        final Record[] records = {soaRecord, recordA, recordNs};

        final Zone zone = new Zone(Name.fromString("test.org."), records);

        System.out.printf("Zone: %n%s%n", zone.toString());
    }
}
