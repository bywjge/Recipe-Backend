package com.recipe.controller;

import com.recipe.UserDetailsImpl;
import com.recipe.entity.TempRequestBody;
import com.recipe.entity.User;
import com.recipe.service.RecipeService;
import com.recipe.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    @Autowired
    RecipeService recipeService;

    @Autowired
    RegisterService registerService;

    @PostMapping("/api/register")
    public ResponseEntity<Object> register(@RequestBody TempRequestBody tempRequestBody) {
//        Map<String, String> db = new ConcurrentHashMap<>();
        String username = tempRequestBody.getEmail();
        String password = tempRequestBody.getPassword();
        System.out.println("---------");
        System.out.println(username);
        System.out.println(password);
        System.out.println("---------");
        if(username != null && recipeService.isLegalString(username) && username.matches("\\w+@\\w+\\..*")
                && password != null && recipeService.isLegalString(password) && password.trim().length() >= 8) {
            if (registerService.findUserByEmail(username) != null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            password = encoder.encode(password);
            User user = new User(null, username, password, "USER");
            registerService.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/details")
    public void currentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails details = (UserDetails) auth.getPrincipal();

        System.out.println("Username: " + details.getUsername());
        System.out.println("User has authorities/roles: " + details.getAuthorities());
        UserDetailsImpl user = (UserDetailsImpl) details;
        System.out.println("User Id:" + user.getUserId());
    }




}
