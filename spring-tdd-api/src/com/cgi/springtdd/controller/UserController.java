package com.cgi.springtdd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgi.springtdd.service.IUserService;
import com.couchbase.client.java.document.json.JsonObject;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping
    public Object getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userid}")
    public Object getUserById(@PathVariable String userid) {
        Assert.notNull(userid);
        if (userid.equals(""))
            return new ResponseEntity<String>(JsonObject.create()
                    .put("message", "Cannot get user with id: " + userid).toString(),
                    HttpStatus.BAD_REQUEST);
        return userService.getUserById(userid);
    }

    @PostMapping
    public Object createUser(@RequestBody String json) {
        Assert.notNull(json);
        JsonObject jsonData = JsonObject.fromJson(json);
        if (jsonData.getString("id") != null && !jsonData.getString("id").equals(""))
            return new ResponseEntity<String>(JsonObject.create()
                    .put("message", "A user id should not be specified when creating resources!")
                    .toString(), HttpStatus.BAD_REQUEST);
        return userService.upsertUser(jsonData);
    }

    @PutMapping
    public Object updateUser(@RequestBody String json) {
        Assert.notNull(json);
        JsonObject jsonData = JsonObject.fromJson(json);
        if (jsonData.getString("id") == null || jsonData.getString("id").equals(""))
            return new ResponseEntity<String>(JsonObject.create()
                    .put("message", "A user id is required to update a user!").toString(),
                    HttpStatus.BAD_REQUEST);
        return userService.upsertUser(jsonData);
    }

    @DeleteMapping
    public Object deleteAllUsers() {
        return userService.deleteAllUsers();
    }

    @DeleteMapping("/{userid}")
    public Object deleteUserById(@PathVariable String userid) {
        Assert.notNull(userid);
        if (userid.equals(""))
            return new ResponseEntity<String>(JsonObject.create()
                    .put("message", "A user id is required to delete a user!").toString(),
                    HttpStatus.BAD_REQUEST);
        return userService.deleteUserById(userid);
    }
}
