package org.example.library.service;

import org.example.library.dto.CustomerDto;
import org.example.library.model.Customer;

public interface CustomerService {
    CustomerDto save(CustomerDto customerDto);

    Customer findByUsername(String username);

    CustomerDto getProfile(String username);

    CustomerDto updateProfile(CustomerDto profileDto);

    CustomerDto changePassword(String username, String oldPassword, String newPassword, String repeatPassword);
}
