package com.nahwu.cassandra4exploration.repository;

import com.nahwu.cassandra4exploration.entity.TestDbObject;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TestDbObjectRepository extends CassandraRepository<TestDbObject, UUID> {
}
