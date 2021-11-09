package com.meta.socialnetwork.controller;

import com.meta.socialnetwork.dto.request.*;
import com.meta.socialnetwork.dto.response.JwtResponse;
import com.meta.socialnetwork.dto.response.Response;
import com.meta.socialnetwork.dto.response.ResponseMessage;
import com.meta.socialnetwork.model.*;
import com.meta.socialnetwork.security.jwt.JwtAuthTokenFilter;
import com.meta.socialnetwork.security.jwt.JwtProvider;
import com.meta.socialnetwork.security.userPrinciple.UserDetailServiceImpl;
import com.meta.socialnetwork.security.userPrinciple.UserPrinciple;
import com.meta.socialnetwork.service.friend.IFriendService;
import com.meta.socialnetwork.service.post.IPostService;
import com.meta.socialnetwork.service.role.IRoleService;
import com.meta.socialnetwork.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    IUserService userService;
    @Autowired
    UserDetailServiceImpl userDetailService;
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
    @Autowired
    IPostService postService;
    @Autowired
    IFriendService friendService;


    @GetMapping("/page-user")
    public ResponseEntity<?> pageUser(@PageableDefault(sort = "username", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<User> userPage = userService.findAll(pageable);
        if (userPage.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userPage, HttpStatus.OK);
    }

//    @GetMapping("/page-user1")
//    public ResponseEntity<?> pageUser1(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 2) Pageable pageable) {
//        Page<User> userPage = userService.findAll(pageable);
//        if (userPage.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//        return new ResponseEntity<>(userPage, HttpStatus.OK);
//    }


    // api lấy gọi ý kết bạn
    @GetMapping("/page-user2")
    public ResponseEntity<?> pageUser2() {
        List<User> userPage = (List<User>) userService.findAllByOrderByIdDesc();
        User user = userDetailService.getCurrentUser();
        List<User> list = new ArrayList<>();
        for (int i = 0; i < userPage.size(); i++) {
            if (userPage.get(i).getId() != userDetailService.getCurrentUser().getId()) {
                Friend friend = friendService.suggestion(user,userPage.get(i),userPage.get(i), user);
                if(friend == null) {
                    list.add(userPage.get(i));
                    if (list.size() == 3) {
                        return new ResponseEntity<>(list, HttpStatus.OK);
                    }
                }
            }
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody SignInForm signInForm) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInForm.getUsername(), signInForm.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.createToken(authentication);
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

        if (userPrinciple.getIsActive() == false) {
            return new ResponseEntity<>(new ResponseMessage("user_was_blocked"), HttpStatus.OK);
        }
        return ResponseEntity.ok(new JwtResponse(token, userPrinciple.getId(), userPrinciple.getAvatarUrl(), userPrinciple.getFullName(), userPrinciple.getEmail(), userPrinciple.getPhone(), userPrinciple.getAuthorities()));

    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignUpForm signUpForm) {
        if (userService.existsByUsername(signUpForm.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage("user_existed"), HttpStatus.OK);
        }
        if (userService.existsByEmail(signUpForm.getEmail())) {
            return new ResponseEntity<>(new ResponseMessage("no_email"), HttpStatus.OK);
        }
        User users = new User();
        users.setFullName(signUpForm.getFullName());
        users.setUsername(signUpForm.getUsername());
        users.setEmail(signUpForm.getEmail());
        users.setPhone(signUpForm.getPhone());
        users.setAvatarUrl("https://vnn-imgs-a1.vgcloud.vn/image1.ictnews.vn/_Files/2020/03/17/trend-avatar-1.jpg");
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
    ResponseEntity<Response> bock(@PathVariable Long id) {
        User user = userService.findById(id).get();
        if (user.getIsActive()) {
            user.setIsActive(false);
            userService.save(user);
            return new ResponseEntity<>(new Response("200", "block", user), HttpStatus.OK);
        }
        user.setIsActive(true);
        userService.save(user);
        return new ResponseEntity<>(new Response("200", "unblock", user), HttpStatus.OK);

    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(HttpServletRequest request, @Valid @RequestBody ChangePasswordForm changePasswordForm) {
        String jwt = jwtAuthTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromJwtToken(jwt);
        User user;
        try {
            user = userService.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found wiht -> username" + username));
            boolean matches = passwordEncoder.matches(changePasswordForm.getCurrentPassword(), user.getPassword());
            if (changePasswordForm.getNewPassword() != null) {
                if (matches) {
                    user.setPassword(passwordEncoder.encode(changePasswordForm.getNewPassword()));
                    userService.save(user);
                } else {
                    return new ResponseEntity<>(new ResponseMessage("no"), HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(new ResponseMessage("yes"), HttpStatus.OK);
        } catch (UsernameNotFoundException exception) {
            return new ResponseEntity<>(new ResponseMessage(exception.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/change-avatar")
    public ResponseEntity<?> changeAvatar(HttpServletRequest request, @Valid @RequestBody ChangeAvatar changeAvatar) {
        String jwt = jwtAuthTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromJwtToken(jwt);
        User user;
        try {
            if (changeAvatar.getAvatarUrl() == null) {
                return new ResponseEntity<>(new ResponseMessage("no"), HttpStatus.OK);
            } else {
                user = userService.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found -> username" + username));
                user.setAvatarUrl(changeAvatar.getAvatarUrl());
                userService.save(user);
                Post post = new Post();
                post.setUser(user);
                post.setContent(user.getFullName() + " đã thay ảnh đại diện");
                post.setImageUrl(changeAvatar.getAvatarUrl());
                post.setStatus("public");
                LocalDate localDate = LocalDate.now();
                post.setCreated_date(localDate);
                postService.save(post);
            }
            return new ResponseEntity<>(new ResponseMessage("yes"), HttpStatus.OK);
        } catch (UsernameNotFoundException exception) {
            return new ResponseEntity<>(new ResponseMessage(exception.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/change-profile")
    public ResponseEntity<?> changeProfile(HttpServletRequest request, @Valid @RequestBody ChangeProfileForm changeProfileForm) {
        String jwt = jwtAuthTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromJwtToken(jwt);
        User user;
        try {
            if (userService.existsByEmail(changeProfileForm.getEmail())) {
                return new ResponseEntity<>(new ResponseMessage("noemail"), HttpStatus.OK);
            }
            user = userService.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found with -> useranme" + username));
            user.setFullName(changeProfileForm.getFullName());
            user.setEmail(changeProfileForm.getEmail());
            user.setPhone(changeProfileForm.getPhone());
            userService.save(user);
            return new ResponseEntity<>(new ResponseMessage("yes"), HttpStatus.OK);
        } catch (UsernameNotFoundException exception) {
            return new ResponseEntity<>(new ResponseMessage(exception.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
//fix error
