/*
 * Purpose: Provide a shared session to Cassandra, within the entire microservice
 */
package com.nahwu.cassandra4exploration.service;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;

@Service
public class CassandraConnector {
    private static final Logger logger = LoggerFactory.getLogger(CassandraConnector.class);

    @Autowired
    private Environment env;

    private CqlSession session;

    private void connect(String node, Integer port, String dataCenter) {
        CqlSessionBuilder builder = CqlSession.builder();
        builder.addContactPoint(new InetSocketAddress(node, port));
        builder.withLocalDatacenter(dataCenter);
        logger.info("__Establishing new session with Cassandra");
        session = builder.build();
    }

    public CqlSession getSession() {
        logger.info("__Getting Cassandra session");
        if (session == null) {
            connect(env.getProperty("service.datastax.cassandra.node.contact-points"),
                    Integer.valueOf(env.getProperty("service.datastax.cassandra.node.port")),
                    env.getProperty("service.datastax.cassandra.node.local-datacenter"));
        }
        return session;
    }

    public void close() {
        session.close();
    }
}
