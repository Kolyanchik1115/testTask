package com.clearSolutions.testTask.controllers;

import com.clearSolutions.testTask.models.User;
import com.clearSolutions.testTask.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testCreateUser() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(LocalDate.of(1990, 1, 1));

        when(userService.createUser(any(User.class))).thenReturn(user);

        String userJson = "{\"email\": \"test@example.com\", \"firstName\": \"John\", \"lastName\": \"Doe\", \"birthDate\": \"1990-01-01\"}";

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser("test@example.com");

        mockMvc.perform(
                        delete("/users/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User with email test@example.com successfully deleted."));
    }

    @Test
    public void testUpdateUserPartial() throws Exception {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");

        when(userService.updateUserPart(anyString(), any(User.class))).thenReturn(user);

        mockMvc.perform(
                        patch("/users/test@example.com")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"firstName\": \"John\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    public void testSearchUsersByBirthDateRange() throws Exception {
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

        when(userService.searchUsersByBirthDateRange(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(user1, user2));

        mockMvc.perform(
                        get("/users/search?from=1990-01-01&to=2000-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("test1@example.com"))
                .andExpect(jsonPath("$[1].email").value("test2@example.com"));
    }
}
