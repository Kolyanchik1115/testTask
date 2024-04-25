package com.clearSolutions.testTask.services;

import com.clearSolutions.testTask.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService();
        userService.setMinimumAge(18);
    }

    @Test
    public void testCreateUser_ValidUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(LocalDate.of(1990, 1, 1));

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals(user.getEmail(), createdUser.getEmail());
    }

    @Test
    public void testCreateUser_TooYoung() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(LocalDate.of(2015, 1, 1));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(user);
        });

        String expectedMessage = "User must be at least " + userService.getMinimumAge() + " years old";
        assertEquals(expectedMessage, exception.getMessage());    }

    @Test
    public void testUpdateUserPart_UserFound() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        userService.createUser(user);

        User update = new User();
        update.setFirstName("UpdatedName");

        User updatedUser = userService.updateUserPart("test@example.com", update);

        assertNotNull(updatedUser);
        assertEquals("UpdatedName", updatedUser.getFirstName());
    }

    @Test
    public void testUpdateUserPart_UserNotFound() {
        User update = new User();
        update.setFirstName("UpdatedName");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUserPart("invalid@example.com", update);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testSearchUsersByBirthDateRange_ValidRange() {
        User user1 = new User();
        user1.setEmail("test1@example.com");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setBirthDate(LocalDate.of(1990, 1, 1));

        User user2 = new User();
        user2.setEmail("test2@example.com");
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setBirthDate(LocalDate.of(1992, 1, 1));

        userService.createUser(user1);
        userService.createUser(user2);

        List<User> result = userService.searchUsersByBirthDateRange(LocalDate.of(1980, 1, 1), LocalDate.of(1995, 1, 1));

        assertEquals(2, result.size());
    }

    @Test
    public void testSearchUsersByBirthDateRange_InvalidRange() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.searchUsersByBirthDateRange(LocalDate.of(2000, 1, 1), LocalDate.of(1990, 1, 1));
        });

        assertEquals("Invalid birth date range", exception.getMessage());
    }
}
