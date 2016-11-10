package com.cgi.springtdd.dao;

import static org.junit.Assert.assertTrue;

import java.util.UUID;

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
import com.couchbase.client.java.document.json.JsonObject;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfigTest.class)
public class UserDaoTest {

    private static Logger logger;
    
    @Autowired
    private IUserDao userDao;

    @BeforeClass
    public static void setUpOnce() throws Exception {
        logger = LogManager.getLogger("UserDaoTest");
        logger.debug("Initialising UserDao tests...");
        logger.debug("Initialisation done!");
    }
    
    @Test
    public void dummyTest() {
        logger.debug("Executing dummyTest");
        JsonObject json = JsonObject.create().put("document_id", UUID.randomUUID().toString()).put("firstname", "Gaspard").put("lastname", "Brochu").put("email", "gbrochu@example.com");
        logger.debug(userDao.upsertUser(json));
        assertTrue(true);
    }

    @AfterClass
    public static void tearDownOnce() throws Exception {
        logger.debug("UserDao tests done!");
    }

}
