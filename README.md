
# 1. Deployment Setup
## SSH into Docker container
docker exec -it cassandra-4-exploration-cassandra_db-1 /bin/bash

## Connect to Cassandra DB via cqlsh tool
cqlsh

## cqlsh> Create keyspace for testing
CREATE KEYSPACE testKeySpace WITH replication = {'class' : 'SimpleStrategy', 'replication_factor' : 1};

## cqlsh> Table creation
CREATE TABLE testkeyspace.cyclist_stats ( id UUID PRIMARY KEY, lastname text, birthday timestamp, nationality text, weight text, height text );

DROP TABLE testkeyspace.testdbobject;

CREATE TABLE testkeyspace.testdbobject (
    id UUID, date timestamp, item text, category text, payer text, receiver text, amount double, imageUrl text,
    PRIMARY KEY ((id), date)
    )
WITH CLUSTERING ORDER BY (date DESC);


## cqlsh> Row insertion
INSERT INTO testkeyspace.cyclist_stats (id, lastname, birthday, nationality, weight, height) 
VALUES (e027962a-3226-4ea3-83e4-57230a457436, 'Scott', dateof(now()), $$Women's Tour of New Zealand$$, '56kg', '163cm');

## Basic select
SELECT * FROM testkeyspace.cyclist_stats;

SELECT * FROM testkeyspace.testdbobject LIMIT 10;



CREATE CUSTOM INDEX cyclist_contains ON testkeyspace.cyclist_stats (lastname) USING 'org.apache.cassandra.index.sasi.SASIIndex'
WITH OPTIONS = { 'mode': 'CONTAINS' };



# X.1 Concepts
1. **SSTable Attached Secondary Indexes (SASI)**
SASI supports prefix and contains queries on strings (similar to SQL’s LIKE = "foo*" or LIKE = "foo"').
https://cassandra.apache.org/doc/latest/cassandra/cql/SASI.html

1. **Pagination**
In most Web UIs and REST services, you need paginated results with random access, for example: “given a page size of 20 elements, fetch page 5”.
Cassandra does not support this natively (see CASSANDRA-6511), because such queries are inherently linear: the database would have to restart from the beginning every time, and skip unwanted rows until it reaches the desired offset.
https://docs.datastax.com/en/developer/java-driver/4.14/manual/core/paging/#offset-queries

1. **Counter table**
https://docs.datastax.com/en/cql-oss/3.3/cql/cql_using/useCountersConcept.html

# X.2 Documentations + Articles
1.  Apache Cassandra SASI
https://cassandra.apache.org/doc/latest/cassandra/cql/SASI.html
1. DataStax SASI
https://docs.datastax.com/en/cql-oss/3.x/cql/cql_using/useSASIIndex.html
1. Enable SASI in Cassandra
https://medium.com/featurepreneur/enabling-sasi-index-86604c37d31b
1. NodeJS + Cassandra
https://www.instaclustr.com/support/documentation/cassandra/using-cassandra/connect-to-cassandra-with-node-js/
1. Elassandra - Guide
https://medium.com/rahasak/elassandra-936ab46a6516
1. Discussion - ElasticSearch vs. ElasticSearch+Cassandra - (2020)
https://stackoverflow.com/questions/61224168/elasticsearch-vs-elasticsearchcassandra/