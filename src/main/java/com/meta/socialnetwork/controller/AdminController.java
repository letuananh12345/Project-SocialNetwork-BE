package com.meta.socialnetwork.controller;

import com.meta.socialnetwork.dto.request.ChangeAvatar;
import com.meta.socialnetwork.dto.request.ChangePasswordForm;
import com.meta.socialnetwork.dto.request.SignInForm;
import com.meta.socialnetwork.dto.request.SignUpForm;
import com.meta.socialnetwork.dto.response.JwtResponse;
import com.meta.socialnetwork.dto.response.ResponseMessage;
import com.meta.socialnetwork.model.Role;
import com.meta.socialnetwork.model.RoleName;
import com.meta.socialnetwork.model.User;
import com.meta.socialnetwork.security.jwt.JwtAuthTokenFilter;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    JwtAuthTokenFilter jwtAuthTokenFilter;


    @PostMapping("/signin")
    public ResponseEntity<?> login(@Valid @RequestBody SignInForm signInForm){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInForm.getUsername(), signInForm.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.createToken(authentication);
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        if (userPrinciple.getIsActive()== false){
            return new ResponseEntity<>(new ResponseMessage("user_was_blocked"), HttpStatus.OK);
        }
        return ResponseEntity.ok(new JwtResponse(token, userPrinciple.getId(), userPrinciple.getAvatarUrl(), userPrinciple.getFullName(), userPrinciple.getAuthorities()));
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
        users.setIsActive(true);
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

    @PutMapping("/block/{id}")
    ResponseEntity<?> bock(@PathVariable Long id){
        User user = userService.findById(id).get();

            user.setIsActive(false);
            userService.save(user);
            return new ResponseEntity<>("blocked", HttpStatus.OK);

    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(HttpServletRequest request, @Valid @RequestBody ChangePasswordForm changePasswordForm){
        String jwt = jwtAuthTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromJwtToken(jwt);
        User user;
        try {
            user = userService.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User Not Found wiht -> username"+username));
            boolean matches = passwordEncoder.matches(changePasswordForm.getCurrentPassword(), user.getPassword());
            if(changePasswordForm.getNewPassword() != null){
                if(matches){
                    user.setPassword(passwordEncoder.encode(changePasswordForm.getNewPassword()));
                    userService.save(user);
                } else {
                    return new ResponseEntity<>(new ResponseMessage("no"), HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(new ResponseMessage("yes"), HttpStatus.OK);
        } catch (UsernameNotFoundException exception){
            return new ResponseEntity<>(new ResponseMessage(exception.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/change-avatar")
    public ResponseEntity<?> changeAvatar(HttpServletRequest request, @Valid @RequestBody ChangeAvatar changeAvatar){
        String jwt = jwtAuthTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromJwtToken(jwt);
        User user;
        try {
            if(changeAvatar.getAvatarUrl()==null){
                return new ResponseEntity<>(new ResponseMessage("no"), HttpStatus.OK);
            } else {
                user = userService.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User Not Found -> username"+username));
                user.setAvatarUrl(changeAvatar.getAvatarUrl());
                userService.save(user);
            }
            return new ResponseEntity<>(new ResponseMessage("yes"), HttpStatus.OK);
        } catch (UsernameNotFoundException exception){
            return new ResponseEntity<>(new ResponseMessage(exception.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
