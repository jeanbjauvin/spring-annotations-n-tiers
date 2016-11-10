package com.cgi.springtdd.service;

import java.util.List;
import java.util.Map;

import com.couchbase.client.java.document.json.JsonObject;

public interface IUserService {

    public List<Map<String, Object>> getAllUsers();
    
    public Map<String, Object> getUserById(String documentId);
    
    public List<Map<String, Object>> deleteAllUsers();
    
    public Map<String, Object> deleteUserById(String documentId);
    
    public Map<String, Object> upsertUser(JsonObject data);
}
