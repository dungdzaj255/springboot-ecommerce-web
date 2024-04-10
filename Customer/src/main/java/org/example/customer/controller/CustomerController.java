package org.example.customer.controller;

import org.example.library.dto.CustomerDto;
import org.example.library.model.Country;
import org.example.library.model.Customer;
import org.example.library.service.CountryService;
import org.example.library.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @Autowired
    CountryService countryService;

    @GetMapping("/profile")
    public String getProfile(Model model, Principal principal) {
        if(principal == null) {
            return "redirect:/login";
        }
        model.addAttribute("title", "My Account");
        String username = principal.getName();
        CustomerDto profileDto = customerService.getProfile(username);
        List<Country> countries = countryService.findAll();
        model.addAttribute("customer", profileDto);
        model.addAttribute("countries", countries);
        return "profile";
    }

    @RequestMapping(value = "/update-info", method = {RequestMethod.POST})
    public String updateProfile(@Valid @ModelAttribute("customer") CustomerDto customerDto,
                                BindingResult result,
                                RedirectAttributes attributes,
                                Model model,
                                Principal principal) {
        if(principal == null) {
            return "redirect:/login";
        }
        if (result.hasErrors()) {
            List<Country> countries = countryService.findAll();
            model.addAttribute("countries", countries);
            return "profile";
        }
        CustomerDto profileDtoSave = customerService.updateProfile(customerDto);
        attributes.addFlashAttribute("success", "Update successfully!");
        return "redirect:/profile";
    }

    @GetMapping("/change-password")
    public String changePassword(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        model.addAttribute("title", "Change password");
        model.addAttribute("page", "Change password");
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                             @RequestParam("newPassword") String newPassword,
                             @RequestParam("repeatNewPassword") String repeatPassword,
                             RedirectAttributes attributes,
                             Model model,
                             Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            String username = principal.getName();
            CustomerDto customerDto = customerService.changePassword(username, oldPassword, newPassword, repeatPassword);
            if (customerDto != null) {
                attributes.addFlashAttribute("success", "Your password has been changed successfully!");
                return "redirect:/profile";
            } else {
                model.addAttribute("message", "Your password is wrong");
                return "change-password";
            }
        }
    }
}
