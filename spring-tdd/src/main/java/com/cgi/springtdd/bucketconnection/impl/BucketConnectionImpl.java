package com.cgi.springtdd.bucketconnection.impl;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cgi.springtdd.bucketconnection.IBucketConnection;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.CouchbaseEnvironment;

@Component("BucketConnection")
public class BucketConnectionImpl implements IBucketConnection, InitializingBean, Serializable {

    private static final long serialVersionUID = -930687137866293492L;
    private static final Logger logger = LogManager.getLogger("BucketConnection");

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Autowired
    private CouchbaseEnvironment couchbaseEnvironment;

    @Value("${hostname}")
    private String hostname;

    @Value("${bucket}")
    private String bucket;

    @Value("${password}")
    private String password;

    @Override
    public Cluster cluster() {
        logger.debug("Creating Couchbase cluster within environment: "
                + couchbaseEnvironment.toString());
        return CouchbaseCluster.create(couchbaseEnvironment, hostname);
    }

    @Override
    public Bucket bucket() {
        logger.debug("Opening bucket");
        return cluster().openBucket(bucket, password);
    }
}
