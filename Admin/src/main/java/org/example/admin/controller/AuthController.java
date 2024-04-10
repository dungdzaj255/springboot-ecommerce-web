package org.example.admin.controller;

import org.example.library.dto.AdminDto;
import org.example.library.model.Admin;
import org.example.library.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AuthController {
    @Autowired
    private AdminService adminService;

    @GetMapping(value = {"", "/index"})
    public String home(Model model) {
        model.addAttribute("title", "Home");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        return "index";
    }

    @GetMapping("/login")
    public  String loginForm(Model model) {
        model.addAttribute("title", "Login");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/index";
        }
        return "login";
    }

    @GetMapping("/register")
    public  String register(Model model) {
        model.addAttribute("title", "Register");
        model.addAttribute("adminDto", new AdminDto());
        return "register";
    }

    @GetMapping("/forgot-password")
    public  String forgotPassword(Model model) {
        model.addAttribute("title", "Forgot Password");
        return "forgot-password";
    }

    @PostMapping("/register-new")
    public String addNewAdmin(@Valid @ModelAttribute("adminDto") AdminDto adminDto,
                              BindingResult result,
                              Model model) {
        try {
            if(result.hasErrors()) {
                model.addAttribute("adminDto", adminDto);
                return "register";
            }
            String username = adminDto.getUsername();
            Admin admin = adminService.findByUsername(username);
            if(admin == null) {
                if(adminDto.getRepeatPassword().equals(adminDto.getPassword())) {
                    adminService.save(adminDto);
                    model.addAttribute("adminDto", adminDto);
                    model.addAttribute("success", "Register successfully!");
                } else {
                    model.addAttribute("adminDto", adminDto);
                    model.addAttribute("passwordError", "Password is not the same!");
                }
            } else {
                model.addAttribute("emailError", "Your email has been registered!");
            }

        } catch(Exception e) {
            e.printStackTrace();
            model.addAttribute("errors", "The server has error");
        }
        return "register";
    }
}
