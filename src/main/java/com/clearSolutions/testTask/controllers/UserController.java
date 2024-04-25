package com.clearSolutions.testTask.controllers;

import com.clearSolutions.testTask.models.User;
import com.clearSolutions.testTask.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Validated User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @PatchMapping("/{email}")
    public ResponseEntity<User> updateUserPartial(
            @PathVariable("email") String email,
            @RequestBody User update) {
        User updatedUser = userService.updateUserPart(email, update);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{email}")
    public ResponseEntity<User> updateUserAll(
            @PathVariable("email") String email,
            @RequestBody User update) {
        User updatedUser = userService.updateUserAll(email, update);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable("email") String email) {
        userService.deleteUser(email);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User with email " + email + " successfully deleted.");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsersByBirthDateRange(
            @RequestParam("from") LocalDate from,
            @RequestParam("to") LocalDate to) {
        List<User> users = userService.searchUsersByBirthDateRange(from, to);
        return ResponseEntity.ok(users);
    }
}

