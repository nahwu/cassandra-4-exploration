package com.nahwu.cassandra4exploration.service;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.paging.OffsetPager;
import com.nahwu.cassandra4exploration.entity.TestDbObject;
import com.nahwu.cassandra4exploration.repository.TestDbObjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TestServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);
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

    public void writeLotsOfData(int insertionSize) {
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

    public void writeLotsOfData2(int insertionSize) {
        ArrayList<TestDbObject> insertionList = new ArrayList();
        ArrayList<String> peopleList = new ArrayList<>(Arrays.asList("Person 1", "Person 2", "Person 3"));
        ArrayList<String> categoryList = new ArrayList<>(Arrays.asList("face", "body", "vehicle", "anpr", "vmmr"));

        logger.info("__Preparing data in-memory");
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
        logger.info("__Ready to insert into database. Size: " + insertionSize);

        //testDbObjectRepository.saveAll(insertionList);
        logger.info("__Data written into DB");
    }

    public void writeLotsOfData3(int insertionSize) {
        ArrayList<TestDbObject> insertionList = new ArrayList();
        ArrayList<String> peopleList = new ArrayList<>(Arrays.asList("Person 1", "Person 2", "Person 3"));
        ArrayList<String> categoryList = new ArrayList<>(Arrays.asList("face", "body", "vehicle", "anpr", "vmmr"));

        int currentLoad = 0;
        logger.info("__Preparing data in-memory");
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
            if (currentLoad >= 5 || index + 1 >= insertionSize){
                testDbObjectRepository.saveAll(insertionList);
                insertionList.clear();
                currentLoad = 0;
            }
        }
        logger.info("__Ready to insert into database. Size: " + insertionSize);
        logger.info("__Data written into DB");
    }

    private double getRandomNumberUsingNextDouble(double min, double max) {
        Random random = new Random();
        return 1.1d;
        //return random.nextDouble(max - min) + min;
    }

    private String getRandomStringFromList(ArrayList<String> list) {
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }
}
