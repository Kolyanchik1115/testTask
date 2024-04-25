package com.clearSolutions.testTask.models;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class User {
    @NotBlank(message = "Email must not be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "First name must not be empty")
    private String firstName;

    @NotBlank(message = "Last name not be empty")
    private String lastName;

    @NotNull(message = "Birthday must not be empty")
    @Past
    private LocalDate birthDate;

    private String address;

    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number")
    private String phoneNumber;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
