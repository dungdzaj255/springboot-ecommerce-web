package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.library.model.Country;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    @Size(min = 3, max = 15, message = "First name should have 3-15 characters")
    private String firstName;
    @Size(min = 3, max = 15, message = "First name should have 3-15 characters")
    private String lastName;
    private String username;
    @Size(min = 8, max = 20, message = "Password has at least 8 characters")
    private String password;
    private String repeatPassword;
    @Size(min = 10, message = "Phone number is not valid")
    private String phoneNumber;
    private String address;
    private Country country;
    private String city;
}
