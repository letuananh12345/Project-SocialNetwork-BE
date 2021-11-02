package com.meta.socialnetwork.controller;

import com.meta.socialnetwork.dto.request.SignInForm;
import com.meta.socialnetwork.dto.request.SignUpForm;
import com.meta.socialnetwork.dto.response.JwtResponse;
import com.meta.socialnetwork.dto.response.ResponseMessage;
import com.meta.socialnetwork.model.Role;
import com.meta.socialnetwork.model.RoleName;
import com.meta.socialnetwork.model.User;
import com.meta.socialnetwork.security.jwt.JwtProvider;
import com.meta.socialnetwork.security.userPrinciple.UserPrinciple;
import com.meta.socialnetwork.service.role.IRoleService;
import com.meta.socialnetwork.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;


@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    IUserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    IRoleService roleService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> login(@Valid @RequestBody SignInForm signInForm){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInForm.getUsername(), signInForm.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.createToken(authentication);
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtResponse(token, userPrinciple.getId(), userPrinciple.getFullName(), userPrinciple.getAvatarUrl(), userPrinciple.getAuthorities()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignUpForm signUpForm) {
        if (userService.existsByUsername(signUpForm.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage("user_existed"), HttpStatus.OK);
        }
        if(userService.existsByEmail(signUpForm.getEmail())){
            return new ResponseEntity<>(new ResponseMessage("no_email"), HttpStatus.OK);
        }
        User users = new User();
        users.setFullName(signUpForm.getFullName());
        users.setUsername(signUpForm.getUsername());
        users.setEmail(signUpForm.getEmail());
        users.setPhone(signUpForm.getPhone());
        users.setDateOfBirth(signUpForm.getDateOfBirth());
        users.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
        users.setRe_password(passwordEncoder.encode(signUpForm.getRe_password()));
        Set<String> strRole = signUpForm.getRoles();
        Set<Role> roles = new HashSet<>();
        strRole.forEach(role -> {
            switch (role) {
                case "admin":
                    Role adminRole = roleService.findByName(RoleName.ADMIN).orElseThrow(() -> new RuntimeException("Role not found"));
                    roles.add(adminRole);
                    break;
                case "user":
                    Role userRole = roleService.findByName(RoleName.USER).orElseThrow(() -> new RuntimeException("Role not found"));
                    roles.add(userRole);
                    break;
                default:
                    Role defaultRole = roleService.findByName(RoleName.DEFAULT).orElseThrow(() -> new RuntimeException("Role not found"));
                    roles.add(defaultRole);
                    break;
            }
        });
        users.setRoles(roles);
        userService.save(users);
        return new ResponseEntity<>(new ResponseMessage("create_success"), HttpStatus.OK);
    }
}
