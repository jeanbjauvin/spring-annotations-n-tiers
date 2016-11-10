package com.cgi.springtdd.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.cgi.springtdd.dao.IUserDao;
import com.cgi.springtdd.service.IUserService;
import com.couchbase.client.java.document.json.JsonObject;

@Service("userService")
public class UserServiceImpl implements IUserService, Serializable, InitializingBean {

    private static final long serialVersionUID = -9002833672475505601L;
    private static final Logger logger = LogManager.getLogger("UserService");
    
    @Autowired
    private IUserDao userDao;

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public List<Map<String, Object>> getAllUsers() {
        logger.debug("Executing getAllUsers");
        return userDao.getAllUsers();
    }

    @Override
    public Map<String, Object> getUserById(String documentId) {
        Assert.notNull(documentId);
        logger.debug("Executing getUserById with documentId: " + documentId);
        return userDao.getUserById(documentId);
    }

    @Override
    public List<Map<String, Object>> deleteAllUsers() {
        logger.debug("Executing deleteAllUsers");
        return userDao.deleteAllUsers();
    }

    @Override
    public Map<String, Object> deleteUserById(String documentId) {
        Assert.notNull(documentId);
        logger.debug("Executing deleteUserById with documentId: " + documentId);
        return userDao.deleteUserById(documentId);
    }

    @Override
    public Map<String, Object> upsertUser(JsonObject data) {
        Assert.notNull(data);
        logger.debug("Executing upsertUser with user data: " + data);
        return userDao.upsertUser(data);
    }

}
