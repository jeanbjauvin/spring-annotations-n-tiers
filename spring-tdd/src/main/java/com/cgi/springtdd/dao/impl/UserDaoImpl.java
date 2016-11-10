package com.cgi.springtdd.dao.impl;

import static com.couchbase.client.java.query.Delete.deleteFrom;
import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;
import static com.couchbase.client.java.query.dsl.Expression.path;
import static com.couchbase.client.java.query.dsl.Expression.x;
import static com.couchbase.client.java.query.dsl.functions.MetaFunctions.meta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.cgi.springtdd.bucketconnection.IBucketConnection;
import com.cgi.springtdd.dao.IUserDao;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlParams;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.couchbase.client.java.query.ParameterizedN1qlQuery;
import com.couchbase.client.java.query.Statement;
import com.couchbase.client.java.query.consistency.ScanConsistency;

@Repository("UserDao")
public class UserDaoImpl implements IUserDao, Serializable, InitializingBean {

    private static final long serialVersionUID = 1238490757113700802L;
    private static final Logger logger = LogManager.getLogger("UserDao");

    @Autowired
    private IBucketConnection bucketConnection;

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public List<Map<String, Object>> getAllUsers() {
        logger.debug("Executing getAll");
        Statement stmt = select(path(meta("users"), x("id")), x("firstname"), x("lastname"),
                x("email")).from(i(this.bucketConnection.bucket().name())).as("users");
        N1qlQueryResult queryResult = this.bucketConnection.bucket().query(N1qlQuery.simple(stmt,
                N1qlParams.build().consistency(ScanConsistency.STATEMENT_PLUS)));
        return extractResultsOrThrow(queryResult);
    }

    @Override
    public Map<String, Object> getUserById(String documentId) {
        logger.debug("Executing getDocumentById with documentId: " + documentId);
        Assert.notNull(documentId, "documentId cannot be null");
        Statement stmt = select(x("firstname"), x("lastname"), x("email"))
                .from(i(this.bucketConnection.bucket().name())).as("users")
                .where(path(meta("users"), x("id")).eq(x("$documentId")));
        ParameterizedN1qlQuery query = ParameterizedN1qlQuery.parameterized(stmt,
                JsonObject.create().put("documentId", documentId));
        N1qlQueryResult queryResult = this.bucketConnection.bucket().query(query);
        return extractResultOrThrow(queryResult);
    }

    @Override
    public List<Map<String, Object>> deleteAllUsers() {
        logger.debug("Executing deleteAll");
        Statement stmt = deleteFrom(i(this.bucketConnection.bucket().name()).as(x("users")));
        N1qlQueryResult queryResult = this.bucketConnection.bucket().query(N1qlQuery.simple(stmt,
                N1qlParams.build().consistency(ScanConsistency.STATEMENT_PLUS)));
        return extractResultsOrThrow(queryResult);
    }

    @Override
    public Map<String, Object> deleteUserById(String documentId) {
        logger.debug("Executing deleteById with documentId: " + documentId);
        Assert.notNull(documentId, "documentId cannot be null");
        Statement stmt = deleteFrom(i(this.bucketConnection.bucket().name()).as(x("users")))
                .where(path(meta("users"), x("id")).eq(x("$documentId")));
        ParameterizedN1qlQuery query = ParameterizedN1qlQuery.parameterized(stmt,
                JsonObject.create().put("documentId", documentId));
        N1qlQueryResult queryResult = this.bucketConnection.bucket().query(query);
        return extractResultOrThrow(queryResult);
    }

    @Override
    public Map<String, Object> upsertUser(JsonObject data) {
        logger.debug("Executing upsert with data: " + data.toString());
        Assert.notNull(data);
        String documentId = data.getString("id");
        documentId = documentId != null && !documentId.equals("") ? documentId
                : UUID.randomUUID().toString();
        String queryStr = "UPSERT INTO `" + this.bucketConnection.bucket().name()
                + "` (KEY, VALUE) VALUES " + "($1, {'firstname': $2, 'lastname': $3, 'email': $4})";
        JsonArray parameters = JsonArray.create().add(documentId).add(data.getString("firstname"))
                .add(data.getString("lastname")).add(data.getString("email"));
        ParameterizedN1qlQuery query = ParameterizedN1qlQuery.parameterized(queryStr, parameters);
        N1qlQueryResult queryResult = this.bucketConnection.bucket().query(query);
        return extractResultOrThrow(queryResult);
    }

    private List<Map<String, Object>> extractResultsOrThrow(N1qlQueryResult results) {
        if (!results.finalSuccess())
            throw new DataRetrievalFailureException("Query error: " + results.errors());
        List<Map<String, Object>> content = new ArrayList<Map<String, Object>>();
        results.forEach((row) -> content.add(row.value().toMap()));
        return content;
    }

    private Map<String, Object> extractResultOrThrow(N1qlQueryResult result) {
        if (!result.finalSuccess())
            throw new DataRetrievalFailureException("Query error: " + result.errors());
        List<N1qlQueryRow> allRows = result.allRows();
        if (allRows.size() == 1)
            return result.allRows().get(0).value().toMap();
        else
            return new HashMap<String, Object>();
    }
}
