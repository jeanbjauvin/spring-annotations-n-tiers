package com.cgi.springtdd.bucketconnection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cgi.springtdd.configuration.AppConfigTest;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfigTest.class)
public class BucketConnectionTest {
    
    private static Logger logger;

    @Autowired
    private IBucketConnection bucketConnection;
    
    @BeforeClass
    public static void setUpOnce() throws Exception {
        logger = LogManager.getLogger("BucketConnectionTest");
        logger.debug("Initialising BucketConnection tests..");
        logger.debug("Initialisation done!");
    }
    
    @Test
    public void shouldCreateACluster() {
        logger.debug("Testing Couchbase cluster creation");
        Cluster cluster = bucketConnection.cluster();
        assertThat(cluster, isA(Cluster.class));
        logger.debug("Disconnecting cluster");
        assertTrue(cluster.disconnect());
    }
    
    @Test
    public void shouldReturnABucket() {
        logger.debug("Testing that BucketConnection.bucket() returns a " + Bucket.class);
        Bucket bucket = bucketConnection.bucket();
        assertThat(bucket, isA(Bucket.class));
    }
    
    @AfterClass
    public static void tearDownOnce() throws Exception {
        logger.debug("BucketConnection tests done!");
    }
}
