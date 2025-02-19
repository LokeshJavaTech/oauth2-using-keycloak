package com.lokesh.oauth2_using_keycloak.controller;

import com.lokesh.oauth2_using_keycloak.dto.User;
import com.lokesh.oauth2_using_keycloak.dto.UserDto;
import com.lokesh.oauth2_using_keycloak.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody User user) {
        boolean isUserSaved = userService.addUser(user);
        if (isUserSaved)
            return ResponseEntity.ok(new UserDto(user.id(), "User Saved Successfully."));
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new UserDto(user.id(), "User already exist with the same user id."));
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        List<User> userList = userService.findAll();
        return ResponseEntity.ok(userList);
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable("userId") Long userId) {
        boolean isUserDeleted = userService.deleteUserById(userId);
        if (isUserDeleted)
            return ResponseEntity.ok(new UserDto(userId, "User Deleted Successfully."));
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new UserDto(userId, "User not found with provided user id."));
    }
}
