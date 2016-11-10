package com.cgi.springtdd.bucketconnection;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;

public interface IBucketConnection {

    public Cluster cluster();
    
    public Bucket bucket();
}
