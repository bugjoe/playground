DROP KEYSPACE test;

CREATE KEYSPACE IF NOT EXISTS test WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

USE test;

CREATE TYPE record (
	name  text,
	type  text,
	clazz text,
	data  text
);

CREATE TABLE zones (
	zoneName text PRIMARY KEY,
	records set<frozen<record>>
);


INSERT INTO zones (zoneName, records) VALUES ('foo.com', {
{name: 'ns1.foo.com', type: 'NS', clazz: 'IN', data: '1.2.3.4'},
{name: 'ns2.foo.com', type: 'NS', clazz: 'IN', data: '2.2.3.3'}
});
//INSERT INTO zones (zoneName, records) VALUES ('foo.com', records = records + {name:'foo.com', type:'A', clazz:'IN', data:'123.111.222.123'});
//INSERT INTO records (zoneName, type, class, data) VALUES ('foo.com', 'NS', 'IN', 'ns1.foo.com');
//INSERT INTO records (zoneName, type, class, data) VALUES ('bar.org', 'A', 'IN', '10.222.111.10');
//INSERT INTO records (zoneName, type, class, data) VALUES ('bar.org', 'NS', 'IN', 'ns1.bar.org');
