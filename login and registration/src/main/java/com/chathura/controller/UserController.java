package com.chathura.controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chathura.model.User;
import com.chathura.service.UserService;

@RestController
public class UserController {

    @Autowired
    UserService service;

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return service.getAllUsers();
    }

    @PostMapping("/users")
    public String creatNewUser(@RequestBody User user){
        int emailExist = checkEmailExists(user.getEmail());
        if(emailExist > 0){
            return "Email already exists.";
        } else {
            user = service.createNewUser(user);
            return user.toString();
        }
    }

    @PatchMapping("/update/{id}")
    public String updateUser(@PathVariable ObjectId id, @RequestBody User user){
        user = service.updateUser(user);
        return user.toString();
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody User user){
        int emailExist = checkEmailExists(user.getEmail());
        if(emailExist > 0){
            return "Email already exists.";
        } else {
            user.setUserRole("passenger");
            
            user = service.createNewUser(user);
            return user.toString();
        }
    }

    @PostMapping("/login")
    public String login(@RequestBody User user){
        List<User> userList =  service.getUserByEmailPassword(user.getEmail(), user.getPassword());
        if(userList.size() != 0){
            return userList.get(0).getUserRole();
        } else {
            return "Invalid email and password";
        }
    }

    public int checkEmailExists(String email){
        List<User> userList = service.getUserByEmail(email);
        return userList.size();
    }

    public int checkTelephoneExists(String telephone){
        List<User> userList = service.getUserByTelephone(telephone);
        return userList.size();
    }
}
