package org.example.customer.controller;

import org.example.library.dto.CustomerDto;
import org.example.library.model.Admin;
import org.example.library.model.Customer;
import org.example.library.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class AuthController {
    @Autowired
    CustomerService customerService;

    @GetMapping("/login")
    public String login(Model model, Principal principal) {
        model.addAttribute("title", "Login");
        if (principal != null) {
            return "redirect:/";
        } else {
            return "login";
        }
    }

    @GetMapping("/register")
    public String register(Model model, Principal principal) {
        model.addAttribute("title", "Register");
        if (principal != null) {
            return "redirect:/";
        } else {
            if(model.getAttribute("customerDto") == null) {
                model.addAttribute("customerDto", new CustomerDto());
            }
            return "register";
        }
    }

    @PostMapping("/do-register")
    public String processRegister(@Valid @ModelAttribute("customerDto") CustomerDto customerDto,
                                  BindingResult result,
                                  RedirectAttributes model) {
        try {
            if (result.hasErrors()) {
                model.addFlashAttribute("customerDto", customerDto);
                return "register";
            }
            String username = customerDto.getUsername();
            Customer customer = customerService.findByUsername(username);
            if (customer == null) {
                if (customerDto.getRepeatPassword().equals(customerDto.getPassword())) {
                    CustomerDto customerDtoSave = customerService.save(customerDto);
                    model.addFlashAttribute("customerDto", customerDto);
                    model.addFlashAttribute("success", "Register successfully!");
                } else {
                    model.addFlashAttribute("customerDto", customerDto);
                    model.addFlashAttribute("error", "Password is not the same!");
                }
            } else {
                model.addFlashAttribute("error", "Your email has been registered!");
                model.addFlashAttribute("customerDto", customerDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addFlashAttribute("error", "The server has error");
        }
        return "redirect:/register";
    }
}
