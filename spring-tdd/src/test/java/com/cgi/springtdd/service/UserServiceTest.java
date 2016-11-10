package com.cgi.springtdd.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cgi.springtdd.configuration.AppConfigTest;
import com.cgi.springtdd.dao.IUserDao;
import com.couchbase.client.java.document.json.JsonObject;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfigTest.class)
public class UserServiceTest {

    private static Logger logger;

    @Mock
    private IUserDao userDao;

    @InjectMocks
    @Autowired
    private IUserService userService;

    private List<Map<String, Object>> userList;

    private Map<String, Object> user;
    
    private JsonObject json;

    @BeforeClass
    public static void setUpOnce() throws Exception {
        logger = LogManager.getLogger("UserServiceTest");
        logger.debug("Initialising UserService tests...");
        logger.debug("Initialisation done!");
    }

    @Before
    public void setUp() throws Exception {
        logger.debug("Initialising mock objects");
        MockitoAnnotations.initMocks(this);
        logger.debug("Initialisation of mock objects done!");
        logger.debug("Initialising single test user having id 1");
        this.user = new HashMap<String, Object>();
        this.user.put("firstname", "Gaspard");
        this.user.put("lastname", "Brochu");
        this.user.put("email", "gbrochu@example.com");
        logger.debug("Initialisation of single test user done!");
        logger.debug("Initialising test user list");
        this.userList = new ArrayList<Map<String, Object>>();
        for (int i = 1; i <= 3; i++) {
            Map<String, Object> u = new HashMap<String, Object>();
            u.put("id", UUID.randomUUID().toString());
            u.put("firstname", "firstname" + i);
            u.put("lastname", "lastname" + i);
            u.put("email", "user" + i + "@example.com");
            this.userList.add(u);
        }
        logger.debug("Initialisation of test user list done!");
        logger.debug("Initialisation of json object");
        this.json = JsonObject.create();
        logger.debug("Initialisation of json object done!");
        logger.debug("Setting up mocked objects");
        when(this.userDao.getAllUsers()).thenReturn(this.userList);
        when(this.userDao.getUserById("1")).thenReturn(this.user);
        when(this.userDao.upsertUser(this.json)).thenReturn(new HashMap<String, Object>());
        when(this.userDao.deleteAllUsers()).thenReturn(new ArrayList<Map<String, Object>>());
        when(this.userDao.deleteUserById("1")).thenReturn(new HashMap<String, Object>());
        logger.debug("Setup of mocked objects done!");
    }
    
    @Test
    public void shouldReturnAUserListOnGet() {
        logger.debug("Testing userService.getAllUsers()");
        List<Map<String, Object>> result = this.userService.getAllUsers();
        verify(this.userDao).getAllUsers();
        assertThat(result, is(this.userList));
    }
    
    @Test
    public void shouldReturnASingleUserOnGet() {
        logger.debug("Testing userService.getUserById()");
        Map<String, Object> result = this.userService.getUserById("1");
        verify(this.userDao).getUserById("1");
        assertThat(result, is(this.user));
    }
    
    @Test
    public void shouldReturnAnEmptyListOnDelete() {
        logger.debug("Testing userService.deleteAllUsers()");
        List<Map<String, Object>> result = this.userService.deleteAllUsers();
        verify(this.userDao).deleteAllUsers();
        assertThat(result, is(empty()));
    }
    
    @Test
    public void shouldReturnAnEmptyMapOnDelete() {
        logger.debug("Testing userService.deleteUserById()");
        Map<String, Object> result = this.userService.deleteUserById("1");
        verify(this.userDao).deleteUserById("1");
        assertThat(result.values(), is(empty()));
    }
    
    @Test
    public void shouldReturnAnEmptyMapOnUpsert() {
        logger.debug("Testing userService.upsert()");
        Map<String, Object> result = this.userService.upsertUser(this.json);
        verify(this.userDao).upsertUser(this.json);
        assertThat(result.values(), is(empty()));
    }

    @AfterClass
    public static void tearDownOnce() throws Exception {
        logger.debug("UserService tests done!");
    }

}
