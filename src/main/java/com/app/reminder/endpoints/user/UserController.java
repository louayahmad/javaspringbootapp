package com.app.reminder.endpoints.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Users", description = "Perform various user actions.")
public class UserController {

    @Autowired
    private UserRepository db;

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return db.findAll();
    }

    @GetMapping("/users/id")
    public User getUserById(@PathVariable String id) {
        return db.findById(id).get();
    }

    @PostMapping("users/add")
    public String createUser(@RequestBody User user){
        db.save(user);
        return "User " + user.getName() + " added successfully!";
    }
    
}
