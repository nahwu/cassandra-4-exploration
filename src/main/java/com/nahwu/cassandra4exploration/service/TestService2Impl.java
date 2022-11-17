package com.nahwu.cassandra4exploration.service;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.paging.OffsetPager;
import com.nahwu.cassandra4exploration.entity.TestDbObject;
import com.nahwu.cassandra4exploration.repository.TestDbObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class TestService2Impl {
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

        TestDbObject javaBook = new TestDbObject(UUID.randomUUID(), new Date(), "Head First Java", "O'Reilly Media", "MY", "", 0.0, "");
        TestDbObject dPatternBook = new TestDbObject(UUID.randomUUID(), new Date(), "Head Design Patterns", "O'Reilly Media", "SG", "", 0.0, "");
        testDbObjectRepository.save(javaBook);
        testDbObjectRepository.save(dPatternBook);

        Iterable<TestDbObject> books = testDbObjectRepository.findAll();
        return books;
    }
}
