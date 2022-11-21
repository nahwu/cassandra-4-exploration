package com.nahwu.cassandra4exploration.service;

import com.datastax.oss.driver.api.core.cql.AsyncResultSet;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.paging.OffsetPager;
import com.datastax.oss.driver.api.querybuilder.insert.InsertInto;
import com.nahwu.cassandra4exploration.entity.TestDbObject;
import com.nahwu.cassandra4exploration.repository.TestDbObjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletionStage;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.*;

@Service
public class TestServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);

    private CassandraSemaphore cassandraSemaphore = new CassandraSemaphore(1000);

    @Autowired
    private TestDbObjectRepository testDbObjectRepository;

    @Autowired
    private CassandraConnector cassConnector;

    public Iterable<TestDbObject> insertAndSelectObjects() {

        String query = "SELECT * FROM testkeyspace.cyclist_stats;";
        OffsetPager pager = new OffsetPager(20);

        // Get page 2: start from a fresh result set, throw away rows 1-20, then return rows 21-40
        ResultSet rs = cassConnector.getSession().execute(query);
        OffsetPager.Page<Row> page2 = pager.getPage(rs, 2);
        logger.info(page2.toString());

        TestDbObject javaBook = new TestDbObject(UUID.randomUUID(), new Date(), "Head First Java", "O'Reilly Media", "MY", "", 0.0, "");
        TestDbObject dPatternBook = new TestDbObject(UUID.randomUUID(), new Date(), "Head Design Patterns", "O'Reilly Media", "SG", "", 0.0, "");
        testDbObjectRepository.save(javaBook);
        testDbObjectRepository.save(dPatternBook);

        Iterable<TestDbObject> books = testDbObjectRepository.findAll();
        return books;
    }

    public void writeLotsOfData1SaveAll(int insertionSize) {
        ArrayList<TestDbObject> insertionList = new ArrayList();
        ArrayList<String> peopleList = new ArrayList<>(Arrays.asList("Person 1", "Person 2", "Person 3"));
        ArrayList<String> categoryList = new ArrayList<>(Arrays.asList("face", "body", "vehicle", "anpr", "vmmr"));

        logger.info("__Preparing data in-memory");
        for (int index = 0; index < insertionSize; index++) {
            //currentLoad++;
            TestDbObject newDbObject = new TestDbObject(UUID.randomUUID(), new Date(),
                    "cake",
                    getRandomStringFromList(categoryList),
                    getRandomStringFromList(peopleList),
                    getRandomStringFromList(peopleList),
                    getRandomNumberUsingNextDouble(0.0d, 50.0d),
                    "a"
                    //"c2RmYXNmdnNkZmR2ZmRzMjRmcjNc2c2RmYXNmdnNkZmR2ZmRzMjRmcjNc2RmYXNmdnNkZmR2ZmRzMjRmcjNc2RmYXNmdnNkZmR2ZmRzMjRmcjNc2RmYXNmdnNkZmR2ZmRzMjRmcjNc2RmYXNmdnNkZmR2ZmRzM0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0"
            );
            insertionList.add(newDbObject);
        }
        logger.info("__Ready to insert into database. Size: " + insertionSize);

        testDbObjectRepository.saveAll(insertionList);
        logger.info("__Data written into DB");
    }

    public void writeLotsOfData2Save(int insertionSize) {
        ArrayList<TestDbObject> insertionList = new ArrayList();
        ArrayList<String> peopleList = new ArrayList<>(Arrays.asList("Person 1", "Person 2", "Person 3"));
        ArrayList<String> categoryList = new ArrayList<>(Arrays.asList("face", "body", "vehicle", "anpr", "vmmr"));

        logger.info("__Started mass data insertion process");
        for (int index = 0; index < insertionSize; index++) {
            TestDbObject newDbObject = new TestDbObject(UUID.randomUUID(), new Date(),
                    "cake",
                    getRandomStringFromList(categoryList),
                    getRandomStringFromList(peopleList),
                    getRandomStringFromList(peopleList),
                    getRandomNumberUsingNextDouble(0.0d, 50.0d),
                    "a"
                    //"c2RmYXNmdnNkZmR2ZmRzMjRmcjNc2c2RmYXNmdnNkZmR2ZmRzMjRmcjNc2RmYXNmdnNkZmR2ZmRzMjRmcjNc2RmYXNmdnNkZmR2ZmRzMjRmcjNc2RmYXNmdnNkZmR2ZmRzMjRmcjNc2RmYXNmdnNkZmR2ZmRzM0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0"
            );
            insertionList.add(newDbObject);
            testDbObjectRepository.save(newDbObject);       // 166 seconds  (save)     165  (saveAll)
        }
        logger.info("__Data written into DB. Size: " + insertionSize);
    }

    public void writeLotsOfData3SaveAllBatched(int insertionSize) {
        ArrayList<TestDbObject> insertionList = new ArrayList();
        ArrayList<String> peopleList = new ArrayList<>(Arrays.asList("Person 1", "Person 2", "Person 3"));
        ArrayList<String> categoryList = new ArrayList<>(Arrays.asList("face", "body", "vehicle", "anpr", "vmmr"));

        int currentLoad = 0;
        logger.info("__Started mass data insertion process");
        for (int index = 0; index < insertionSize; index++) {
            currentLoad++;
            TestDbObject newDbObject = new TestDbObject(UUID.randomUUID(), new Date(),
                    "cake",
                    getRandomStringFromList(categoryList),
                    getRandomStringFromList(peopleList),
                    getRandomStringFromList(peopleList),
                    getRandomNumberUsingNextDouble(0.0d, 50.0d),
                    "a"
                    //"c2RmYXNmdnNkZmR2ZmRzMjRmcjNc2c2RmYXNmdnNkZmR2ZmRzMjRmcjNc2RmYXNmdnNkZmR2ZmRzMjRmcjNc2RmYXNmdnNkZmR2ZmRzMjRmcjNc2RmYXNmdnNkZmR2ZmRzMjRmcjNc2RmYXNmdnNkZmR2ZmRzM0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0"
            );
            insertionList.add(newDbObject);
            if (currentLoad >= 5 || index + 1 >= insertionSize) {
                testDbObjectRepository.saveAll(insertionList);
                insertionList.clear();
                currentLoad = 0;
            }
        }
        logger.info("__Data written into DB. Size: " + insertionSize);
    }

    public void writeLotsOfData4Execute(int insertionSize) {
        ArrayList<String> peopleList = new ArrayList<>(Arrays.asList("Person 1", "Person 2", "Person 3"));
        ArrayList<String> categoryList = new ArrayList<>(Arrays.asList("face", "body", "vehicle", "anpr", "vmmr"));

        logger.info("__Started mass data insertion process");
        for (int index = 0; index < insertionSize; index++) {
            Date input = new Date();
            InsertInto a = (InsertInto) insertInto("testkeyspace", "testdbobject")
                    .value("id", literal(UUID.randomUUID()))
                    .value("date", literal(input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()))
                    .value("item", literal("cake2"))
                    .value("category", literal(getRandomStringFromList(categoryList)))
                    .value("payer", literal(getRandomStringFromList(peopleList)))
                    .value("receiver", literal(getRandomStringFromList(peopleList)))
                    .value("amount", literal(getRandomNumberUsingNextDouble(0.0d, 50.0d)));

            //ResultSet rs = cassConnector.getSession().execute(a.toString());
            cassConnector.getSession().execute(a.toString());
        }
        logger.info("__Data written into DB. Size: " + insertionSize);
    }

    public void writeLotsOfData5ExecuteAsync(int insertionSize) {
        ArrayList<String> peopleList = new ArrayList<>(Arrays.asList("Person 1", "Person 2", "Person 3"));
        ArrayList<String> categoryList = new ArrayList<>(Arrays.asList("face", "body", "vehicle", "anpr", "vmmr"));

        logger.info("__Started mass data insertion process");
        for (int index = 0; index < insertionSize; index++) {
            Date newDate = new Date();
            InsertInto a = (InsertInto) insertInto("testkeyspace", "testdbobject")
                    .value("id", literal(UUID.randomUUID()))
                    .value("date", literal(newDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()))
                    .value("item", literal("cake2"))
                    .value("category", literal(getRandomStringFromList(categoryList)))
                    .value("payer", literal(getRandomStringFromList(peopleList)))
                    .value("receiver", literal(getRandomStringFromList(peopleList)))
                    .value("amount", literal(getRandomNumberUsingNextDouble(0.0d, 50.0d)))
                    .value("imageUrl", literal("c2RmYXNmdnNkZmR2ZmRzMjRmcjNc2c2RmYXNmdnNkZmR2ZmRzMjRmcjNc2RmYXNmdnNkZmR2ZmRzMjRmcjNc2RmYXNmdnNkZmR2ZmRzMjRmcjNc2RmYXNmdnNkZmR2ZmRzMjRmcjNc2RmYXNmdnNkZmR2ZmRzM0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0c2RmYXNmdnNkZmR2ZmRzMjRmcjN0MzR0MzR0NHQzdHZ3NXQ1dHd0MzR0"
                    ));

            while (!cassandraSemaphore.tryGettingPermissionToInsertIntoCassandra()) {
                // Busy wait in this loop if no available slot
                // TODO - Look for solution to avoid busy wait
            }

            CompletionStage<AsyncResultSet> resultStage = cassConnector.getSession().executeAsync(a.toString());

            resultStage.whenComplete((version, error) -> {
                if (error != null) {
                    logger.error(error.getMessage());
                } else {
                }
                cassandraSemaphore.releasePermissionToInsertIntoCassandra();
            });
        }

        logger.info("__Data written into DB. Size: " + insertionSize);
    }


    private double getRandomNumberUsingNextDouble(double min, double max) {
        Random random = new Random();
        return random.nextDouble(max - min) + min;
    }

    private String getRandomStringFromList(ArrayList<String> list) {
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }
}
