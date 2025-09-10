package com.taskwebsite.controller;

import com.taskwebsite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * AuthController - API and Presentation Layer
 * 
 * Handles authentication-related requests:
 * - GET/POST /sign-in: Display login form and handle login
 * - GET/POST /sign-up: Display registration form and handle registration
 * 
 * Implements the exact user flow specified:
 * - Invalid login redirects to sign-up with error message
 * - Successful registration redirects to sign-in with success message
 * - Form validation with Bootstrap alerts
 */
@Controller
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    /**
     * Display sign-in page
     * Handles error and success messages from URL parameters
     */
    @GetMapping("/sign-in")
    public String showSignIn(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "success", required = false) String success,
            Model model) {
        
        // Handle different message types
        if (error != null) {
            if ("true".equals(error)) {
                model.addAttribute("error", "Invalid credentials, please sign up.");
            } else if ("access_denied".equals(error)) {
                model.addAttribute("error", "Please sign in to access this page.");
            }
        }
        
        if (logout != null && "true".equals(logout)) {
            model.addAttribute("success", "You have been logged out successfully.");
        }
        
        if (success != null && "true".equals(success)) {
            model.addAttribute("success", "Account created, please sign in.");
        }
        
        return "sign-in";
    }
    
    /**
     * Handle sign-in form submission
     * Spring Security handles the actual authentication
     * This method is mainly for custom validation if needed
     */
    @PostMapping("/sign-in")
    public String processSignIn(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model) {
        
        // Basic validation
        if (email == null || email.trim().isEmpty()) {
            model.addAttribute("error", "Email is required");
            return "sign-in";
        }
        
        if (password == null || password.trim().isEmpty()) {
            model.addAttribute("error", "Password is required");
            return "sign-in";
        }
        
        // Validate user credentials
        if (!userService.validateUser(email, password)) {
            // Redirect to sign-up with error message as per requirements
            return "redirect:/sign-up?error=invalid_credentials";
        }
        
        // If validation passes, Spring Security will handle the authentication
        return "redirect:/tasks";
    }
    
    /**
     * Display sign-up page
     * Handles error messages from registration attempts
     */
    @GetMapping("/sign-up")
    public String showSignUp(
            @RequestParam(value = "error", required = false) String error,
            Model model) {
        
        if (error != null) {
            if ("invalid_credentials".equals(error)) {
                model.addAttribute("error", "Invalid credentials, please sign up.");
            }
        }
        
        return "sign-up";
    }
    
    /**
     * Handle sign-up form submission
     * Validates input and creates new user account
     */
    @PostMapping("/sign-up")
    public String processSignUp(
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            @RequestParam(value = "terms", required = false) String terms,
            Model model) {
        
        // Validate terms checkbox
        if (terms == null) {
            model.addAttribute("error", "You must accept the Terms of Service and Privacy Policy");
            model.addAttribute("fullName", fullName);
            model.addAttribute("email", email);
            return "sign-up";
        }
        
        // Attempt to register user
        String result = userService.registerUser(fullName, email, password, confirmPassword);
        
        if ("SUCCESS".equals(result)) {
            // Redirect to sign-in with success message
            return "redirect:/sign-in?success=true";
        } else {
            // Show error message
            model.addAttribute("error", result);
            model.addAttribute("fullName", fullName);
            model.addAttribute("email", email);
            return "sign-up";
        }
    }
    
    /**
     * Root path redirect
     * Redirect to sign-in page
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/sign-in";
    }
}