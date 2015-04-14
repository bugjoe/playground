DROP KEYSPACE IF EXISTS test;

CREATE KEYSPACE IF NOT EXISTS test WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 2 };

USE test;

CREATE TYPE TypeA (
	a1 text,
	a2 text
);

CREATE TYPE TypeB (
	b1 text,
	b2 frozen<TypeA>
);

CREATE TABLE ab (
	id int PRIMARY KEY,
	test frozen<TypeB>
);

CREATE TYPE record (
	name  text,
	type  text,
	clazz text,
	data  text
);

CREATE TABLE zones (
	zoneName text PRIMARY KEY,
	records  set<frozen<record>>,
	typeA    frozen<TypeA>
);

CREATE TABLE user (
	name text,
	zone text,
	PRIMARY KEY(name)
);


INSERT INTO zones (zoneName, records, typeA) VALUES (
'foo.com',
{
	{name: 'ns1.foo.com', type: 'NS', clazz: 'IN', data: '1.2.3.4'},
	{name: 'ns2.foo.com', type: 'NS', clazz: 'IN', data: '2.2.3.3'}
},
{a2: 'a2'}
);

INSERT INTO user (name, zone) VALUES ('hubera', 'foo.com');
INSERT INTO user (name, zone) VALUES ('schmidtp', 'bar.org');

INSERT INTO ab (id, test) VALUES (1, {b1: 'b1', b2: {a1: 'a1', a2: 'a2'}});
