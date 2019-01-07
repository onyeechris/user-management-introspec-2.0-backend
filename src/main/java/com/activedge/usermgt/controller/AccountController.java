package com.activedge.usermgt.controller;


import com.activedge.usermgt.model.User;
import com.activedge.usermgt.model.dto.NewUserDTO;
import com.activedge.usermgt.model.dto.UserDTO;
import com.activedge.usermgt.repository.UserRepository;
import com.activedge.usermgt.security.SecurityUtils;
import com.activedge.usermgt.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountController {

    private final Logger log = LoggerFactory.getLogger(AccountController.class);

    private final UserRepository userRepository;

    private final UserService userService;

    public AccountController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * POST  /register : register the user.
     *
     * @param newUser the manually created user
     * @throws ValidationException 400 (Bad Request) if the password is incorrect
     * @throws ValidationException 400 (Bad Request) if the email is already used
     * @throws ValidationException 400 (Bad Request) if the login is already used
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody NewUserDTO newUser, Errors errors) {
        log.info("REST request to create new User : {}", newUser);

        if (errors.hasErrors()) {
            log.error("Error in creating new user detected...\n{}", errors.getAllErrors());
            throw new ValidationException(errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(", ")));
        }
//        User user = userService.registerUser(newUser, newUser.getPassword());
    }

    /**
     * GET  /activate : activate the registered user.
     *
     * @param key the activation key
     * @throws RuntimeException 500 (Internal Server Error) if the user couldn't be activated
     */
    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "key") String key) {
//        Optional<User> user = userService.activateRegistration(key);
//        if (!user.isPresent()) {
//            throw new ValidationException("No user was found for this activation key");
//        }
    }

    /**
     * GET  /authenticate : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request
     * @return the login if the user is authenticated
     */
    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET  /account : get the current user.
     *
     * @return the current user
     * @throws RuntimeException 500 (Internal Server Error) if the user couldn't be returned
     */
    @GetMapping("/account")
    public UserDTO getAccount() {
        return null;
//        return userService.getUserWithAuthorities()
//            .map(UserDTO::new)
//            .orElseThrow(() -> new ValidationException("User could not be found"));
    }

    /**
     * POST  /account : update the current user information.
     *
     * @param userDTO the current user information
     * @throws ValidationException 400 (Bad Request) if the email is already used
     * @throws RuntimeException 500 (Internal Server Error) if the user login wasn't found
     */
    @PostMapping("/account")
    public void saveAccount(@Valid @RequestBody UserDTO userDTO) {
        final String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new ValidationException("Current user login not found"));
//        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
//        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
//            throw new ValidationException("Email already used");
//        }
//        Optional<User> user = userRepository.findOneByLogin(userLogin);
//        if (!user.isPresent()) {
//            throw new ValidationException("User could not be found");
//        }
//        userService.updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
//            userDTO.getLangKey(), userDTO.getImageUrl());
    }

}
