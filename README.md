# 1. Docker image build (example)
    docker build -t nahwu2/cassandra-4-exploration:0.0.1 .

# 2. Deployment
### Deploy Cassandra DB + Java Application

    docker compose up -d

### Bring down Cassandra DB + Java Application

    docker compose down


# 3. Deployment Configuration
## 3.1 Cassandra Database initial configuration
### 3.1.1 SSH into Docker container
    docker exec -it cassandra-4-exploration-cassandra_db-1 /bin/bash

### 3.1.2 Connect to Cassandra DB via cqlsh tool
    cqlsh

### 3.1.3 cqlsh> Create keyspace for testing
    CREATE KEYSPACE testKeySpace WITH replication = {'class' : 'SimpleStrategy', 'replication_factor' : 1};

### 3.1.4 cqlsh> Table creation
    DROP TABLE testkeyspace.testdbobject;

    CREATE TABLE testkeyspace.testdbobject (
        id UUID, date timestamp, item text, category text, payer text, receiver text, amount double, imageUrl text,
        PRIMARY KEY ((id), date)
        )
    WITH CLUSTERING ORDER BY (date DESC);

## 3.2 Other Commands
### [OPTIONAL] cqlsh> Row insertion
    INSERT INTO testkeyspace.testdbobject (id, date, item, category, payer, receiver, amount, imageUrl) 
    VALUES (e027962a-3226-4ea3-83e4-57230a457436, dateof(now()), $$Women's Tour of New Zealand$$, 'NEW_CATEGORY','payer1','receiver2', 123.45, '');

### [OPTIONAL] Basic select
    SELECT * FROM testkeyspace.testdbobject LIMIT 10;

### [OPTIONAL] Table count. 
PS: This operation is highly inefficient as it's basically an SELECT * operation

    SELECT COUNT(*) FROM testkeyspace.testdbobject;

### [OPTIONAL] SASI Index creation
    CREATE CUSTOM INDEX cyclist_contains ON testkeyspace.testdbobject (item) USING 'org.apache.cassandra.index.sasi.SASIIndex'
    WITH OPTIONS = { 'mode': 'CONTAINS' };


# X.1 Concepts
1. **SSTable Attached Secondary Indexes (SASI)**
SASI supports prefix and contains queries on strings (similar to SQL’s LIKE = "foo*" or LIKE = "foo"').
<br>https://cassandra.apache.org/doc/latest/cassandra/cql/SASI.html

1. **Pagination**
In most Web UIs and REST services, you need paginated results with random access, for example: “given a page size of 20 elements, fetch page 5”.
<br>Cassandra does not support this natively (see CASSANDRA-6511), because such queries are inherently linear: the database would have to restart from the beginning every time, and skip unwanted rows until it reaches the desired offset.
<br>https://docs.datastax.com/en/developer/java-driver/4.14/manual/core/paging/#offset-queries

1. **Counter table**
<br>https://docs.datastax.com/en/cql-oss/3.3/cql/cql_using/useCountersConcept.html

# X.2 Documentations + Articles
1.  Apache Cassandra SASI
<br>https://cassandra.apache.org/doc/latest/cassandra/cql/SASI.html
1. DataStax SASI
<br>https://docs.datastax.com/en/cql-oss/3.x/cql/cql_using/useSASIIndex.html
1. Enable SASI in Cassandra
<br>https://medium.com/featurepreneur/enabling-sasi-index-86604c37d31b
1. NodeJS + Cassandra
<br>https://www.instaclustr.com/support/documentation/cassandra/using-cassandra/connect-to-cassandra-with-node-js/
1. Elassandra - Guide
<br>https://medium.com/rahasak/elassandra-936ab46a6516
1. Discussion - ElasticSearch vs. ElasticSearch+Cassandra - (2020)
<br>https://stackoverflow.com/questions/61224168/elasticsearch-vs-elasticsearchcassandra/

# X.3 JMeter commands
### CLI command to run JMeter test plan

    .\jmeter -n -t createTransactionAPICall.jmx -l testResults.csv

### Process test result CSV file's timestamp column
1. Open testResults.csv with Excel.
1. Create new column after col A. 
1. Use this formula in the newly created col B. 

        =(A2/86400/1000)+25569

1. Format col B to 

        d/m/yyyy h:mm:ss.000