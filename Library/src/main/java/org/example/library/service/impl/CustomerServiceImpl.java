package org.example.library.service.impl;

import org.example.library.dto.CustomerDto;
import org.example.library.model.Customer;
import org.example.library.repository.CustomerRepository;
import org.example.library.repository.RoleRepository;
import org.example.library.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public CustomerDto save(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setUsername(customerDto.getUsername());
        customer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        customer.setRoles(Arrays.asList(roleRepository.findByName("CUSTOMER")));
        Customer customerSave = customerRepository.save(customer);
        return mapperDto(customerSave);
    }

    @Override
    public Customer findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    @Override
    public CustomerDto getProfile(String username) {
        Customer customer = customerRepository.findByUsername(username);
        return mapperDto(customer);
    }

    @Override
    public CustomerDto updateProfile(CustomerDto profileDto) {
        String username = profileDto.getUsername();
        Customer customer = customerRepository.findByUsername(username);
        customer.setPhoneNumber(profileDto.getPhoneNumber());
        customer.setAddress(profileDto.getAddress());
        customer.setCity(profileDto.getCity());
        customer.setCountry(profileDto.getCountry());
        Customer customerSave = customerRepository.save(customer);
        return mapperDto(customerSave);
    }

    @Override
    public CustomerDto changePassword(String username, String oldPassword, String newPassword, String repeatPassword) {
        Customer customer = customerRepository.findByUsername(username);
        if (passwordEncoder.matches(oldPassword, customer.getPassword())
                && !passwordEncoder.matches(newPassword, oldPassword)
                && !passwordEncoder.matches(newPassword, customer.getPassword())
                && repeatPassword.equals(newPassword) && newPassword.length() >= 8) {
            customer.setPassword(passwordEncoder.encode(newPassword));
            Customer customerSave = customerRepository.save(customer);
            return mapperDto(customerSave);
        } else {
            return null;
        }
    }

    private CustomerDto mapperDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setUsername(customer.getUsername());
        customerDto.setPhoneNumber(customer.getPhoneNumber());
        customerDto.setAddress(customer.getAddress());
        customerDto.setCity(customer.getCity());
        customerDto.setCountry(customer.getCountry());
        return customerDto;
    }

//    private ProfileDto mapperAccount(Customer customer) {
//        ProfileDto profileDto = new ProfileDto();
//        profileDto.setFirstName(customer.getFirstName());
//        profileDto.setLastName(customer.getLastName());
//        profileDto.setUsername(customer.getUsername());
//        profileDto.setPhoneNumber(customer.getPhoneNumber());
//        profileDto.setAddress(customer.getAddress());
//        profileDto.setCity(customer.getCity());
//        profileDto.setCountry(customer.getCountry());
//        return profileDto;
//    }
}
