package com.clearSolutions.testTask.services;


import com.clearSolutions.testTask.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Value("${minimum.age}")
    private int minimumAge;


    private final List<User> userList = new ArrayList<>();

    public int getMinimumAge() {
        return minimumAge;
    }

    public void setMinimumAge(int minimumAge) {
        this.minimumAge = minimumAge;
    }

    public User createUser(User user) {
        LocalDate currentDate = LocalDate.now();
        if (Period.between(user.getBirthDate(), currentDate).getYears() < minimumAge) {
            throw new IllegalArgumentException("User must be at least " + minimumAge + " years old");
        }
        userList.add(user);
        return user;
    }

    public User updateUserPart(String email, User update) {
        User user = findUserByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (update.getFirstName() != null) {
            user.setFirstName(update.getFirstName());
        }
        if (update.getLastName() != null) {
            user.setLastName(update.getLastName());
        }
        if (update.getAddress() != null) {
            user.setAddress(update.getAddress());
        }
        if (update.getPhoneNumber() != null) {
            user.setPhoneNumber(update.getPhoneNumber());
        }

        return user;
    }

    public User updateUserAll(String email, User update) {
        User user = findUserByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        user.setFirstName(update.getFirstName());
        user.setLastName(update.getLastName());
        user.setAddress(update.getAddress());
        user.setPhoneNumber(update.getPhoneNumber());

        return user;
    }

    public void deleteUser(String email) {
        User user = findUserByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        userList.remove(user);
    }

    public List<User> searchUsersByBirthDateRange(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Invalid birth date range");
        }

        return userList.stream()
                .filter(u -> u.getBirthDate().isAfter(from) && u.getBirthDate().isBefore(to))
                .collect(Collectors.toList());
    }

    private User findUserByEmail(String email) {
        return userList.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
}

