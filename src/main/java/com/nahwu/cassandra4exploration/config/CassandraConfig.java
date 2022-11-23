package com.nahwu.cassandra4exploration.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories(
        basePackages = "com.nahwu.cassandra4exploration.repository")
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Autowired
    private Environment env;

    @Override
    protected String getKeyspaceName() {
        return "testkeyspace";
    }

    // Changed since driver v4.0.0   Link: https://docs.datastax.com/en/developer/java-driver/4.14/upgrade_guide/#4-0-0
    @Override
    public CqlSessionFactoryBean cassandraSession() {
        final CqlSessionFactoryBean cqlSessionFactoryBean = new CqlSessionFactoryBean();
        cqlSessionFactoryBean.setContactPoints(env.getProperty("service.datastax.cassandra.node.contact-points"));
        cqlSessionFactoryBean.setKeyspaceName(env.getProperty("service.datastax.cassandra.keyspace"));
        cqlSessionFactoryBean.setLocalDatacenter(env.getProperty("service.datastax.cassandra.node.local-datacenter"));
        cqlSessionFactoryBean.setPort(Integer.valueOf(env.getProperty("service.datastax.cassandra.node.port")));
        return cqlSessionFactoryBean;
    }
}
