package com.app.reminder.endpoints.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository db;

    @GetMapping("/get-all-users")
    public List<User> getAllUsers(){
        return db.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        return db.findById(id).get();
    }

    @PostMapping("/add-user")
    public String createUser(@RequestBody User user){
        db.save(user);
        return "User " + user.getName() + " added successfully!";
    }

    @PutMapping("/edit-user/{id}")
    public String editUser(@PathVariable String id, @RequestBody User user){
        User existingUser = db.findById(id).get();
        existingUser.setEmail(user.getEmail());
        existingUser.setName(user.getName());
        db.save(existingUser);
        return "User " + existingUser.getName() + " modified successfully!";
    }

    @DeleteMapping("/delete-user{id}")
    public String deleteUser(@PathVariable String id){
        try{
            db.deleteById(id);
        } catch (Exception e) {
            return "An error occured while deleting user with id " + id + " : " + e;
        }
        return "Successfully deleted user " + id + " !";
    }
    
}
